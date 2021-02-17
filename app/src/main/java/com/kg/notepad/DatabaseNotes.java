package com.kg.notepad;

import android.content.Context;

import androidx.room.Database;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Notes.class},version = 1)
public abstract class DatabaseNotes extends RoomDatabase {

private static DatabaseNotes Instance;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);


public abstract DaoInterface daoInterface();

public static synchronized DatabaseNotes getInstance(Context context)
{
    if(Instance ==null)
    {
        Instance= Room.databaseBuilder(context.getApplicationContext(),DatabaseNotes.class,
                "databaseNotes")
                .fallbackToDestructiveMigration()
                .build();
    }
    return Instance;
}



}
