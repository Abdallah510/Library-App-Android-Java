package edu.birzeit.project1.student_fragments;

import static edu.birzeit.project1.prelogin.LoginActivity.logedInId;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import edu.birzeit.project1.database.LibraryDataBase;
import edu.birzeit.project1.R;
import edu.birzeit.project1.entities.Reservation;
import edu.birzeit.project1.librarian_fragment.ReservationAdapter;


public class BorrowedBooksFragment extends Fragment {

    private RecyclerView recyclerView;
    private BorrowedBooksAdapter adapter;
    private List<Reservation> borrowedBooksList;
    private TextView tvEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_borrowed_books, container, false);
        LibraryDataBase db =new LibraryDataBase(requireContext(),LibraryDataBase.DATABASE_NAME,null,1);
        recyclerView = view.findViewById(R.id.recyclerViewBorrowedBooks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tvEmpty = view.findViewById(R.id.tvEmptyBorrowedBooks);

        borrowedBooksList = new ArrayList<>();
        Cursor allBorrowedBooks = db.getAllReservations();
        while (allBorrowedBooks.moveToNext()) {
            if(logedInId ==Integer.parseInt(allBorrowedBooks.getString(1)) && !allBorrowedBooks.getString(5).equals("Pending")){
                int id = allBorrowedBooks.getInt(0);
                int studentId = allBorrowedBooks.getInt(1);
                int bookId = allBorrowedBooks.getInt(2);
                String reservationDate = allBorrowedBooks.getString(3);
                String dueDate = allBorrowedBooks.getString(4);
                String status = allBorrowedBooks.getString(5);
                String collectionMethod = allBorrowedBooks.getString(6);
                String notes = allBorrowedBooks.getString(7);
                String returnDate = allBorrowedBooks.getString(8);
                double fineAmount = allBorrowedBooks.getDouble(9);
                borrowedBooksList.add(new Reservation(id, studentId, bookId, reservationDate,
                        dueDate, status, collectionMethod, notes, returnDate, fineAmount));
            }
        }
        if (borrowedBooksList == null || borrowedBooksList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new BorrowedBooksAdapter(requireContext(), borrowedBooksList);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

}
