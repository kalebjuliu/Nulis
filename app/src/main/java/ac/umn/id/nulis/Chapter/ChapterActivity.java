package ac.umn.id.nulis.Chapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ac.umn.id.nulis.Adapter.ChapterAdapter;
import ac.umn.id.nulis.Dialog.AddDialogChapter;
import ac.umn.id.nulis.Dialog.EditDialogChapter;
import ac.umn.id.nulis.HelperClass.Chapter;
import ac.umn.id.nulis.R;

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

        SharedPreferences sp1 = this.getSharedPreferences("Book Info", MODE_PRIVATE);
        bookId = sp1.getString("bId", null);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Chapters");

        fabAddChapter = findViewById(R.id.fab_add_chapter);
        recyclerView = findViewById(R.id.rv_chapter);
        database = FirebaseDatabase.getInstance("https://nulis-d3354-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users")
                .child(FirebaseAuth.getInstance()
                        .getCurrentUser()
                        .getUid())
                .child("Book")
                .child(bookId)
                .child("Chapter");

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        chapterAdapter = new ChapterAdapter(this, list);
        recyclerView.setAdapter(chapterAdapter);
        chapterAdapter.setOnItemClickListener(new ChapterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("CARD CLICK", list.get(position).getcId());
                Intent intent = new Intent(getApplicationContext(), ChapterDetailActivity.class);

                SharedPreferences spChapterId = getSharedPreferences("Chapter Info", MODE_PRIVATE);
                SharedPreferences.Editor Ed = spChapterId.edit();

                Ed.putString("cId", list.get(position).getcId());
                Ed.putString("cTitle", list.get(position).getTitle());
                Ed.apply();

                startActivity(intent);
            }

            @Override
            public void onDeleteClick(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChapterActivity.this);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database.child(list.get(position).getcId()).removeValue().addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Toast.makeText(ChapterActivity.this, "Chapter is deleted", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ChapterActivity.this, "Failed to delete chapter", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setMessage("Are you sure you want to delete this chapter?");
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
                    Chapter chapter =
                            new Chapter(dataSnapshot.child("title").getValue().toString(),
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

    private void openDialogEdit(int position) {
        SharedPreferences sp1 = this.getSharedPreferences("Book Info", MODE_PRIVATE);
        bookId = sp1.getString("bId", null);

        Bundle args = new Bundle();
        args.putString("bookId", bookId);
        args.putString("chapterId", list.get(position).getcId());
        args.putString("chapterTitle", list.get(position).getTitle());

        EditDialogChapter editDialogChapter = new EditDialogChapter();
        editDialogChapter.setArguments(args);
        editDialogChapter.show(getSupportFragmentManager(), "edit chapter");
    }
}