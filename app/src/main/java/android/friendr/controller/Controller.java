package android.friendr.controller;

import android.friendr.integration.DatabaseReturner;
import android.friendr.integration.InterestAndGroupDAO;

import com.google.firebase.database.DataSnapshot;

import java.io.Serializable;

public class Controller implements Serializable {

    private InterestAndGroupDAO interestAndGroupDAO = new InterestAndGroupDAO();

    public void getUserInterest(DatabaseReturner databaseReturner, String id){
        interestAndGroupDAO.getUserInterest(databaseReturner, id);
    }

    public void addInterest(String interestName, String userID){
        interestAndGroupDAO.addInterest(interestName, userID);
    }

    public void getUserGroup(DatabaseReturner databaseReturner, String id){
        interestAndGroupDAO.getUserGroup(databaseReturner, id);
    }

    public void getInterestSearchResult(DatabaseReturner databaseReturner, String id, String searchText){
        interestAndGroupDAO.getInterestSearchResult(databaseReturner, id, searchText);
    }

    public void getGroupSearchResult(DatabaseReturner databaseReturner, int id, String searchText){
        interestAndGroupDAO.getGroupSearchResult(databaseReturner, id, searchText);
    }

    public void addUserInterest(String interestName, String currentUserID) {
        interestAndGroupDAO.addUserInterest(interestName, currentUserID);
    }

    public void addUserGroup(String groupName, String groupDescription, String interest, String currentUserID) {
        interestAndGroupDAO.addUserGroup(groupName, groupDescription, interest, currentUserID);
    }

    public void addGroup(String groupName, String groupDescription, String interest, String currentUserID) {
        interestAndGroupDAO.addGroup(groupName, groupDescription, interest, currentUserID);
    }

    public void getAllInterests(DatabaseReturner databaseReturner) {
        interestAndGroupDAO.getAllInterests(databaseReturner);
    }
}
