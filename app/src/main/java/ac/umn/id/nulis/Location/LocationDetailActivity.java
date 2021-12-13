package ac.umn.id.nulis.Location;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ac.umn.id.nulis.R;

public class LocationDetailActivity extends AppCompatActivity {
    FloatingActionButton editLocation;
    String bookId, locationId, locationTitle;
    DatabaseReference database;

    TextView locDetailTitle, locDetailDesc, locDetailHistory, locDetailDesign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_detail);

        locDetailTitle = findViewById(R.id.location_detail_title);
        locDetailDesc = findViewById(R.id.location_detail_desc);
        locDetailHistory = findViewById(R.id.location_detail_his);
        locDetailDesign = findViewById(R.id.location_detail_design);
        editLocation = findViewById(R.id.fab_edit_location);

        SharedPreferences sp1 = this.getSharedPreferences("Location Info", MODE_PRIVATE);
        locationId = sp1.getString("lId", null);
        locationTitle = sp1.getString("lTitle", null);

        SharedPreferences sp2 = this.getSharedPreferences("Book Info", MODE_PRIVATE);
        bookId = sp2.getString("bId", null);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(locationTitle);

        database = FirebaseDatabase.getInstance("https://nulis-d3354-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                        child("Book").
                        child(bookId).
                        child("Location");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                   if(dataSnapshot.getKey().equals(locationId)){
                       Log.d("MATCH", "TRUE");
                       Log.d("LOCATION", dataSnapshot.child("title").getValue().toString());
                       locDetailTitle.setText(dataSnapshot.child("title").getValue().toString());
                       locDetailDesc.setText(dataSnapshot.child("description").getValue().toString());
                       locDetailHistory.setText(dataSnapshot.child("history").getValue().toString());
                       locDetailDesign.setText(dataSnapshot.child("design").getValue().toString());
                   }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        editLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocationDetailActivity.this, EditLocationActivity.class);
                startActivity(intent);
            }
        });
    }
}