package android.friendr.controller;

import android.friendr.integration.DatabaseReturner;
import android.friendr.integration.InterestAndGroupDAO;

public class Controller {

    private InterestAndGroupDAO interestAndGroupDAO = new InterestAndGroupDAO();

    public void getUserInterest(DatabaseReturner databaseReturner, int id){
        interestAndGroupDAO.getUserInterest(databaseReturner, id);
    }
}
