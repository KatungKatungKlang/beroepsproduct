package org.example;

import com.mashape.unirest.http.exceptions.UnirestException;


public class Main {
    public static void main(String[] args) throws UnirestException {

        ApiCalls apiCalls = new ApiCalls();
//        apiCalls.getNewDeck();

       Game gaem = new Game();
        gaem.start();
       /* while(true) {
            gaem.playerOneTurn();
            gaem.playerTwoTurn();

        }*/

    }
}
