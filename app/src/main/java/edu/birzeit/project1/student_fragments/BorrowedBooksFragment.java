package edu.birzeit.project1.student_fragments;

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

import edu.birzeit.project1.R;

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

        recyclerView = view.findViewById(R.id.recyclerViewBorrowedBooks);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        borrowedBooksList = new ArrayList<>();
        adapter = new BorrowedBooksAdapter(requireContext(), borrowedBooksList);
        recyclerView.setAdapter(adapter);

        loadBorrowedBooks();

        return view;
    }

    private void loadBorrowedBooks() {
        borrowedBooksList.add(new BorrowedBooks(
                1, 101, "01-09-2025", "10-09-2025", "", "Active", 0
        ));
        borrowedBooksList.add(new BorrowedBooks(
                2, 102, "20-08-2025", "30-08-2025", "29-08-2025", "Returned", 0
        ));
        borrowedBooksList.add(new BorrowedBooks(
                1, 103, "01-08-2025", "15-08-2025", "", "Overdue", 5
        ));

        adapter.notifyDataSetChanged();
    }
}
