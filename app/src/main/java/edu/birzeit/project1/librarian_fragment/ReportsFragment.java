package edu.birzeit.project1.librarian_fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import android.os.Environment;
import android.widget.Button;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.birzeit.project1.R;
import edu.birzeit.project1.database.LibraryDataBase;

public class ReportsFragment extends Fragment {

    private TextView textViewStudentCount, textViewPopularBooks, textViewOverdueItems;
    private LibraryDataBase dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        // Initialize Views
        textViewStudentCount = view.findViewById(R.id.textViewStudentCount);
        textViewPopularBooks = view.findViewById(R.id.textViewPopularBooks);
        textViewOverdueItems = view.findViewById(R.id.textViewOverdueItems);
        Button btnGenerateSheet = view.findViewById(R.id.btnGenerateSheet);
        // Initialize Database Helper
        dbHelper = new LibraryDataBase(getContext(), LibraryDataBase.DATABASE_NAME, null, 1);

        // Load all report data immediately
        loadReportData();


        btnGenerateSheet.setOnClickListener(v -> generateCSVReport());

        return view;
    }

    private void loadReportData() {
        showStudentCount();
        showPopularBooks();
        showOverdueItems();
    }

    // 1. Count all registered students
    private void showStudentCount() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM STUDENTS", null);

        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            textViewStudentCount.setText("Registered Students: " + count);
        } else {
            textViewStudentCount.setText("Registered Students: 0");
        }
        cursor.close();
    }

    // 2. Show most popular books by counting how many times they appear in the reading list
    private void showPopularBooks() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT B.TITLE, COUNT(R.BOOK_ID) AS book_count " +
                "FROM READING_LIST R " +
                "JOIN BOOKS B ON R.BOOK_ID = B.ID " +
                "GROUP BY R.BOOK_ID " +
                "ORDER BY book_count DESC LIMIT 5";

        Cursor cursor = db.rawQuery(query, null);
        StringBuilder popularBooks = new StringBuilder();

        if (cursor.moveToFirst()) {
            int rank = 1;
            do {
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                int count = cursor.getInt(cursor.getColumnIndexOrThrow("book_count"));

                popularBooks.append(rank)
                        .append(". ")
                        .append(title)
                        .append(" - Added ")
                        .append(count)
                        .append(" times\n");

                rank++;
            } while (cursor.moveToNext());
        } else {
            popularBooks.append("No popular books found.");
        }

        textViewPopularBooks.setText(popularBooks.toString());
        cursor.close();
    }

    // 3. Show overdue borrowed books
    private void showOverdueItems() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String query = "SELECT B.TITLE, R.DUE_DATE " +
                "FROM RESERVATIONS R " +
                "JOIN BOOKS B ON R.BOOK_ID = B.ID " +
                "WHERE R.STATUS = 'Overdue' OR (date(R.DUE_DATE) < date('now') AND R.RETURN_DATE IS NULL)";

        Cursor cursor = db.rawQuery(query, null);
        StringBuilder overdueItems = new StringBuilder();

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String dueDate = cursor.getString(cursor.getColumnIndexOrThrow("due_date"));

                overdueItems.append("• ")
                        .append(title)
                        .append(" (Due: ")
                        .append(dueDate)
                        .append(")\n");
            } while (cursor.moveToNext());
        } else {
            overdueItems.append("No overdue items found.");
        }

        textViewOverdueItems.setText(overdueItems.toString());
        cursor.close();
    }

    private void generateCSVReport() {
        File exportDir = new File(requireContext().getExternalFilesDir(null), "");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "LibraryReports.csv");

        try (FileWriter writer = new FileWriter(file)) {
            // Write headers
            writer.append("Library Reports\n\n");

            // 1. Student Count
            writer.append("Registered Students\n");
            writer.append(textViewStudentCount.getText().toString()).append("\n\n");

            // 2. Popular Books
            writer.append("Popular Books\n");
            writer.append(textViewPopularBooks.getText().toString().replace("\n", ",\n")).append("\n\n");

            // 3. Overdue Items
            writer.append("Overdue Items\n");
            writer.append(textViewOverdueItems.getText().toString().replace("\n", ",\n")).append("\n\n");

            writer.flush();

            // Inform user
            android.widget.Toast.makeText(getContext(),
                    "CSV generated at: " + file.getAbsolutePath(),
                    android.widget.Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
            android.widget.Toast.makeText(getContext(),
                    "Error generating CSV: " + e.getMessage(),
                    android.widget.Toast.LENGTH_LONG).show();
        }
    }

}