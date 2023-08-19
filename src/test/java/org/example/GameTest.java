package org.example;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class GameTest {


    private Game game;
    @Mock
    private ApiCalls mockApiCalls;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        game = Game.getInstance();
    }

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

            apiCalls.addingToPiles("playerOne", drawnCardsForPlayerOne);
            apiCalls.addingToPiles("playerTwo", drawnCardsForPlayerTwo);

            assertEquals(7, drawnCardsForPlayerOne.length);
            assertEquals(7, drawnCardsForPlayerTwo.length);
            assertEquals(7, apiCalls.returnPile("playerOne").size());
            assertEquals(7, apiCalls.returnPile("playerTwo").size());


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

        apiCalls.addingToPiles("playerOne", drawnCardsForPlayerOne);
        apiCalls.addingToPiles("playerTwo", drawnCardsForPlayerTwo);

        System.out.println(apiCalls.returnPile("playerOne"));
        System.out.println(apiCalls.returnPile("playerTwo"));

        apiCalls.emptyPile("playerOne", apiCalls.getDeckId());
        apiCalls.emptyPile("playerTwo", apiCalls.getDeckId());

        assertEquals(0, apiCalls.returnPile("playerOne").size());
        assertEquals(0, apiCalls.returnPile("playerTwo").size());
    }





    @Test
    void testAlternateWinConCheckPlayerOneWins() throws UnirestException {

        ArrayList<String>winningPile=new ArrayList<>();
        ArrayList<String>losingPile=new ArrayList<>();

        winningPile.add("2S");
        winningPile.add("2D");
        winningPile.add("2C");
        winningPile.add("2H");
        winningPile.add("3H");
        winningPile.add("3D");
        winningPile.add("3C");
        winningPile.add("3S");

        losingPile.add("4S");
        losingPile.add("4D");
        losingPile.add("4H");
        losingPile.add("4C");

        when(mockApiCalls.returnPile(eq("winPlayerOne"))).thenReturn(winningPile);
        when(mockApiCalls.returnPile(eq("winPlayerTwo"))).thenReturn(losingPile);

        if(!mockApiCalls.returnPile("winPlayerOne").get(0).contains("error")||
                !mockApiCalls.returnPile("winPlayerTwo").get(0).contains("error")) {

            int setsPlayerOne = mockApiCalls.returnPile("winPlayerOne").size()/4;
            int setsPlayerTwo = mockApiCalls.returnPile("winPlayerTwo").size()/4;

            assertTrue(setsPlayerOne>setsPlayerTwo);
        }

    }

    @Test
    void testAlternateWinConCheckPlayerTwoWins() throws UnirestException {

        ArrayList<String>winningPile=new ArrayList<>();
        ArrayList<String>losingPile=new ArrayList<>();

        winningPile.add("2S");
        winningPile.add("2D");
        winningPile.add("2C");
        winningPile.add("2H");
        winningPile.add("3H");
        winningPile.add("3D");
        winningPile.add("3C");
        winningPile.add("3S");

        losingPile.add("4S");
        losingPile.add("4D");
        losingPile.add("4H");
        losingPile.add("4C");

        when(mockApiCalls.returnPile(eq("winPlayerOne"))).thenReturn(losingPile);
        when(mockApiCalls.returnPile(eq("winPlayerTwo"))).thenReturn(winningPile);

        if(!mockApiCalls.returnPile("winPlayerOne").get(0).contains("error")||
                !mockApiCalls.returnPile("winPlayerTwo").get(0).contains("error")) {

            int setsPlayerOne = mockApiCalls.returnPile("winPlayerOne").size()/4;
            int setsPlayerTwo = mockApiCalls.returnPile("winPlayerTwo").size()/4;

            assertFalse(setsPlayerOne>setsPlayerTwo);
        }
    }
}