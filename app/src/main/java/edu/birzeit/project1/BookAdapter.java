package edu.birzeit.project1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

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
        holder.tvISBN.setText(book.getIsbn());              // NEW
        holder.tvPubYear.setText(String.valueOf(book.getPublicationYear())); // NEW

//        // Load cover image
//        Glide.with(context)
//                .load(book.getCoverUrl())
//                .placeholder(R.drawable.placeholder_cover)
//                .into(holder.imgCover);
//
//        holder.itemView.setOnClickListener(v -> listener.onItemClick(book));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCover;
        TextView tvTitle, tvAuthor, tvCategory, tvAvailability, tvISBN, tvPubYear;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCover = itemView.findViewById(R.id.img_cover);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvAuthor = itemView.findViewById(R.id.tv_author);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvAvailability = itemView.findViewById(R.id.tv_availability);
            tvISBN = itemView.findViewById(R.id.tv_isbn);        // NEW
            tvPubYear = itemView.findViewById(R.id.tv_pub_year); // NEW
        }
    }
    public void addBook(Book book) {
        books.add(book);
        notifyItemInserted(books.size() - 1);
    }
}
