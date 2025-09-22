package edu.birzeit.project1.librarian_fragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import edu.birzeit.project1.R;
import edu.birzeit.project1.database.LibraryDataBase;
import edu.birzeit.project1.entities.Reservation;
import edu.birzeit.project1.prelogin.LoginActivity;
import edu.birzeit.project1.student_fragments.BookAdapter;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ViewHolder> {

    private final List<Reservation> reservations;
    private final Context context;
    private final LibraryDataBase db;
    private String availibility;
    public ReservationAdapter(Context context, List<Reservation> reservations) {
        this.context = context;
        this.reservations = reservations;
        db = new LibraryDataBase(context, LibraryDataBase.DATABASE_NAME, null, 1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.reservation_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reservation reservation = reservations.get(position);
        String firstName = null;
        String lastName = null;
        String universityId = null;
        Button btnApprove = holder.itemView.findViewById(R.id.btnApprove);
        Button btnReject = holder.itemView.findViewById(R.id.btnReject);
        Button btnReturn = holder.itemView.findViewById(R.id.btnReturn);
        if("Pickup".equalsIgnoreCase(reservation.getCollectionMethod()) && !"Returned".equalsIgnoreCase(reservation.getStatus())){
            btnReturn.setVisibility(View.VISIBLE);
        }else{
            btnReturn.setVisibility(View.GONE);
        }
        if ("Pending".equalsIgnoreCase(reservation.getStatus()) || "Pending Extend".equalsIgnoreCase(reservation.getStatus())) {
            btnApprove.setVisibility(View.VISIBLE);
            btnReject.setVisibility(View.VISIBLE);
            btnReturn.setVisibility(View.GONE);
        } else {
            btnApprove.setVisibility(View.GONE);
            btnReject.setVisibility(View.GONE);
        }
        if(!"Pending Extend".equalsIgnoreCase(reservation.getStatus()) && !"Rejected".equalsIgnoreCase(reservation.getStatus())
                && !"Returned".equalsIgnoreCase(reservation.getStatus())){
            if (db.isOverdue(reservation.getDueDate())) {
                if (!"Overdue".equalsIgnoreCase(reservation.getStatus())) {
                    int currentPosition = holder.getAdapterPosition();
                    db.updateStatus(reservation.getId(), "Overdue");
                    db.updateFine(reservation.getId(), reservation.getFineAmount() + 50);
                    reservation.setStatus("Overdue");
                    reservation.setFineAmount(reservation.getFineAmount() + 50);
                    holder.itemView.post(() -> notifyItemChanged(currentPosition));
                }
            }
        }
        Cursor allStudentsCursor = db.getAllStudents();
        while (allStudentsCursor.moveToNext()) {
            if (allStudentsCursor.getInt(0) == reservation.getStudentId()){
                firstName = allStudentsCursor.getString(2);
                lastName = allStudentsCursor.getString(3);
                universityId = allStudentsCursor.getString(1);
                break;
            }
        }
        String title = null;
        String coverUrl = null;
        Cursor allBooksCursor = db.getAllBooks();
        while (allBooksCursor.moveToNext()) {
            if (allBooksCursor.getInt(0) == reservation.getBookId()) {
                title = allBooksCursor.getString(1);
                coverUrl = allBooksCursor.getString(6);
                availibility = allBooksCursor.getString(5);
                break;
            }
        }
        holder.tvStudentName.setText(firstName + " " + lastName);
        holder.tvUniversityId.setText("Univ. ID: " + universityId);
        holder.tvBookTitle.setText(title);
        if (coverUrl != null && !coverUrl.isEmpty()) {
            Picasso.get()
                    .load(coverUrl)
                    .into(holder.imgBookCover);
        }
        holder.tvReservationDate.setText("Reserved on: " + reservation.getReservationDate());
        holder.tvDueDate.setText("Due: " + reservation.getDueDate());
        holder.tvStatus.setText("Status: " + reservation.getStatus());
        holder.tvCollectionMethod.setText("Collection: " + reservation.getCollectionMethod());
        holder.tvFine.setText("Fine: $" + reservation.getFineAmount());
        holder.tvReturnDate.setText("Returned: " + reservation.getReturnDate());
        holder.tvNotes.setText("Notes: " + (reservation.getNotes() == null || reservation.getNotes().isEmpty() ? "—" : reservation.getNotes()));
        switch (reservation.getStatus().toLowerCase()) {
            case "active":
                holder.tvStatus.setTextColor(context.getColor(R.color.blue));
                holder.itemView.setBackgroundColor(context.getColor(R.color.midnight_blue));
                break;
            case "returned":
                holder.tvStatus.setTextColor(context.getColor(R.color.green));
                holder.itemView.setBackgroundColor(context.getColor(R.color.midnight_blue));
                break;
            case "overdue":
                holder.tvStatus.setTextColor(context.getColor(R.color.red));
                holder.itemView.setBackgroundColor(context.getColor(R.color.midnight_blue));
                break;
            case "extended":
                holder.tvStatus.setTextColor(context.getColor(R.color.orange));
                holder.itemView.setBackgroundColor(context.getColor(R.color.midnight_blue));
                break;
            case "rejected":
                holder.tvStatus.setTextColor(context.getColor(R.color.purple_200));
                holder.itemView.setBackgroundColor(context.getColor(R.color.midnight_blue));
                break;
            default:
                holder.tvStatus.setTextColor(context.getColor(R.color.white));
                holder.itemView.setBackgroundColor(context.getColor(R.color.midnight_blue));
                break;
        }

        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                if ("Pending".equalsIgnoreCase(reservation.getStatus())) {
                    if("Borrowed".equalsIgnoreCase(availibility)){
                        Toast.makeText(context, "Already borrowed", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    db.updateStatus(reservation.getId(), "Active");
                    reservation.setStatus("Active");
                } else {
                    db.updateStatus(reservation.getId(), "Extended");
                    reservation.setStatus("Extended");

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
                    LocalDate dueDate = LocalDate.parse(reservation.getDueDate(), formatter);

                    LocalDate newDueDate = dueDate.plusWeeks(1);

                    String newDateStr = newDueDate.format(formatter);

                    db.updateDueDate(reservation.getId(), newDateStr);
                    reservation.setDueDate(newDateStr);
                }
                notifyItemChanged(currentPosition);

                Cursor thisBook = db.getBookById(reservation.getBookId());
                if (thisBook != null && thisBook.moveToFirst()){
                    SQLiteDatabase sqlDb = db.getWritableDatabase();
                    sqlDb.execSQL("UPDATE Books SET availability = 'Borrowed' WHERE id = ?", new Object[]{reservation.getBookId()});
                }
                if (thisBook != null) thisBook.close();
            }

        });
        btnReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                if ("Pending".equalsIgnoreCase(reservation.getStatus())) {
                    db.updateStatus(reservation.getId(), "Rejected");
                    reservation.setStatus("Rejected");
                } else {
                    db.updateStatus(reservation.getId(), "Active");
                    reservation.setStatus("Active");
                }
                holder.itemView.post(() -> notifyItemChanged(currentPosition));

                SQLiteDatabase sqlDb = db.getWritableDatabase();
                Cursor cursor = sqlDb.rawQuery(
                        "SELECT status FROM Reservations WHERE book_id = ?",
                        new String[]{String.valueOf(reservation.getBookId())}
                );

                boolean hasPending = false;

                while (cursor.moveToNext()) {
                    String status = cursor.getString(0);
                    if ("Pending".equalsIgnoreCase(status)) {
                        hasPending = true;
                    }
                }
                cursor.close();
                String bookStatus;
                if (hasPending) {
                    bookStatus = "Reserved";
                } else {
                    bookStatus = "Available";
                }
                ContentValues bookValues = new ContentValues();
                bookValues.put("availability", bookStatus);
                sqlDb.update("Books", bookValues, "id = ?", new String[]{String.valueOf(reservation.getBookId())});
            }
        });
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                String currentDate = new java.text.SimpleDateFormat("yyyy/MM/dd", java.util.Locale.getDefault())
                        .format(new java.util.Date());
                db.updateStatus(reservation.getId(), "Returned");
                db.updateReturnDate(reservation.getId(), currentDate);
                reservation.setStatus("Returned");
                reservation.setReturnDate(currentDate);
                holder.itemView.post(() -> notifyItemChanged(currentPosition));

                SQLiteDatabase sqlDb = db.getWritableDatabase();
                Cursor cursor = sqlDb.rawQuery(
                        "SELECT status FROM Reservations WHERE book_id = ?",
                        new String[]{String.valueOf(reservation.getBookId())}
                );

                boolean hasPending = false;

                while (cursor.moveToNext()) {
                    String status = cursor.getString(0);
                    if ("Pending".equalsIgnoreCase(status)) {
                        hasPending = true;
                    }
                }
                cursor.close();
                String bookStatus;
                if (hasPending) {
                    bookStatus = "Reserved";
                } else {
                    bookStatus = "Available";
                }
                ContentValues bookValues = new ContentValues();
                bookValues.put("availability", bookStatus);
                sqlDb.update("Books", bookValues, "id = ?", new String[]{String.valueOf(reservation.getBookId())});
            }
        });

    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBookCover;
        TextView tvBookTitle, tvStudentName, tvUniversityId, tvFine, tvReturnDate;
        TextView tvReservationDate, tvDueDate, tvStatus, tvCollectionMethod, tvNotes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBookCover = itemView.findViewById(R.id.imgBookCover);
            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            tvUniversityId = itemView.findViewById(R.id.tvUniversityId);
            tvReservationDate = itemView.findViewById(R.id.tvReservationDate);
            tvDueDate = itemView.findViewById(R.id.tvDueDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvCollectionMethod = itemView.findViewById(R.id.tvCollectionMethod);
            tvNotes = itemView.findViewById(R.id.tvNotes);
            tvFine = itemView.findViewById(R.id.tvFine);
            tvReturnDate = itemView.findViewById(R.id.tvReturnDate);
        }
    }
}
