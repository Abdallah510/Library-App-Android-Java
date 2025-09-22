package edu.birzeit.project1.librarian_fragment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.birzeit.project1.R;
import edu.birzeit.project1.database.LibraryDataBase;

public class ReportsFragment extends Fragment {

    private TextView tvStudentCount, tvPopularBooks, tvOverdueItems,tvBookCount;
    private LibraryDataBase dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        tvStudentCount = view.findViewById(R.id.textViewStudentCount);
        tvPopularBooks = view.findViewById(R.id.textViewPopularBooks);
        tvOverdueItems = view.findViewById(R.id.textViewOverdueItems);
        tvBookCount = view.findViewById(R.id.textViewBookCount);
        Button btnGenerateSheet = view.findViewById(R.id.btnGenerateSheet);

        dbHelper = new LibraryDataBase(getContext(), LibraryDataBase.DATABASE_NAME, null, 1);

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM STUDENTS", null);

        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            tvStudentCount.setText("Registered Students: " + count);
        } else {
            tvStudentCount.setText("Registered Students: 0");
        }
        cursor.close();


        cursor = db.rawQuery("SELECT COUNT(*) FROM BOOKS", null);

        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);
            tvBookCount.setText("Registered Books: " + count);
        } else {
            tvBookCount.setText("No Registered Books");
        }
        cursor.close();

        String query = "SELECT B.TITLE, COUNT(R.BOOK_ID) AS book_count " +
                "FROM READING_LIST R " +
                "JOIN BOOKS B ON R.BOOK_ID = B.ID " +
                "GROUP BY R.BOOK_ID " +
                "ORDER BY book_count DESC LIMIT 3";

        cursor = db.rawQuery(query, null);
        StringBuilder popularBooks = new StringBuilder();

        if (cursor.moveToFirst()) {
            int placement = 1;
            do {
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                int count = cursor.getInt(cursor.getColumnIndexOrThrow("book_count"));

                popularBooks.append("TOP "+placement + ": " + title + " - Favorites " + count + "\n");

                placement++;
            } while (cursor.moveToNext());
        } else {
            popularBooks.append("No Data on Book popularity.");
        }

        tvPopularBooks.setText(popularBooks.toString());
        cursor.close();


        query = "SELECT B.TITLE, R.DUE_DATE " +
                "FROM RESERVATIONS R " +
                "JOIN BOOKS B ON R.BOOK_ID = B.ID " +
                "WHERE R.STATUS = 'Overdue' OR (date(R.DUE_DATE) < date('now') AND R.RETURN_DATE IS NULL)";

        cursor = db.rawQuery(query, null);
        StringBuilder overdueBooks = new StringBuilder();

        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String dueDate = cursor.getString(cursor.getColumnIndexOrThrow("due_date"));
                overdueBooks.append("• " + title + " (Due: " + dueDate + ")\n");
            } while (cursor.moveToNext());
        } else {
            overdueBooks.append("No overdue Books.");
        }

        tvOverdueItems.setText(overdueBooks.toString());
        cursor.close();


        btnGenerateSheet.setOnClickListener(v -> generateCSVReport());

        return view;
    }

    private void generateCSVReport() {
        File exportDir = new File(requireContext().getExternalFilesDir(null), "");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        File file = new File(exportDir, "LibraryReports.csv");

        try (FileWriter writer = new FileWriter(file)) {
            writer.append("Digital Library Generated Report\n\n");

            writer.append("Registered Students\n");
            writer.append(tvStudentCount.getText().toString()).append("\n\n");

            writer.append("Registered Books\n");
            writer.append(tvBookCount.getText().toString().replace("\n", ",\n")).append("\n\n");

            writer.append("Popular Books\n");
            writer.append(tvPopularBooks.getText().toString().replace("\n", ",\n")).append("\n\n");

            writer.append("Overdue Items\n");
            writer.append(tvOverdueItems.getText().toString().replace("\n", ",\n")).append("\n\n");



            writer.flush();

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