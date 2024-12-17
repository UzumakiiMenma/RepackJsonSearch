package org.example;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonWordSearch {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // List of JSON URLs
        List<String> jsonLinks = new ArrayList<>();
        jsonLinks.add("https://hydralinks.cloud/sources/gog.json");
        jsonLinks.add("https://hydralinks.cloud/sources/fitgirl.json");
        jsonLinks.add("https://hydralinks.cloud/sources/xatab.json");
        jsonLinks.add("https://hydralinks.cloud/sources/tinyrepacks.json");
        jsonLinks.add("https://hydralinks.cloud/sources/onlinefix.json");
        jsonLinks.add("https://hydralinks.cloud/sources/kaoskrew.json");
        jsonLinks.add("https://hydralinks.cloud/sources/empress.json");
        jsonLinks.add("https://hydralinks.cloud/sources/dodi.json");
        jsonLinks.add("https://hydralinks.cloud/sources/steamrip.json");
        jsonLinks.add("https://hydralinks.cloud/sources/steamrip-software.json");

        // Keyword to search for
        System.out.print("Enter the game keyword: ");
        String keyword = scanner.nextLine();

        // Iterate through links and search for the keyword in JSON files
        for (String link : jsonLinks) {
            try {
                System.out.println("\nSearching in: " + link);
                String jsonContent = fetchJsonFromUrl(link);
                searchAndDisplayResults(jsonContent, keyword);
            } catch (Exception e) {
                System.out.println("Error processing link " + link + ": " + e.getMessage());
            }
        }
        scanner.close();
    }

    // Makes an HTTP request to fetch the JSON content
    private static String fetchJsonFromUrl(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }

    // Searches and displays formatted results
    private static void searchAndDisplayResults(String jsonContent, String keyword) {
        JSONArray jsonArray = new JSONArray(jsonContent); // Convert JSON to Array
        boolean foundSomething = false;

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject item = jsonArray.getJSONObject(i);
            String title = item.optString("title", "");
            String fileSize = item.optString("fileSize", "Unknown");
            JSONArray uris = item.optJSONArray("uris");

            if (title.toLowerCase().contains(keyword.toLowerCase())) {
                foundSomething = true;
                System.out.println("--------------------------------");
                System.out.println("Title: " + title);
                System.out.println("File Size: " + fileSize);

                // Get the first magnet link
                if (uris != null) {
                    for (int j = 0; j < uris.length(); j++) {
                        String link = uris.getString(j);
                        if (link.startsWith("magnet")) {
                            System.out.println("Magnet Link: " + link);
                            break;
                        }
                    }
                } else {
                    System.out.println("Magnet Link: Not available");
                }
            }
        }

        if (!foundSomething) {
            System.out.println("No results found.");
        }
    }
}
