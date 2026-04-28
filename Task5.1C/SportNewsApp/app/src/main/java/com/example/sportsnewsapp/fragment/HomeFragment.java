package com.example.sportsnewsapp.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportsnewsapp.R;
import com.example.sportsnewsapp.adapter.FeaturedAdapter;
import com.example.sportsnewsapp.adapter.NewsAdapter;
import com.example.sportsnewsapp.data.NewsRepository;
import com.example.sportsnewsapp.model.NewsItem;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FeaturedAdapter featuredAdapter;
    private NewsAdapter     newsAdapter;

    private List<NewsItem> allFeatured;
    private List<NewsItem> allNews;

    private String searchQuery       = "";
    private String selectedCategory  = "All";

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        allFeatured = NewsRepository.getFeaturedMatches();
        allNews     = NewsRepository.getLatestNews();

        // Featured horizontal RecyclerView
        RecyclerView rvFeatured = view.findViewById(R.id.rvFeatured);
        rvFeatured.setLayoutManager(new LinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL, false));
        featuredAdapter = new FeaturedAdapter(
                new ArrayList<>(allFeatured), this::openDetail);
        rvFeatured.setAdapter(featuredAdapter);

        // News vertical RecyclerView
        RecyclerView rvNews = view.findViewById(R.id.rvNews);
        rvNews.setLayoutManager(new LinearLayoutManager(getContext()));
        newsAdapter = new NewsAdapter(
                new ArrayList<>(allNews), this::openDetail);
        rvNews.setAdapter(newsAdapter);

        // Search bar
        EditText etSearch = view.findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int a, int b, int c) {}
            @Override public void onTextChanged(CharSequence s, int a, int b, int c) {
                searchQuery = s.toString().toLowerCase().trim();
                applyFilter();
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // Category spinner
        Spinner spinner = view.findViewById(R.id.spinnerCategory);
        String[] categories = {"All", "Football", "Basketball", "Cricket"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                categories);
        spinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v,
                                       int pos, long id) {
                selectedCategory = categories[pos];
                applyFilter();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Bookmarks button
        view.findViewById(R.id.btnBookmarks).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer, new BookmarksFragment())
                        .addToBackStack(null)
                        .commit());
    }

    private void applyFilter() {
        featuredAdapter.updateList(filterList(allFeatured));
        newsAdapter.updateList(filterList(allNews));
    }

    private List<NewsItem> filterList(List<NewsItem> source) {
        List<NewsItem> result = new ArrayList<>();
        for (NewsItem item : source) {
            boolean matchCat = selectedCategory.equals("All")
                    || item.getCategory().equalsIgnoreCase(selectedCategory);
            boolean matchSearch = searchQuery.isEmpty()
                    || item.getTitle().toLowerCase().contains(searchQuery)
                    || item.getCategory().toLowerCase().contains(searchQuery);
            if (matchCat && matchSearch) result.add(item);
        }
        return result;
    }

    private void openDetail(NewsItem item) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, DetailFragment.newInstance(item))
                .addToBackStack(null)
                .commit();
    }
}