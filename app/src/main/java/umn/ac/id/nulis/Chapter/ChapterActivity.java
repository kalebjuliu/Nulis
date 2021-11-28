package umn.ac.id.nulis.Chapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import umn.ac.id.nulis.Adapter.BookAdapter;
import umn.ac.id.nulis.Adapter.ChapterAdapter;
import umn.ac.id.nulis.Dashboard;
import umn.ac.id.nulis.Dialog.AddDialog;
import umn.ac.id.nulis.Dialog.AddDialogChapter;
import umn.ac.id.nulis.HelperClass.Book;
import umn.ac.id.nulis.HelperClass.Chapter;
import umn.ac.id.nulis.MainMenu;
import umn.ac.id.nulis.R;

public class ChapterActivity extends AppCompatActivity {
    String bookId;
    FloatingActionButton fabAddChapter;
    RecyclerView recyclerView;
    ChapterAdapter chapterAdapter;

    DatabaseReference database;
    ArrayList<Chapter> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Chapters");

        SharedPreferences sp1 = this.getSharedPreferences("Book Info", MODE_PRIVATE);
        bookId = sp1.getString("bId", null);

        fabAddChapter = findViewById(R.id.fab_add_chapter);
        recyclerView = findViewById(R.id.rv_chapter);
        database = FirebaseDatabase.getInstance("https://nulis-d3354-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Book").child(bookId).child("Chapter");

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        chapterAdapter = new ChapterAdapter(this, list);
        recyclerView.setAdapter(chapterAdapter);
        chapterAdapter.setOnItemClickListener(new ChapterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("CARD CLICK", list.get(position).getTitle());
            }
            @Override
            public void onDeleteClick(int position) {
                Log.d("click", "click delete");
            }
        });

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Chapter chapter = new Chapter(dataSnapshot.child("title").getValue().toString(),
                            dataSnapshot.getKey());
                    list.add(chapter);
                    Log.d("DEBUG", dataSnapshot.getKey());
                }
                chapterAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        fabAddChapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    private void openDialog() {
        AddDialogChapter addDialog = new AddDialogChapter(bookId);
        addDialog.show(getSupportFragmentManager(), "add chapter");
    }
}