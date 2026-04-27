package com.example.eventplanner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.google.android.material.snackbar.Snackbar;
import java.util.Calendar;

public class AddEditEventFragment extends Fragment {

    private EditText etTitle, etLocation;
    private Spinner spinnerCategory;
    private TextView tvSelectedDate;
    private String selectedDateTime = "";
    private AppDatabase db;
    private int eventId = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_edit_event, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        db = AppDatabase.getInstance(requireContext());

        etTitle = view.findViewById(R.id.etTitle);
        etLocation = view.findViewById(R.id.etLocation);
        spinnerCategory = view.findViewById(R.id.spinnerCategory);
        tvSelectedDate = view.findViewById(R.id.tvSelectedDate);
        Button btnPickDate = view.findViewById(R.id.btnPickDate);
        Button btnSave = view.findViewById(R.id.btnSave);

        // Setup category spinner
        String[] categories = {"Work", "Social", "Travel", "Health", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(adapter);

        // Check if editing
        if (getArguments() != null) {
            eventId = getArguments().getInt("eventId", -1);
        }
        if (eventId != -1) {
            Event event = db.eventDao().getAllEvents()
                    .stream().filter(e -> e.id == eventId).findFirst().orElse(null);
            if (event != null) {
                etTitle.setText(event.title);
                etLocation.setText(event.location);
                selectedDateTime = event.dateTime;
                tvSelectedDate.setText(event.dateTime);
                int pos = java.util.Arrays.asList(categories).indexOf(event.category);
                if (pos >= 0) spinnerCategory.setSelection(pos);
            }
        }

        // Date & Time picker
        btnPickDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            new DatePickerDialog(requireContext(), (datePicker, year, month, day) -> {
                new TimePickerDialog(requireContext(), (timePicker, hour, minute) -> {
                    Calendar selected = Calendar.getInstance();
                    selected.set(year, month, day, hour, minute);
                    if (selected.before(Calendar.getInstance())) {
                        Snackbar.make(view, "Cannot pick a past date!", Snackbar.LENGTH_SHORT).show();
                    } else {
                        selectedDateTime = String.format("%04d/%02d/%02d %02d:%02d",
                                year, (month + 1), day, hour, minute);
                        tvSelectedDate.setText(selectedDateTime);
                    }
                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
        });

        // Save button
        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String location = etLocation.getText().toString().trim();
            String category = spinnerCategory.getSelectedItem().toString();

            if (title.isEmpty()) {
                Snackbar.make(view, "Title is required!", Snackbar.LENGTH_SHORT).show();
                return;
            }
            if (selectedDateTime.isEmpty()) {
                Snackbar.make(view, "Please pick a date and time!", Snackbar.LENGTH_SHORT).show();
                return;
            }

            Event event = new Event();
            event.title = title;
            event.category = category;
            event.location = location;
            event.dateTime = selectedDateTime;

            if (eventId == -1) {
                db.eventDao().insert(event);
                Snackbar.make(view, "Event added!", Snackbar.LENGTH_SHORT).show();
            } else {
                event.id = eventId;
                db.eventDao().update(event);
                Snackbar.make(view, "Event updated!", Snackbar.LENGTH_SHORT).show();
            }

            Navigation.findNavController(view).navigate(R.id.eventListFragment);
        });
    }
}