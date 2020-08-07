package tech.codingclub.helix.entity;

public class WikiResult {
    private String query;
    private String text;
    private String image_url;

    public String getQuery() {
        return query;
    }

    public String getText() {
        return text;
    }

    public String getImage_url() {
        return image_url;
    }

    public WikiResult()
    {

    }
    public WikiResult(String query, String text, String image_url) {
        this.query = query;
        this.text = text;
        this.image_url = image_url;
    }
}
