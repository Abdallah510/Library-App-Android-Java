package edu.birzeit.project1.student_fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import edu.birzeit.project1.R;


public class LibraryInfoFragment extends Fragment {

    public static String libraryHours = "Mon - Fri: 8:00 AM - 10:00 PM\nSat: 9:00 AM - 5:00 PM";
    public static String servicesOffered = "• Borrowing Books\n• Research Assistance\n• Printing\n• Scanning\n• Study Rooms";
    public static String policiesAndRules = "• Maintain Silence\n• No Food or Drinks\n• Return Books on Time";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_library_info, container, false);
        Button btnCallLibrary = view.findViewById(R.id.btnCallLibrary);
        Button btnFindUs = view.findViewById(R.id.btnFindUs);
        Button btnEmailLibrarian = view.findViewById(R.id.btnEmailLibrarian);
        TextView tvLibraryHours = view.findViewById(R.id.tvLibraryHours);
        TextView tvLibraryServices = view.findViewById(R.id.tvLibraryServices);
        TextView tvLibraryPolicies = view.findViewById(R.id.tvLibraryPolicies);
        SharedPreferences prefs = getContext().getSharedPreferences("LibraryPrefs", Context.MODE_PRIVATE);
        String hours = prefs.getString("libraryHours", libraryHours);
        String services = prefs.getString("servicesOffered", servicesOffered);
        String policies = prefs.getString("policiesAndRules", policiesAndRules);
        tvLibraryHours.setText(hours);
        tvLibraryServices.setText(services);
        tvLibraryPolicies.setText(policies);
        btnCallLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dialIntent =new Intent();
                dialIntent.setAction(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:+970-2-"));
                startActivity(dialIntent);
            }
        });
        btnFindUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mapsIntent =new Intent();
                mapsIntent.setAction(Intent.ACTION_VIEW);
                mapsIntent.setData(Uri.parse("geo:31.921500,35.237500"));
                startActivity(mapsIntent);
            }
        });
        btnEmailLibrarian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gmailIntent =new Intent();
                gmailIntent.setAction(Intent.ACTION_SENDTO);
                gmailIntent.setType("message/rfc822");
                gmailIntent.setData(Uri.parse("mailto:"));
                gmailIntent.putExtra(Intent.EXTRA_EMAIL, "library@birzeit.edu");
                gmailIntent.putExtra(Intent.EXTRA_SUBJECT,"My Subject");
                gmailIntent.putExtra(Intent.EXTRA_TEXT,"Content of the message");
                startActivity(gmailIntent);
            }
        });

        return view;
    }
}