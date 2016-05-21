package com.factory.rimon.tcgtournamentapp.DAL;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.factory.rimon.tcgtournamentapp.BE.BEPlayer;
import com.factory.rimon.tcgtournamentapp.BE.BETournament;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rimon on 5/9/2016.
 */
public class DAO {

    private static final String DATABASE_NAME = "tournament.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Tournament";

    private Context context;

    private SQLiteDatabase db;
    private SQLiteStatement insertStmt;
    private static final String INSERT = "insert into " + TABLE_NAME
            + " (title, date, location, edition, rel, format, entryTime, startTime, price, info, players, mongoid) values (?,?,?,?,?,?,?,?,?,?,?,?)";


    public DAO(Context context) {
        this.context = context;
        OpenHelper openHelper = new OpenHelper(this.context);
        this.db = openHelper.getWritableDatabase();
        this.insertStmt = this.db.compileStatement(INSERT);
    }

    /*
        inserts a enw tournament into the database
     */
    public long insert(BETournament t) {

                this.insertStmt.bindString(1, t.getTitle());
                this.insertStmt.bindString(2, t.getDate());
                this.insertStmt.bindString(3, t.getLocation());
                this.insertStmt.bindString(4, t.getEdition());
                this.insertStmt.bindString(5, t.getRel());
                this.insertStmt.bindString(6, t.getFormat());
                this.insertStmt.bindString(7, t.getEntryTime());
                this.insertStmt.bindString(8, t.getStartTime());
                this.insertStmt.bindString(9, t.getPrice());
                this.insertStmt.bindString(10, t.getInfo());

                try {

                    JSONArray jsonArray = new JSONArray();
                    if(t.getPlayers().size() > 0) {
                        for (int i = 0; i < t.getPlayers().size(); i++) {
                            JSONObject player = new JSONObject();
                            player.put("firstName", t.getPlayers().get(i).getFirstName());
                            player.put("lastName", t.getPlayers().get(i).getLastName());
                            player.put("DCI", t.getPlayers().get(i).getDCI());
                            player.put("email", t.getPlayers().get(i).getEmail());
                            jsonArray.put(player);
                        }

                        this.insertStmt.bindString(11, jsonArray.toString());
                    }
                    else
                    {
                        this.insertStmt.bindString(11, "no players");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                this.insertStmt.bindString(12, t.getMongoId());


        return this.insertStmt.executeInsert();
    }



    /*
        gets all tournaments from the DB
    */
    public List<BETournament> selectAll()  {
        List<BETournament> list = new ArrayList<BETournament>();
        Cursor cursor = this.db.query(TABLE_NAME, new String[] { "id", "title", "date", "location", "edition", "rel", "format", "entryTime", "startTime", "price", "info", "players", "mongoid"},
                null, null, null, null, "title asc");
        if (cursor.moveToFirst()) {
            do {
                ArrayList<BEPlayer> playerList = new ArrayList<>();

                try
                {


                if(!cursor.getString(11).equals("no players")) {
                    //JSONObject json = new JSONObject(cursor.getString(11));
                    JSONArray jsonList = new JSONArray(cursor.getString(11));

                    for (int i = 0; i < jsonList.length(); i++) {
                        JSONObject oPlayer = jsonList.getJSONObject(i);
                        BEPlayer p = new BEPlayer(
                                oPlayer.getString("firstName")
                                , oPlayer.getString("lastName")
                                , oPlayer.getString("DCI")
                                , oPlayer.getString("email")
                        );

                        playerList.add(p);
                    }
                }

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }


                list.add(new BETournament(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)
                        , cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getString(9), cursor.getString(10), playerList, cursor.getString(12)));
            } while (cursor.moveToNext());
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return list;
    }





    private static class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME
                    + "( id TEXT PRIMARY KEY,title TEXT,date TEXT, location TEXT, edition TEXT, rel TEXT, format TEXT, entryTime TEXT, startTime TEXT, price TEXT, info TEXT, players TEXT, mongoid TEXT)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db,
                              int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }


}
