package ac.umn.id.nulis.Location;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import ac.umn.id.nulis.R;

public class EditLocationActivity extends AppCompatActivity {

    String bookId, locationId, locationTitle;
    TextInputLayout editTitle, editDescription, editHistory, editDesign;
    Button editLocation;
    String title;

    DatabaseReference database;
    Query getLocationData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Edit location");

        SharedPreferences sp1 = this.getSharedPreferences("Location Info", MODE_PRIVATE);
        locationId = sp1.getString("lId", null);
        locationTitle = sp1.getString("lTitle", null);

        SharedPreferences sp2 = this.getSharedPreferences("Book Info", MODE_PRIVATE);
        bookId = sp2.getString("bId", null);

        database = FirebaseDatabase.getInstance("https://nulis-d3354-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        getLocationData = database.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                child("Book").
                child(bookId).
                child("Location");

        editTitle = findViewById(R.id.edit_location_title);
        editDescription = findViewById(R.id.edit_location_description);
        editHistory = findViewById(R.id.edit_location_history);
        editDesign = findViewById(R.id.edit_location_design);
        editLocation = findViewById(R.id.editLocation);



        getLocationData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.getKey().equals(locationId)){
                        Log.d("MATCH", "TRUE");
                        Log.d("TESTING", dataSnapshot.child("title").getValue().toString());
                        editTitle.getEditText().setText(dataSnapshot.child("title").getValue().toString());
                        editDescription.getEditText().setText(dataSnapshot.child("description").getValue().toString());
                        editHistory.getEditText().setText(dataSnapshot.child("history").getValue().toString());
                        editDesign.getEditText().setText(dataSnapshot.child("design").getValue().toString());
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
                String editLocationTitle = editTitle.getEditText().getText().toString().trim();
                String editLocationDescription = editDescription.getEditText().getText().toString().trim();
                String editLocationHistory = editHistory.getEditText().getText().toString().trim();
                String editLocationDesign = editDesign.getEditText().getText().toString().trim();

                FirebaseDatabase database = FirebaseDatabase.getInstance("https://nulis-d3354-default-rtdb.asia-southeast1.firebasedatabase.app/");
                DatabaseReference myRef = database.getReference("Users");

                Map<String, Object> postValues = new HashMap<String,Object>();
                postValues.put("title", editLocationTitle);
                postValues.put("description", editLocationDescription);
                postValues.put("history", editLocationHistory);
                postValues.put("design", editLocationDesign);

                myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Book").child(bookId).child("Location").child(locationId).updateChildren(postValues);

                Toast.makeText(EditLocationActivity.this, "Data berhasil di edit", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), LocationDetailActivity.class);
                startActivity(intent);
            }
        });
    }
}
