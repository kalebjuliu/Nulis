package umn.ac.id.nulis;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.widget.TextView;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        TextView test = findViewById(R.id.book_title_test);
        String testString = "";

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            testString = extras.getString("bookTitle");
            Log.d("ID", extras.getString("bookId"));
            test.setText(extras.getString("bookId"));
        }
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(testString);


    }
}