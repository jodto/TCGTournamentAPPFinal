package com.factory.rimon.tcgtournamentapp.UI.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.factory.rimon.tcgtournamentapp.BE.BETournament;
import com.factory.rimon.tcgtournamentapp.DAL.TournamentRepository;
import com.factory.rimon.tcgtournamentapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    static TournamentRepository tRepo;
    TextView txtFirstName;
    TextView txtLastName;
    TextView txtDCI;
    TextView txtEmail;
    String player = "";
    static BETournament tournament;

    public SignUpFragment() {
        tRepo = new TournamentRepository();
    }

    public static SignUpFragment newInstance(BETournament pickedTournament) {
        SignUpFragment f = new SignUpFragment();

        tournament = pickedTournament;

        return f;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        txtFirstName = (TextView) view.findViewById(R.id.txtFirstName);
        txtLastName = (TextView) view.findViewById(R.id.txtLastName);
        txtDCI = (TextView) view.findViewById(R.id.txtDCI);
        txtEmail = (TextView) view.findViewById(R.id.txtEmail);
        Button btnSignUp = (Button) view.findViewById(R.id.btnSignUp);

        /*
            happens when user clicks sign up
         */
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtFirstName.getText().toString().isEmpty() && txtLastName.getText().toString().isEmpty() &&
                        txtDCI.getText().toString().isEmpty() && txtEmail.getText().toString().isEmpty()) {
                    Toast.makeText(view.getContext(), "Fill out all information ", Toast.LENGTH_SHORT).show();

                } else {
                    new signUpPlayer().execute("{\"firstName\": \"" + txtFirstName.getText() + "\", \"lastName\": \"" + txtLastName.getText() + "\", \"DCI\":\"" + txtDCI.getText() + "\", \"email\":\"" + txtEmail.getText() + "\"}");
                    Toast.makeText(view.getContext(), "Signed Up ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }


    class signUpPlayer extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            tRepo.updateTournament(params[0], tournament.getMongoId());
            return null;
        }

        @Override
        protected void onPostExecute(Void beTournaments) {
            txtFirstName.setText("");
            txtLastName.setText("");
            txtDCI.setText("");
            txtEmail.setText("");
        }
    }

}
