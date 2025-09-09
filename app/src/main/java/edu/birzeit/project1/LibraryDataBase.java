package edu.birzeit.project1;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


public class LibraryDataBase extends SQLiteOpenHelper {

    public LibraryDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Books Table
        db.execSQL("CREATE TABLE Books (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "author TEXT, " +
                "isbn TEXT, " +
                "category TEXT, " +
                "availability INTEGER DEFAULT 1, " +
                "cover_url TEXT, " +
                "publication_year INTEGER" +
                ");");

        // Create Students Table
        db.execSQL("CREATE TABLE Students (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "university_id TEXT UNIQUE NOT NULL, " +
                "first_name TEXT, " +
                "last_name TEXT, " +
                "email TEXT UNIQUE, " +
                "password_hash TEXT, " +
                "department TEXT, " +
                "level TEXT" +
                ");");

        // Create Reservations Table
        db.execSQL("CREATE TABLE Reservations (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id INTEGER, " +
                "book_id INTEGER, " +
                "reservation_date TEXT, " +
                "due_date TEXT, " +
                "status TEXT, " +
                "FOREIGN KEY(student_id) REFERENCES Students(id), " +
                "FOREIGN KEY(book_id) REFERENCES Books(id)" +
                ");");

        // Create Reading_List Table
        db.execSQL("CREATE TABLE Reading_List (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id INTEGER, " +
                "book_id INTEGER, " +
                "added_date TEXT, " +
                "FOREIGN KEY(student_id) REFERENCES Students(id), " +
                "FOREIGN KEY(book_id) REFERENCES Books(id)" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
