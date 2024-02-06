package com.example.secret;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secret.ClassItem;
import com.example.secret.R;

import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private List<ClassItem> classList;

    public ScheduleAdapter(List<ClassItem> classList) {
        this.classList = classList;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout and create a new ViewHolder
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item, parent, false);
        return new ScheduleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        // Bind data to views in the ViewHolder
        ClassItem classItem = classList.get(position);

        holder.classNameTextView.setText(classItem.getClassName());
        holder.dayOfWeekTextView.setText(getDayOfWeekString(classItem.getDayOfWeek()));
        holder.timeTextView.setText(String.format("%s - %s", classItem.getStartTime(), classItem.getEndTime()));
    }

    @Override
    public int getItemCount() {
        // Check if classList is null, and return 0 if it is
        return (classList != null) ? classList.size() : 0;
    }

    public void setClassList(List<ClassItem> classList) {
        this.classList = classList;
    }

    // ViewHolder class
    public static class ScheduleViewHolder extends RecyclerView.ViewHolder {
        // Declare views here
        TextView classNameTextView;
        TextView dayOfWeekTextView;
        TextView timeTextView;
        AppCompatButton editDelete;

        public ScheduleViewHolder(View itemView) {
            super(itemView);
            // Initialize views here
            classNameTextView = itemView.findViewById(R.id.classNameTextView);
            dayOfWeekTextView = itemView.findViewById(R.id.dayOfWeekTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            editDelete = itemView.findViewById(R.id.Edit_Delete);
        }
    }

    // Helper method to get the string representation of the day of the week
    private String getDayOfWeekString(String dayOfWeek) {
        return dayOfWeek;
    }
}