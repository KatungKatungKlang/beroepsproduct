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

        apiCalls.listPiles("playerOne", null);
        apiCalls.listPiles("playerTwo", null);
        apiCalls.listPiles("winPlayerOne", null);
        apiCalls.listPiles("winPlayerTwo", null);


        apiCalls.addingToPiles(null, "playerOne", drawnCardsForPlayerOne);
        apiCalls.addingToPiles(null, "playerTwo", drawnCardsForPlayerTwo);

        while(true) {
            playerTurn("playerOne", "playerTwo");
            playerTurn("playerTwo", "playerOne");

        }


    }





    public void playerTurn(String player, String otherPlayer) throws UnirestException {
        ArrayList<String> pilePlayer = apiCalls.listPiles(player, null);
        apiCalls.listPiles(otherPlayer, null);


        System.out.println(player + ", Which card do you want?");

        String wantedCard = scanner.nextLine();
        wantedCard = wantedCard.toUpperCase();

        ArrayList<String> cardsINeed = apiCalls.searchPileForCardContainingThisNumberOrChar(apiCalls.returnPile(otherPlayer, null), wantedCard);
        System.out.println(cardsINeed);

        if (cardsINeed.isEmpty()) {
            System.out.println("Go Fish!");
            String[] goFished = apiCalls.drawCardFromDeck("1");
            System.out.println(goFished[0]); //now we add to pile
            apiCalls.addingToPiles(null, player, goFished);
            checkCards(player);


        } else {
            apiCalls.drawingFromPile(cardsINeed, otherPlayer);
            apiCalls.addingToPiles(null, player, cardsINeed.toArray(new String[0]));
            checkCards(player);
            playerTurn(player, otherPlayer);
        }
    }

    private void checkCards(String player) throws UnirestException {
        //4 zelfde cards in player pile, word dan extracted naar player win pile

        Map<String, Integer> cardCounts = new HashMap<>(); // Map to store card counts
        ArrayList<String> removedCards = new ArrayList<>(); //arrayList to store elements to remove

        ArrayList<String> playersPiles = apiCalls.listPiles(player, null);
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
            //do nothing this is intentional do not change or everything breaks

        } else {
            if (player.equals("playerOne")) {
                apiCalls.drawingFromPile(removedCards, player);
                apiCalls.addingToPiles(null, "winPlayerOne", removedCards.toArray(new String[0]));
                apiCalls.listPiles("winPlayerOne", null);
            } else {
                apiCalls.drawingFromPile(removedCards, player);
                apiCalls.addingToPiles(null, "winPlayerTwo", removedCards.toArray(new String[0]));
                apiCalls.listPiles("winPlayerTwo", null);
            }
        }
        alternateWinConCheck();
    }


    public void alternateWinConCheck() throws UnirestException {
        //als winplayerone zn pile niet leeg is gaat hij checken
        if(!apiCalls.returnPile("winPlayerOne", null).get(0).contains("error")||
        !apiCalls.returnPile("winPlayerTwo", null).get(0).contains("error")) {

            int setsPlayerOne = apiCalls.returnPile("winPlayerOne", null).size()/4;
            int setsPlayerTwo = apiCalls.returnPile("winPlayerTwo", null).size()/4;

            System.out.println("test");

            if (setsPlayerOne >= 1) {
                System.out.println("Congratulations, Player One You Have Won the Game!");
                resetGame();
            } else if (setsPlayerTwo >= 1) {
                System.out.println("Congwatulations, UwU !!! You won, Player Two <3<3<3 *Nuzzles your Weewee*");
                resetGame();
            }
        }

    }

    //comment
    private void resetGame() throws UnirestException {
        System.out.println("Do you wanna start a new game? y/n");
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
        }

    }

}
