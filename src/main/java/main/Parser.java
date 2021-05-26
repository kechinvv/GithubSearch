package main;


import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Parser {
    SampleEq sample = new SampleEq();
    int limit = 1000;

    public void cloneRep(String urlStr, String file) {
        try {
            Process proc = Runtime.getRuntime().exec("git clone --depth=1 --recurse-submodules -j8 " + urlStr + " " + file);
            proc.waitFor();
            proc.destroy();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void begin() throws IOException {
        int i = 0;
        Link l = new Link();
        while (i < limit) {
            l.nextPage();
            String GITHUB_API_BASE_URL = l.getLink();
            List<String> links = getReps(GITHUB_API_BASE_URL);
            for (String s : links) {
                i++;
                String sourceFolder = "C:/Users/valer/IdeaProjects/GithubSearch/zips/" + i;
                cloneRep(s, sourceFolder);
                if (walking(sourceFolder)) break;
                if (delete(sourceFolder)) System.out.println("Folder " + i + " deleted successful");
            }
        }
    }

    public List<String> getReps(String url) throws IOException {
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
        JsonObject json = JsonParser.parseString(String.valueOf(response)).getAsJsonObject();
        JsonArray array = (json.getAsJsonArray("items"));
        List<String> href = new ArrayList<>();
        for (JsonElement j : array) {
            href.add(j.getAsJsonObject().getAsJsonPrimitive("html_url").getAsString());
        }
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

    public boolean walking(String path) throws IOException {
        List<String> ktFiles = new ArrayList<>();
        Files.walk(Paths.get(path), FileVisitOption.FOLLOW_LINKS)
                .map(Path::toFile)
                .forEach(f -> {
                    if (f.isFile() && f.getName().endsWith(".kt")) ktFiles.add(f.getAbsolutePath());
                });
        //remove it after tests
        List<String> ktFiless = new ArrayList<>();
        ktFiless.add("C:/Users/valer/IdeaProjects/src");
        for (String pathKt : ktFiless) {
            if (sample.equal(pathKt)) {
                System.out.println(pathKt);
                return true;
            }
        }
        return false;
    }


}
