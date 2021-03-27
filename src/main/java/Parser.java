import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    public void getPage() throws IOException {
        String url = "https://github.com/search?l=Kotlin&p=2&q=kotlin&type=Repositories";
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        List<String> href = new ArrayList<>();
        Regex r = new Regex("href=\".+\"");
        Pattern pattern = Pattern.compile("(?<=Repository&quot;,&quot;url&quot;:&quot;)(https:.+?)(?=&quot)");
        Matcher matcher = pattern.matcher(response.toString());
        while (matcher.find())
            href.add(matcher.group());
        System.out.println(href);
    }
}
