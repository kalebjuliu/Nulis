package umn.ac.id.nulis;

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

import com.google.firebase.database.DatabaseReference;

import umn.ac.id.nulis.Chapter.ChapterActivity;

public class MainMenu extends AppCompatActivity {
    RelativeLayout chapterCard, characterCard, locationCard;
    String bookId;

    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        chapterCard = findViewById(R.id.chaptersCard);
        characterCard = findViewById(R.id.characterCard);
        locationCard = findViewById(R.id.locationCard);

        //TextView test = findViewById(R.id.book_title_test);
        String appBarTitle = "";
        SharedPreferences sp1 = this.getSharedPreferences("Book Info", MODE_PRIVATE);
        bookId = sp1.getString("bId", null);
        appBarTitle = sp1.getString("bTitle", null);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(appBarTitle);

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