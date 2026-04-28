package com.example.sportsnewsapp.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.sportsnewsapp.model.NewsItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BookmarkManager {

    private static final String PREF_NAME = "sportsnewsapp_bookmarks";
    private static final String KEY_LIST  = "bookmark_list";

    public static void saveBookmark(Context ctx, NewsItem item) {
        List<NewsItem> list = getBookmarks(ctx);
        for (NewsItem n : list) {
            if (n.getId() == item.getId()) return; // already saved
        }
        list.add(item);
        persist(ctx, list);
    }

    public static void removeBookmark(Context ctx, NewsItem item) {
        List<NewsItem> list = getBookmarks(ctx);
        list.removeIf(n -> n.getId() == item.getId());
        persist(ctx, list);
    }

    public static boolean isBookmarked(Context ctx, NewsItem item) {
        for (NewsItem n : getBookmarks(ctx)) {
            if (n.getId() == item.getId()) return true;
        }
        return false;
    }

    public static List<NewsItem> getBookmarks(Context ctx) {
        SharedPreferences prefs =
                ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_LIST, null);
        if (json == null) return new ArrayList<>();
        Type type = new TypeToken<List<NewsItem>>() {}.getType();
        return new Gson().fromJson(json, type);
    }

    private static void persist(Context ctx, List<NewsItem> list) {
        SharedPreferences prefs =
                ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit()
                .putString(KEY_LIST, new Gson().toJson(list))
                .apply();
    }
}