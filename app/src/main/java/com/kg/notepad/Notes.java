package com.kg.notepad;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes")
public class Notes {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String noteContent;
    private int priority;
    private String password;
    private String date;
   // private String imageUri;
    private byte[] image;

    public Notes(String title, String noteContent, int priority, String password, String date, byte[] image) {

        this.title = title;
        this.noteContent = noteContent;
        this.priority = priority;
        this.password = password;
        this.date = date;
        this.image = image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public int getPriority() {
        return priority;
    }

    public String getPassword() {
        return password;
    }

    public String getDate() {
        return date;
    }

    public byte[] getImage() {
        return image;
    }
}
