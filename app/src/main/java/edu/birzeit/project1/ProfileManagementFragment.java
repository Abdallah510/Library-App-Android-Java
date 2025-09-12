package edu.birzeit.project1;

import static edu.birzeit.project1.LoginActivity.logedInId;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class ProfileManagementFragment extends Fragment {

    private ImageView imgProfile;
    private LibraryDataBase db;
    private static final int PICK_IMAGE = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_management, container, false);
        EditText etFirstName = view.findViewById(R.id.etFirstName);
        EditText etLastName = view.findViewById(R.id.etLastName);
        EditText etPhone = view.findViewById(R.id.etPhone);
        EditText etPassword = view.findViewById(R.id.etPassword);
        Button btnUpdate = view.findViewById(R.id.btnUpdate);
        Button btnChangePhoto = view.findViewById(R.id.btnChangePhoto);
        TextView tvBorrowingBookNum = view.findViewById(R.id.tvBorrowingBookNum);
        TextView tvBorrowingActive = view.findViewById(R.id.tvBorrowingActive);
        TextView tvBorrowingOverdue = view.findViewById(R.id.tvBorrowingOverdue);
        TextView tvBorrowingReturned = view.findViewById(R.id.tvBorrowingReturned);
        TextView tvBorrowingExtended = view.findViewById(R.id.tvBorrowingExtended);
        TextView tvBorrowingFines = view.findViewById(R.id.tvBorrowingFines);
        int borrowedBooks = 0;
        int active = 0;
        int overdue = 0;
        int returned = 0;
        int extended = 0;
        double fines = 0;
        imgProfile = view.findViewById(R.id.imgProfile);
        db = new LibraryDataBase(requireContext(), LibraryDataBase.DATABASE_NAME, null, 1);
        Cursor allStudents = db.getAllStudents();
        while (allStudents.moveToNext()) {
            if (logedInId == allStudents.getInt(0)) {
                etFirstName.setText(allStudents.getString(2));
                etLastName.setText(allStudents.getString(3));
                etPhone.setText(allStudents.getString(8));
                String decryptedPassword = "";
                for (char c : allStudents.getString(5).toCharArray())
                    decryptedPassword += (char) (c - 3);
                etPassword.setText(decryptedPassword);
                Cursor allBorrowings = db.getAllBorrowedBooks();
                while (allBorrowings.moveToNext()) {
                    if (logedInId == allBorrowings.getInt(1)) {
                        borrowedBooks++;
                        switch (allBorrowings.getString(7).toLowerCase()) {
                            case "active":
                                active++;
                                break;
                            case "returned":
                                returned++;
                                break;
                            case "overdue":
                                overdue++;
                                break;
                            case "extended":
                                extended++;
                                break;
                            default:
                                break;
                        }
                        fines += allBorrowings.getDouble(7);
                    }
                }
                tvBorrowingBookNum.setText("Number of borrowed books: " + borrowedBooks);
                tvBorrowingActive.setText("Active borrowings: " + active);
                tvBorrowingOverdue.setText("Overdue borrowings: " + overdue);
                tvBorrowingReturned.setText("Returned borrowings: " + returned);
                tvBorrowingExtended.setText("Extended borrowings: " + extended);
                tvBorrowingFines.setText("Total fines: " + String.format("%.2f", fines) + " $");
                // Load profile picture
                int photoColIndex = allStudents.getColumnIndex("profile_picture");
                if (photoColIndex != -1) {
                    byte[] photoBytes = allStudents.getBlob(photoColIndex);
                    if (photoBytes != null && photoBytes.length > 0) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(photoBytes, 0, photoBytes.length);
                        imgProfile.setImageBitmap(bitmap);
                    }
                }
                break;
            }
        }
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = etFirstName.getText().toString().trim();
                String lastName = etLastName.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (firstName.length() < 3) {
                    etFirstName.setError("First Name must be ≥3 characters");
                    return;
                }
                if (lastName.length() < 3) {
                    etLastName.setError("Last Name must be ≥3 characters");
                    return;
                }
                if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{6,}$")) {
                    etPassword.setError("Password must have ≥6 chars, 1 upper, 1 lower, 1 number, 1 special char");
                    return;
                }
                String encryptedPassword = "";
                for (char c : password.toCharArray())
                    encryptedPassword += (char) (c + 3);

                db.updateStudent(firstName, lastName, encryptedPassword, phone, logedInId);
                Toast.makeText(requireContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(requireContext(), MainActivity.class));
                requireActivity().finish();
            }
        });
        btnChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        return view;
    }

    private byte[] uriToBytes(Uri uri) {
        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
            return byteBuffer.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                imgProfile.setImageURI(selectedImageUri);
                byte[] photoBytes = uriToBytes(selectedImageUri);
                db.updateStudentPhoto(photoBytes, logedInId);
            }
        }
    }
}
