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
import static org.mockito.Mockito.times;

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



/*    @Test
    void testPlayerTurn() throws UnirestException { // todo: fix this
        when(mockApiCalls.listPiles(anyString(), any())).thenReturn(new ArrayList<>());
        when(mockApiCalls.drawCardFromDeck(eq("1"))).thenReturn(new String[] {""});
        when(mockApiCalls.searchPileForCardContainingThisNumberOrChar(anyList(), anyString())).thenReturn(new ArrayList<>());

        game.playerTurn("playerOne", "playerTwo");

        verify(mockApiCalls, times(1)).drawCardFromDeck(eq("1"));
        verify(mockApiCalls, times(1)).addingToPiles(eq(null), eq("playerOne"), eq(new String[] {""}));
    }*/




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

        apiCalls.emptyPile("playerOne", apiCalls.getDeckId());
        apiCalls.emptyPile("playerTwo", apiCalls.getDeckId());

        assertEquals(0, apiCalls.returnPile("playerOne", null).size());
        assertEquals(0, apiCalls.returnPile("playerTwo", null).size());
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

        when(mockApiCalls.returnPile(eq("winPlayerOne"), any())).thenReturn(winningPile);
        when(mockApiCalls.returnPile(eq("winPlayerTwo"), any())).thenReturn(losingPile);

        if(!mockApiCalls.returnPile("winPlayerOne", null).get(0).contains("error")||
                !mockApiCalls.returnPile("winPlayerTwo", null).get(0).contains("error")) {

            int setsPlayerOne = mockApiCalls.returnPile("winPlayerOne", null).size()/4;
            int setsPlayerTwo = mockApiCalls.returnPile("winPlayerTwo", null).size()/4;

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

        when(mockApiCalls.returnPile(eq("winPlayerOne"), any())).thenReturn(losingPile);
        when(mockApiCalls.returnPile(eq("winPlayerTwo"), any())).thenReturn(winningPile);

        if(!mockApiCalls.returnPile("winPlayerOne", null).get(0).contains("error")||
                !mockApiCalls.returnPile("winPlayerTwo", null).get(0).contains("error")) {

            int setsPlayerOne = mockApiCalls.returnPile("winPlayerOne", null).size()/4;
            int setsPlayerTwo = mockApiCalls.returnPile("winPlayerTwo", null).size()/4;

            assertFalse(setsPlayerOne>setsPlayerTwo);
        }
    }
}