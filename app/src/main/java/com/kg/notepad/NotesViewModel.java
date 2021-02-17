package com.kg.notepad;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NotesViewModel extends AndroidViewModel {


    private NotesRepository notesRepository;



    private LiveData<List<Notes>> notesList;

    public NotesViewModel(@NonNull Application application) {
        super(application);
        notesRepository=new NotesRepository(application);
        notesList=notesRepository.getNotesList();
    }

    public void insert(Notes notes)
    {
       notesRepository.insert(notes);
    }
    public void delete(Notes notes)
    {
        notesRepository.delete(notes);
    }

    public void update(Notes notes)
    {
        notesRepository.update(notes);
    }

    public void deleteAllNotes()
    {
        notesRepository.deleteAllNotes();
    }

    public LiveData<List<Notes>> getNotesList() {
        return notesList;
    }
}
