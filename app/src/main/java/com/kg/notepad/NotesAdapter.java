package com.kg.notepad;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesHolder> {


    Context context;
    private List<Notes> notes=new ArrayList<>();
    clickListener listener;


    public NotesAdapter(Context context)
    {
        this.context=context;

    }

    @NonNull
    @Override
    public NotesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.note,parent,false);

        return new NotesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesHolder holder, int position) {
        String noPass="not password protected";
        String pass="password protected";
       final Notes currentNote=notes.get(position);
       holder.text_title.setText(currentNote.getTitle());
       holder.text_priority.setText(Integer.toString(currentNote.getPriority())) ;
       if(currentNote.getPassword()==null)
       {
           holder.text_password.setText(noPass);
       }
       else
       {
           holder.text_password.setText(pass);
       }

       holder.text_date.setText(currentNote.getDate());





       holder.cardView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {


                   listener.onclick();

               final Intent intent=new Intent(context,DetailActivity.class);
               intent.putExtra(String.valueOf(R.string.title),currentNote.getTitle());
               intent.putExtra(String.valueOf(R.string.content),currentNote.getNoteContent());
               intent.putExtra(String.valueOf(R.string.date),currentNote.getDate());
               intent.putExtra(String.valueOf(R.string.priority),currentNote.getPriority());
               intent.putExtra(String.valueOf(R.string.password),currentNote.getPassword());


               intent.putExtra("image",currentNote.getImage());
               intent.putExtra("id",currentNote.getId());


             if(currentNote.getPassword()==null)
             {
                 context.startActivity(intent);
             }
             else
             {

                 final android.app.AlertDialog.Builder alert=new AlertDialog.Builder(context);
                 final EditText input = new EditText(context);
                 input.setTransformationMethod(PasswordTransformationMethod.getInstance());
                 alert.setView(input);
                 alert.setTitle("Type Your PIN");
                 alert.setIcon(R.drawable.ic_launcher_background);
                 alert.setMessage("Please Type Your PASSWORD to Authenticate");
                 final String CORRECT_PIN = currentNote.getPassword();
                 alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int whichButton) {
                         if (CORRECT_PIN.equals(input.getText().toString())) {

                             context.startActivity(intent);
                         } else {
                             input.setError("Error");
                         }
                     }
                 });

                 alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog, int whichButton) {
                         dialog.cancel();
                     }
                 });

                 alert.show();
             }


           }
       });


    }

    @Override
    public int getItemCount() {
        return notes.size();
    }



    class NotesHolder extends RecyclerView.ViewHolder{

        private TextView text_title;
        private TextView text_priority;
        private TextView text_password;
        private TextView text_date;
        private CardView cardView;


        public NotesHolder(@NonNull View itemView) {
            super(itemView);
            text_priority=itemView.findViewById(R.id.text_priority);
            text_title=itemView.findViewById(R.id.text_title);
            text_password=itemView.findViewById(R.id.text_password);
            text_date=itemView.findViewById(R.id.text_date);
            cardView=itemView.findViewById(R.id.cardView);


        }
    }

    public void setNotes(List<Notes> notes)
    {
        this.notes=notes;
        notifyDataSetChanged();
    }

    public Notes getNote(int pos)
    {
        return notes.get(pos);
    }

    public interface clickListener{
          void onclick();


    }

    public void setOnClickListener(clickListener listener)
    {
        this.listener=listener;
    }


}
