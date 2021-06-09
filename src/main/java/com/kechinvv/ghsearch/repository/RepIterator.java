package com.kechinvv.ghsearch.repository;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kechinvv.ghsearch.repository.link.OrderType;
import com.kechinvv.ghsearch.repository.link.SortType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Iterator;

public class RepIterator implements Iterator<RemoteRepository> {

    private final static String TEMPLATE_URL = "https://api.github.com/search/repositories?q=%s+language:kotlin&sort=%s&order=%s&per_page=10&page=";
    private final int limit;
    private final String link;
    private ArrayDeque<RemoteRepository> reps = new ArrayDeque<>(10);
    private int counter = 0;
    private int page = -1;

    public RepIterator(int limit, String keywords, SortType sort, OrderType order) {
        this.limit = limit;
        this.link = String.format(TEMPLATE_URL, keywords.trim().replace(' ', '+'), sort.getCode(), order.getCode());
    }

    @Override
    public boolean hasNext() {
        try {
            nextPage();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return (counter < limit && !reps.isEmpty());
    }

    public RemoteRepository next() {
        counter++;
        return reps.removeLast();
    }

    private ArrayDeque<RemoteRepository> getReps(String url) throws IOException {
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
        connection.disconnect();
        JsonObject json = JsonParser.parseString(String.valueOf(response)).getAsJsonObject();
        JsonArray array = (json.getAsJsonArray("items"));
        ArrayDeque<RemoteRepository> href = new ArrayDeque<>(10);
        for (JsonElement j : array) {
            JsonObject repObj = j.getAsJsonObject();
            String repUrl = repObj.getAsJsonPrimitive("html_url").getAsString();
            String name = repObj.getAsJsonObject().getAsJsonPrimitive("full_name").getAsString();
            href.add(new RemoteRepository(repUrl, name));
        }
        return href;
    }

    private void nextPage() throws IOException {
        if (reps.isEmpty()) {
            page++;
            reps = getReps(link + page);
        }
    }
}
