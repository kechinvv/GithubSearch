import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private static void download(String urlStr, String file) throws IOException {
        URL url = new URL(urlStr);
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(file);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
    }

    public void get() throws IOException {
        String url = "https://github.com/search?l=Kotlin&p=2&q=kotlin&type=Repositories";
        List<String> pageRep = getPage(url, "(?<=Repository&quot;,&quot;url&quot;:&quot;)(https:.+?)(?=&quot)");
        int i = 0;
        for (String s : pageRep) {
            i++;
            List<String> zipDown = getPage(s, "(?<=data-ga-click=\"Repository, download zip, location:repo overview\" data-open-app=\"link\" href=\")(.+?)(?=\">)");
            download(" https://github.com"+zipDown.get(0), "C:/Users/valer/IdeaProjects/GithubSearch/zips/" + i + ".zip");
        }
    }

    public List<String> getPage(String url, String reg) throws IOException {
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
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(response.toString());
        while (matcher.find())
            href.add(matcher.group());
        System.out.println(href);
        return href;
    }
}
