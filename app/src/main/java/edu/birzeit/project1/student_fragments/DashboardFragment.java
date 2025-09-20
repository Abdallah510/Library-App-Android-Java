package edu.birzeit.project1.student_fragments;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.birzeit.project1.R;
import edu.birzeit.project1.database.LibraryDataBase;
import edu.birzeit.project1.entities.Announcement;
import edu.birzeit.project1.entities.Reservation;
import edu.birzeit.project1.librarian_fragment.ReservationAdapter;


public class DashboardFragment extends Fragment {

    private RecyclerView recyclerView;
    private AnnouncementAdapter adapter;
    List<Announcement> announcements = new ArrayList<>();
    private TextView tvEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        recyclerView = view.findViewById(R.id.recyclerAnnouncements);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tvEmpty = view.findViewById(R.id.tvEmptyAnnouncement);
        LibraryDataBase db = new LibraryDataBase(getContext(), LibraryDataBase.DATABASE_NAME, null, 1);
        Cursor cursor = db.getAllAnnouncements();
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String message = cursor.getString(cursor.getColumnIndexOrThrow("message"));
            String date = cursor.getString(cursor.getColumnIndexOrThrow("date_posted"));
            announcements.add(new Announcement(id,title, message, date));
        }
        if(announcements.isEmpty() || announcements == null){
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new AnnouncementAdapter(getContext(), announcements);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }
}