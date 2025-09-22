package edu.birzeit.project1.student_fragments;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;
import edu.birzeit.project1.database.LibraryDataBase;
import edu.birzeit.project1.R;
import edu.birzeit.project1.entities.Reservation;

public class BorrowedBooksAdapter extends RecyclerView.Adapter<BorrowedBooksAdapter.ViewHolder> {

    private final List<Reservation> borrowedBooks;
    private final Context context;

    public LibraryDataBase db;
    public BorrowedBooksAdapter(Context context, List<Reservation> borrowedBooks) {
        this.context = context;
        this.borrowedBooks = borrowedBooks;
        db =new LibraryDataBase(context,LibraryDataBase.DATABASE_NAME,null,1);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.borrowed_book_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Reservation reservation = borrowedBooks.get(position);
        Cursor cursor = db.getAllBooks();
        Button btnReturn = holder.itemView.findViewById(R.id.btnReturn);
        Button btnExtend = holder.itemView.findViewById(R.id.btnExtend);
        String coverUrl = null;
        if ("Rejected".equalsIgnoreCase(reservation.getStatus()) || "Returned".equalsIgnoreCase(reservation.getStatus())){
            btnReturn.setVisibility(View.GONE);
            btnExtend.setVisibility(View.GONE);
        }
        if("Pickup".equalsIgnoreCase(reservation.getCollectionMethod())){
            btnReturn.setVisibility(View.GONE);
        }
        if("Pending Extend".equalsIgnoreCase(reservation.getStatus())){
            btnExtend.setVisibility(View.GONE);
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
        while (cursor.moveToNext()) {
            if (cursor.getInt(0) == reservation.getBookId()) {
                holder.tvBookTitle.setText(cursor.getString(1));
                holder.tvAuthor.setText(cursor.getString(2));
                coverUrl = cursor.getString(6);
                break;
            }
        }
        if (coverUrl != null && !coverUrl.isEmpty()) {
            Picasso.get()
                    .load(coverUrl)
                    .into(holder.imgBookCover);
        }
        holder.tvBorrowDate.setText("Borrow: " + reservation.getReservationDate());
        holder.tvDueDate.setText("Due: " + reservation.getDueDate());
        holder.tvReturnDate.setText("Returned: " + reservation.getReturnDate());
        holder.tvStatus.setText("Status: " + reservation.getStatus());
        holder.tvFine.setText("Fine: $" + reservation.getFineAmount());

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
            }
        });
        btnExtend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                db.updateStatus(reservation.getId(), "Pending Extend");
                reservation.setStatus("Pending Extend");
                holder.itemView.post(() -> notifyItemChanged(currentPosition));
            }
        });
    }
    @Override
    public int getItemCount() {
        return borrowedBooks.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookTitle, tvAuthor, tvBorrowDate, tvDueDate, tvReturnDate, tvStatus, tvFine;
        ImageView imgBookCover;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvBorrowDate = itemView.findViewById(R.id.tvBorrowDate);
            tvDueDate = itemView.findViewById(R.id.tvDueDate);
            tvReturnDate = itemView.findViewById(R.id.tvReturnDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvFine = itemView.findViewById(R.id.tvFine);
            imgBookCover = itemView.findViewById(R.id.imgBookCover);
        }
    }
}
