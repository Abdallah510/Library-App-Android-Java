package edu.birzeit.project1.student_fragments;

import static android.app.PendingIntent.getActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import edu.birzeit.project1.R;
import edu.birzeit.project1.prelogin.LoginActivity;
import edu.birzeit.project1.prelogin.RegistrationActivity;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        Button btn_catalog = findViewById(R.id.btn_catalog);
        Button btn_dashboard = findViewById(R.id.btn_dashboard);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView, new DashboardFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_dashboard);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.nav_dashboard:
                        selectedFragment = new DashboardFragment();
                        break;
                    case R.id.nav_borrowed_books:
                        selectedFragment = new BorrowedBooksFragment();
                        break;
                    case R.id.nav_reading_list:
                        selectedFragment = new ReadingListFragment();
                        break;
                    case R.id.nav_profile:
                        selectedFragment = new ProfileManagementFragment();
                        break;
                    case R.id.nav_new_arrivals:
                        selectedFragment = new NewArrivalsFragment();
                        break;
                    case R.id.nav_library_info:
                        selectedFragment = new LibraryInfoFragment();
                        break;
                    case R.id.nav_logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Logout");
                        builder.setMessage("sure you want to Logout?")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                        builder.show();
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
        btn_catalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment selectedFragment = null;
                selectedFragment = new BookCatalogFragment();
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
                selectedFragment = new DashboardFragment();
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainerView, selectedFragment)
                            .commit();
                }
            }
        });


    }
}
