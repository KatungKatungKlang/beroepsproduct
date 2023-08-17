package org.example;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @org.junit.jupiter.api.Test
    void testGetInstance() {
        Game instance1 = Game.getInstance();
        Game instance2 = Game.getInstance();
        assertEquals(instance1,instance2);
    }

    @Test
    void testAssigningCardsToPlayers(){
        ApiCalls apiCalls = new ApiCalls();
        try {
            apiCalls.setDeckId(apiCalls.getNewDeck());

            String[] drawnCardsForPlayerOne = apiCalls.drawCardFromDeck("7");
            String[] drawnCardsForPlayerTwo = apiCalls.drawCardFromDeck("7");

            apiCalls.addingToPiles(null, "playerOne", drawnCardsForPlayerOne);
            apiCalls.addingToPiles(null, "playerTwo", drawnCardsForPlayerTwo);

            assertEquals(7, drawnCardsForPlayerOne.length);
            assertEquals(7, drawnCardsForPlayerTwo.length);
            assertEquals(7, apiCalls.returnPile("playerOne", null).size());
            assertEquals(7, apiCalls.returnPile("playerTwo", null).size());


        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testEmptyingPiles() throws UnirestException {
        ApiCalls apiCalls = new ApiCalls();
        String newDeck = apiCalls.getNewDeck();
        apiCalls.setDeckId(newDeck);

        String[] drawnCardsForPlayerOne = apiCalls.drawCardFromDeck("7");
        String[] drawnCardsForPlayerTwo = apiCalls.drawCardFromDeck("7");

        apiCalls.addingToPiles(null, "playerOne", drawnCardsForPlayerOne);
        apiCalls.addingToPiles(null, "playerTwo", drawnCardsForPlayerTwo);

        System.out.println(apiCalls.returnPile("playerOne", null));
        System.out.println(apiCalls.returnPile("playerTwo", null));

        apiCalls.oppenheimer("playerOne", apiCalls.getDeckId());
        apiCalls.oppenheimer("playerTwo", apiCalls.getDeckId());

        assertEquals(0, apiCalls.returnPile("playerOne", null).size());
        assertEquals(0, apiCalls.returnPile("playerTwo", null).size());
    }

}