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
import edu.birzeit.project1.entities.Reservation;

public class LibrarianReservationManagementFragment  extends Fragment {

    private RecyclerView recyclerView;
    private ReservationAdapter adapter;
    private LibraryDataBase db;
    private TextView tvEmpty;
    List<Reservation> reservations = new ArrayList<>();;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_librarian_reservation_management, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewReservation);
        tvEmpty = view.findViewById(R.id.tvEmptyReservations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        db = new LibraryDataBase(getContext(), LibraryDataBase.DATABASE_NAME, null, 1);

        Cursor allReservations = db.getAllReservations();
        while (allReservations.moveToNext()) {
            int id = allReservations.getInt(0);
            int studentId = allReservations.getInt(1);
            int bookId = allReservations.getInt(2);
            String reservationDate = allReservations.getString(3);
            String dueDate = allReservations.getString(4);
            String status = allReservations.getString(5);
            String collectionMethod = allReservations.getString(6);
            String notes = allReservations.getString(7);
            String returnDate = allReservations.getString(8);
            Double fineAmount = allReservations.getDouble(9);
            reservations.add(new Reservation(id, studentId, bookId, reservationDate,
                    dueDate, status, collectionMethod, notes, returnDate, fineAmount));
        }

        if (reservations == null || reservations.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter = new ReservationAdapter(getContext(), reservations);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }
}
