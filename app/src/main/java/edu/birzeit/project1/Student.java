package edu.birzeit.project1;

public class Student {
    private int id;
    private String universityId;
    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash;
    private String department;
    private String level;
    private String phoneNumber;


    public Student(){

    }
    public Student(int id, String universityId, String firstName, String lastName,
                   String email, String passwordHash, String department, String level,String phoneNumber) {
        this.id = id;
        this.universityId = universityId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.department = department;
        this.level = level;
        this.phoneNumber = phoneNumber;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUniversityId() {
        return universityId;
    }
    public void setUniversityId(String universityId) {
        this.universityId = universityId;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public String getLevel() {
        return level;
    }
    public void setLevel(String level) {
        this.level = level;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
