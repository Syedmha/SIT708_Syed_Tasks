package com.example.sportsnewsapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sportsnewsapp.R;
import com.example.sportsnewsapp.adapter.RelatedAdapter;
import com.example.sportsnewsapp.data.NewsRepository;
import com.example.sportsnewsapp.model.NewsItem;
import com.example.sportsnewsapp.util.BookmarkManager;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends Fragment {

    private static final String ARG_ITEM = "news_item";

    public static DetailFragment newInstance(NewsItem item) {
        DetailFragment f = new DetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM, item);
        f.setArguments(args);
        return f;
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NewsItem item = (NewsItem) requireArguments().getSerializable(ARG_ITEM);
        if (item == null) return;

        // Bind views
        ((ImageView) view.findViewById(R.id.imgDetail))
                .setImageResource(item.getImageResId());
        ((TextView) view.findViewById(R.id.tvDetailTitle))
                .setText(item.getTitle());
        ((TextView) view.findViewById(R.id.tvDetailCategory))
                .setText(item.getCategory());
        ((TextView) view.findViewById(R.id.tvDetailDesc))
                .setText(item.getDescription());

        // Bookmark button
        Button btnBookmark = view.findViewById(R.id.btnBookmark);
        refreshBookmarkLabel(btnBookmark, item);
        btnBookmark.setOnClickListener(v -> {
            if (BookmarkManager.isBookmarked(requireContext(), item)) {
                BookmarkManager.removeBookmark(requireContext(), item);
                Toast.makeText(getContext(),
                        "Bookmark removed", Toast.LENGTH_SHORT).show();
            } else {
                BookmarkManager.saveBookmark(requireContext(), item);
                Toast.makeText(getContext(),
                        "Bookmarked!", Toast.LENGTH_SHORT).show();
            }
            refreshBookmarkLabel(btnBookmark, item);
        });

        // Back button
        view.findViewById(R.id.btnBack).setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack());

        // Related stories — same category, exclude current
        RecyclerView rvRelated = view.findViewById(R.id.rvRelated);
        rvRelated.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRelated.setAdapter(new RelatedAdapter(
                getRelatedStories(item),
                relatedItem -> {
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer,
                                    DetailFragment.newInstance(relatedItem))
                            .addToBackStack(null)
                            .commit();
                }));
    }

    private void refreshBookmarkLabel(Button btn, NewsItem item) {
        if (BookmarkManager.isBookmarked(requireContext(), item)) {
            btn.setText("★ Bookmarked");
        } else {
            btn.setText("☆ Bookmark This Story");
        }
    }

    private List<NewsItem> getRelatedStories(NewsItem current) {
        List<NewsItem> related = new ArrayList<>();
        for (NewsItem n : NewsRepository.getAllNews()) {
            if (n.getId() != current.getId()
                    && n.getCategory().equalsIgnoreCase(current.getCategory())) {
                related.add(n);
            }
        }
        return related;
    }
}