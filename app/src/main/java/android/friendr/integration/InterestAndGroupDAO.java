package android.friendr.integration;

import android.friendr.view.EventList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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

    public void getGroupSearchResult(final DatabaseReturner databaseReturner, String id, String searchText) {
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

    public void chatListener(final DatabaseReturner databaseReturner, String groupName) {
        DatabaseReference groupNameRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(groupName).child("Chat");
        groupNameRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                databaseReturner.returner(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                databaseReturner.returner(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                databaseReturner.returner(dataSnapshot);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                databaseReturner.returner(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void userInfo(final DatabaseReturner databaseReturner, String currentUserID) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        usersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseReturner.returner(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void saveMessage(String currentUserName, String groupName, String txt_message, String currentDate, String currentTime) {
        DatabaseReference groupNameRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(groupName).child("Chat");
        String messageKey = groupNameRef.push().getKey();
        HashMap<String, Object> groupMessageKey = new HashMap<>();
        groupNameRef.updateChildren(groupMessageKey);

        DatabaseReference groupMessageKeyRef = groupNameRef.child(messageKey);

        HashMap<String, Object> messageInfoMap = new HashMap<>();
        messageInfoMap.put("username", currentUserName);
        messageInfoMap.put("message", txt_message);
        messageInfoMap.put("date", currentDate);
        messageInfoMap.put("time", currentTime);
        groupMessageKeyRef.updateChildren(messageInfoMap);
    }

    public void getEvents(final DatabaseReturner databaseReturner, String currentGroupName) {
        DatabaseReference eventRef = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName).child("Events");
        eventRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                databaseReturner.returner(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                databaseReturner.returner(dataSnapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                databaseReturner.returner(dataSnapshot);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                databaseReturner.returner(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
