package com.example.secret;

import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.security.auth.callback.Callback;

public class WeeklyCalendarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<GridObject> mGridObjects;

    private static OnAdapterItemClickListener adapterItemClickListener = null;

    public static class TimeViewHolder extends RecyclerView.ViewHolder {
        public TextView eventTextView;
        public TimeViewHolder(View v) {
            super(v);
            eventTextView = v.findViewById(R.id.eventTextView); // Assuming this ID in your item layout
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 8 == 0) {
            return 1;
        }
        return 0;
    }

    public static class ClassViewHolder extends RecyclerView.ViewHolder {
        public TextView eventTextView;
        public Button crudButton;

        //public Icon classIcon;
        public ClassViewHolder(View v) {
            super(v);
            eventTextView = v.findViewById(R.id.eventTextView);
            crudButton = v.findViewById(R.id.timeSlotButton);
            crudButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Notify the activity or parent fragment
                    adapterItemClickListener.onAdapterItemClickListener(getLayoutPosition());
                }
            });
        }
    }

    public WeeklyCalendarAdapter(List<GridObject> timeSlots, OnAdapterItemClickListener listener) {
        mGridObjects = timeSlots;
        adapterItemClickListener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_hour_event, parent, false);
                return new TimeViewHolder(v);
            default:
                View w = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_class_event, parent, false);
                return new ClassViewHolder(w);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == 1) {
            TimeViewHolder timeViewHolder = (TimeViewHolder) holder;
            timeViewHolder.eventTextView.setText(formatHour(position));
        } else {
            ClassViewHolder classViewHolder = (ClassViewHolder) holder;
            GridObject gridObject = mGridObjects.get(position);
            classViewHolder.eventTextView.setText(gridObject.getDisplayName());
        }
        // Set other text views or views as needed
    }

    private static String formatHour(int position) {
        // Customize this method based on how you want to display the hours
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, position/8); // Set the hour based on the position
        SimpleDateFormat sdf = new SimpleDateFormat("ha", Locale.getDefault()); // "ha" will format it like "1AM", "2PM"
        return sdf.format(calendar.getTime());
    }

    @Override
    public int getItemCount() {
        return mGridObjects.size();
    }
}
