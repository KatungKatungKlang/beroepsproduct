package org.example;

import com.mashape.unirest.http.exceptions.UnirestException;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Game {


    ApiCalls apiCalls;
    Scanner scanner;

    private static Game instance;

    private Game() {
        scanner = new Scanner(System.in);

        apiCalls = new ApiCalls();


    }

    public static Game getInstance(){
        if (Game.instance == null) {
            Game.instance = new Game();
        }
            return Game.instance;

    }


    public void start() throws UnirestException {
/*
        7 cards worden aan elke player gegeven
        vervolgens wordt er een pile(hand) gemaakt voor elke player en hierin
        worden de gedrawn cards gestored
*/

        apiCalls.setDeckId(apiCalls.getNewDeck());

        String[] drawnCardsForPlayerOne = apiCalls.drawCardFromDeck("7");
        String[] drawnCardsForPlayerTwo = apiCalls.drawCardFromDeck("7");



        apiCalls.addingToPiles("playerOne", drawnCardsForPlayerOne);
        apiCalls.addingToPiles("playerTwo", drawnCardsForPlayerTwo);

        while(true) {
            playerTurn("playerOne", "playerTwo");
            playerTurn("playerTwo", "playerOne");

        }
    }


    public void playerTurn(String player, String otherPlayer) throws UnirestException {
        ArrayList<String> pilePlayer = apiCalls.listPiles(player);
        apiCalls.listPiles(otherPlayer);


        System.out.println(player + ", Which card do you want?");

        String wantedCard = scanner.nextLine();
        wantedCard = wantedCard.toUpperCase();

        ArrayList<String> cardsINeed = apiCalls.searchPileForCardContainingThisNumberOrChar(apiCalls.returnPile(otherPlayer), wantedCard);


        if (cardsINeed.isEmpty()) {
            System.out.println("Go Fish!");
            String[] goFished = apiCalls.drawCardFromDeck("1");
            System.out.println(goFished[0]); //now we add to pile
            apiCalls.addingToPiles(player, goFished);
            checkCards(player);


        } else {
            apiCalls.drawingFromPile(cardsINeed, otherPlayer);
            apiCalls.addingToPiles(player, cardsINeed.toArray(new String[0]));
            checkCards(player);
            playerTurn(player, otherPlayer);
        }
    }

    private void checkCards(String player) throws UnirestException {
        //4 zelfde cards in player pile, word dan extracted naar player win pile

        Map<String, Integer> cardCounts = new HashMap<>(); // Map to store card counts
        ArrayList<String> removedCards = new ArrayList<>(); //arrayList to store elements to remove

        ArrayList<String> playersPiles = apiCalls.listPiles(player);
        for (String pile : playersPiles) {
            String card = pile.substring(0, 1);

            cardCounts.put(card, cardCounts.getOrDefault(card, 0) + 1);
        }
        // Check for cards with a count of 4
        for (String card : cardCounts.keySet()) {
            if (cardCounts.get(card) == 4) {
                removedCards = apiCalls.searchPileForCardContainingThisNumberOrChar(playersPiles, card);
            }
        }
        System.out.println(player + ": " + cardCounts);
        if (removedCards.isEmpty()) {
            //do nothing

        } else {
            if (player.equals("playerOne")) {
                apiCalls.drawingFromPile(removedCards, player);
                apiCalls.addingToPiles("winPlayerOne", removedCards.toArray(new String[0]));
                apiCalls.listPiles("winPlayerOne");
            } else {
                apiCalls.drawingFromPile(removedCards, player);
                apiCalls.addingToPiles("winPlayerTwo", removedCards.toArray(new String[0]));
                apiCalls.listPiles("winPlayerTwo");
            }
        }
        alternateWinConCheck();
    }


    public void alternateWinConCheck() throws UnirestException {

        if(!apiCalls.returnPile("winPlayerOne").get(0).contains("error")||
        !apiCalls.returnPile("winPlayerTwo").get(0).contains("error")) {

            int setsPlayerOne = apiCalls.returnPile("winPlayerOne").size()/4;
            int setsPlayerTwo = apiCalls.returnPile("winPlayerTwo").size()/4;

            System.out.println();

            if (setsPlayerOne >= 1) {
                System.out.println("Congratulations, Player One You Have Won the Game!");
                resetGame();
            } else if (setsPlayerTwo >= 1) {
                System.out.println("Congratulations, Player Two You Have Won the Game!");
                resetGame();
            }
        }

    }

    //comment
    private void resetGame() throws UnirestException {
        System.out.println("Do you want to start a new game? y/n");
        String answer = scanner.nextLine().toLowerCase();
        if (answer.equals("y")) {
            System.out.println("Starting new game!");
            apiCalls.emptyPile("playerOne", apiCalls.getDeckId());
            apiCalls.emptyPile("playerTwo", apiCalls.getDeckId());
            apiCalls.emptyPile("winPlayerOne", apiCalls.getDeckId());
            apiCalls.emptyPile("winPlayerTwo", apiCalls.getDeckId());

            String newDeck = apiCalls.getNewDeck();
            apiCalls.setDeckId(newDeck);

            start();

        } else {
            System.out.println("Thank you for playing!");
            System.exit(0);
        }

    }

}
