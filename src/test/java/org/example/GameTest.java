package org.example;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
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
        assertEquals(instance1, instance2);
    }

    @Test
    void testPlayerTurn() throws UnirestException { // todo: fix this
        when(mockApiCalls.listPiles(anyString(), any())).thenReturn(new ArrayList<>());
        when(mockApiCalls.drawCardFromDeck(eq("1"))).thenReturn(new String[]{""});
        when(mockApiCalls.searchPileForCardContainingThisNumberOrChar(anyList(), anyString())).thenReturn(new ArrayList<>());

        game.playerTurn("playerOne", "playerTwo");

        verify(mockApiCalls, times(1)).drawCardFromDeck(eq("1"));
        verify(mockApiCalls, times(1)).addingToPiles(eq(null), eq("playerOne"), eq(new String[]{""}));
    }


    @Test
    void testAssigningCardsToPlayers() {
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


        apiCalls.oppenheimer("playerOne", apiCalls.getDeckId());
        apiCalls.oppenheimer("playerTwo", apiCalls.getDeckId());


        assertEquals(0, apiCalls.returnPile("playerOne", null).size());
        assertEquals(0, apiCalls.returnPile("playerTwo", null).size());


    }


    @Test
    void testAlternateWinConCheck_PlayerOneWins() throws UnirestException {
        // Arrange
        ApiCalls mockApiCalls = mock(ApiCalls.class);
        Game mockGame = mock(Game.class);

        ApiCalls apiCalls = new ApiCalls();

        String deckId = apiCalls.getDeckId();
        apiCalls.setDeckId(deckId);

        String[] cardsToAdd = {"2A", "3A", "4A", "5A"};

        apiCalls.addingToPiles(null, "playerTwo", cardsToAdd);
        ArrayList<String> playerOne = apiCalls.returnPile("playerTwo", null);
        System.out.println(playerOne);
    }
}



//make returnPile and listPile match each other