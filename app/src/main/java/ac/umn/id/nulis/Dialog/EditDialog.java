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

public class EditDialog extends AppCompatDialogFragment {
    TextInputLayout bookTitleInput, bookDescInput;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_add_book, null);

        Bundle mArgs = getArguments();
        String bookId = mArgs.getString("bookId");
        String bookTitle = mArgs.getString("bookTitle");
        String bookDesc = mArgs.getString("bookDesc");

        builder.setView(view)
                .setTitle("Edit book")
                .setNegativeButton("cancel", (dialogInterface, i) -> {

                })
                .setPositiveButton("save", (dialogInterface, i) -> {
                    String newBookTitle = bookTitleInput.getEditText().getText().toString().trim();
                    String newBookDesc = bookDescInput.getEditText().getText().toString().trim();

                    //mencegah mengupload data yang sama
                    if((!newBookTitle.equals(bookTitle)) || (!newBookDesc.equals(bookTitle))){
                        FirebaseDatabase database = FirebaseDatabase.getInstance("https://nulis-d3354-default-rtdb.asia-southeast1.firebasedatabase.app/");
                        DatabaseReference myRef = database.getReference("Users");

                        Map<String, Object> postValues = new HashMap<String,Object>();
                        postValues.put("title", newBookTitle);
                        postValues.put("desc", newBookDesc);

                        myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Book").child(bookId).updateChildren(postValues);
                    }
                });
        bookTitleInput = view.findViewById(R.id.add_book_title);
        bookDescInput = view.findViewById(R.id.add_book_desc);

        bookTitleInput.getEditText().setText(bookTitle);
        bookDescInput.getEditText().setText(bookDesc);

        return builder.create();
    }
}
