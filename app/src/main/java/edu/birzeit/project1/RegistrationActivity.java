package edu.birzeit.project1;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Locale;


public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        String[] academicOptions = { "Academic Level", "Senior", "Sophomore", "Junior", "Freshman", "Graduate" };
        setupSpinner(R.id.spinnerAcademicLevel, academicOptions);
        String[] departmenOptions = { "Select Department", "Computer Science", "Engineering", "Business", "Literature", "Medicine" };
        setupSpinner(R.id.spinnerDepartment,  departmenOptions);
        EditText etUniversityId = findViewById(R.id.etUniversityId);
        EditText etFirstName = findViewById(R.id.etFirstName);
        EditText etLastName = findViewById(R.id.etLastName);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        EditText etConfirmPassword = findViewById(R.id.etConfirmPassword);
        EditText etPhoneNumber = findViewById(R.id.etPhoneNumber);
        Button btnRegister = findViewById(R.id.btnRegister);
        Button btnBackToWelcome = findViewById(R.id.btnBackToWelcome);
        LibraryDataBase db =new LibraryDataBase(RegistrationActivity.this,"Library_DB",null,1);
        btnBackToWelcome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String universityId = etUniversityId.getText().toString().trim();
                String firstName = etFirstName.getText().toString().trim();
                String lastName = etLastName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();
                String academicLevel = ((Spinner)findViewById(R.id.spinnerAcademicLevel)).getSelectedItem().toString();
                String department = ((Spinner)findViewById(R.id.spinnerDepartment)).getSelectedItem().toString();
                String phone = etPhoneNumber.getText().toString().trim();
                Cursor allStudents = db.getAllStudents();

                if (!universityId.matches("\\d{8}")) {
                    etUniversityId.setError("Invalid University ID (YYYY####)");
                    return;
                }else{
                    int year = Integer.parseInt(universityId.substring(0, 4));
                    if(year >2025 || year < 2000){
                        etUniversityId.setError("Invalid University ID (YYYY####)");
                        return;
                    }
                }
                if (firstName.length() < 3){
                    etFirstName.setError("First Name must be ≥3 characters");
                    return;

                }
                if (lastName.length() < 3){
                    etLastName.setError("Last Name must be ≥3 characters");
                    return;
                }
                if (!email.matches("[a-zA-Z0-9._%+-]+@university\\.edu")){
                    etEmail.setError("Email must be a university email with @university.edu ");
                    return;
                }
                if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{6,}$")){
                    etPassword.setError("Password must have ≥6 chars, 1 upper, 1 lower, 1 number, 1 special char");
                    return;
                }

                if (!password.equals(confirmPassword)){
                    etConfirmPassword.setError("Passwords do not match");
                    return;
                }
                if (academicLevel.equals("Academic Level")) {
                    TextView errorText = (TextView)((Spinner)findViewById(R.id.spinnerAcademicLevel)).getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Please select an academic level");
                    return;
                }
                if (department.equals("Select Department")) {
                    TextView errorText = (TextView)((Spinner)findViewById(R.id.spinnerDepartment)).getSelectedView();
                    errorText.setError("");
                    errorText.setTextColor(Color.RED);
                    errorText.setText("Please select a department");
                    return;
                }
                while (allStudents.moveToNext()) {
                    if (allStudents.getString(1).equals(universityId)) {
                        Toast.makeText(RegistrationActivity.this, "University ID already exists", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(allStudents.getString(4).equals(email)){
                        Toast.makeText(RegistrationActivity.this, "Email already exists", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                String encryptedPassword = "";
                for (char c : password.toCharArray()) {
                    encryptedPassword += (char)(c + 3);
                }
                String phoneNew = "+970" + phone;
                Student student = new Student();
                student.setUniversityId(universityId);
                student.setFirstName(firstName);
                student.setLastName(lastName);
                student.setEmail(email);
                student.setPasswordHash(encryptedPassword);
                student.setDepartment(department);
                student.setLevel(academicLevel);
                student.setPhoneNumber(phoneNew);
                db.insertStudent(student);
                Toast.makeText(RegistrationActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void setupSpinner(int spinnerId, String[] options) {
        Spinner spinner = findViewById(spinnerId);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                if (position == 0) {
                    view.setTextColor(Color.parseColor("#9A8C98"));
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPopupBackgroundDrawable(new ColorDrawable(Color.parseColor("#11151C")));
        spinner.setAdapter(adapter);
    }
}