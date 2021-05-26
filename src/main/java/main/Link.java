package main;

public class Link {
    int p = -1;
    String link = "https://api.github.com/search/repositories?q=kotlin+language:kotlin&sort=stars&order=desc&page=" + p;

    public String getLink() {
        return link;
    }

    public void nextPage(){
        p++;
    }

}
