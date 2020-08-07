package tech.codingclub.helix.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import tech.codingclub.helix.global.HttpURLConnectionEx;

import java.io.IOException;
import java.util.Date;

public class WikepediaDownloader{
 private    String keyword;

    public WikepediaDownloader(String keyword) {
        this.keyword = keyword;
    }

    public WikiResult getResult() {
        //1. get clean Keyword
        //2. get the url of wikipedia
        //3. make GET request to wikipedia
        //4. parsing the useful result using Jsoup
        //5. showing results
        if (this.keyword==null || this.keyword.length()==0)
            return null;
        //step1
        this.keyword=this.keyword.trim().replaceAll("[ ]+","_");
        //step2
        String wikiUrl=getWikiUrl(keyword);
        String wikipediaResponse= null;
        String response="";
        String imageURL = null;
        try {
            //step3
            wikipediaResponse = HttpURLConnectionEx.sendget(wikiUrl);
           // System.out.println(wikipediaResponse);

            //step4
            Document document= Jsoup.parse(wikipediaResponse,"https://en.wikipedia.org/wiki/");
            Elements childElement=document.body().select(".mw-parser-output > *");
            int state=0;
            for (Element elements:childElement)
            {
                if (state==0)
                {
                    if (elements.tagName().equals("table"))
                        state=1;
                }
                else if(state==1)
                {
                    if (elements.tagName().equals("p"))
                    {
                        state=2;
                        response=elements.text();
                        break;
                    }
                }
            }
            try {
                imageURL=document.body().select(".infobox img").get(0).attr("src");
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        if (imageURL.startsWith("//"))
        {
            imageURL="https:"+imageURL;
        }
      WikiResult wikiResult=new WikiResult(this.keyword,response,imageURL);
        //new GenericDB<WikiResult>().addRow(tech.codingclub.tables.WikiResult.WIKIRESULT,wikiResult);
        //push result into database
        //for now just print the json
       return wikiResult;
    }

    private String getWikiUrl(String cleankeyword) {
        return "https://en.wikipedia.org/wiki/"+cleankeyword;
    }

}
