package com.example.eventplanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import java.util.List;

public class EventListFragment extends Fragment {

    private EventAdapter adapter;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        db = AppDatabase.getInstance(requireContext());
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Event> events = db.eventDao().getAllEvents();
        adapter = new EventAdapter(events, new EventAdapter.OnEventClickListener() {
            @Override
            public void onEdit(Event event) {
                Bundle bundle = new Bundle();
                bundle.putInt("eventId", event.id);
                Navigation.findNavController(view)
                        .navigate(R.id.addEditEventFragment, bundle);
            }

            @Override
            public void onDelete(Event event) {
                db.eventDao().delete(event);
                adapter.updateList(db.eventDao().getAllEvents());
                Snackbar.make(view, "Event deleted", Snackbar.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.updateList(db.eventDao().getAllEvents());
        }
    }
}