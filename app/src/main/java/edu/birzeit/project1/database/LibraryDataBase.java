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
        // Books Table
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

        // Students Table
        db.execSQL("CREATE TABLE Students (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "university_id TEXT UNIQUE NOT NULL, " +
                "first_name TEXT, " +
                "last_name TEXT, " +
                "email TEXT UNIQUE, " +
                "password_hash TEXT, " +
                "department TEXT, " +
                "level TEXT, " +
                "phone_number TEXT, " +
                "profile_picture BLOB" +
                ");");

        // Reservations Table (updated version)
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

        // Reading_List Table
        db.execSQL("CREATE TABLE Reading_List (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "student_id INTEGER, " +
                "book_id INTEGER, " +
                "added_date TEXT, " +
                "FOREIGN KEY(student_id) REFERENCES Students(id), " +
                "FOREIGN KEY(book_id) REFERENCES Books(id)" +
                ");");

        // Borrowings Table
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

    // ================== Student Methods ==================
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

    public void updateStudent(String firstName, String lastName, String passwordHash, String phoneNumber, int studentId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("first_name", firstName);
        values.put("last_name", lastName);
        values.put("password_hash", passwordHash);
        values.put("phone_number", phoneNumber);
        db.update("Students", values, "id = ?", new String[]{String.valueOf(studentId)});
        db.close();
    }

    public void updateStudentPhoto(byte[] photoBytes, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("profile_picture", photoBytes);
        db.update("Students", cv, "id=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public Cursor getAllStudents() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM Students", null);
    }

    // ================== Book Methods ==================
    public void insertBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", book.getTitle());
        values.put("author", book.getAuthor());
        values.put("category", book.getCategory());
        values.put("availability", book.getAvailability());
        values.put("cover_url", book.getCoverUrl());
        values.put("isbn", book.getIsbn());
        values.put("publication_year", book.getPublicationYear());
        db.insert("Books", null, values);
        db.close();
    }

    public Cursor getAllBooks() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM Books", null);
    }

    public Cursor getBookBySearch(String query) {
        SQLiteDatabase db = getReadableDatabase();
        String searchQuery = "%" + query + "%";
        return db.rawQuery(
                "SELECT * FROM Books WHERE title LIKE ? OR author LIKE ? OR isbn LIKE ?",
                new String[]{searchQuery, searchQuery, searchQuery}
        );
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

        return db.rawQuery(query, argsList.toArray(new String[0]));
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

        return db.rawQuery(query.toString(), argsList.toArray(new String[0]));
    }


    public Cursor getFilteredBooksWithSearchid(String search, String category, String availability,
                                             int minYear, int maxYear, Integer studentId) {
        SQLiteDatabase db = getReadableDatabase();

        // Start query differently if filtering by student
        StringBuilder query = new StringBuilder();
        if (studentId != null) {
            query.append("SELECT Books.* FROM Books ")
                    .append("INNER JOIN Reading_List ON Books.id = Reading_List.book_id ")
                    .append("WHERE Reading_List.student_id = ? ")
                    .append("AND Books.publication_year BETWEEN ? AND ?");
        } else {
            query.append("SELECT * FROM Books WHERE publication_year BETWEEN ? AND ?");
        }

        List<String> argsList = new ArrayList<>();

        if (studentId != null) {
            argsList.add(String.valueOf(studentId));
        }

        argsList.add(String.valueOf(minYear));
        argsList.add(String.valueOf(maxYear));

        if (search != null && !search.isEmpty()) {
            query.append(" AND (Books.title LIKE ? OR Books.author LIKE ? OR Books.isbn LIKE ?)");
            String like = "%" + search + "%";
            argsList.add(like);
            argsList.add(like);
            argsList.add(like);
        }

        if (category != null && !category.isEmpty()) {
            query.append(" AND Books.category = ?");
            argsList.add(category);
        }

        if (availability != null && !availability.isEmpty()) {
            query.append(" AND Books.availability = ?");
            argsList.add(availability);
        }

        return db.rawQuery(query.toString(), argsList.toArray(new String[0]));
    }

    public Cursor getLatestBooks(int limit) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM Books ORDER BY id DESC LIMIT ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(limit)});

        return cursor;
    }



    // ================== Borrowings Methods ==================
    public Cursor getAllBorrowedBooks() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM Borrowings", null);
    }

    // ================== Reservations Methods ==================
    public boolean insertReservation(int studentId, int bookId, int durationWeeks, String collectionMethod, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        long now = System.currentTimeMillis();
        long due = now + durationWeeks * 7L * 24 * 60 * 60 * 1000;

        String reservationDate = new java.text.SimpleDateFormat("yyyy/MM/dd").format(new java.util.Date(now));
        String dueDate = new java.text.SimpleDateFormat("yyyy/MM/dd").format(new java.util.Date(due));

        values.put("student_id", studentId);
        values.put("book_id", bookId);
        values.put("reservation_date", reservationDate);
        values.put("due_date", dueDate);
        values.put("status", "Pending");
        values.put("collection_method", collectionMethod);
        values.put("notes", notes);

        long result = db.insert("Reservations", null, values);
        db.close();
        return result != -1;
    }
    // ================== READING LIST Methods ==================
    public boolean insertReadingList(int studentId, int bookId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        long now = System.currentTimeMillis();
        String reservationDate = new java.text.SimpleDateFormat("yyyy/MM/dd").format(new java.util.Date(now));

        values.put("student_id", studentId);
        values.put("book_id", bookId);
        values.put("added_date", reservationDate);

        long result = db.insert("Reading_List", null, values);
        db.close();
        return result != -1;
    }

    public Cursor getReadingListByStudentId(int studentId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT Books.* " +
                "FROM Books " +
                "WHERE Books.id IN (SELECT book_id FROM Reading_List WHERE student_id = ?)";

        return db.rawQuery(query, new String[]{String.valueOf(studentId)});
    }

    public boolean removeBookFromReadingList(int studentId, int bookId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(
                "Reading_List",
                "student_id = ? AND book_id = ?",
                new String[]{String.valueOf(studentId), String.valueOf(bookId)}
        );
        db.close();
        return rowsAffected > 0;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Implement schema upgrades if needed
    }
}
