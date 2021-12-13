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

import java.util.HashMap;
import java.util.Map;

import ac.umn.id.nulis.R;

public class EditDialogChapter extends AppCompatDialogFragment {
    TextInputLayout chapterTitleInput;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_add_chapter, null);

        Bundle mArgs = getArguments();
        String bookId = mArgs.getString("bookId");
        String chapterId = mArgs.getString("chapterId");
        String chapterTitle = mArgs.getString("chapterTitle");

        builder.setView(view)
                .setTitle("Edit chapter")
                .setNegativeButton("cancel", (dialogInterface, i) -> {

                })
                .setPositiveButton("save", (dialogInterface, i) -> {
                    String newChapterTitle = chapterTitleInput.getEditText().getText().toString().trim();

                    //mencegah mengupload data yang sama
                    if (!newChapterTitle.equals(chapterTitle)) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance("https://nulis-d3354-default-rtdb.asia-southeast1.firebasedatabase.app/");
                        DatabaseReference myRef = database.getReference("Users");

                        Map<String, Object> postValues = new HashMap<String, Object>();
                        postValues.put("title", newChapterTitle);

                        myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Book").child(bookId).child("Chapter").child(chapterId).updateChildren(postValues);
                    }
                });
        chapterTitleInput = view.findViewById(R.id.add_chapter_title);
        chapterTitleInput.getEditText().setText(chapterTitle);

        return builder.create();
    }
}