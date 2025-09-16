package edu.birzeit.project1.librarian_fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import edu.birzeit.project1.R;
import edu.birzeit.project1.database.LibraryDataBase;
import edu.birzeit.project1.entities.Librarian;
import edu.birzeit.project1.entities.Student;
import edu.birzeit.project1.prelogin.LoginActivity;
import edu.birzeit.project1.prelogin.RegistrationActivity;
import edu.birzeit.project1.prelogin.WelcomeActivity;


public class LibrarianRegistrationFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_librarian_registration, container, false);
        EditText etUniversityId = view.findViewById(R.id.etUniversityId);
        EditText etFirstName = view.findViewById(R.id.etFirstName);
        EditText etLastName = view.findViewById(R.id.etLastName);
        EditText etEmail = view.findViewById(R.id.etEmail);
        EditText etPassword = view.findViewById(R.id.etPassword);
        EditText etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        EditText etPhoneNumber = view.findViewById(R.id.etPhoneNumber);
        Button btnRegister = view.findViewById(R.id.btnRegister);
        LibraryDataBase db =new LibraryDataBase(requireContext(),LibraryDataBase.DATABASE_NAME,null,1);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String universityId = etUniversityId.getText().toString().trim();
                String firstName = etFirstName.getText().toString().trim();
                String lastName = etLastName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();
                String phone = etPhoneNumber.getText().toString().trim();
                Cursor allLibrarians = db.getAllLibrarians();
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
                if (!email.matches("[a-zA-Z0-9._%+-]+@university\\.edu") || email.equals("librarian@library.edu")){
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
                while (allLibrarians.moveToNext()) {
                    if (allLibrarians.getString(1).equals(universityId)) {
                        Toast.makeText(requireContext(), "University ID already exists", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(allLibrarians.getString(4).equals(email)){
                        Toast.makeText(requireContext(), "Email already exists", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                String encryptedPassword = "";
                for (char c : password.toCharArray()) {
                    encryptedPassword += (char)(c + 3);
                }
                String phoneNew = "+970" + phone;
                Librarian librarian = new Librarian();
                librarian.setUniversityId(universityId);
                librarian.setFirstName(firstName);
                librarian.setLastName(lastName);
                librarian.setEmail(email);
                librarian.setPasswordHash(encryptedPassword);
                librarian.setPhoneNumber(phoneNew);
                db.insertLibrarian(librarian);
                Toast.makeText(requireContext(), "Registration successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(requireContext(), MainLibrarianActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}