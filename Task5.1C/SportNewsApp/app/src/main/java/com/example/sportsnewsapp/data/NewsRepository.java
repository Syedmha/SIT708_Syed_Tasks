package com.example.sportsnewsapp.data;

import com.example.sportsnewsapp.R;
import com.example.sportsnewsapp.model.NewsItem;

import java.util.ArrayList;
import java.util.List;

public class NewsRepository {

    // Featured matches — shown in horizontal RecyclerView
    public static List<NewsItem> getFeaturedMatches() {
        List<NewsItem> list = new ArrayList<>();
        list.add(new NewsItem(1,
                "Man Utd vs Arsenal",
                "A thrilling Premier League clash ended 2-2 at Old Trafford. " +
                        "Both teams showed incredible form with goals from both sides " +
                        "in a dramatic finish that kept fans on the edge of their seats.",
                "Football", R.drawable.img_football));
        list.add(new NewsItem(2,
                "Lakers vs Warriors",
                "LeBron James led the Lakers to a stunning 112-108 victory " +
                        "over the Warriors in a game that went down to the final buzzer. " +
                        "A masterclass performance from the entire squad.",
                "Basketball", R.drawable.img_basketball));
        list.add(new NewsItem(3,
                "AUS vs IND Test",
                "Australia dominated Day 1 of the Test match, posting 320/4 " +
                        "by stumps. Warner scored a brilliant century to put Australia " +
                        "firmly in control of the opening Test.",
                "Cricket", R.drawable.img_cricket));
        return list;
    }

    // Latest news — shown in vertical RecyclerView
    public static List<NewsItem> getLatestNews() {
        List<NewsItem> list = new ArrayList<>();
        list.add(new NewsItem(4,
                "World Cup Qualifiers Round-Up",
                "Several nations secured their World Cup spots after nail-biting " +
                        "qualifier matches played across Europe and South America this week.",
                "Football", R.drawable.img_football));
        list.add(new NewsItem(5,
                "NBA Playoffs Preview",
                "The NBA playoffs begin next week with eight exciting matchups. " +
                        "Analysts predict a tough road for all top seeds this season.",
                "Basketball", R.drawable.img_basketball));
        list.add(new NewsItem(6,
                "IPL Season Highlights",
                "The IPL season has delivered breathtaking moments. Here are the " +
                        "top performances that defined this year's tournament so far.",
                "Cricket", R.drawable.img_cricket));
        list.add(new NewsItem(7,
                "Transfer Window Rumours",
                "Top clubs are eyeing major signings as the summer transfer window " +
                        "approaches. Several high-profile moves are reportedly close to " +
                        "being finalised this month.",
                "Football", R.drawable.img_football));
        list.add(new NewsItem(8,
                "Women's Test Series",
                "The women's Test series saw a historic performance as the home " +
                        "team clinched the series 2-1 after an outstanding batting display " +
                        "in the final match.",
                "Cricket", R.drawable.img_cricket));
        list.add(new NewsItem(9,
                "Rising Stars to Watch",
                "A new generation of basketball talent is emerging in college " +
                        "leagues. Scouts are watching these five players very closely " +
                        "throughout this season.",
                "Basketball", R.drawable.img_basketball));
        return list;
    }

    // All stories combined — used for related stories
    public static List<NewsItem> getAllNews() {
        List<NewsItem> all = new ArrayList<>();
        all.addAll(getFeaturedMatches());
        all.addAll(getLatestNews());
        return all;
    }
}