package org.example;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;
import java.util.List;

public class ApiCalls {

    private String deckId="";
    public ApiCalls(){

    }

public String getNewDeck() {
   Unirest.setTimeouts(0, 0);
    try {
        HttpResponse<JsonNode> response = Unirest.get("https://deckofcardsapi.com/api/deck/new/shuffle/?deck_count=1")
          .asJson();

        return response.getBody().getObject().get("deck_id").toString();
    } catch (UnirestException e) {
        throw new RuntimeException(e);
    }
}

public String getDeckId(){
    return deckId;
}


    public void setDeckId(String newDeckId){
        this.deckId=newDeckId;
    }


public String[] drawCardFromDeck(String numberOfCardsToDraw) throws UnirestException {

    String[]drawnCards = new String[7];

    Unirest.setTimeouts(0, 0);

        HttpResponse<JsonNode> response =
                Unirest.get("https://deckofcardsapi.com/api/deck/" +  deckId + "/draw/?count="+numberOfCardsToDraw)
                .asJson();


    JSONArray sevenCards = response.getBody().getObject().getJSONArray("cards");


    for(int i = 0; i< sevenCards.length(); i++){
      drawnCards[i]=sevenCards.getJSONObject(i).getString("code");
  }
    return drawnCards;
}

    public void addingToPiles(String pileName, String[] cardsToAdd) throws UnirestException {

        String joinedString = String.join(",", cardsToAdd);

        Unirest.setTimeouts(0, 0);

        HttpResponse<JsonNode> response =
                Unirest.get("https://deckofcardsapi.com/api/deck/" + deckId + "/pile/" + pileName + "/add/?cards="+ joinedString)
                        .asJson();


    }


    public String drawingFromPile(ArrayList<String>cardsINeed, String pileName) throws UnirestException {
        System.out.println("Card(s) to draw: " + cardsINeed);

        String joinedString = String.join(",", cardsINeed);

        Unirest.setTimeouts(0, 0);

        HttpResponse<JsonNode> response;

        response = Unirest.get("https://deckofcardsapi.com/api/deck/" +  deckId + "/pile/" + pileName + "/draw/?cards=" + joinedString)
                .asJson();

        JSONArray sevenCards =  response.getBody().getObject().getJSONArray("cards");


        String[] drawnCards=new String[10];
        for(int i = 0; i< sevenCards.length(); i++){
            drawnCards[i]=sevenCards.getJSONObject(i).getString("code");
        }
        return drawnCards[0];

    }

    public ArrayList<String> listPiles(String pileName) throws UnirestException {
        Unirest.setTimeouts(0, 0);
        HttpResponse<JsonNode> response = null;
        ArrayList<String> pile = new ArrayList<>();
        JSONArray pileCards = new JSONArray();

        try {
            response =
                    Unirest.get("https://deckofcardsapi.com/api/deck/" + deckId + "/pile/" + pileName + "/list/")
                            .asJson();

            pileCards = response.getBody().
                    getObject().
                    getJSONObject("piles").
                    getJSONObject(pileName).
                    getJSONArray("cards");

        }catch(UnirestException u){
            if(response==null){
                System.out.println(pileName + " doesn't exist");
            }

        }catch (JSONException e){
            System.out.println(e.getMessage());
        pile.add(e.getMessage() + "error?");
        return pile;
    }

    for(int i = 0; i<pileCards.length(); i++) {
       pile.add(pileCards.getJSONObject(i).getString("code"));
    }


    pile.sort((a, b)-> b.substring(0, 1).compareTo(a.substring(0, 1)));
    System.out.println("player " + pileName + " cards: " + pile + " and his hand has " + pile.size() + " cards");

   return pile;
}

    public ArrayList<String> returnPile(String pileName) throws UnirestException {
        Unirest.setTimeouts(0, 0);
        HttpResponse<JsonNode> response = null;
        ArrayList<String> pile = new ArrayList<>();
        JSONArray pileCards = new JSONArray();


        try {
            response =
                    Unirest.get("https://deckofcardsapi.com/api/deck/" + deckId + "/pile/" + pileName + "/list/")
                            .asJson();

            pileCards = response.getBody().
                    getObject().
                    getJSONObject("piles").
                    getJSONObject(pileName).
                    getJSONArray("cards");

        }catch(UnirestException u){
            System.out.println(pileName + " doesn't exist");

        }catch (JSONException e) {
            pile.add(e.getMessage());
            return pile;
        }

        for(int i = 0; i<pileCards.length(); i++) {
            pile.add(pileCards.getJSONObject(i).getString("code"));
        }

        pile.sort((a, b)-> b.substring(0, 1).compareTo(a.substring(0, 1)));
        return pile;
    }


public ArrayList<String> searchPileForCardContainingThisNumberOrChar(List<String> pile, String number){

    ArrayList<String>cardsOtherPlayerHas = new ArrayList<>();

    pile.forEach(element->{
        if(element.contains(number)){
           cardsOtherPlayerHas.add(element);
        }
    });
    return cardsOtherPlayerHas;
}


public boolean checkIfDeckIsEmpty()throws UnirestException{
HttpResponse<JsonNode> response = null;

        response =  Unirest.get("https://deckofcardsapi.com/api/deck/" + deckId + "/pile/" + "playerOne" + "/list/")
                .asJson();


       String remainingCardsInDeck = response.getBody().getObject().get("remaining").toString();

        if(remainingCardsInDeck.equals("0")){
            return true;
        } else {
            return false;
        }
}

    public void emptyPile(String pileToRemove, String deckId)throws UnirestException{



        ArrayList<String> EmptyThisPile = returnPile(pileToRemove);
        HttpResponse<JsonNode> response = null;

        try {
                    response = Unirest.get("https://deckofcardsapi.com/api/deck/" + deckId +
                            "/pile/" + pileToRemove + "/draw/?count=" + EmptyThisPile.size()).asJson();


        } catch (UnirestException e){
            System.out.println("Creating piles...");
        }

    }

}
