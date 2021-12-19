package com.ineedyourcode.donotes.domain;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.StringRes;

import java.util.Date;
import java.util.Objects;

public class Note implements Parcelable {

    private String id;

    private String title;

    private String message;

    private Date createdAt;

    public Note(String id, String title, String message, Date createdAt) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.createdAt = createdAt;
    }

    protected Note(Parcel in) {
        id = in.readString();
        title = in.readString();
        message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return Objects.equals(id, note.id) && Objects.equals(title, note.title) && Objects.equals(message, note.message) && Objects.equals(createdAt, note.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, message, createdAt);
    }
}
