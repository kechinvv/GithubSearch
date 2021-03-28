import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
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
            String source = "C:/Users/valer/IdeaProjects/GithubSearch/zips/" + i + ".zip";
            String sourceFolder = "C:/Users/valer/IdeaProjects/GithubSearch/zips/" + i;
            String dist = "C:/Users/valer/IdeaProjects/GithubSearch/zips/"+i;
            String reg = "(?<=data-ga-click=\"Repository, download zip, location:repo overview\" data-open-app=\"link\" href=\")(.+?)(?=\">)";
            List<String> zipDown = getPage(s, reg);
            download(" https://github.com" + zipDown.get(0), source);
            extract(source, dist);
            if (delete(source)) System.out.println("Archive " + i + " deleted successful");
            if (delete(sourceFolder)) System.out.println("Folder " + i + " deleted successful");
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

    public void extract(String source, String destination) {
        try {
            ZipFile zipFile = new ZipFile(source);
            zipFile.extractAll(destination);
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    public boolean delete(String source) throws IOException {
        File file = new File(source);
        if (file.isDirectory()) {
            Files.walk(file.toPath())
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
        if (!file.exists()) return true;
        return file.delete();
    }

}
