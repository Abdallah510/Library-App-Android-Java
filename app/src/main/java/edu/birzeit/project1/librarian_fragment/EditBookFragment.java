package edu.birzeit.project1.librarian_fragment;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import edu.birzeit.project1.R;
import edu.birzeit.project1.database.LibraryDataBase;
import edu.birzeit.project1.entities.Book;
import edu.birzeit.project1.entities.Product;
import edu.birzeit.project1.prelogin.LoginActivity;
import edu.birzeit.project1.student_fragments.BookAdapter;
import edu.birzeit.project1.student_fragments.ConnectionAsyncTask;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditBookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditBookFragment extends DialogFragment {

    private int bookId = BookAdapter.BOOK_ID;
    private int studentId = LoginActivity.logedInId;
    private int position;





    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;





    public EditBookFragment() {
        // Required empty public constructor
    }

    private EditText etBookId, etBookTitle, etBookAuthor, etBookIsbn, etBookYear, etBookCoverUrl;
    private Spinner spinnerCategory, spinnerAvailability;
    private Button btnCancel, btnDone;
    private ImageView imageBookCover;


    public static EditBookFragment newInstance(String param1, String param2) {
        EditBookFragment fragment = new EditBookFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.MyAlertDialogTheme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (getArguments() != null) {
            bookId = getArguments().getInt("book_id");
            studentId = getArguments().getInt("student_id");
            position = getArguments().getInt("position");
        }

        View view = inflater.inflate(R.layout.fragment_edit_book, container, false);
        etBookId = view.findViewById(R.id.editBookId);
        etBookTitle = view.findViewById(R.id.editBookTitle);
        etBookAuthor = view.findViewById(R.id.editBookAuthor);
        etBookIsbn = view.findViewById(R.id.editBookIsbn);
        etBookYear = view.findViewById(R.id.editBookYear);
        etBookCoverUrl = view.findViewById(R.id.editBookCoverUrl);

        spinnerCategory = view.findViewById(R.id.spinnerCategory);

        List<String> spinnerItems = new ArrayList<>();
        for (Product product : ConnectionAsyncTask.products) {
            spinnerItems.add(product.getName());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, spinnerItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerAdapter);

        spinnerAvailability = view.findViewById(R.id.spinnerAvailability);

        btnCancel = view.findViewById(R.id.btnCancel);
        btnDone = view.findViewById(R.id.btnDone);

        imageBookCover = view.findViewById(R.id.imageBookCover);
        etBookCoverUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // Load the image from the new URL
                Picasso.get()
                        .load(s.toString()).into(imageBookCover);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
        LibraryDataBase db = new LibraryDataBase(requireContext(), LibraryDataBase.DATABASE_NAME, null, 1);
        if (bookId!=-1) {

            Cursor thisBook = db.getBookById(bookId);
            if (thisBook.moveToFirst()) {
                etBookId.setText(String.valueOf(thisBook.getInt(0)));
                etBookTitle.setText(thisBook.getString(1));
                etBookAuthor.setText(thisBook.getString(2));
                etBookIsbn.setText(thisBook.getString(3));
                etBookYear.setText(String.valueOf(thisBook.getInt(7)));
                etBookCoverUrl.setText(thisBook.getString(6));
                spinnerCategory.setSelection(thisBook.getInt(4));
                String availability = thisBook.getString(5);


                int spinnerPosition = 0;
                for (int i = 0; i < spinnerAvailability.getCount(); i++) {
                    if (spinnerAvailability.getItemAtPosition(i).toString().equalsIgnoreCase(availability)) {
                        spinnerPosition = i;
                        break;
                    }
                }

                spinnerAvailability.setSelection(spinnerPosition, true);

            }
        }


        btnCancel.setOnClickListener(v -> dismiss());
        btnDone.setOnClickListener(v -> {
            try{
                bookId = Integer.parseInt(etBookId.getText().toString());
            }
            catch(Exception e){
                bookId = -1;
                e.printStackTrace();
            }

            String title = etBookTitle.getText().toString();
            String author = etBookAuthor.getText().toString();
            String isbn = etBookIsbn.getText().toString();
            String coverUrl = etBookCoverUrl.getText().toString();
            int publicationYear = Integer.parseInt(etBookYear.getText().toString());
            String category = spinnerCategory.getSelectedItem().toString();
            String availability = spinnerAvailability.getSelectedItem().toString();
            Book updatedBook = new Book(bookId, title, author, category, availability, coverUrl, isbn, publicationYear);

            if (bookId==-1){
                Book book = new Book(title, author,category,availability,coverUrl, isbn,  publicationYear );
                book.setID((int) db.insertBook(book));
                BookAdapter.BOOKADAPTER.addBook(book);

            }
            else{
                BookAdapter.BOOKADAPTER.updateBook(position, updatedBook);
                db.updateBook(bookId, title, author, isbn, category, availability, coverUrl, publicationYear);
                BookAdapter.BOOKADAPTER.updateBook(position, updatedBook);
            }



            dismiss();


        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Ensure fragment is shown as dialog with custom size
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(1000, 1500);
        }

    }
}