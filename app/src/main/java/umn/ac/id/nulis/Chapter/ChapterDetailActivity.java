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

import umn.ac.id.nulis.Adapter.ChapterAdapter;
import umn.ac.id.nulis.Adapter.ChapterDetailAdapter;
import umn.ac.id.nulis.Dialog.AddDialogChapterDetail;
import umn.ac.id.nulis.HelperClass.Chapter;
import umn.ac.id.nulis.HelperClass.ChapterDetail;
import umn.ac.id.nulis.R;

public class ChapterDetailActivity extends AppCompatActivity {
    private String bookId;
    private String chapterTitle;
    private String chapterId;

    FloatingActionButton fabAddChapter;
    RecyclerView recyclerView;
    ChapterDetailAdapter chapterDetailAdapter;

    DatabaseReference database;
    ArrayList<ChapterDetail> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_detail);

        //action bar
        SharedPreferences sp1 = this.getSharedPreferences("Chapter Info", MODE_PRIVATE);
        chapterTitle = sp1.getString("cTitle", null);
        chapterId = sp1.getString("cId", null);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(chapterTitle);

        //sp for bookId
        SharedPreferences sp2 = this.getSharedPreferences("Book Info", MODE_PRIVATE);
        bookId = sp2.getString("bId", null);

        fabAddChapter = findViewById(R.id.fab_add_chapter_detail);
        recyclerView = findViewById(R.id.rv_chapter_detail);
        database = FirebaseDatabase.getInstance("https://nulis-d3354-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users")
                .child(FirebaseAuth
                        .getInstance()
                        .getCurrentUser()
                        .getUid())
                .child("Book")
                .child(bookId)
                .child("Chapter")
                .child(chapterId)
                .child("content");

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        chapterDetailAdapter = new ChapterDetailAdapter(this, list);
        recyclerView.setAdapter(chapterDetailAdapter);
        chapterDetailAdapter.setOnItemClickListener(new ChapterDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("CARD CLICK", list.get(position).getCDetailId());
//                Intent intent = new Intent(getApplicationContext(), ChapterDetailActivity.class);
//
//                SharedPreferences spChapterId = getSharedPreferences("Chapter Info", MODE_PRIVATE);
//                SharedPreferences.Editor Ed = spChapterId.edit();
//
//                Ed.putString("cId", list.get(position).getCDetailId());
//                Ed.putString("cTitle", list.get(position).getChapterDetailTitle());
//                Ed.apply();
//
//                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChapterDetailActivity.this);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database.child(list.get(position).getCDetailId()).removeValue().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Toast.makeText(ChapterDetailActivity.this, "Chapter Detail is deleted", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ChapterDetailActivity.this, "Failed to delete chapter detail", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setMessage("Are you sure you want to delete this chapter detail?");
                builder.show();
            }

            @Override
            public void onEditClick(int position) {
                openDialogEdit(position);
            }
        });

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ChapterDetail chapterDetail =
                            new ChapterDetail(dataSnapshot.child("chapterDetailTitle").getValue().toString(),
                            dataSnapshot.child("chapterDetailContent").getValue().toString(),
                            dataSnapshot.getKey());
                    list.add(chapterDetail);
                    Log.d("DEBUG", dataSnapshot.getKey());
                }
                chapterDetailAdapter.notifyDataSetChanged();
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

    //menambahkan cerita kedalam chapter
    private void openDialog() {
        AddDialogChapterDetail addDialog = new AddDialogChapterDetail(bookId, chapterId);
        addDialog.show(getSupportFragmentManager(), "add chapter content");
    }

    private void openDialogEdit(int position) {
        Log.d("DEBUG", "Edit Button clicked");
//        AddDialogChapterDetail addDialog = new AddDialogChapterDetail(bookId, chapterId);
//        addDialog.show(getSupportFragmentManager(), "add chapter content");
    }

    //mengedit atau menambahkan detail chapter (seperti karakter, lokasi, dll).
    private void openDialogChapterDesc() {
//        AddDialogChapterDetail addDialog = new AddDialogChapterDetail(bookId, chapterId);
//        addDialog.show(getSupportFragmentManager(), "add chapter content");
    }
}