package com.factory.rimon.tcgtournamentapp.UI;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.factory.rimon.tcgtournamentapp.BE.BETournament;
import com.factory.rimon.tcgtournamentapp.DAL.DAO;
import com.factory.rimon.tcgtournamentapp.DAL.TournamentRepository;
import com.factory.rimon.tcgtournamentapp.InternetHelper;
import com.factory.rimon.tcgtournamentapp.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView lvTournaments;


    TournamentRepository tRepo;
    TournamentAdapter tAdapter;
    LinearLayout mainLayout;
    Button btnRetry;
    private DAO dao;

    int nextPage = 1;
    boolean loading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dao = new DAO(this);
        lvTournaments = (ListView) findViewById(R.id.lvTournaments);

        tAdapter = new TournamentAdapter(this);
        tRepo = new TournamentRepository();
        lvTournaments.setAdapter(tAdapter);
        mainLayout =  (LinearLayout)findViewById(R.id.layout);
        populateTournaments();

        lvTournaments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClickTournament(position);
            }
        });


        lvTournaments.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                onScrollList(firstVisibleItem, visibleItemCount, totalItemCount);
            }
        });

    }




    /*
     populates the listview depending on if the internet is on or not and if the database is empty or not
     */
    public void populateTournaments() {
        if(dao.isDatabaseEmpty() && InternetHelper.isNetworkAvailable(this)){
            new LoadDataTask().execute(nextPage);
        }
        else if(dao.isDatabaseEmpty() && !InternetHelper.isNetworkAvailable(this))
        {
            Toast.makeText(getApplicationContext(), "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
            lvTournaments.setVisibility(View.GONE);
            addRetryButton(this);
        }
        else if(!dao.isDatabaseEmpty() && !InternetHelper.isNetworkAvailable(this))
        {
            tAdapter.addAll(dao.selectAll());
        }

    }

    /*
        The listview adapter
     */
    private class TournamentAdapter extends ArrayAdapter<BETournament> {

        public TournamentAdapter(Context context) {
            super(context, R.layout.activity_row);
        }

        public View getView(int pos, View v, ViewGroup p) {
            if (v == null) {
                LayoutInflater li = (LayoutInflater) getContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                v = li.inflate(R.layout.activity_row, null);
            }
            BETournament t = getItem(pos);

            TextView txtTitle = (TextView) v.findViewById(R.id.txtTitle);
            TextView txtDate = (TextView) v.findViewById(R.id.txtDate);
            TextView txtLocation = (TextView) v.findViewById(R.id.txtLocation);

            txtTitle.setText("" + t.getTitle());
            txtDate.setText("" + t.getDate());
            txtLocation.setText("" + t.getLocation());

            return v;
        }
    }

    /*
        would either send the mongoid to the tabAcivity if internet is on so it can get the tournament fresh from the DB
        Or if internet is not on, it just sends over what it has in the database
     */
    public void onClickTournament(int position){
        BETournament tournament = tAdapter.getItem(position);
        Intent intent = new Intent(getApplicationContext(), TabActivity.class);
        if(!InternetHelper.isNetworkAvailable(this)) {
            intent.putExtra("tournament", tournament);
            startActivity(intent);
        }
        else
        {
            intent.putExtra("mongoid", tournament.getId());
            startActivity(intent);
        }
    }

    /*
        Checks if the view is in the bottom, then load new page, if internet is available
     */
    public void onScrollList(int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        boolean loadMore =
                firstVisibleItem + visibleItemCount >= totalItemCount;

        //If we are at bottom at the page and not loading, load more data
        if (loadMore && !loading) {
            if (InternetHelper.isNetworkAvailable(this)) {
                Log.d("TOURNAMENTS", "onScroll page to load = " + nextPage);
                new LoadDataTask().execute(nextPage);
                loading = true;
                nextPage++;
            } else {
                Toast.makeText(getApplicationContext(), "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /*
        creates adds the retry button used in populateTournaments()
     */
    public void addRetryButton(final Context context)
    {
        btnRetry = new Button(this);
        btnRetry.setText("retry");
        btnRetry.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER
        ));
        btnRetry.setTextSize(100);
        mainLayout.addView(btnRetry);

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InternetHelper.isNetworkAvailable(context)) {
                    mainLayout.removeView(btnRetry);
                    lvTournaments.setVisibility(View.VISIBLE);
                    new LoadDataTask().execute(nextPage);
                    tAdapter.addAll(dao.selectAll());

                } else {
                    Toast.makeText(getApplicationContext(), "NO INTERNET FOUND", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*
        Running in the background, only loading data into the database if it does not already exist
     */
    class LoadDataTask extends AsyncTask<Integer, Void, List<BETournament>> {
        @Override
        protected List<BETournament> doInBackground(Integer... page) {
             for (BETournament t : tRepo.loadPage(page[0])) {
                    dao.insert(t);
                }

            return tRepo.loadPage(page[0]);
        }



        @Override
        protected void onPostExecute(List<BETournament> beTournaments) {
            tAdapter.addAll(beTournaments);
            loading = false;
            System.out.println("AMOUNT " + dao.selectAll().size());
        }
    }
}
