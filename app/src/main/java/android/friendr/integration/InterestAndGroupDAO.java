package android.friendr.integration;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class InterestAndGroupDAO implements Serializable {

    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public void getUserInterest(final DatabaseReturner databaseReturner, String id) {
        getData(databaseReturner, id, "interest_profile");
    }

    public void addInterest(String interestName, String currentUserID){
        DatabaseReference databaseUser = database.child("interest").push();
        String postId = databaseUser.getKey();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name", interestName);
        database.child("interest").child(postId).setValue(hashMap);
        addUserInterest(interestName, currentUserID);
    }

    public void getUserGroup(DatabaseReturner databaseReturner, String id) {
        getData(databaseReturner, id, "group_profile");
    }

    public void getInterestSearchResult(final DatabaseReturner databaseReturner, final String userID, String searchText){
        Query query = database.child("interest").orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /*ArrayList<DataSnapshot> dataSnapshotList = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if(!postSnapshot.child("user_id").getValue(String.class).equals(userID)) {
                        dataSnapshotList.add(postSnapshot);
                    }
                }*/
                databaseReturner.returner(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void addUserInterest(String interestName, String currentUserID) {
        DatabaseReference databaseUser = database.child("interest_profile").push();
        String postId = databaseUser.getKey();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("interest", interestName);
        hashMap.put("user_id", currentUserID);
        database.child("interest_profile").child(postId).setValue(hashMap);
    }

    public void getGroupSearchResult(final DatabaseReturner databaseReturner, int id, String searchText) {
        Query query = database.child("group").orderByChild("name").startAt(searchText).endAt(searchText + "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseReturner.returner(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void addGroup(String groupName, String groupDescription, String interest, String currentUserID) {
        DatabaseReference databaseUser = database.child("group").push();
        String postId = databaseUser.getKey();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("description", groupDescription);
        hashMap.put("interest_base", interest);
        hashMap.put("name", groupName);
        database.child("group").child(postId).setValue(hashMap);
        addUserGroup(groupName, groupDescription, interest, currentUserID);
    }

    public void addUserGroup(String groupName, String groupDescription, String interest, String currentUserID) {
        DatabaseReference databaseUser = database.child("group").push();
        String postId = databaseUser.getKey();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("description", groupDescription);
        hashMap.put("interest_base", interest);
        hashMap.put("name", groupName);
        hashMap.put("user_id", currentUserID);
        database.child("group_profile").child(postId).setValue(hashMap);
    }

    public void getAllInterests(final DatabaseReturner databaseReturner) {
        Query query = database.child("interest");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseReturner.returner(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void getData(final DatabaseReturner databaseReturner, String id, String table) {
        Query query = database.child(table).orderByChild("user_id").equalTo(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseReturner.returner(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
}
