package edu.birzeit.project1.student_fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.List;

import edu.birzeit.project1.R;
import edu.birzeit.project1.entities.Book;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private Context context;
    private List<Book> books;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Book book);
    }

    public BookAdapter(Context context, List<Book> books, OnItemClickListener listener) {
        this.context = context;
        this.books = books;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);

        holder.tvTitle.setText(book.getTitle());
        holder.tvAuthor.setText(book.getAuthor());
        holder.tvCategory.setText(book.getCategory());
        holder.tvAvailability.setText(book.getAvailability());
        holder.tvISBN.setText(book.getIsbn());
        holder.tvPubYear.setText(String.valueOf(book.getPublicationYear()));

        if (book.getAvailability().equals("Available"))
            holder.tvAvailability.setTextColor(context.getResources().getColor(R.color.green));
        else if (book.getAvailability().equals("Reserved"))
            holder.tvAvailability.setTextColor(context.getResources().getColor(R.color.orange));
        else
            holder.tvAvailability.setTextColor(context.getResources().getColor(R.color.red));


        Picasso.get().load(book.getCoverUrl()).into(holder.imgCover);

        holder.btnReserve.setOnClickListener(v -> {
            ReserveFormFragment dialog = new ReserveFormFragment();

            Bundle args = new Bundle();
            args.putInt("book_id", 2004);
            args.putInt("student_id",2004);
            dialog.setArguments(args);

            dialog.show(((AppCompatActivity) v.getContext())
                    .getSupportFragmentManager(), "ReservationDialog");
        });
    }


    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCover;
        TextView tvTitle, tvAuthor, tvCategory, tvAvailability, tvISBN, tvPubYear;
        Button btnReserve;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCover = itemView.findViewById(R.id.img_cover);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvAuthor = itemView.findViewById(R.id.tv_author);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvAvailability = itemView.findViewById(R.id.tv_availability);
            tvISBN = itemView.findViewById(R.id.tv_isbn);
            tvPubYear = itemView.findViewById(R.id.tv_pub_year);
            btnReserve = itemView.findViewById(R.id.btn_reserve_borrow);
        }
    }

    public void addBook(Book book) {
        books.add(book);
        notifyItemInserted(books.size() - 1);
    }
    public void updateList(List<Book> newList) {
        books.clear();
        books.addAll(newList);
        notifyDataSetChanged();
    }

}
