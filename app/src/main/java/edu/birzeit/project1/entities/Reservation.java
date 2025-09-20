package edu.birzeit.project1.entities;

public class Reservation {
    private int id;
    private int studentId;
    private int bookId;
    private String reservationDate;
    private String dueDate;
    private String status;
    private String collectionMethod;
    private String notes;
    private String returnDate;
    private double fineAmount;

    public Reservation(){

    }
    public Reservation(int id, int studentId, int bookId, String reservationDate, String dueDate,
                       String status, String collectionMethod, String notes, String returnDate, double fineAmount) {
        this.id = id;
        this.studentId = studentId;
        this.bookId = bookId;
        this.reservationDate = reservationDate;
        this.dueDate = dueDate;
        this.status = status;
        this.collectionMethod = collectionMethod;
        this.notes = notes;
        this.returnDate = returnDate;
        this.fineAmount = fineAmount;

    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getStudentId() {
        return studentId;
    }
    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
    public int getBookId() {
        return bookId;
    }
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
    public String getReservationDate() {
        return reservationDate;
    }
    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }
    public String getDueDate() {
        return dueDate;
    }
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getCollectionMethod() {
        return collectionMethod;
    }
    public void setCollectionMethod(String collectionMethod) {
        this.collectionMethod = collectionMethod;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public String getReturnDate() {
        return returnDate;
    }
    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }
    public double getFineAmount() {
        return fineAmount;
    }
    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }
}
