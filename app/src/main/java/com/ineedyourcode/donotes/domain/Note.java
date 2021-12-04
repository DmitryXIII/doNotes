package com.ineedyourcode.donotes.domain;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.StringRes;

public class Note implements Parcelable {

    @StringRes
    private final int noteTitle;

    @StringRes
    private final int noteContent;

    @StringRes
    private final int noteCreateDate;

    public Note(int noteTitle, int noteContent, int noteCreateDate) {
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.noteCreateDate = noteCreateDate;
    }

    protected Note(Parcel in) {
        noteTitle = in.readInt();
        noteContent = in.readInt();
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

    public int getNoteTitle() {
        return noteTitle;
    }

    public int getNoteContent() {
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
        dest.writeInt(noteTitle);
        dest.writeInt(noteContent);
        dest.writeInt(noteCreateDate);
    }
}
