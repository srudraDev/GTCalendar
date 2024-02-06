package com.example.secret;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;
import java.util.List;


public class CalendarFragment extends Fragment implements OnAdapterItemClickListener {
    public static final String EVENT = "event";
    public static final String RETURN = "return";
    public static int mostRecentPos = 0;
    private ActivityResultLauncher<Intent> newActivityResultLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        newActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                (ActivityResultCallback<ActivityResult>) result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                            TimeSlot myDay = (((TimeSlot) data.getParcelableExtra(RETURN)));
                            CalendarService.slots.set(mostRecentPos, myDay);
                    }
                });
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        RecyclerView recyclerViewDays = view.findViewById(R.id.recyclerViewDays);

        int numberOfColumns = 8; // 7 days of the week, 1 column for time
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getContext(), numberOfColumns);
        recyclerViewDays.setLayoutManager(gridLayoutManager);
        WeeklyCalendarAdapter adapter = new WeeklyCalendarAdapter(CalendarService.slots, this);

        recyclerViewDays.setAdapter(adapter);
    }
    @Override
    public void onAdapterItemClickListener(int position) {
        mostRecentPos = position;
        Intent intent = new Intent(this.getContext(), OptionsFragment.class);
        intent.putExtra(EVENT, (TimeSlot) CalendarService.slots.get(position));
        newActivityResultLauncher.launch(intent);
    }
}
