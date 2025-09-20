package edu.birzeit.project1.student_fragments;


import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.util.List;
import androidx.fragment.app.Fragment;
import edu.birzeit.project1.R;
import edu.birzeit.project1.database.LibraryDataBase;
import edu.birzeit.project1.entities.Book;
import edu.birzeit.project1.librarian_fragment.EditBookFragment;
import edu.birzeit.project1.prelogin.LoginActivity;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private Context context;
    private static List<Book> books;

    public static int BOOK_ID;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Book book);
    }
    private boolean btn_del_or_Add;
    private boolean btn_admin_control;

    public static BookAdapter BOOKADAPTER;

    public BookAdapter(Context context, List<Book> books,boolean btn_del_or_Add,boolean btn_admin_control, OnItemClickListener listener) {
        this.context = context;
        this.books = books;
        this.listener = listener;
        this.btn_del_or_Add = btn_del_or_Add;
        this.btn_admin_control =btn_admin_control;
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

        holder.btnEdit.setVisibility(View.GONE);
        holder.btnDelete.setVisibility(View.GONE);

        if(btn_admin_control){
            holder.btnAddFav.setVisibility(View.GONE);
            holder.btnRemove.setVisibility(View.GONE);
            holder.btnShare.setVisibility(View.GONE);
            holder.btnReserve.setVisibility(View.GONE);
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);
        }
        else {
            if(!btn_del_or_Add){
                holder.btnAddFav.setVisibility(View.GONE);
                holder.btnRemove.setVisibility(View.VISIBLE);
            }
            else{
                holder.btnAddFav.setVisibility(View.VISIBLE);
                holder.btnRemove.setVisibility(View.GONE);
            }

        }
        if (book.getAvailability().equals("Borrowed"))
            holder.btnReserve.setVisibility(View.GONE);


        holder.btnReserve.setOnClickListener(v -> {
            Animation bounce = AnimationUtils.loadAnimation(v.getContext(), R.anim.bounce);
            v.startAnimation(bounce);

            SQLiteDatabase db = new LibraryDataBase(v.getContext(), LibraryDataBase.DATABASE_NAME, null, 1).getReadableDatabase();
            Cursor cursor = db.rawQuery(
                    "SELECT COUNT(*) FROM Reservations WHERE student_id = ? AND book_id = ? AND status NOT IN ('Returned', 'Rejected')",
                    new String[]{String.valueOf(LoginActivity.logedInId), String.valueOf(book.getId())}
            );

            boolean canReserve = true;
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    canReserve = (cursor.getInt(0) == 0);
                }
                cursor.close();
            }
            db.close();

            if (!canReserve) {
                Toast.makeText(v.getContext(), "You have already reserved this book.", Toast.LENGTH_SHORT).show();
                return;
            }

            ReserveFormFragment dialog = new ReserveFormFragment();
            Bundle args = new Bundle();
            args.putInt("book_id", book.getId());
            args.putInt("student_id", LoginActivity.logedInId);
            args.putInt("position", position);
            BOOKADAPTER = this;
            dialog.setArguments(args);

            dialog.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), "ReservationDialog");

            Toast.makeText(v.getContext(), "Reservation form opened.", Toast.LENGTH_SHORT).show();
        });

        holder.btnAddFav.setOnClickListener(v ->{

            Animation bounce = AnimationUtils.loadAnimation(v.getContext(), R.anim.cover_flip);
            holder.imgCover.startAnimation(bounce);

            LibraryDataBase db =new LibraryDataBase(context,LibraryDataBase.DATABASE_NAME,null,1);
            boolean success = db.insertReadingList(LoginActivity.logedInId, book.getId());

            if (success) {
                Toast.makeText(context, "Reservation placed successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Failed to place reservation", Toast.LENGTH_SHORT).show();
            }

        });
        holder.btnRemove.setOnClickListener(v ->{
            LibraryDataBase db =new LibraryDataBase(context,LibraryDataBase.DATABASE_NAME,null,1);
            boolean success = db.removeBookFromReadingList(LoginActivity.logedInId, book.getId());
            removeBook(position);
        });

        holder.btnShare.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String appName = context.getString(R.string.app_name);
            String shareText = "Check out this book: "+holder.tvTitle.getText()+" by "+holder.tvAuthor.getText()+" on "+appName+"!";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            v.getContext().startActivity(Intent.createChooser(shareIntent, "Share via"));
        });
        holder.btnEdit.setOnClickListener(v -> {

            EditBookFragment dialog = new EditBookFragment();
            Bundle args = new Bundle();
            args.putInt("book_id", book.getId());
            args.putInt("student_id", LoginActivity.logedInId);
            args.putInt("position", position);
            BOOKADAPTER = this;
            dialog.setArguments(args);

            dialog.show(((AppCompatActivity) v.getContext())
                    .getSupportFragmentManager(), "EditBookDialog");
        });
        holder.btnDelete.setOnClickListener(v -> {
            LibraryDataBase db =new LibraryDataBase(context,LibraryDataBase.DATABASE_NAME,null,1);
            removeBook(position);
            db.deleteBook(book.getId());
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
        Button btnAddFav,btnRemove,btnShare,btnEdit,btnDelete;

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
            btnAddFav = itemView.findViewById(R.id.btn_Add);
            btnRemove = itemView.findViewById(R.id.btn_remove);
            btnShare = itemView.findViewById(R.id.btn_share);
            btnEdit = itemView.findViewById(R.id.btn_edit);
            btnDelete = itemView.findViewById(R.id.btn_delete);

        }
    }
    public void removeBook(int i){
        books.remove(i);
        notifyDataSetChanged();
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

    public void updateBook(int position,Book book) {
        books.set(position, book);
        notifyItemChanged(position);
    }

    public void updateBookAvailability(int position,String avail) {
        books.get(position).setAvailability(avail);
        notifyItemChanged(position);
    }



}
