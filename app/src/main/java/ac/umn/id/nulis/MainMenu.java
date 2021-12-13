package ac.umn.id.nulis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import ac.umn.id.nulis.Chapter.ChapterActivity;
import ac.umn.id.nulis.Character.CharacterActivity;
import ac.umn.id.nulis.Location.LocationActivity;

public class MainMenu extends AppCompatActivity {
    RelativeLayout chapterCard, characterCard, locationCard;
    TextView tvChapterCount, tvLocationCount, tvCharacterCount;
    String bookId;
    Query getChapter, getLocation, getChara;
    int chapterCount, locationCount, charaCount;

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        chapterCard = findViewById(R.id.chaptersCard);
        characterCard = findViewById(R.id.characterCard);
        locationCard = findViewById(R.id.locationCard);
        tvChapterCount = findViewById(R.id.tv_chapter_count);
        tvLocationCount = findViewById(R.id.tv_location_count);
        tvCharacterCount = findViewById(R.id.tv_chara_count);

        //TextView test = findViewById(R.id.book_title_test);
        String appBarTitle = "";
        SharedPreferences sp1 = this.getSharedPreferences("Book Info", MODE_PRIVATE);
        bookId = sp1.getString("bId", null);
        appBarTitle = sp1.getString("bTitle", null);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(appBarTitle);

        database = FirebaseDatabase.getInstance("https://nulis-d3354-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("Users");
        getChapter = database.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Book").child(bookId).child("Chapter");
        getLocation = database.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Book").child(bookId).child("Location");
        getChara = database.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Book").child(bookId).child("Character");


        getChapter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(!dataSnapshot.getKey().isEmpty()){
                        chapterCount += 1;
                    }

                }
                tvChapterCount.setText("Total chapter: " + chapterCount);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        getChara.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(!dataSnapshot.getKey().isEmpty()){
                        charaCount += 1;
                    }

                }
                tvCharacterCount.setText("Total character: " + charaCount);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        getLocation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Log.d("COUNT Location", dataSnapshot.getKey());
                    if(!dataSnapshot.getKey().isEmpty()){
                        locationCount += 1;
                    }
                }
                tvLocationCount.setText("Total location: " + locationCount);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        chapterCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChapterActivity.class);
                startActivity(intent);
            }
        });

        characterCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CharacterActivity.class);
                startActivity(intent);
            }
        });

        locationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LocationActivity.class);
                startActivity(intent);
            }
        });


    }
}