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
        Query query = database.child("interest_profile").orderByChild("user_id").equalTo(id);
        //Query query = myRef.orderByChild("user_id").equalTo("1");
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
