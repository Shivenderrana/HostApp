package com.sonu.hostelmanagementsystem;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import java.util.List;


class StudentsAttendanceAdapter extends RecyclerView.Adapter<StudentsAttendanceAdapter.ViewHolder> {

    public List<Student> students;

    public StudentsAttendanceAdapter(List<Student> students) {
        this.students = students;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_attendance_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Student student = students.get(position);
        holder.checkedTextView.setText(student.getFullName());
        holder.checkedTextView.setChecked(students.get(position).isPresent());

        if(holder.checkedTextView.isChecked()){
            holder.checkedTextView.setCheckMarkDrawable(R.drawable.star_icon_green);
        } else if(!holder.checkedTextView.isChecked()){
            holder.checkedTextView.setCheckMarkDrawable(R.drawable.star_icon_red);
        }

        holder.checkedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.checkedTextView.isChecked()) {
                    holder.checkedTextView.setChecked(false);

                    int adapterPosition = position;
                    students.get(position).setPresent(false);
                    holder.checkedTextView.setCheckMarkDrawable(R.drawable.star_icon_red);
                } else {
                    holder.checkedTextView.setChecked(true);

                    int adapterPosition = position;
                    students.get(adapterPosition).setPresent(true);
                    holder.checkedTextView.setCheckMarkDrawable(R.drawable.star_icon_green);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public CheckedTextView checkedTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            checkedTextView = (CheckedTextView) itemView.findViewById(R.id.checkedTextView);

        }
    }
}
