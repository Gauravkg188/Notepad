package com.kg.notepad;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NotesActivity extends AppCompatActivity {

    EditText edit_title,edit_content,edit_password,edit_priority;
    Button button_submit;
    String title,content,password;
    int priority;
    Uri imageUri;
    private static int Code = 1 ;
    private static int REQUESTCODE = 1 ;
    private NotesViewModel notesViewModel;
    ImageView temp_image,imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        edit_content=findViewById(R.id.edit_content);
        edit_title=findViewById(R.id.edit_Title);
        edit_password=findViewById(R.id.edit_password);
        edit_priority=findViewById(R.id.edit_priority);
        button_submit=findViewById(R.id.button_submit);
        temp_image=findViewById(R.id.temp_img);
        imageView=findViewById(R.id.imageView);
        imageView.setEnabled(false);

        notesViewModel= new ViewModelProvider(this).get(NotesViewModel.class);
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       getSupportActionBar().setDisplayShowHomeEnabled(true);
       getSupportActionBar().setTitle("Add Note");


        temp_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(Build.VERSION.SDK_INT>=23)
                {
                    checkPermission();

                }
                else
                {
                    openGallery();
                }
            }

        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mBuilder = new
                        AlertDialog.Builder(NotesActivity.this);
                View mView=getLayoutInflater().inflate(R.layout.zoom_image,null);
                PhotoView photoView = mView.findViewById(R.id.imageView);
                Picasso.get().load(imageUri).into(photoView);
                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });


        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               title=edit_title.getText().toString().trim();
               content=edit_content.getText().toString().trim();
               password=edit_password.getText().toString().trim();
               priority= Integer.parseInt(edit_priority.getText().toString().trim());

               if(title.isEmpty() || content.isEmpty() || edit_priority.getText().toString().isEmpty())
               {
                   Toast.makeText(NotesActivity.this,"fill title,content,priority duly",Toast.LENGTH_LONG).show();
               }
               else
               {
                   Date currentDate = Calendar.getInstance().getTime();


                   SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                   String date = df.format(currentDate);
                   if(password.isEmpty())
                       password=null;
                   byte[] image_byte;
                   if(imageView.getDrawable()==null)
                   {
                       String images="no";
                    image_byte=images.getBytes();
                   }
                   else
                   {
                       image_byte=imageViewToByte(imageView);
                   }
                   Notes notes=new Notes(title,content,priority,password,date,image_byte);
                  notesViewModel.insert(notes);
                   Toast.makeText(NotesActivity.this, "successfully added", Toast.LENGTH_SHORT).show();
                   finish();
               }

            }
        });


    }


    private void checkPermission() {


        if(ContextCompat.checkSelfPermission(NotesActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {
          /*  if(ActivityCompat.shouldShowRequestPermissionRationale(NotesActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                Toast.makeText(NotesActivity.this, "Please Accept the required permission", Toast.LENGTH_SHORT).show();
            }
            else
            {
                ActivityCompat.requestPermissions(NotesActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},Code);
            }*/

            ActivityCompat.requestPermissions(NotesActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},Code);
            if(ContextCompat.checkSelfPermission(NotesActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
            {
                openGallery();
            }

        }
        else
        {
            openGallery();
        }
    }

    private void openGallery() {

        Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESTCODE);


    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && requestCode==REQUESTCODE && data!=null)
        {
            imageUri=data.getData();

            try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);

            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            imageView.setImageBitmap(bitmap);
                imageView.setEnabled(true);
            } catch ( FileNotFoundException e) {
            e.printStackTrace();}

        }

    }

}