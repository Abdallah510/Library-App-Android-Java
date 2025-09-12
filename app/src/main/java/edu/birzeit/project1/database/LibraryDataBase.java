package edu.birzeit.project1.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import edu.birzeit.project1.entities.Book;
import edu.birzeit.project1.entities.Student;


public class LibraryDataBase extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "Library_DB.db";
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
                "level TEXT, " +
                "phone_number TEXT" +
                ");");

        // Create Reservations Table
//        db.execSQL("CREATE TABLE Reservations (" +
//                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                "student_id INTEGER, " +
//                "book_id INTEGER, " +
//                "reservation_date TEXT, " +
//                "due_date TEXT, " +
//                "status TEXT, " +
//                "FOREIGN KEY(student_id) REFERENCES Students(id), " +
//                "FOREIGN KEY(book_id) REFERENCES Books(id)" +
//                ");");
        db.execSQL("CREATE TABLE Reservations (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id INTEGER, " +
                "book_id INTEGER, " +
                "reservation_date TEXT, " +
                "due_date TEXT, " +
                "status TEXT, " +
                "collection_method TEXT, " +
                "notes TEXT, " +
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
        // Create Borrowings Table
        db.execSQL("CREATE TABLE Borrowings (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id INTEGER NOT NULL, " +
                "book_id INTEGER NOT NULL, " +
                "borrow_date TEXT NOT NULL, " +
                "due_date TEXT NOT NULL, " +
                "return_date TEXT, " +
                "status TEXT CHECK(status IN ('Active','Overdue','Returned','Extended')), " +
                "fine_amount REAL DEFAULT 0, " +
                "FOREIGN KEY(student_id) REFERENCES Students(id), " +
                "FOREIGN KEY(book_id) REFERENCES Books(id)" +
                ");");
    }
    public void insertStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("university_id", student.getUniversityId());
        values.put("first_name", student.getFirstName());
        values.put("last_name", student.getLastName());
        values.put("email", student.getEmail());
        values.put("password_hash", student.getPasswordHash());
        values.put("department", student.getDepartment());
        values.put("level", student.getLevel());
        values.put("phone_number", student.getPhoneNumber());
        db.insert("Students", null, values);
        db.close();
    }

//    public boolean insertReservation(int studentId, int bookId, int durationWeeks, String collectionMethod, String notes) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//
//        long now = System.currentTimeMillis();
//        long due = now + durationWeeks * 7 * 24 * 60 * 60 * 1000;
//
//        String reservationDate = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(now));
//        String dueDate = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date(due));
//
//        values.put("student_id", studentId);
//        values.put("book_id", bookId);
//        values.put("reservation_date", reservationDate);
//        values.put("due_date", dueDate);
//        values.put("status", "Pending"); // set default status
//        values.put("collection_method", collectionMethod);
//        values.put("notes", notes);
//
//        long result = db.insert("Reservations", null, values);
//        return result != -1;
//    }

    public boolean insertReservation(int studentId, int bookId, int durationWeeks,
                                     String collectionMethod, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();


        long now = System.currentTimeMillis();
        long due = now + durationWeeks * 7L * 24 * 60 * 60 * 1000;

        String reservationDate = new java.text.SimpleDateFormat("yyyy/MM/dd")
                .format(new java.util.Date(now));
        String dueDate = new java.text.SimpleDateFormat("yyyy/MM/dd")
                .format(new java.util.Date(due));

        values.put("student_id", studentId);
        values.put("book_id", bookId);
        values.put("reservation_date", reservationDate);
        values.put("due_date", dueDate);
        values.put("status", "Pending");             // default pending
        values.put("collection_method", collectionMethod);
        values.put("notes", notes);

        long result = db.insert("Reservations", null, values);
        return result != -1;
    }



    public void insertBook(Book book) {
        SQLiteDatabase libraryDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", book.getTitle());
        values.put("author", book.getAuthor());
        values.put("category", book.getCategory());
        values.put("availability", book.getAvailability());
        values.put("cover_url", book.getCoverUrl());
        values.put("isbn", book.getIsbn());
        values.put("publication_year", book.getPublicationYear());
        libraryDatabase.insert("Books", null, values);
        libraryDatabase.close();
    }

    public Cursor getAllStudents() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM Students", null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
   public Cursor getAllBooks() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM Books", null);
   }

    public Cursor getFilteredBooksWithSearch(String search, String category, String availability, int minYear, int maxYear) {
        SQLiteDatabase db = getReadableDatabase();
        StringBuilder query = new StringBuilder("SELECT * FROM Books WHERE publication_year BETWEEN ? AND ?");
        List<String> argsList = new ArrayList<>();
        argsList.add(String.valueOf(minYear));
        argsList.add(String.valueOf(maxYear));

        if (search != null && !search.isEmpty()) {
            query.append(" AND (title LIKE ? OR author LIKE ? OR isbn LIKE ?)");
            String like = "%" + search + "%";
            argsList.add(like);
            argsList.add(like);
            argsList.add(like);
        }

        if (category != null && !category.isEmpty()) {
            query.append(" AND category = ?");
            argsList.add(category);
        }

        if (availability != null && !availability.isEmpty()) {
            query.append(" AND availability = ?");
            argsList.add(availability);
        }

        String[] args = argsList.toArray(new String[0]);
        return db.rawQuery(query.toString(), args);
    }



    public Cursor getFilteredBooks(String category, String availability, int minYear, int maxYear) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM Books WHERE 1=1";
        List<String> argsList = new ArrayList<>();

        if (category != null && !category.isEmpty()) {
            query += " AND category = ?";
            argsList.add(category);
        }

        if (availability != null && !availability.isEmpty()) {
            query += " AND availability = ?";
            argsList.add(availability);
        }

        query += " AND publication_year BETWEEN ? AND ?";
        argsList.add(String.valueOf(minYear));
        argsList.add(String.valueOf(maxYear));

        String[] args = argsList.toArray(new String[0]);
        return db.rawQuery(query, args);
    }


    public Cursor getBookBySearch(String query) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String searchQuery = "%" + query + "%";

        return sqLiteDatabase.rawQuery(
                "SELECT * FROM Books WHERE title LIKE ? OR author LIKE ? OR isbn LIKE ?",
                new String[]{searchQuery, searchQuery, searchQuery}
        );
    }

}
