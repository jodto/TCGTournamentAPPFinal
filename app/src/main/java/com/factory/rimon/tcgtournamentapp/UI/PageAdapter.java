package com.factory.rimon.tcgtournamentapp.UI;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.factory.rimon.tcgtournamentapp.BE.BETournament;
import com.factory.rimon.tcgtournamentapp.UI.Fragments.DetailsFragment;
import com.factory.rimon.tcgtournamentapp.UI.Fragments.PlayersFragment;
import com.factory.rimon.tcgtournamentapp.UI.Fragments.SignUpFragment;

/**
 * Created by rimon on 5/6/2016.
 */
public class PageAdapter extends FragmentPagerAdapter {

    Intent intent;
    BETournament tournament;

    public PageAdapter(FragmentManager fm, BETournament tournament) {
        super(fm);
        this.tournament = tournament;
    }

    /*
        sets the fragments assosiated to each tab
     */
    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0:
                return DetailsFragment.newInstance(tournament);
            case 1:
                return PlayersFragment.newInstance(tournament);
            case 2:
                return SignUpFragment.newInstance(tournament);
            default:
                return null;
        }
    }

    /*
        sets the titles of each tab
     */
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position)
        {
            case 0:
                return "Details";
            case 1:
                return "Players";
            case 2:
                return "Sign Up";
            default:
                return null;
        }    }

    /*
        amount of tabs
     */
    @Override
    public int getCount() {
        return 3;
    }


}
