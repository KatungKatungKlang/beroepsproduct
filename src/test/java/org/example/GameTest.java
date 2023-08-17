package org.example;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

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
        assertEquals(instance1,instance2);
    }

    @Test
    void testPlayerTurn() throws UnirestException { // todo: fix this
        when(mockApiCalls.listPiles(anyString(), any())).thenReturn(new ArrayList<>());
        when(mockApiCalls.drawCardFromDeck(eq("1"))).thenReturn(new String[] {""});
        when(mockApiCalls.searchPileForCardContainingThisNumberOrChar(anyList(), anyString())).thenReturn(new ArrayList<>());

        game.playerTurn("playerOne", "playerTwo");

        verify(mockApiCalls, times(1)).drawCardFromDeck(eq("1"));
        verify(mockApiCalls, times(1)).addingToPiles(eq(null), eq("playerOne"), eq(new String[] {""}));
    }

}