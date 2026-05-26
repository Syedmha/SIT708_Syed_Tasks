package com.example.lostandfound;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView  recyclerView;
    private ItemAdapter   adapter;
    private DatabaseHelper dbHelper;
    private Spinner        spinnerFilter;

    private final String[] filterOptions = {
            "All", "Electronics", "Pets",
            "Wallets", "Keys", "Bags", "Clothing", "Documents", "Other"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerView  = findViewById(R.id.recyclerView);
        spinnerFilter = findViewById(R.id.spinnerFilter);
        dbHelper      = new DatabaseHelper(this);

        // Set RecyclerView layout — LinearLayoutManager = vertical list
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load all items initially
        loadItems("All");

        // Set up the filter spinner
        ArrayAdapter<String> filterAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, filterOptions);
        filterAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinnerFilter.setAdapter(filterAdapter);

        // When user selects a category in spinner, reload the list filtered
        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view,
                                       int position, long id) {
                loadItems(filterOptions[position]);
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    // Reload RecyclerView data based on filter selection
    private void loadItems(String category) {
        List<LostFoundItem> items;

        if (category.equals("All")) {
            items = dbHelper.getAllItems();       // No filter
        } else {
            items = dbHelper.getItemsByCategory(category); // Filtered query
        }

        // Create new adapter with the fetched list and attach to RecyclerView
        adapter = new ItemAdapter(this, items);
        recyclerView.setAdapter(adapter);
    }

    // onResume() is called when you come BACK to this screen
    // We reload the list so new/deleted items are always up to date
    @Override
    protected void onResume() {
        super.onResume();
        loadItems(spinnerFilter.getSelectedItem().toString());
    }
}
