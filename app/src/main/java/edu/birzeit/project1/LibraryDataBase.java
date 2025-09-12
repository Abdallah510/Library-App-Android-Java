package edu.birzeit.project1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


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
                "phone_number TEXT, " +
                "profile_picture BLOB" +
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
    public Cursor getAllStudents() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM Students", null);
    }
    public Cursor getAllBooks() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM Books", null);
    }
    public Cursor getAllBorrowedBooks() {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        return sqLiteDatabase.rawQuery("SELECT * FROM Borrowings", null);
    }
    public void updateStudent(String firstName,String lastName,String passwordHash,String phoneNumber,int studentId){
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
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

}
