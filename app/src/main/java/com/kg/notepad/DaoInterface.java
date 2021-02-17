package com.kg.notepad;



import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DaoInterface {

    @Insert
    void insert(Notes notes);

    @Delete
    void delete(Notes notes);

    @Update
    void update(Notes notes);

    @Query("SELECT * FROM notes ORDER BY priority ASC")
    LiveData<List<Notes>> getAllNotes();

    @Query("DELETE FROM notes")
    void deleteAllNotes();
}
