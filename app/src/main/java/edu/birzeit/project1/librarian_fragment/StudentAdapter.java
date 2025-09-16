package edu.birzeit.project1.librarian_fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import edu.birzeit.project1.R;
import edu.birzeit.project1.database.LibraryDataBase;
import edu.birzeit.project1.entities.Student;
import edu.birzeit.project1.student_fragments.MainActivity;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private final List<Student> students;
    private final Context context;
    public LibraryDataBase db;
    public StudentAdapter(Context context, List<Student> students) {
        this.context = context;
        this.students = students;
        db =new LibraryDataBase(context,LibraryDataBase.DATABASE_NAME,null,1);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.student_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Student student = students.get(position);

        holder.tvName.setText(student.getFirstName() + " " + student.getLastName());
        holder.tvUniversityId.setText("ID: " + student.getUniversityId());
        holder.tvEmail.setText("Email: " + student.getEmail());
        holder.tvDepartment.setText("Dept: " + student.getDepartment());
        holder.tvLevel.setText("Level: " + student.getLevel());
        holder.tvPhone.setText("Phone: " + student.getPhoneNumber());

        if (student.getProfilePicture() != null && student.getProfilePicture().length > 0) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(student.getProfilePicture(), 0, student.getProfilePicture().length);
            holder.imgProfile.setImageBitmap(bitmap);
        } else {
            holder.imgProfile.setImageResource(R.drawable.logo);
        }
        holder.itemView.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Delete Student");
                    builder.setMessage("Are you sure you want to delete " + student.getFirstName() + " " + student.getLastName() + "?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        LibraryDataBase db = new LibraryDataBase(context, LibraryDataBase.DATABASE_NAME, null, 1);
                        db.deleteStudentById(student.getId());  // 🔹 implement this function
                        students.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, students.size());
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvUniversityId, tvEmail, tvDepartment, tvLevel, tvPhone;
        ImageView imgProfile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            tvName = itemView.findViewById(R.id.tvName);
            tvUniversityId = itemView.findViewById(R.id.tvUniversityId);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvDepartment = itemView.findViewById(R.id.tvDepartment);
            tvLevel = itemView.findViewById(R.id.tvLevel);
            tvPhone = itemView.findViewById(R.id.tvPhone);
        }
    }
}
