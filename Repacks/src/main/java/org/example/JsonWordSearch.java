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
        // Lista de URLs .json
        List<String> jsonLinks = new ArrayList<>();
        jsonLinks.add("https://hydralinks.cloud/sources/gog.json");
        jsonLinks.add("https://hydralinks.cloud/sources/fitgirl.json");

        // Palavra a buscar
        System.out.print("Digite uma palavra chave do jogo: ");
        String palavraDesejada = scanner.nextLine();

        // Itera sobre os links e busca a palavra nos arquivos JSON
        for (String link : jsonLinks) {
            try {
                // Faz a requisição HTTP e obtém o JSON
                String jsonContent = fetchJsonFromUrl(link);

                // Procura a palavra desejada no JSON
                boolean found = searchWordInJson(jsonContent, palavraDesejada);

                // Imprime o resultado
                System.out.println("Palavra \"" + palavraDesejada + "\" encontrada no link " + link + ": " + found);
            } catch (Exception e) {
                System.out.println("Erro ao processar o link " + link + ": " + e.getMessage());
            }
        }
        scanner.close();
    }

    // Faz uma requisição HTTP para obter o conteúdo do JSON
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

    // Busca uma palavra específica em um JSON (case insensitive)
    private static boolean searchWordInJson(String jsonContent, String palavra) {
        JSONObject jsonObject = new JSONObject(jsonContent);
        String jsonString = jsonObject.toString();

        // Define a palavra como um padrão case insensitive
        Pattern pattern = Pattern.compile(Pattern.quote(palavra), Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(jsonString);

        // Retorna true se o padrão for encontrado
        return matcher.find();
    }
}