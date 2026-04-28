package com.example.sportsnewsapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportsnewsapp.R;
import com.example.sportsnewsapp.adapter.NewsAdapter;
import com.example.sportsnewsapp.model.NewsItem;
import com.example.sportsnewsapp.util.BookmarkManager;

import java.util.List;

public class BookmarksFragment extends Fragment {

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bookmarks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Back button
        view.findViewById(R.id.btnBackFromBookmarks).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());

        List<NewsItem> bookmarks =
                BookmarkManager.getBookmarks(requireContext());
        TextView tvEmpty = view.findViewById(R.id.tvEmpty);

        if (bookmarks.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            RecyclerView rv = view.findViewById(R.id.rvBookmarks);
            rv.setLayoutManager(new LinearLayoutManager(getContext()));
            rv.setAdapter(new NewsAdapter(bookmarks, item -> {
                requireActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainer,
                                DetailFragment.newInstance(item))
                        .addToBackStack(null)
                        .commit();
            }));
        }
    }
}