package edu.birzeit.project1.student_fragments;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import edu.birzeit.project1.database.LibraryDataBase;
import edu.birzeit.project1.R;
import edu.birzeit.project1.entities.Book;
import edu.birzeit.project1.entities.Product;

public class NewArrivalsFragment extends Fragment {

    private static final int LIMIT = 1;
    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private List<Book> bookList;

    private LibraryDataBase dataBaseHelper;
    private Spinner spinnerSort;
    private EditText minYearInput, maxYearInput, tvSearchbar;
    private CheckBox checkAvailable;


    public NewArrivalsFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.fragment_new_arrivals, container, false);
        dataBaseHelper = new LibraryDataBase(requireContext(), LibraryDataBase.DATABASE_NAME, null, 1);
        Cursor allBooks = dataBaseHelper.getLatestBooks(LIMIT);

        recyclerView = view.findViewById(R.id.recycler_books);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        tvSearchbar = view.findViewById(R.id.search_bar);


        spinnerSort = view.findViewById(R.id.spinner_sort);
        minYearInput = view.findViewById(R.id.min_year);
        maxYearInput = view.findViewById(R.id.max_year);
        checkAvailable = view.findViewById(R.id.check_available);

        tvSearchbar.setVisibility(View.INVISIBLE);
        spinnerSort.setVisibility(View.GONE);
        minYearInput.setVisibility(View.GONE);
        maxYearInput.setVisibility(View.GONE);
        checkAvailable.setVisibility(View.GONE);





        bookList = new ArrayList<>();

        if (allBooks != null && allBooks.moveToFirst()) {
            do {
                int id = Integer.parseInt(allBooks.getString(allBooks.getColumnIndexOrThrow("id")));
                String title = allBooks.getString(allBooks.getColumnIndexOrThrow("title"));
                String author = allBooks.getString(allBooks.getColumnIndexOrThrow("author"));
                String category = allBooks.getString(allBooks.getColumnIndexOrThrow("category"));
                String availability = allBooks.getString(allBooks.getColumnIndexOrThrow("availability"));
                String coverUrl = allBooks.getString(allBooks.getColumnIndexOrThrow("cover_url"));
                String isbn = allBooks.getString(allBooks.getColumnIndexOrThrow("isbn"));
                int publicationYear = allBooks.getInt(allBooks.getColumnIndexOrThrow("publication_year"));

                Book book = new Book(id,title, author, category, availability, coverUrl, isbn, publicationYear);
                bookList.add(book);
            } while (allBooks.moveToNext());
            allBooks.close();
        }

        Spinner spinnerSort = view.findViewById(R.id.spinner_sort);

        List<String> spinnerItems = new ArrayList<>();
        spinnerItems.add("Categories");
        for (Product product : ConnectionAsyncTask.products) {
            spinnerItems.add(product.getName());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, spinnerItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(spinnerAdapter);

        EditText minYearInput = view.findViewById(R.id.min_year);
        EditText maxYearInput = view.findViewById(R.id.max_year);

        CheckBox checkAvailable = view.findViewById(R.id.check_available);


        adapter = new BookAdapter(requireContext(), bookList, true,false, new BookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Book book) {

            }
        });


        recyclerView.setAdapter(adapter);



        return view;
    }


    private void applyFilter() {
        String search = tvSearchbar.getText().toString();
        String availability = checkAvailable.isChecked() ? "Available" : null;
        String category = spinnerSort.getSelectedItemPosition() == 0 ? null : spinnerSort.getSelectedItem().toString();
        int minYear = 0, maxYear = 9999;
        try { minYear = Integer.parseInt(minYearInput.getText().toString()); } catch (NumberFormatException ignored) { }
        try { maxYear = Integer.parseInt(maxYearInput.getText().toString()); } catch (NumberFormatException ignored) { }

        bookList = new ArrayList<>();
        Cursor someBooks = dataBaseHelper.getFilteredBooksWithSearch(search, category, availability, minYear, maxYear);
        while (someBooks.moveToNext()) {
            int id = Integer.parseInt(someBooks.getString(someBooks.getColumnIndexOrThrow("id")));
            String title = someBooks.getString(someBooks.getColumnIndexOrThrow("title"));
            String author = someBooks.getString(someBooks.getColumnIndexOrThrow("author"));
            String cat = someBooks.getString(someBooks.getColumnIndexOrThrow("category"));
            String avail = someBooks.getString(someBooks.getColumnIndexOrThrow("availability"));
            String coverUrl = someBooks.getString(someBooks.getColumnIndexOrThrow("cover_url"));
            String isbn = someBooks.getString(someBooks.getColumnIndexOrThrow("isbn"));
            int publicationYear = someBooks.getInt(someBooks.getColumnIndexOrThrow("publication_year"));

            Book book = new Book(id,title, author, cat, avail, coverUrl, isbn, publicationYear);
            bookList.add(book);
        }
        adapter.updateList(bookList);
    }
}
