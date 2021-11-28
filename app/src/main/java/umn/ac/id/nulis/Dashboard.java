package umn.ac.id.nulis;

import androidx.annotation.NonNull;
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
import umn.ac.id.nulis.Dialog.AddDialog;
import umn.ac.id.nulis.HelperClass.Book;

public class Dashboard extends AppCompatActivity {

    FloatingActionButton fabAddBook;
    RecyclerView recyclerView;
    BookAdapter bookAdapter;

    DatabaseReference database;
    ArrayList<Book> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        fabAddBook = findViewById(R.id.fab_add_book);
        recyclerView = findViewById(R.id.rv_book);
        database = FirebaseDatabase.getInstance("https://nulis-d3354-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Book");
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        bookAdapter = new BookAdapter(this, list);
        recyclerView.setAdapter(bookAdapter);
        bookAdapter.setOnItemClickListener(new BookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("CARD CLICK", list.get(position).getbId());
                Intent intent = new Intent(getApplicationContext(), MainMenu.class);

                SharedPreferences spBookId = getSharedPreferences("Book Info", MODE_PRIVATE);
                SharedPreferences.Editor Ed = spBookId.edit();
                Ed.putString("bId", list.get(position).getbId());
                Ed.putString("bTitle", list.get(position).getTitle());

                Ed.apply();

                startActivity(intent);
            }
            @Override
            public void onDeleteClick(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database.child(list.get(position).getbId()).removeValue().addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()){
                                Toast.makeText(Dashboard.this, "Book is deleted", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(Dashboard.this, "Failed to delete book", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setMessage("Are you sure you want to delete this book?");
                builder.show();
            }
        });

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Book book = new Book(dataSnapshot.child("title").getValue().toString(),
                            dataSnapshot.child("desc").getValue().toString(),
                            dataSnapshot.getKey());
                    list.add(book);
                    Log.d("DEBUG", dataSnapshot.getKey());
                }
                bookAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        fabAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });


    }

    private void openDialog() {
        AddDialog addDialog = new AddDialog();
        addDialog.show(getSupportFragmentManager(), "add book");
    }
}