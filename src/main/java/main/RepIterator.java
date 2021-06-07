package main;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import main.link.OrderType;
import main.link.SortType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayDeque;
import java.util.Iterator;

public class RepIterator implements Iterator<String> {

    private final static String TEMPLATE_URL = "https://api.github.com/search/repositories?q=%s+language:kotlin&sort=%s&order=%s&per_page=10&page=";
    private final int limit;
    String link;
    ArrayDeque<String> reps;
    private int counter = 0;
    private int page = -1;
    // String link = "https://api.github.com/search/repositories?q=kotlin+language:kotlin&sort=stars&order=desc&per_page=10&page=" + p;

    public RepIterator(int limit, String keywords, SortType sort, OrderType order) {
        this.limit = limit;
        this.link = String.format(TEMPLATE_URL, keywords, sort.getCode(), order.getCode());
    }

    @Override
    public boolean hasNext() {
        nextPage();
        return (counter < limit && !reps.isEmpty());
    }

    public String next() {
        nextPage();
        counter++;
        return reps.pollLast();
    }

    private ArrayDeque<String> getReps(String url) throws IOException {
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
        ArrayDeque<String> href = new ArrayDeque<>(10);
        for (JsonElement j : array) {
            href.add(j.getAsJsonObject().getAsJsonPrimitive("html_url").getAsString());
        }
        return href;
    }

    private void nextPage() {
        if (reps.isEmpty()) {
            page++;
            try {
                reps = getReps(link + page);
            } catch (IOException e) {
                System.out.println("Network error");
            }
        }
    }
}
