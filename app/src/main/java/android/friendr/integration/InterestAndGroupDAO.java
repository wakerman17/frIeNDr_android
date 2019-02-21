package android.friendr.integration;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class InterestAndGroupDAO {

    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public void getUserInterest(final DatabaseReturner databaseReturner, int id) {
        getData(databaseReturner, id, "interest_profile");
    }

    public void getUserGroup(DatabaseReturner databaseReturner, int id) {
        getData(databaseReturner, id, "group");
    }

    public void getInterestSearchResult(final DatabaseReturner databaseReturner, int id, String searchText){
        Query query = database.child("interest").orderByChild("name").startAt(searchText);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseReturner.returner(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    private void getData(final DatabaseReturner databaseReturner, int id, String table) {
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
