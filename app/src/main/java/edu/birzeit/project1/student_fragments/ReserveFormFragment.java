package edu.birzeit.project1.student_fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import edu.birzeit.project1.database.LibraryDataBase;
import edu.birzeit.project1.R;
import edu.birzeit.project1.prelogin.LoginActivity;

public class ReserveFormFragment extends DialogFragment {

    private int bookId = BookAdapter.BOOK_ID;
    private int studentId = LoginActivity.logedInId;
    private int position;

    private Spinner spinnerDuration;
    private RadioGroup radioCollectionMethod;
    private EditText editNotes;
    private Button btnConfirm;

    public ReserveFormFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reserve_form, container, false);

        // Get arguments
        if (getArguments() != null) {
            bookId = getArguments().getInt("book_id");
            studentId = getArguments().getInt("student_id");
            position = getArguments().getInt("position");
        }

        // Bind views
        spinnerDuration = view.findViewById(R.id.spinnerDuration);
        radioCollectionMethod = view.findViewById(R.id.radioCollectionMethod);
        editNotes = view.findViewById(R.id.editNotes);
        btnConfirm = view.findViewById(R.id.btnConfirm);
        LibraryDataBase db =new LibraryDataBase(requireContext(),LibraryDataBase.DATABASE_NAME,null,1);
        btnConfirm.setOnClickListener(v -> {
            Cursor thisBook = db.getBookById(bookId);


            saveReservation();
            if (thisBook != null && thisBook.moveToFirst()) {
                String availability = "Reserved";

                BookAdapter.BOOKADAPTER.updateBookAvailability(position, availability);
            }

            if (thisBook != null) thisBook.close();
            dismiss();
        });



        return view;
    }

    private void saveReservation() {
        // Get selected duration (1-4 weeks)
        int durationWeeks = Integer.parseInt(spinnerDuration.getSelectedItem().toString());

        // Get collection method
        int selectedId = radioCollectionMethod.getCheckedRadioButtonId();
        String collectionMethod = selectedId == R.id.radioPickup ? "Pickup" : "Digital";

        String notes = editNotes.getText().toString();



        LibraryDataBase db =new LibraryDataBase(requireContext(),LibraryDataBase.DATABASE_NAME,null,1);
        SQLiteDatabase sqlDb = db.getWritableDatabase();
        sqlDb.execSQL("UPDATE Books SET availability = 'Reserved' WHERE id = ?", new Object[]{bookId});
        boolean success = db.insertReservation(studentId, bookId, durationWeeks, collectionMethod, notes);

        if (success) {
            Toast.makeText(getContext(), "Reservation placed successfully", Toast.LENGTH_SHORT).show();
            dismiss();
        } else {
            Toast.makeText(getContext(), "Failed to place reservation", Toast.LENGTH_SHORT).show();
        }
    }
}
