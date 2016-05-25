package com.factory.rimon.tcgtournamentapp.UI.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.factory.rimon.tcgtournamentapp.BE.BEPlayer;
import com.factory.rimon.tcgtournamentapp.BE.BETournament;
import com.factory.rimon.tcgtournamentapp.DAL.TournamentRepository;
import com.factory.rimon.tcgtournamentapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayersFragment extends Fragment {


    private static BETournament tournament;
    static TournamentRepository tRepo;

    public PlayersFragment() {
        tRepo = new TournamentRepository();
    }

    public static PlayersFragment newInstance(BETournament pickedTournament) {
        PlayersFragment f = new PlayersFragment();

        tournament = pickedTournament;

        return f;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_players, container, false);
        ListView listView = (ListView) view.findViewById(R.id.lstPlayers);
        if(tournament.getPlayers().size() > 0) {
            ArrayAdapter<BEPlayer> adapter = new ArrayAdapter<BEPlayer>(getContext(), android.R.layout.simple_list_item_1, tournament.getPlayers());
            listView.setAdapter(adapter);
        }
        else
        {
            FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.framelayout);
            TextView textView = new TextView(getContext());
            textView.setText("No players signed up yet");
            frameLayout.addView(textView);
        }

        return view;
    }

}
