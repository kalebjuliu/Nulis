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

public class EditDialogChapterDetail extends AppCompatDialogFragment {
    TextInputLayout contentTitleInput;
    TextInputLayout contentContentInput;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_add_chapter_detail, null);

        Bundle mArgs = getArguments();
        String bId = mArgs.getString("bookId");
        String cId = mArgs.getString("chapterId");
        String contentId = mArgs.getString("contentId");
        String contentTitle = mArgs.getString("contentTitle");
        String contentContent = mArgs.getString("contentContent");

        builder.setView(view)
                .setTitle("Edit a chapter detail")
                .setNegativeButton("cancel", (dialogInterface, i) -> {

                })
                .setPositiveButton("Edit", (dialogInterface, i) -> {
                    String newContentTitle = contentTitleInput.getEditText().getText().toString().trim();
                    String newContentContent = contentContentInput.getEditText().getText().toString().trim();

                    if (!newContentTitle.equals(contentTitle) || !newContentContent.equals(contentContent)) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance("https://nulis-d3354-default-rtdb.asia-southeast1.firebasedatabase.app/");
                        DatabaseReference myRef = database.getReference("Users");

                        Map<String, Object> postValues = new HashMap<String, Object>();
                        postValues.put("chapterDetailTitle", newContentTitle);
                        postValues.put("chapterDetailContent", newContentContent);

                        myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("Book")
                                .child(bId)
                                .child("Chapter")
                                .child(cId)
                                .child("content")
                                .child(contentId)
                                .updateChildren(postValues);
                    }
                });
        contentTitleInput = view.findViewById(R.id.add_chapterDetail_title);
        contentContentInput = view.findViewById(R.id.add_chapterDetail_content);

        contentTitleInput.getEditText().setText(contentTitle);
        contentContentInput.getEditText().setText(contentContent);

        return builder.create();
    }
}
