package com.factory.rimon.tcgtournamentapp.BE;

import java.io.Serializable;

/**
 * Created by rimon on 4/29/2016.
 */
public class BEPlayer implements Serializable
{
        String firstName;
        String lastName;
        String DCI;
        String email;

        public BEPlayer(String firstName, String lastName, String DCI, String email)
        {
            this.firstName = firstName;
            this.lastName = lastName;
            this.DCI = DCI;
            this.email = email;
        }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDCI() {
        return DCI;
    }

    public String getEmail() {
        return email;
    }
}
