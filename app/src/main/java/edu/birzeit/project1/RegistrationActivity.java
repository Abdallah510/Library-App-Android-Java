package edu.birzeit.project1;

import android.content.Intent;
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
        //Spinners
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
        Button btnBackToLogin = findViewById(R.id.btnBackToLogin);
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
                LibraryDataBase db =new LibraryDataBase(RegistrationActivity.this,"Library_DB",null,1);
                String errorMessage = null;

                if (!universityId.matches("\\d{4}\\d{4}")) {
                    errorMessage="Invalid University ID (YYYY####)";
                }

                if (firstName.length() < 3) {
                    errorMessage="First Name must be ≥3 characters";
                }

                if (lastName.length() < 3) {
                    errorMessage="Last Name must be ≥3 characters";

                }

                if (!email.matches("[a-zA-Z0-9._%+-]+@university\\.edu")) {
                    errorMessage="Email must be a university email";
                }

                if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{6,}$")) {
                    errorMessage="Password must have ≥6 chars, 1 upper, 1 lower, 1 number, 1 special char";
                }

                if (!password.equals(confirmPassword)) {
                    errorMessage="Passwords do not match";
                }

                if (academicLevel.equals("Academic Level")) {
                    Toast.makeText(RegistrationActivity.this, "Please select an academic level", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (department.equals("Select Department")) {
                    Toast.makeText(RegistrationActivity.this, "Please select a department", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (errorMessage != null) {
                    Toast.makeText(RegistrationActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegistrationActivity.this, RegistrationActivity.class);
                    startActivity(intent);
                    finish();
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
                Intent intent = new Intent(RegistrationActivity.this, RegistrationActivity.class);
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