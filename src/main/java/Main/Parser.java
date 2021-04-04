package Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Parser {

    public void cloneRep(String urlStr, String file) {
        try {
            Process proc = Runtime.getRuntime().exec("git clone --depth=1 --recurse-submodules -j8 " + urlStr + " " + file);
            proc.waitFor();
            proc.destroy();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void get() throws IOException {
        String url = "https://github.com/search?l=Kotlin&p=2&q=kotlin&type=Repositories";
        List<String> pageRep = getPage(url, "(?<=Repository&quot;,&quot;url&quot;:&quot;)(https:.+?)(?=&quot)");
        int i = 0;
        for (String s : pageRep) {
            i++;
            String sourceFolder = "C:/Users/valer/IdeaProjects/GithubSearch/zips/" + i;
            cloneRep(s, sourceFolder);
            createPSI(sourceFolder);
            // if (delete(sourceFolder)) System.out.println("Folder " + i + " deleted successful");
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

    public void createPSI(String path) {

    }

}
