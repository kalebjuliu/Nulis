package umn.ac.id.nulis.Dialog;

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

import umn.ac.id.nulis.HelperClass.Location;
import umn.ac.id.nulis.R;

public class AddDialogLocation extends AppCompatDialogFragment {

    TextInputLayout addTitle;
    String bId;

    public AddDialogLocation(String bId){
        this.bId = bId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_add_location, null);

        builder.setView(view)
                .setTitle("Add a Location")
                .setNegativeButton("cancel", (dialogInterface, i) -> {

                })
                .setPositiveButton("ok", (dialogInterface, i) -> {
                    String addLocationTitle = addTitle.getEditText().getText().toString().trim();

                    FirebaseDatabase database = FirebaseDatabase.getInstance("https://nulis-d3354-default-rtdb.asia-southeast1.firebasedatabase.app/");
                    DatabaseReference myRef = database.getReference("Users");

                    Location location = new Location(addLocationTitle);

                    myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Book").child(bId).child("Location").push().setValue(location);

                });
        addTitle = view.findViewById(R.id.add_location_title);

        return builder.create();
    }
}
