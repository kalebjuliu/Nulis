package ac.umn.id.nulis.Location;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ac.umn.id.nulis.HelperClass.Location;
import ac.umn.id.nulis.R;

public class AddLocationActivity extends AppCompatActivity {

    String bookId;
    TextInputLayout addTitle, addDescription, addHistory, addDesign;
    Button addLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create location");

        SharedPreferences sp1 = this.getSharedPreferences("Book Info", MODE_PRIVATE);
        bookId = sp1.getString("bId", null);

        addTitle = findViewById(R.id.add_location_title);
        addDescription = findViewById(R.id.add_location_description);
        addHistory = findViewById(R.id.add_location_history);
        addDesign = findViewById(R.id.add_location_design);
        addLocation = findViewById(R.id.addLocation);

        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String addLocationTitle = addTitle.getEditText().getText().toString().trim();
                String addLocationDescription = addDescription.getEditText().getText().toString().trim();
                String addLocationHistory = addHistory.getEditText().getText().toString().trim();
                String addLocationDesign = addDesign.getEditText().getText().toString().trim();

                FirebaseDatabase database = FirebaseDatabase.getInstance("https://nulis-d3354-default-rtdb.asia-southeast1.firebasedatabase.app/");
                DatabaseReference myRef = database.getReference("Users");

                Location location = new Location(addLocationTitle, addLocationDescription, addLocationHistory, addLocationDesign);

                myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Book").child(bookId).child("Location").push().setValue(location);

                finish();
            }
        });



    }
}