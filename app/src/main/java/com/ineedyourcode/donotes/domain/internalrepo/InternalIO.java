package com.ineedyourcode.donotes.domain.internalrepo;

import android.content.Context;
import java.util.Date;

public interface InternalIO {
    void filesToNotesList();

    void saveNoteFile(String noteTitle, String noteContent);

    String openNoteFile(String noteTitle, Context context);

    void deleteNoteFile(String noteTitle);

    Date getNoteFileCreateDate(String noteTitle);
}