package edu.birzeit.project1.librarian_fragment;

import android.app.AlertDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import edu.birzeit.project1.R;
import edu.birzeit.project1.database.LibraryDataBase;
import edu.birzeit.project1.entities.Announcement;
import edu.birzeit.project1.student_fragments.AnnouncementAdapter;

public class AnnouncemnetManagerFragment extends Fragment {

    private RecyclerView recyclerView;
    private AnnouncementAdapter adapter;
    private List<Announcement> announcements =new ArrayList<>();;
    private LibraryDataBase db;
    private FloatingActionButton btnAddAnnouncement;
    private TextView tvEmpty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_announcemnet_manager, container, false);

        recyclerView = view.findViewById(R.id.recyclerAnnouncements);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        btnAddAnnouncement = view.findViewById(R.id.fabAddAnnouncement);
        tvEmpty = view.findViewById(R.id.tvEmptyAnnouncement);
        db = new LibraryDataBase(getContext(), LibraryDataBase.DATABASE_NAME, null, 1);

        Cursor cursor = db.getAllAnnouncements();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String message = cursor.getString(cursor.getColumnIndexOrThrow("message"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("date_posted"));
            announcements.add(new Announcement(id, title, message, date));
        }
        adapter = new AnnouncementAdapter(getContext(), announcements);

        if(announcements.isEmpty() || announcements == null) {
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        recyclerView.setAdapter(adapter);
        btnAddAnnouncement.setOnClickListener(v -> showAddAnnouncementDialog());
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        showDeleteConfirmation(position);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));
        return view;
    }


    private void showAddAnnouncementDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_announcement, null);
        EditText etTitle = dialogView.findViewById(R.id.etAnnouncementTitle);
        EditText etMessage = dialogView.findViewById(R.id.etAnnouncementMessage);

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Add Announcement")
                .setView(dialogView)
                .setPositiveButton("Add", (dialogInterface, which) -> {
                    String title = etTitle.getText().toString().trim();
                    String message = etMessage.getText().toString().trim();
                    if (title.isEmpty() || message.isEmpty()) {
                        Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Announcement ann = new Announcement();
                    ann.setTitle(title);
                    ann.setMessage(message);
                    ann.setDatePosted(LocalDate.now().toString());
                    int id = db.insertAnnouncement(ann);



                    announcements.add(new Announcement(id, ann.getTitle(), ann.getMessage(), ann.getDatePosted()));
                    adapter.notifyItemInserted(announcements.size() - 1);

                    if (announcements.size()>0){
                        tvEmpty.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    Toast.makeText(getContext(), "Announcement added", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .create();

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#212D40")));
        }
        dialog.show();

        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        positiveButton.setTextColor(Color.parseColor("#4CAF50"));
        negativeButton.setTextColor(Color.parseColor("#F44336"));
    }

    private void showDeleteConfirmation(int position) {
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Delete Announcement")
                .setMessage("Are you sure you want to delete this announcement?")
                .setPositiveButton("Yes", (dialogInterface, which) -> {
                    Announcement announcement = announcements.get(position);
                    db.deleteAnnouncement(announcement.getId());
                    announcements.remove(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(getContext(), "Announcement deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#212D40")));
        }
        dialog.show();
        Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        positiveButton.setTextColor(Color.parseColor("#4CAF50"));
        negativeButton.setTextColor(Color.parseColor("#F44336"));
    }
}
