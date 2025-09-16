package edu.birzeit.project1.librarian_fragment;

import static edu.birzeit.project1.student_fragments.LibraryInfoFragment.libraryHours;
import static edu.birzeit.project1.student_fragments.LibraryInfoFragment.policiesAndRules;
import static edu.birzeit.project1.student_fragments.LibraryInfoFragment.servicesOffered;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.birzeit.project1.R;


public class LibraryInfoManagerFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.fragment_library_info_manager, container, false);
        EditText etLibraryHours = view.findViewById(R.id.etLibraryHours);
        EditText etServices = view.findViewById(R.id.etServices);
        EditText etPolicies = view.findViewById(R.id.etPolicies);

        SharedPreferences prefs = getContext().getSharedPreferences("LibraryPrefs", Context.MODE_PRIVATE);
        String hours = prefs.getString("libraryHours", libraryHours);
        String services = prefs.getString("servicesOffered", servicesOffered);
        String policies = prefs.getString("policiesAndRules", policiesAndRules);
        etLibraryHours.setText(hours);
        etServices.setText(services);
        etPolicies.setText(policies);

        Button btnSaveLibraryInfo = view.findViewById(R.id.btnSaveLibraryInfo);
        btnSaveLibraryInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newHours = etLibraryHours.getText().toString();
                String newServices = etServices.getText().toString();
                String newPolicies = etPolicies.getText().toString();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("libraryHours", newHours);
                editor.putString("servicesOffered", newServices);
                editor.putString("policiesAndRules", newPolicies);
                editor.apply();
                Toast.makeText(getContext(), "Library information saved successfully", Toast.LENGTH_SHORT).show();
            }
        });


        return view;

    }
}