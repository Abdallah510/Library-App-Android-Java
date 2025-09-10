package edu.birzeit.project1;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BorrowedBooksAdapter extends RecyclerView.Adapter<BorrowedBooksAdapter.ViewHolder> {

    private final List<BorrowedBooks> borrowedBooks;
    private final Context context;
    public LibraryDataBase db;
    public BorrowedBooksAdapter(Context context, List<BorrowedBooks> borrowedBooks) {
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

        BorrowedBooks book = borrowedBooks.get(position);
        Cursor cursor = db.getAllBooks();
        while (cursor.moveToNext()) {
            if (cursor.getInt(0) == book.getBookId()) {
                holder.tvBookTitle.setText(cursor.getString(1));
                holder.tvBookTitle.setText(cursor.getString(2));
                break;
            }
        }
        holder.tvBorrowDate.setText("Borrow: " + book.getBorrowDate());
        holder.tvDueDate.setText("Due: " + book.getDueDate());
        holder.tvReturnDate.setText("Returned: " + (book.getReturnDate().isEmpty() ? "-" : book.getReturnDate()));
        holder.tvStatus.setText(book.getStatus());
        holder.tvFine.setText("Fine: $" + book.getFineAmount());

        switch (book.getStatus().toLowerCase()) {
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
            default:
                holder.tvStatus.setTextColor(context.getColor(R.color.white));
                holder.itemView.setBackgroundColor(context.getColor(R.color.midnight_blue));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return borrowedBooks.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvBookTitle, tvAuthor, tvBorrowDate, tvDueDate, tvReturnDate, tvStatus, tvFine;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBookTitle = itemView.findViewById(R.id.tvBookTitle);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvBorrowDate = itemView.findViewById(R.id.tvBorrowDate);
            tvDueDate = itemView.findViewById(R.id.tvDueDate);
            tvReturnDate = itemView.findViewById(R.id.tvReturnDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvFine = itemView.findViewById(R.id.tvFine);
        }
    }
}
