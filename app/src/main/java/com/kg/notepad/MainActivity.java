/**
  by Gaurav Kumar
  A Note making application with functionalities to save note with text and image and have password for each note
 */



package com.kg.notepad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NotesViewModel notesViewModel;
    private RecyclerView recyclerView;
    NotesAdapter adapter;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        floatingActionButton = findViewById(R.id.add_button);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new NotesAdapter(MainActivity.this);
        recyclerView.setAdapter(adapter);
        notesViewModel = new ViewModelProvider(this).get(NotesViewModel.class);
        notesViewModel.getNotesList().observe(MainActivity.this, new Observer<List<Notes>>() {
            @Override
            public void onChanged(List<Notes> notes) {

                adapter.setNotes(notes);
            }
        });

        adapter.setOnClickListener(new NotesAdapter.clickListener() {
            @Override
            public void onclick() {
                Toast.makeText(MainActivity.this,"hello",Toast.LENGTH_LONG).show();


            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NotesActivity.class);
                startActivity(intent);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                final Notes notes = adapter.getNote(viewHolder.getAdapterPosition());
                if (direction == ItemTouchHelper.LEFT) {

                    notesViewModel.delete(notes);
                    Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
                    Snackbar.make(recyclerView, null, Snackbar.LENGTH_LONG)
                            .setAction("Undo delete", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    notesViewModel.insert(notes);
                                    Toast.makeText(MainActivity.this, "undo successfully", Toast.LENGTH_SHORT).show();
                                }
                            }).show();
                }

            }
        }).attachToRecyclerView(recyclerView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                notesViewModel.deleteAllNotes();
                Toast.makeText(this, "Notes deleted", Toast.LENGTH_SHORT).show();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }

    }

}





