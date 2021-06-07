package main;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Iterator;

public class RepIterator implements Iterator<String> {
    int p = -1; //order - desc asc    sort - stars forks help-wanted-issues updated and default - best match
    String link = "https://api.github.com/search/repositories?q=kotlin+language:kotlin&sort=stars&order=desc&per_page=10&page=" + p;
    ArrayDeque<String> page;


    @Override
    public boolean hasNext() {
        return false;
    }

    public String next() {
        if (page.isEmpty()) {
            p++;
            try {
                page = getReps(link);
            } catch (IOException e) {
                System.out.println("Network error");
            }
        }
        return page.pollLast();
    }

    public ArrayDeque<String> getReps(String url) throws IOException {
        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        StringBuilder response = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }
        JsonObject json = JsonParser.parseString(String.valueOf(response)).getAsJsonObject();
        JsonArray array = (json.getAsJsonArray("items"));
        ArrayDeque<String> href = new ArrayDeque<>(10);
        for (JsonElement j : array) {
            href.add(j.getAsJsonObject().getAsJsonPrimitive("html_url").getAsString());
        }
        return href;
    }
}
