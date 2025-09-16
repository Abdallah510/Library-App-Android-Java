package edu.birzeit.project1.prelogin;

import static edu.birzeit.project1.database.LibraryDataBase.DATABASE_NAME;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.birzeit.project1.database.LibraryDataBase;
import edu.birzeit.project1.librarian_fragment.MainLibrarianActivity;
import edu.birzeit.project1.student_fragments.MainActivity;
import edu.birzeit.project1.R;

public class LoginActivity extends AppCompatActivity {
    boolean validLoginStudent = false;
    boolean validLoginLibrarian = false;
    public static int logedInId;
    public static String adminEmail = "librarian@library.edu";
    public static String adminPass = "Library123!";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText etLoginIdEmail = findViewById(R.id.etLoginIdEmail);
        EditText etLoginPassword = findViewById(R.id.etLoginPassword);
        CheckBox cbRememberMe = findViewById(R.id.cbRememberMe);
        CheckBox cbLibrarian = findViewById(R.id.cbLibrarian);
        Button btnLogin = findViewById(R.id.btnLogin);

        Button btnBackToRegister = findViewById(R.id.btnBackToRegister);
        LibraryDataBase db =new LibraryDataBase(LoginActivity.this,DATABASE_NAME,null,1);
        String loginIdEmail = SharedPrefManager.getInstance(LoginActivity.this).readString("loginIdEmail", "");
        String loginPassword = SharedPrefManager.getInstance(LoginActivity.this).readString("loginPassword", "");
        if (!loginIdEmail.equals("") && !loginPassword.equals("")){
            etLoginIdEmail.setText(loginIdEmail);
            etLoginPassword.setText(loginPassword);
        }
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginIdEmail = etLoginIdEmail.getText().toString().trim();
                String loginPassword = etLoginPassword.getText().toString().trim();
                Cursor allStudents = db.getAllStudents();
                Cursor allLibrarians = db.getAllLibrarians();
                if (loginIdEmail.equals(adminEmail) && loginPassword.equals(adminPass)) {
                    Toast.makeText(LoginActivity.this, "Admin login successful!", Toast.LENGTH_SHORT).show();
                    if (cbRememberMe.isChecked()) {
                        SharedPrefManager.getInstance(LoginActivity.this).writeString("loginIdEmail", loginIdEmail);
                        SharedPrefManager.getInstance(LoginActivity.this).writeString("loginPassword", loginPassword);
                    }
                    if(cbLibrarian.isChecked()){
                        Intent intent = new Intent(LoginActivity.this, MainLibrarianActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                if(cbLibrarian.isChecked()){
                    if (loginIdEmail.matches("\\d{8}")){
                        checkUser(allLibrarians,loginIdEmail,loginPassword,1,false);
                    }else {
                        checkUser(allLibrarians,loginIdEmail,loginPassword,4,false);
                    }
                }else{
                    if (loginIdEmail.matches("\\d{8}")){
                        checkUser(allStudents,loginIdEmail,loginPassword,1,true);
                    }else {
                        checkUser(allStudents,loginIdEmail,loginPassword,4,true);
                    }
                }
                if (validLoginStudent|| validLoginLibrarian) {
                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    if (cbRememberMe.isChecked()) {
                        SharedPrefManager.getInstance(LoginActivity.this).writeString("loginIdEmail", loginIdEmail);
                        SharedPrefManager.getInstance(LoginActivity.this).writeString("loginPassword", loginPassword);
                    }
                    if(validLoginStudent){
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent(LoginActivity.this, MainLibrarianActivity.class);
                        startActivity(intent);
                        finish();
                    }

                }else{
                    Toast.makeText(LoginActivity.this, "Invalid login credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnBackToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
                finish();
            }
        });




    }
    public void checkUser(Cursor allUsers,String loginIdEmail,String loginPassword,int sw,boolean student){
        while (allUsers.moveToNext()) {
            if (allUsers.getString(sw).equals(loginIdEmail)){
                String encryptedPassword = "";
                for (char c : loginPassword.toCharArray())
                    encryptedPassword += (char)(c + 3);
                if (allUsers.getString(5).equals(encryptedPassword)){
                    if(student){
                        validLoginStudent =true;
                        logedInId = Integer.parseInt(allUsers.getString(0));
                    }else{
                        validLoginLibrarian =true;
                    }
                }
            }
        }
    }
}