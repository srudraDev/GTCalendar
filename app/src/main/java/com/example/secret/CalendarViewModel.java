package com.example.secret;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class CalendarViewModel extends ViewModel {

    private MutableLiveData<List<ClassItem>> classList = new MutableLiveData<>();

    public LiveData<List<ClassItem>> getClassList() {
        if (classList.getValue() == null) {
            // Initialize the classList if it's not already set
            classList.setValue(new ArrayList<>());
        }
        return classList;
    }

    public void addClass(ClassItem classItem) {
        List<ClassItem> currentList = classList.getValue();
        assert currentList != null;
        currentList.add(classItem);
        classList.setValue(currentList);
    }

    // Other methods for manipulating the class list
}
