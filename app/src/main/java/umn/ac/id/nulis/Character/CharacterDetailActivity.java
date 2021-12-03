package umn.ac.id.nulis.Character;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import umn.ac.id.nulis.Location.EditLocationActivity;
import umn.ac.id.nulis.Location.LocationDetailActivity;
import umn.ac.id.nulis.R;

public class CharacterDetailActivity extends AppCompatActivity {

    DatabaseReference database;
    FloatingActionButton editChara;
    String bookId, charaId, charaTitle;

    TextView charaDetailTitle, charaDetailDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_detail);

        charaDetailTitle = findViewById(R.id.character_detail_title);
        charaDetailDesc = findViewById(R.id.character_detail_desc);

        SharedPreferences sp1 = this.getSharedPreferences("Character Info", MODE_PRIVATE);
        charaId = sp1.getString("chId", null);
        charaTitle = sp1.getString("chTitle", null);

        SharedPreferences sp2 = this.getSharedPreferences("Book Info", MODE_PRIVATE);
        bookId = sp2.getString("bId", null);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(charaTitle);

        database = FirebaseDatabase.getInstance("https://nulis-d3354-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                        child("Book").
                        child(bookId).
                        child("Character");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.getKey().equals(charaId)){
                        Log.d("MATCH", "TRUE");
                        Log.d("Character", dataSnapshot.child("title").getValue().toString());
                        charaDetailTitle.setText(dataSnapshot.child("title").getValue().toString());
                        charaDetailDesc.setText(dataSnapshot.child("description").getValue().toString());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        editLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LocationDetailActivity.this, EditLocationActivity.class);
//                startActivity(intent);
//            }
//        });
    }
}
