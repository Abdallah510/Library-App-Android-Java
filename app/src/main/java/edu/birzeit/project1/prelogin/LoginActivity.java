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
import edu.birzeit.project1.student_fragments.MainActivity;
import edu.birzeit.project1.R;

public class LoginActivity extends AppCompatActivity {
    int validLogin = 0;
    public static int logedInId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText etLoginIdEmail = findViewById(R.id.etLoginIdEmail);
        EditText etLoginPassword = findViewById(R.id.etLoginPassword);
        CheckBox cbRememberMe = findViewById(R.id.cbRememberMe);
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

                if (loginIdEmail.matches("\\d{8}")){
                    checkUser(allStudents,loginIdEmail,loginPassword,1);
                }else {
                    checkUser(allStudents,loginIdEmail,loginPassword,4);
                }
                if (validLogin == 1) {
                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    if (cbRememberMe.isChecked()) {
                        SharedPrefManager.getInstance(LoginActivity.this).writeString("loginIdEmail", loginIdEmail);
                        SharedPrefManager.getInstance(LoginActivity.this).writeString("loginPassword", loginPassword);
                    }
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
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
    public void checkUser(Cursor allStudents,String loginIdEmail,String loginPassword,int sw){
        while (allStudents.moveToNext()) {
            if (allStudents.getString(sw).equals(loginIdEmail)){
                String encryptedPassword = "";
                for (char c : loginPassword.toCharArray())
                    encryptedPassword += (char)(c + 3);
                if (allStudents.getString(5).equals(encryptedPassword)){
                    validLogin =1;
                    logedInId = Integer.parseInt(allStudents.getString(0));
                }
            }
        }
    }
}