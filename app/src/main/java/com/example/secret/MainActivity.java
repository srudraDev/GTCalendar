package com.example.secret;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.widget.Toolbar;


public class MainActivity extends AppCompatActivity {
    private Button sortDateButton;
    private TaskViewModel taskViewModel;
    private MutableLiveData<Boolean> sortTrigger = new MutableLiveData<>();

    AppBarConfiguration appBarConfiguration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_calendar,
                R.id.navigation_todo  // Specify the ID of your ToDoFragment
                // Add other fragment IDs if needed
        ).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {

            // Add the visibility logic for the "Sort by Date" button
            if (destination.getId() == R.id.navigation_todo) {
                // Show the Sort button in the ToDo section
                findViewById(R.id.buttonSortByDate).setVisibility(View.VISIBLE);
            } else {
                // Hide the Sort button in other sections
                findViewById(R.id.buttonSortByDate).setVisibility(View.GONE);
            }
        });

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        sortDateButton = findViewById(R.id.buttonSortByDate);
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        sortDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskViewModel.sortByDate();
                refreshToDoFragment();
                sortTrigger.setValue(true);
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public LiveData<Boolean> getSortTrigger() {
        return sortTrigger;
    }

    private void refreshToDoFragment() {
        // Find the ToDoFragment by its tag
        ToDoFragment toDoFragment = (ToDoFragment) getSupportFragmentManager().findFragmentByTag("ToDoFragment");
        // If the fragment is found, refresh it
        if (toDoFragment != null) {
            toDoFragment.refreshList();
        }
    }
}