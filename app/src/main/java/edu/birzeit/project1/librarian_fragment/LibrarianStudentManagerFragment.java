package edu.birzeit.project1.librarian_fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import edu.birzeit.project1.R;
import edu.birzeit.project1.database.LibraryDataBase;
import edu.birzeit.project1.entities.Student;
import edu.birzeit.project1.librarian_fragment.StudentAdapter;

public class LibrarianStudentManagerFragment extends Fragment {

    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private List<Student> studentList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_librarian_student_manager, container, false);
        LibraryDataBase db = new LibraryDataBase(requireContext(), LibraryDataBase.DATABASE_NAME, null, 1);
        recyclerView = view.findViewById(R.id.recyclerViewStudents);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TextView tvEmpty = view.findViewById(R.id.tvNoStudents);
        studentList = new ArrayList<>();

        Cursor allStudents = db.getAllStudents();
        while (allStudents.moveToNext()) {
            int id = allStudents.getInt(allStudents.getColumnIndexOrThrow("id"));
            String universityId = allStudents.getString(allStudents.getColumnIndexOrThrow("university_id"));
            String firstName = allStudents.getString(allStudents.getColumnIndexOrThrow("first_name"));
            String lastName = allStudents.getString(allStudents.getColumnIndexOrThrow("last_name"));
            String email = allStudents.getString(allStudents.getColumnIndexOrThrow("email"));
            String passwordHash = allStudents.getString(allStudents.getColumnIndexOrThrow("password_hash"));
            String department = allStudents.getString(allStudents.getColumnIndexOrThrow("department"));
            String level = allStudents.getString(allStudents.getColumnIndexOrThrow("level"));
            String phoneNumber = allStudents.getString(allStudents.getColumnIndexOrThrow("phone_number"));
            byte[] profilePicture = allStudents.getBlob(allStudents.getColumnIndexOrThrow("profile_picture"));

            Student student = new Student(id, universityId, firstName, lastName, email,passwordHash, department, level, phoneNumber, profilePicture);
            studentList.add(student);
        }
        if (studentList == null || studentList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new StudentAdapter(requireContext(), studentList);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }
}
