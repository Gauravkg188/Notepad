package com.kg.notepad;

import android.app.Application;


import androidx.lifecycle.LiveData;

import java.util.List;


public class NotesRepository {


    DatabaseNotes databaseNotes;
    private DaoInterface daoInterface;
    private LiveData<List<Notes>> notesList;

    public NotesRepository(Application application)
    {
         databaseNotes=DatabaseNotes.getInstance(application);
         daoInterface=databaseNotes.daoInterface();
         notesList=daoInterface.getAllNotes();
    }

    public void insert(final Notes notes)
    {

        DatabaseNotes.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                daoInterface.insert(notes);
            }
        });

    }


    public void delete(final Notes notes){

        DatabaseNotes.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                daoInterface.delete(notes);
            }
        });

    }

    public void update(final Notes notes)
    {
       DatabaseNotes.databaseWriteExecutor.execute(new Runnable() {
           @Override
           public void run() {
               daoInterface.update(notes);
           }
       });
    }

    public void deleteAllNotes()
    {
       DatabaseNotes.databaseWriteExecutor.execute(new Runnable() {
           @Override
           public void run() {
               daoInterface.deleteAllNotes();
           }
       });
    }

    public LiveData<List<Notes>> getNotesList() {
        return notesList;
    }



}
