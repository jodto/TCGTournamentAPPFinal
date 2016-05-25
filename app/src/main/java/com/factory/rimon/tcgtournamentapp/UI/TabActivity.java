package com.factory.rimon.tcgtournamentapp.UI;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.factory.rimon.tcgtournamentapp.BE.BETournament;
import com.factory.rimon.tcgtournamentapp.DAL.TournamentRepository;
import com.factory.rimon.tcgtournamentapp.R;

/**
 * Created by rimon on 5/6/2016.
 */
public class TabActivity extends AppCompatActivity {


    BETournament tournament;
    TournamentRepository tRepo;
    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager);
        tRepo = new TournamentRepository();

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Checking if it got a whole tournament object with the intent or the mongoid
        if (getIntent().getSerializableExtra("tournament") instanceof BETournament) {
            tournament = (BETournament) getIntent().getSerializableExtra("tournament");
            setUpTabs();
        }
        else
        {
            new signUpPlayer().execute(getIntent());
        }


    }

    /*
        runs in the background, receiving a player form the database
    */
    class signUpPlayer extends AsyncTask<Intent, Void, BETournament> {

        @Override
        protected BETournament doInBackground(Intent... params) {
            return tRepo.getOneTournament(params[0].getSerializableExtra("mongoid").toString());
        }

        @Override
        protected void onPostExecute(BETournament beTournament) {
            tournament = beTournament;
            setUpTabs();
        }
    }


    /*
        Method sets up the tabs, reason why we do this in a method and not directly in the oncreate its because it is needed in two places
        If you were to have this only in the OnCreate, the background thread of SignUpPlayer would not finish before the tabs are set up
        leading to a nullpointerexception because the tournament its null, because the thread could no retrieve it form the DB fast enough
    */
    private void setUpTabs()
    {
        PagerAdapter pagerAdapter = new PageAdapter(getSupportFragmentManager(), tournament);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabTextColors(Color.BLACK, Color.WHITE);
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
        tabLayout.setupWithViewPager(viewPager);
    }
}
