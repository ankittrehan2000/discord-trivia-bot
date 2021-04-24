package com.discordbot;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.lang.Boolean.parseBoolean;

public class Trivia {
    private String category;
    private String question;
    private String correctAnswer;

    public void makeTriviaRequest() {
        try {
            URL url = new URL("https://opentdb.com/api.php?amount=1&type=boolean");

            //after opening a connection cast it to the type of url connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("accept", "application/json");

            //read in the response from the api call
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer bufferContent = new StringBuffer();

            while ((inputLine = br.readLine()) != null) {
                bufferContent.append(inputLine);
            }
            br.close();

            //initialize gson in order to convert the returned string into a json object
            Gson gson = new Gson();
            JsonObject returnObj = gson.fromJson(String.valueOf(bufferContent), JsonObject.class);
            JsonArray nestedArray = returnObj.get("results").getAsJsonArray();

            //parse through the array of trivia questions returned - in our case only 1 since we are requesting
            //one at any given time
            for (Object triviaObj :
                    nestedArray) {
                JsonObject currentTriviaObj = (JsonObject) triviaObj;
                this.category = currentTriviaObj.get("category").getAsString();
                this.question = currentTriviaObj.get("question").getAsString();
                this.correctAnswer = currentTriviaObj.get("correct_answer").getAsString();
            }
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCategory() {
        return this.category;
    }

    public String getQuestion() {
        return this.question;
    }

    public String getCorrectAnswer() {
        return this.correctAnswer;
    }

    public String getIncorrectAnswer() {
        String lowerCase = this.correctAnswer.toLowerCase();
        boolean value = parseBoolean(lowerCase);
        return Boolean.toString(!value);
    }
}
