package com.kg.notepad;


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


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

  EditText title,content,password,priority,date;
    ImageView  imageView,image_click,image_close;
    Button button_submit;
    int id;
    Uri imageUri;
    private static int Code = 1 ;
    private static int REQUESTCODE = 1 ;
    Bitmap bitmap;

    byte[] imagesBytes;
    NotesViewModel notesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent=getIntent();
        title=findViewById(R.id.edit_Title);
        imageView=findViewById(R.id.imageView);
        content=findViewById(R.id.edit_content);
        password=findViewById(R.id.edit_password);
        priority=findViewById(R.id.edit_priority);
        date=findViewById(R.id.edit_date);
        button_submit=findViewById(R.id.button_submit);
        image_click=findViewById(R.id.image_click);
        image_close=findViewById(R.id.image_close);

        title.setText(intent.getStringExtra(String.valueOf(R.string.title)));
        content.setText(intent.getStringExtra(String.valueOf(R.string.content)));
        final String passwrd=intent.getStringExtra(String.valueOf(R.string.password));
        if(passwrd==null)
        {    password.setText("No password");}
        else
        { password.setText(passwrd);}

        priority.setText(Integer.toString(intent.getIntExtra(String.valueOf(R.string.priority),0)));
        date.setText(intent.getStringExtra(String.valueOf(R.string.date)));
        id=intent.getIntExtra("id",0);

        title.setEnabled(false);
        content.setEnabled(false);
        priority.setEnabled(false);
        password.setEnabled(false);
        date.setEnabled(false);
        image_close.setEnabled(false);
        image_click.setEnabled(false);

        notesViewModel= new ViewModelProvider(DetailActivity.this).get(NotesViewModel.class);


        getSupportActionBar().setTitle("DetailActivity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        byte[] image=intent.getByteArrayExtra("image");
        final byte[] image_byte;
        String images="no";
        image_byte=images.getBytes();
       imagesBytes=images.getBytes();


        if(image_byte==image)
        {
            imageView.setImageBitmap(null);
            imageView.setEnabled(false);
        }
        else{
        bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        imageView.setImageBitmap(bitmap);
        }



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder mBuilder = new
                        AlertDialog.Builder(DetailActivity.this);
                View mView=getLayoutInflater().inflate(R.layout.zoom_image,null);
                PhotoView photoView = mView.findViewById(R.id.imageView);
                photoView.setImageBitmap(bitmap);

                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });


        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(button_submit.getText().toString().equals("Update")) {
                    button_submit.setText("Submit");
                    password.getText().clear();
                    title.setEnabled(true);
                    content.setEnabled(true);
                    priority.setEnabled(true);
                    password.setEnabled(true);
                    date.setEnabled(true);
                    image_click.setEnabled(true);
                    image_close.setEnabled(true);

                }
                else if(button_submit.getText().toString().equals("Submit"))
                {
                    if(title.getText().toString().isEmpty() || content.getText().toString().isEmpty() || priority.getText().toString().isEmpty())
                {
                    Toast.makeText(DetailActivity.this,"fill title,content,priority duly",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Date currentDate = Calendar.getInstance().getTime();


                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                    String date = df.format(currentDate);

                    String temp_pass=password.getText().toString().trim();
                    if(temp_pass.isEmpty())
                        temp_pass=null;



                        Notes note=new Notes(title.getText().toString(),content.getText().toString(),Integer.parseInt(priority.getText().toString().trim())
                                ,temp_pass,date,imagesBytes);
                        note.setId(id);
                        notesViewModel.update(note);


                    Toast.makeText(DetailActivity.this, "successfully updated", Toast.LENGTH_SHORT).show();
                    finish();

                }

                }


            }
        });


      image_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageView.setImageBitmap(null);
                imagesBytes=image_byte;


            }
        });

        image_click.setOnClickListener(new View.OnClickListener() {
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

    }


    private void checkPermission() {


        if(ContextCompat.checkSelfPermission(DetailActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)
        {


            ActivityCompat.requestPermissions(DetailActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},Code);
            if(ContextCompat.checkSelfPermission(DetailActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)
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




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode==RESULT_OK && requestCode==REQUESTCODE && data!=null)
        {
            imageUri=data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);

                bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
                imagesBytes=imageViewToByte(imageView);
                imageView.setEnabled(true);

            } catch ( FileNotFoundException e) {
                e.printStackTrace();}

        }

    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }



}