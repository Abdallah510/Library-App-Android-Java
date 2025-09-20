package edu.birzeit.project1.librarian_fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import edu.birzeit.project1.R;
import edu.birzeit.project1.prelogin.LoginActivity;
import edu.birzeit.project1.prelogin.RegistrationActivity;
import edu.birzeit.project1.student_fragments.BookCatalogFragment;
import edu.birzeit.project1.student_fragments.BorrowedBooksFragment;
import edu.birzeit.project1.student_fragments.DashboardFragment;
import edu.birzeit.project1.student_fragments.LibraryInfoFragment;
import edu.birzeit.project1.student_fragments.NewArrivalsFragment;
import edu.birzeit.project1.student_fragments.ProfileManagementFragment;
import edu.birzeit.project1.student_fragments.ReadingListFragment;

public class MainLibrarianActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_librarian_main);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        Button btn_reports = findViewById(R.id.btn_reports);
        Button btn_dashboard = findViewById(R.id.btn_dashboard);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new AnnouncemnetManagerFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setHomeAsUpIndicator(R.drawable.logo); // your hamburger or logo
        }

        toolbar.setNavigationOnClickListener(v -> drawer.openDrawer(GravityCompat.START));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.nav_dashboard:
                        selectedFragment = new AnnouncemnetManagerFragment();
                        break;
                    case R.id.nav_Registration:
                        selectedFragment = new LibrarianRegistrationFragment();
                        break;
                    case R.id.nav_books:
                        selectedFragment = new LibrarianBookFragment();
                        break;
                    case R.id.nav_students:
                        selectedFragment = new LibrarianStudentManagerFragment();
                        break;
                    case R.id.nav_LibraryInfo:
                        selectedFragment = new LibraryInfoManagerFragment();
                        break;
                    case R.id.nav_ReservationManagement:
                        selectedFragment =  new LibrarianReservationManagementFragment();
                        break;
                    case R.id.nav_new_arrivals:
                        //     selectedFragment = new NewArrivalsFragment();
                        break;
                    case R.id.nav_logout:
                        new AlertDialog.Builder(MainLibrarianActivity.this)
                                .setTitle("Logout")
                                .setMessage("Are you sure you want to logout?")
                                .setPositiveButton("YES", (dialog, which) -> {
                                    Intent intent = new Intent(MainLibrarianActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                })
                                .setNegativeButton("CANCEL", (dialog, which) -> dialog.dismiss())
                                .show();
                        break;
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView, selectedFragment)
                            .commit();
                }

                item.setChecked(true);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        btn_reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment selectedFragment = null;
                selectedFragment = new ReportsFragment();
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView, selectedFragment)
                            .commit();
                }
            }
        });
        btn_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment selectedFragment = null;
                selectedFragment = new AnnouncemnetManagerFragment();
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView, selectedFragment)
                            .commit();
                }
            }
        });


    }
}
