package ac.umn.id.nulis.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import ac.umn.id.nulis.HelperClass.Chapter;
import ac.umn.id.nulis.R;

public class AddDialogChapter extends AppCompatDialogFragment {
    TextInputLayout addTitle;
    String bId;

    public AddDialogChapter(String bId){
        this.bId = bId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_add_chapter, null);

        builder.setView(view)
                .setTitle("Add a chapter")
                .setNegativeButton("cancel", (dialogInterface, i) -> {

                })
                .setPositiveButton("ok", (dialogInterface, i) -> {
                    String addChapterTitle = addTitle.getEditText().getText().toString().trim();

                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://nulis-d3354-default-rtdb.asia-southeast1.firebasedatabase.app/");
                    DatabaseReference myRef = database.getReference("Users");

                    Chapter chapter = new Chapter(addChapterTitle);

                    myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Book").child(bId).child("Chapter").push().setValue(chapter);

                });
        addTitle = view.findViewById(R.id.add_chapter_title);

        return builder.create();
    }
}
