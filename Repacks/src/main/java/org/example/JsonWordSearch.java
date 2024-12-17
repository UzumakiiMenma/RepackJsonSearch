package org.example;

import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.json.JSONObject;

public class JsonWordSearch {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // List of .json URLs
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

        // Key to search
        System.out.print("Game key: ");
        String gameKey = scanner.nextLine();

        // Iterate over the links and search for the word in the JSON files
        for (String link : jsonLinks) {
            try {
                // Make the HTTP request and get the JSON
                String jsonContent = fetchJsonFromUrl(link);

                // Search for the desired word in JSON
                boolean found = searchWordInJson(jsonContent, gameKey);

                // Print the result
                System.out.println("Key \"" + gameKey + "\" found in the link " + link + ": " + found);
            } catch (Exception e) {
                System.out.println("Error processing the link " + link + ": " + e.getMessage());
            }
        }
        scanner.close();
    }

    // Make an HTTP request to obtain the JSON content
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

    // Search for a specific word in JSON (case insensitive)
    private static boolean searchWordInJson(String jsonContent, String palavra) {
        JSONObject jsonObject = new JSONObject(jsonContent);
        String jsonString = jsonObject.toString();

        // Defines the word as a case insensitive pattern
        Pattern pattern = Pattern.compile(Pattern.quote(palavra), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(jsonString);

        // Returns true if pattern is found
        return matcher.find();
    }
}
