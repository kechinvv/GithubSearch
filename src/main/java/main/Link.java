package main;

public class Link {
    StringBuffer link = new StringBuffer("https://api.github.com/search/repositories?q=kotlin+language:kotlin&sort=stars&order=desc&page=0");
    // StringBuffer link2 = new StringBuffer("https://searchcode.com/api/codesearch_I/?q=get&lan=145&src=2&p=0");
    int pageIndex = 0;
    int p = 1;

    public Link() {
    getPageIndex();
    }

    public String getLink(){
        return link.toString();
    }

    public String nextPage() {
        link.delete(pageIndex, link.length() - 1);
        p++;
        link.append(p);
        return link.toString();
    }

    private void getPageIndex() {
        for (int i = link.length() - 1; i > 0; i--)
            if (link.charAt(i) == '=') {
                pageIndex = i + 1;
                break;
            }
    }
}
