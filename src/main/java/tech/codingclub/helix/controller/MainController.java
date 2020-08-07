package tech.codingclub.helix.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.log4j.Logger;
import org.jooq.Condition;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import tech.codingclub.helix.database.GenericDB;
import tech.codingclub.helix.entity.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;

/**
 * User: rishabh
 */
@Controller
@RequestMapping("/")
public class MainController extends BaseController {

    private static Logger logger = Logger.getLogger(MainController.class);

    @RequestMapping(method = RequestMethod.GET, value = "/helloworld")
    public String getQuiz(ModelMap modelMap, HttpServletResponse response, HttpServletRequest request) {
        return "hello";
    }
    @RequestMapping(method = RequestMethod.GET, value = "/welcome")
    public String welcome(ModelMap modelMap, HttpServletResponse response, HttpServletRequest request) {
        Member x=ControllerUtils.getCurrentMember(request);
        modelMap.addAttribute("NAME",x.name);
        return "welcome";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/signup")
    public String signup(ModelMap modelMap, HttpServletResponse response, HttpServletRequest request) {
        return "signup";
    }
    @RequestMapping(method = RequestMethod.POST, value = "/signup")
    public
    @ResponseBody
    SignUpResponse signUpData(@RequestBody Member member, HttpServletRequest request, HttpServletResponse response) {
        boolean user_created=false;
        String message="";
        member.role="cm";
        if (new GenericDB<Member>().getCount(tech.codingclub.helix.tables.Member.MEMBER,  tech.codingclub.helix.tables.Member.MEMBER.EMAIL.eq(member.email))>0)
        {
            message="user already exists for this email id";
        }
        else {
            new GenericDB<Member>().addRow(tech.codingclub.helix.tables.Member.MEMBER, member);
            user_created=true;
            message="user created";
        }
        return new SignUpResponse(message,user_created);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/time")
    public @ResponseBody String getTime(ModelMap modelMap, HttpServletResponse response, HttpServletRequest request) {
        TimeAPI timeapi=new TimeAPI(new Date().toString(),new Date().getTime());
        return new Gson().toJson(timeapi);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/wiki")
    public @ResponseBody String getWikiResultGson(ModelMap modelMap, @RequestParam("country") String country, HttpServletResponse response, HttpServletRequest request) {
        WikepediaDownloader wikepediaDownloader=new WikepediaDownloader(country);
        WikiResult x=wikepediaDownloader.getResult();
        return new Gson().toJson(x);
    }
    @RequestMapping(method = RequestMethod.GET, value = "/api/wiki/html")
    public String getWikiResultHtml(ModelMap modelMap, @RequestParam("country") String country, HttpServletResponse response, HttpServletRequest request) {
        WikepediaDownloader wikepediaDownloader=new WikepediaDownloader(country);
        WikiResult x=wikepediaDownloader.getResult();
        modelMap.addAttribute("IMAGE",x.getImage_url());
        modelMap.addAttribute("DESCRIPTION",x.getText());
        return "wikiapi";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/handle")
    public
    @ResponseBody
    String handleEncrypt(@RequestBody String data, HttpServletRequest request, HttpServletResponse response) {
        return "ok";
    }
}