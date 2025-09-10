package edu.birzeit.project1;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class BookCatalogFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private List<Book> bookList;

    public BookCatalogFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_catalog, container, false);

        recyclerView = view.findViewById(R.id.recycler_books);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        bookList = new ArrayList<>();
        adapter = new BookAdapter(getContext(), bookList, new BookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Book book) {
                // Handle item click here
            }
        });
        recyclerView.setAdapter(adapter);


        adapter.addBook(new Book("Title 1", "Author A", "Science", "Available", "","1234567890", 2020));
        adapter.addBook(new Book("Title 2", "Author B", "Math", "Borrowed", "","0987654321", 2022));

        return view;
    }

    // Method to add books dynamically from API or DB
    public void addBook(Book book) {
        if(adapter != null){
            adapter.addBook(book);
        }
    }
}
