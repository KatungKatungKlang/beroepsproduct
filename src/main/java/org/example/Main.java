package org.example;

import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class Main {
    public static void main(String[] args) throws UnirestException, IOException {


    /*    ApiCalls apiCalls = new ApiCalls();
        String newDeckId = apiCalls.getNewDeck();
        System.out.println(newDeckId);*/


        Game gaem = Game.getInstance();

        gaem.start();


    }
}
