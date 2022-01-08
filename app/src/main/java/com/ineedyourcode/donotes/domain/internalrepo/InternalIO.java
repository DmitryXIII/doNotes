package com.ineedyourcode.donotes.domain.internalrepo;

import android.content.Context;

import com.ineedyourcode.donotes.domain.Note;

import java.util.ArrayList;
import java.util.Date;

public interface InternalIO {
    ArrayList<Note> filesToNotesList();

    void saveNoteFile(String noteTitle, String noteContent);

    String openNoteFile(String noteTitle, Context context);

    void deleteNoteFile(String noteTitle);

    Date getNoteFileCreateDate(String noteTitle);
}