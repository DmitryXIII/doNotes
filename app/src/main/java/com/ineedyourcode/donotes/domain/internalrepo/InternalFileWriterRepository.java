package com.ineedyourcode.donotes.domain.internalrepo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.ineedyourcode.donotes.R;
import com.ineedyourcode.donotes.domain.Callback;
import com.ineedyourcode.donotes.domain.Note;
import com.ineedyourcode.donotes.domain.NotesRepository;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiresApi(api = Build.VERSION_CODES.O)
public class InternalFileWriterRepository implements NotesRepository, InternalIO {

    private static InternalFileWriterRepository INSTANCE;

    public static InternalFileWriterRepository getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new InternalFileWriterRepository(context);
        }
        return INSTANCE;
    }

    private Context context;

    private List<Note> result = new ArrayList<>();

    public InternalFileWriterRepository(Context context) {
        this.context = context.getApplicationContext();
        filesToNotesList();
    }

    @Override
    public void getAll(Callback<List<Note>> callback) {
        callback.onSuccess(result);
    }

    @Override
    public void save(String noteTitle, String noteContent, Callback<Note> callback) {
        saveNoteFile(noteTitle, noteContent);
        Date createAt = null;
        File file = new File(context.getString(R.string.path_name));
        for (File listFile : file.listFiles()) {
            if (listFile.isFile() && listFile.getName().equals(noteTitle + ".txt")) {
                createAt = getNoteFileCreateDate(noteTitle);
            }
        }
        Note note = new Note(UUID.randomUUID().toString(), noteTitle, noteContent, createAt);
        result.add(note);
        callback.onSuccess(note);
    }

    @Override
    public void update(String noteId, String title, String content, Callback<Note> callback) {
        int index = 0;

        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getId().equals(noteId)) {
                index = i;
                break;
            }
        }

        Note editableNote = result.get(index);

        editableNote.setTitle(title);
        editableNote.setContent(content);

        saveNoteFile(title, content);

        callback.onSuccess(editableNote);
    }

    @Override
    public void delete(Note note, Callback<Void> callback) {
        result.remove(note);
        deleteNoteFile(note.getTitle());
        callback.onSuccess(null);
    }

    @Override
    public void filesToNotesList() {
        String noteTitle;
        String noteContent;
        Date createAt;
        File file = new File(context.getString(R.string.path_name));
        for (File listFile : file.listFiles()) {
            if (listFile.isFile()) {
                noteTitle = listFile.getName().substring(0, (listFile.getName().length() - 4));
                noteContent = openNoteFile(noteTitle, context);
                createAt = getNoteFileCreateDate(noteTitle);
                result.add(new Note(UUID.randomUUID().toString(), noteTitle, noteContent, createAt));
            }
        }
    }

    @Override
    public void saveNoteFile(String noteTitle, String noteContent) {
        try {
            OutputStream outputStream = context.openFileOutput(noteTitle + ".txt", 0);
            OutputStreamWriter osw = new OutputStreamWriter(outputStream);
            osw.write(noteContent);
            osw.close();
        } catch (IOException e) {
            Toast.makeText(context, "Exception: " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public String openNoteFile(String noteTitle, Context context) {
        try {
            InputStream inputStream = context.openFileInput(noteTitle + ".txt");

            if (inputStream != null) {
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(isr);
                String line;
                StringBuilder builder = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }

                inputStream.close();
                return builder.toString();
            }
        } catch (IOException e) {
            Toast.makeText(context, "Exception: " + e.toString(), Toast.LENGTH_LONG).show();
        }
        return "null";
    }

    @Override
    public void deleteNoteFile(String noteTitle) {
        File file = new File(context.getString(R.string.path_name));
        for (File listFile : file.listFiles()) {
            if (listFile.isFile() && listFile.getName().equals(noteTitle + ".txt")) {
                listFile.delete();
            }
        }
    }

    @Override
    public Date getNoteFileCreateDate(String noteTitle) {
        File file = new File(context.getString(R.string.path_name) + noteTitle + ".txt");
        Path filePath = file.toPath();

        BasicFileAttributes attributes = null;

        try {
            attributes = Files.readAttributes(filePath, BasicFileAttributes.class);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        long milliseconds = attributes.creationTime().to(TimeUnit.MILLISECONDS);

        if ((milliseconds > Long.MIN_VALUE) && (milliseconds < Long.MAX_VALUE)) {
            return new Date(attributes.creationTime().to(TimeUnit.MILLISECONDS));
        }
        return null;
    }
}