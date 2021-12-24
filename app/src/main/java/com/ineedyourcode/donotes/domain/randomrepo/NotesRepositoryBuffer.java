package com.ineedyourcode.donotes.domain.randomrepo;

import android.os.Handler;
import android.os.Looper;

import com.ineedyourcode.donotes.domain.Callback;
import com.ineedyourcode.donotes.domain.Note;
import com.ineedyourcode.donotes.domain.NotesRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NotesRepositoryBuffer implements NotesRepository {

    public static final NotesRepository INSTANCE = new NotesRepositoryBuffer();

    private final ArrayList<Note> notes = new ArrayList<>();

    private final Executor executor = Executors.newSingleThreadExecutor();

    private final Handler handler = new Handler(Looper.getMainLooper());

    private NotesRepositoryBuffer() {

        Calendar calendar = Calendar.getInstance();

        for (int i = 1; i < 51; i++) {
            calendar.add(Calendar.DAY_OF_YEAR, -(int) (1 + Math.random() * 365));
            calendar.add(Calendar.MINUTE, -(int) (1 + Math.random() * 60));
            String text = "Note_" + i;
            notes.add(new Note(UUID.randomUUID().toString(), text, contentMaker(text), calendar.getTime()));
        }
    }

    private String contentMaker(String content) {
        String newContent = content;
        for (int i = 0; i < 200; i++) {
            newContent = newContent + ", " + content;
        }
        return newContent;
    }

    @Override
    public void getAll(Callback<List<Note>> callback) {

        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(notes);
                    }
                });
            }
        });
    }

    @Override
    public void save(String title, String content, Callback<Note> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Note note = new Note(UUID.randomUUID().toString(), title, content, new Date());

                        notes.add(note);

                        callback.onSuccess(note);
                    }
                });
            }
        });
    }

    @Override
    public void update(String noteId, String title, String content, Callback<Note> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        int index = 0;

                        for (int i = 0; i < notes.size(); i++) {
                            if (notes.get(i).getId().equals(noteId)) {
                                index = i;
                                break;
                            }
                        }

                        Note editableNote = notes.get(index);

                        editableNote.setTitle(title);
                        editableNote.setContent(content);

                        callback.onSuccess(editableNote);
                    }
                });
            }
        });
    }

    @Override
    public void delete(Note note, Callback<Void> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        notes.remove(note);

                        callback.onSuccess(null);
                    }
                });
            }
        });
    }
}
