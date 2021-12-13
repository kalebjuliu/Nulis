package ac.umn.id.nulis.Character;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ac.umn.id.nulis.R;

public class CharacterDetailActivity extends AppCompatActivity {

    DatabaseReference database;
    FloatingActionButton editChara;
    String bookId, charaId, charaTitle;

    TextView charaDetailTitle, charaDetailDesc, charaDetailRole, charaDetailGoal, charaDetailOutcome, charaDetailStrength, charaDetailWeakness, charaDetailSkills;
    ImageView charaDetailIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_detail);

        charaDetailTitle = findViewById(R.id.character_detail_title);
        charaDetailDesc = findViewById(R.id.character_detail_desc);
        charaDetailRole = findViewById(R.id.character_detail_role);
        charaDetailGoal = findViewById(R.id.character_detail_goal);
        charaDetailOutcome = findViewById(R.id.character_detail_outcome);
        charaDetailStrength = findViewById(R.id.character_detail_strength);
        charaDetailWeakness = findViewById(R.id.character_detail_weakness);
        charaDetailSkills = findViewById(R.id.character_detail_skills);

        charaDetailIv = findViewById(R.id.character_detail_Iv);

        editChara = findViewById(R.id.fab_edit_chara);

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
                        charaDetailRole.setText(dataSnapshot.child("role").getValue().toString());
                        charaDetailGoal.setText(dataSnapshot.child("goal").getValue().toString());
                        charaDetailOutcome.setText(dataSnapshot.child("outcome").getValue().toString());
                        charaDetailStrength.setText(dataSnapshot.child("strength").getValue().toString());
                        charaDetailWeakness.setText(dataSnapshot.child("weakness").getValue().toString());
                        charaDetailSkills.setText(dataSnapshot.child("skills").getValue().toString());
                        Glide.with(getApplicationContext()).load(dataSnapshot.child("imgUrl").getValue().toString()).into(charaDetailIv);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        editChara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                charaDetailIv.setImageResource(R.drawable.ic_baseline_face_24);
                Intent intent = new Intent(CharacterDetailActivity.this, EditCharacterActivity.class);
                startActivity(intent);
            }
        });
    }
}
