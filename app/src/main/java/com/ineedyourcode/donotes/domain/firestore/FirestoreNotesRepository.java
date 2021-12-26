package com.ineedyourcode.donotes.domain.firestore;

import android.app.Notification;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.ineedyourcode.donotes.DoNotesApp;
import com.ineedyourcode.donotes.R;
import com.ineedyourcode.donotes.domain.Callback;
import com.ineedyourcode.donotes.domain.Note;
import com.ineedyourcode.donotes.domain.NotesRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.O)
public class FirestoreNotesRepository implements NotesRepository {
    public static final NotesRepository INSTANCE = new FirestoreNotesRepository();

    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_CREATED_AT = "createdAt";
    private static final String COLLECTION_NOTES = "Notes";

    private static final String CHANNEL_ID = "CHANNEL_ID";
    private static final int NOTIFICATION_ID = 1;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    FirestoreNotesRepository() {
        NotificationChannelCompat channel = new NotificationChannelCompat.Builder(CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT)
                .setDescription(DoNotesApp.getAppContext().getString(R.string.channel_description))
                .setName(DoNotesApp.getAppContext().getString(R.string.channel_name))
                .build();

        NotificationManagerCompat.from(DoNotesApp.getAppContext()).createNotificationChannel(channel);
        notifyMe();
    }

    @Override
    public void getAll(Callback<List<Note>> callback) {
        db.collection(COLLECTION_NOTES)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        ArrayList<Note> result = new ArrayList<>();

                        for (DocumentSnapshot document : documents) {
                            String id = document.getId();
                            String title = document.getString(KEY_TITLE);
                            String content = document.getString(KEY_CONTENT);
                            Date createdAt = document.getDate(KEY_CREATED_AT);
                            result.add(new Note(id, title, content, createdAt));
                        }

                        callback.onSuccess(result);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onError(e);
                    }
                });
    }

    @Override
    public void save(String title, String content, Callback<Note> callback) {
        Map<String, Object> data = new HashMap<>();

        Date createdAt = new Date();

        data.put(KEY_TITLE, title);
        data.put(KEY_CONTENT, content);
        data.put(KEY_CREATED_AT, createdAt);

        db.collection(COLLECTION_NOTES)
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String id = documentReference.getId();

                        callback.onSuccess(new Note(id, title, content, createdAt));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onError(e);
                    }
                });
    }

    @Override
    public void update(Note note, String title, String content, Callback<Note> callback) {
        Map<String, Object> data = new HashMap<>();

        data.put(KEY_CREATED_AT, note.getCreatedAt());
        data.put(KEY_TITLE, title);
        data.put(KEY_CONTENT, content);

        db.collection(COLLECTION_NOTES)
                .document(note.getId())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        note.setTitle(title);
                        note.setContent(content);

                        callback.onSuccess(note);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onError(e);
                    }
                });
    }

    @Override
    public void delete(Note note, Callback<Void> callback) {
        db.collection(COLLECTION_NOTES)
                .document(note.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        callback.onSuccess(unused);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        callback.onError(e);
                    }
                });
    }

    private void notifyMe() {
        db.collection(COLLECTION_NOTES)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        String notifyMessage;
                        if (e != null) {
                            System.out.println(DoNotesApp.getAppContext().getString(R.string.failed) + e);
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    notifyMessage = DoNotesApp.getAppContext().getString(R.string.notification_new_note) + " \"" + dc.getDocument().getString(KEY_TITLE) + "\"";
                                    showNotification(notifyMessage);
                                    break;
                                case MODIFIED:
                                    notifyMessage = DoNotesApp.getAppContext().getString(R.string.notification_note_edited) + " \"" + dc.getDocument().getString(KEY_TITLE) + "\"";
                                    showNotification(notifyMessage);
                                    break;
                                case REMOVED:
                                    notifyMessage = DoNotesApp.getAppContext().getString(R.string.notification_note_deleted) + " \"" + dc.getDocument().getString(KEY_TITLE) + "\"";
                                    showNotification(notifyMessage);
                                    break;
                            }
                        }

                    }
                });
    }

    private void showNotification(String message) {

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(DoNotesApp.getAppContext());

        Notification compat = new  NotificationCompat.Builder(DoNotesApp.getAppContext(), CHANNEL_ID)
                .setContentTitle(DoNotesApp.getAppContext().getString(R.string.notification_name))
                .setContentText(message)
                .setSmallIcon(R.drawable.app_icon)
                .build();

        notificationManager.notify(NOTIFICATION_ID, compat);
    }
}