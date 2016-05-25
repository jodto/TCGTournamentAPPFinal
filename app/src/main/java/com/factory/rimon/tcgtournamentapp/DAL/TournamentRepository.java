package com.factory.rimon.tcgtournamentapp.DAL;

import android.util.Log;

import com.factory.rimon.tcgtournamentapp.BE.BEPlayer;
import com.factory.rimon.tcgtournamentapp.BE.BETournament;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by rimon on 4/29/2016.
 */
public class TournamentRepository {
    private final String URL = "http://tcg-limitedevents.rhcloud.com/api/tournaments";

    private final String TAG = "TOURNAMENT";

    ArrayList<BETournament> tournaments;

    public TournamentRepository(){
        tournaments = new ArrayList<BETournament>();
    }

    public ArrayList<BETournament> loadPage(int page) {

            int limit = 5;
            String result = getContent(URL + "?limit=" + limit + "&page=" + page);
            if (result == null) {
                Log.d(TAG, "Nothing returned...");
                return null;
            }
            return getTournaments(result);
    }

    /*
        gets one specific tournament, depended on the mongoid it was given
     */
    public BETournament getOneTournament(String mongoId){
        String result = getContent(URL + "/" + mongoId);
        BETournament returnedTournament = null;
        try
        {
            JSONObject jsonTournament = new JSONObject(result);

                ArrayList<BEPlayer> players = new ArrayList<BEPlayer>();

                if(jsonTournament.has("players")){
                    JSONArray jarr = (JSONArray) jsonTournament.get("players");
                    if(jarr != null){
                        for(int j=0; j<jarr.length(); j++){
                            JSONObject oPlayer = jarr.getJSONObject(j);

                            BEPlayer p = new BEPlayer(
                                    oPlayer.getString("firstName")
                                    , oPlayer.getString("lastName")
                                    , oPlayer.getString("DCI")
                                    , oPlayer.getString("email")
                            );

                            players.add(p);
                        }
                }

                if(players == null){
                    players.add(new BEPlayer("Nik", "Ras", "615", "nik@nikmail.nik"));
                }

                returnedTournament = createTournament(jsonTournament, players);
            }

            Log.d("Amount of players: ", returnedTournament.getPlayers().size() + "");
            return returnedTournament;
        }
        catch(JSONException e) {
            Log.e(TAG, "There was an error parsing the JSON", e);
        }catch(Exception e){
            Log.d(TAG, "General exception in loadPage " + e.getMessage());
        }
        return returnedTournament;
    }

    /*
        gets mutiple tournaments from the specific page
     */
    public ArrayList<BETournament> getTournaments(String result){
        try
        {
            JSONObject object = new JSONObject(result);
            JSONArray array = object.getJSONArray("docs");

            ArrayList<BETournament> myList = new ArrayList<>();
            for(int i = 0; i < array.length(); i++){
                JSONObject o = array.getJSONObject(i);

                ArrayList<BEPlayer> players = new ArrayList<BEPlayer>();

                if(o.has("players")){
                    JSONArray jarr = (JSONArray) o.get("players");
                    if(jarr != null){
                        for(int j=0; j<jarr.length(); j++){
                            JSONObject oPlayer = jarr.getJSONObject(j);

                            BEPlayer p = new BEPlayer(
                                    oPlayer.getString("firstName")
                                    , oPlayer.getString("lastName")
                                    , oPlayer.getString("DCI")
                                    , oPlayer.getString("email")
                            );

                            players.add(p);
                        }
                    }
                }

                if(players == null){
                    players.add(new BEPlayer("Nik", "Ras", "615", "nik@nikmail.nik"));
                }

                BETournament t = createTournament(o, players);

                myList.add(t);
        }
        Log.d("Repository:", "amount in array" + myList.size());
        return myList;
    }
    catch(JSONException e) {
        Log.e(TAG, "There was an error parsing the JSON", e);
    }catch(Exception e){
        Log.d(TAG, "General exception in loadPage " + e.getMessage());
    }
    return null;
    }


    /**
     * Get the content of the url as a string. Based on using a scanner.
     * @param urlString - the url must return data typical in either json, xml, csv etc..
     * @return the content as a string. Null if something goes wrong.
     */
    private String getContent(String urlString)
    {
        StringBuilder sb = new StringBuilder();
        try {
            java.net.URL url = new URL(urlString);
            Scanner s = new Scanner(url.openStream());

            while (s.hasNextLine()) {
                String line = s.nextLine();
                sb.append(line);
            }
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return sb.toString();
    }


    /*
        create a tournament based on parameters
     */
    public BETournament createTournament(JSONObject json, ArrayList<BEPlayer> playerList) throws JSONException
    {
        BETournament tournament = new BETournament(
                 json.getString("_id")
                , json.getString("title")
                , json.getString("location")
                , json.getString("date")
                , json.getString("format")
                , json.getString("edition")
                , json.getString("rel")
                , json.getString("price")
                , json.getString("entryTime")
                , json.getString("startTime")
                , json.getString("info")
                , playerList);
        return tournament;
    }


    /*
        sends out a PUT request to the database to update a tournament's players
     */
    public void updateTournament(String player, String id) {
        try {

            //make new URL with the specififed id of the tournament
            URL url = new URL(URL + "/" + id);
            String result = getContent(url.toString());

            //get the the tournament information and get the current players of that tournament
            JSONObject tournament = new JSONObject(result);
            JSONObject newPlayer = new JSONObject(player);
            JSONArray currentPlayers = (JSONArray) tournament.get("players");

            //add the enw player to the end of the player list
            currentPlayers.put(newPlayer);

            //setup a HTTP connection
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            httpCon.setDoOutput(true);

            //set the request to a PUT request
            httpCon.setRequestMethod("PUT");

            //send as JSON
            httpCon.setRequestProperty("Content-Type", "application/json");

            //Set up the outputstream to send information
            OutputStreamWriter out = new OutputStreamWriter(
                    httpCon.getOutputStream());

            //write to the database all the current players with the new player added
            out.write("{\"players\": " + currentPlayers.toString() + "}");

            //flush out the outdatastream and close it
            out.flush();
            out.close();


        } catch (MalformedURLException ex) {

        } catch (IOException ex) {

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
