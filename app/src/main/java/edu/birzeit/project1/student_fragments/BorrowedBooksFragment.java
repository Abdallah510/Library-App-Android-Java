package edu.birzeit.project1.student_fragments;

import static edu.birzeit.project1.prelogin.LoginActivity.logedInId;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import edu.birzeit.project1.LibraryDataBase;


public class BorrowedBooksFragment extends Fragment {

    private RecyclerView recyclerView;
    private BorrowedBooksAdapter adapter;
    private List<BorrowedBooks> borrowedBooksList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_borrowed_books, container, false);
        LibraryDataBase db =new LibraryDataBase(requireContext(),LibraryDataBase.DATABASE_NAME,null,1);
        recyclerView = view.findViewById(R.id.recyclerViewBorrowedBooks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        borrowedBooksList = new ArrayList<>();
        Cursor allBorrowedBooks = db.getAllBorrowedBooks();
        while (allBorrowedBooks.moveToNext()) {
            if(logedInId ==Integer.parseInt(allBorrowedBooks.getString(1))){
                int id = allBorrowedBooks.getInt(allBorrowedBooks.getColumnIndexOrThrow("id"));
                int studentId = allBorrowedBooks.getInt(allBorrowedBooks.getColumnIndexOrThrow("studentId"));
                int bookId = allBorrowedBooks.getInt(allBorrowedBooks.getColumnIndexOrThrow("bookId"));
                String borrowDate = allBorrowedBooks.getString(allBorrowedBooks.getColumnIndexOrThrow("borrowDate"));
                String dueDate = allBorrowedBooks.getString(allBorrowedBooks.getColumnIndexOrThrow("dueDate"));
                String returnDate = allBorrowedBooks.getString(allBorrowedBooks.getColumnIndexOrThrow("returnDate"));
                String status = allBorrowedBooks.getString(allBorrowedBooks.getColumnIndexOrThrow("status"));
                double fineAmount = allBorrowedBooks.getDouble(allBorrowedBooks.getColumnIndexOrThrow("fineAmount"));
                BorrowedBooks book = new BorrowedBooks(
                        id, studentId, bookId, borrowDate, dueDate, returnDate, status, fineAmount
                );
                borrowedBooksList.add(book);
            }

        }
        adapter = new BorrowedBooksAdapter(requireContext(), borrowedBooksList);
        recyclerView.setAdapter(adapter);
        return view;
    }

}
