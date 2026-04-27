package com.example.eventplanner;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface EventDao {
    @Insert
    void insert(Event e);

    @Update
    void update(Event e);

    @Delete
    void delete(Event e);

    @Query("SELECT * FROM events ORDER BY dateTime ASC")
    List<Event> getAllEvents();
}