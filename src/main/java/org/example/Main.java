package org.example;

import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


public class Main {
    public static void main(String[] args) throws UnirestException, IOException {

        Game game = Game.getInstance();

        game.start();


    }
}
