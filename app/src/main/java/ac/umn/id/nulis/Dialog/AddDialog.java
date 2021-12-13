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

import ac.umn.id.nulis.HelperClass.Book;
import ac.umn.id.nulis.R;

public class AddDialog extends AppCompatDialogFragment {

    TextInputLayout addTitle, addDesc;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_add_book, null);

        builder.setView(view)
                .setTitle("Add a book")
                .setNegativeButton("cancel", (dialogInterface, i) -> {

                })
                .setPositiveButton("ok", (dialogInterface, i) -> {
                    String addBookTitle = addTitle.getEditText().getText().toString().trim();
                    String addBookDesc = addDesc.getEditText().getText().toString().trim();

                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://nulis-d3354-default-rtdb.asia-southeast1.firebasedatabase.app/");
                    DatabaseReference myRef = database.getReference("Users");

                    Book book = new Book(addBookTitle, addBookDesc);

                    myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Book").push().setValue(book);

                });
        addTitle = view.findViewById(R.id.add_book_title);
        addDesc = view.findViewById(R.id.add_book_desc);

        return builder.create();
    }

}
