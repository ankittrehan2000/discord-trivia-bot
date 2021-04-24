package com.discordbot;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

public class Main {
    public static void main(String[] args) {
        Trivia trivia = new Trivia();

        //insert your bot's token here
        String token = "your token here";

        //create a new instance of your discordbot's api
        DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();

        //message listener for create channel request
        api.addMessageCreateListener(event -> {
            if (event.getMessageContent().equalsIgnoreCase("Let's play trivia")) {
                trivia.makeTriviaRequest();

                EmbedHelper embed = new EmbedHelper(trivia.getQuestion(), trivia.getCategory());
                event.getChannel().sendMessage(embed.createEmbed());
                event.getMessage().addReaction("\uD83D\uDC4D");
            }
        });

        //check if the answer if correct and send a reply
        api.addMessageCreateListener(replyEvent -> {
            if (replyEvent.getMessageContent().equalsIgnoreCase("Trivia answer: " + trivia.getCorrectAnswer())) {
                replyEvent.getChannel().sendMessage("You are correct!");
            } else if (replyEvent.getMessageContent().equalsIgnoreCase("Trivia answer: " + trivia.getIncorrectAnswer())) {
                replyEvent.getChannel().sendMessage("Incorrect! The answer is " + trivia.getCorrectAnswer());
            }
        });

        System.out.println("Invite discordbot:" + api.createBotInvite());
    }
}
