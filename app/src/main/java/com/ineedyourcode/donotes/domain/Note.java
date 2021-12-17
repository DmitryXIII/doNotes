package com.ineedyourcode.donotes.domain;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.StringRes;

public class Note implements Parcelable {

    private String noteTitle;

    private String noteContent;

    @StringRes
    private final int noteCreateDate;

    public Note(String noteTitle, String noteContent, int noteCreateDate) {
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.noteCreateDate = noteCreateDate;
    }

    protected Note(Parcel in) {
        noteTitle = in.readString();
        noteContent = in.readString();
        noteCreateDate= in.readInt();
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

    public String getNoteTitle() {
        return noteTitle;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public int getNoteCreateDate() {
        return noteCreateDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(noteTitle);
        dest.writeString(noteContent);
        dest.writeInt(noteCreateDate);
    }
}
