package umn.ac.id.nulis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import umn.ac.id.nulis.Chapter.ChapterActivity;
import umn.ac.id.nulis.HelperClass.Chapter;

public class MainMenu extends AppCompatActivity {
    RelativeLayout chapterCard, characterCard, locationCard;
    TextView tvChapterCount;
    String bookId;
    int chapterCount;

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        chapterCard = findViewById(R.id.chaptersCard);
        characterCard = findViewById(R.id.characterCard);
        locationCard = findViewById(R.id.locationCard);
        tvChapterCount = findViewById(R.id.tv_chapter_count);

        //TextView test = findViewById(R.id.book_title_test);
        String appBarTitle = "";
        SharedPreferences sp1 = this.getSharedPreferences("Book Info", MODE_PRIVATE);
        bookId = sp1.getString("bId", null);
        appBarTitle = sp1.getString("bTitle", null);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(appBarTitle);

        database = FirebaseDatabase.getInstance("https://nulis-d3354-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Book").child(bookId).child("Chapter");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Log.e(dataSnapshot.getKey(),dataSnapshot.getChildrenCount() + "");
                    chapterCount += dataSnapshot.getChildrenCount();

                }
                Log.d("COUNT", chapterCount + "");
                tvChapterCount.setText("Total chapter: " + chapterCount);
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
                Log.d("CARD", "character");
            }
        });

        locationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CARD", "location");
            }
        });


    }
}