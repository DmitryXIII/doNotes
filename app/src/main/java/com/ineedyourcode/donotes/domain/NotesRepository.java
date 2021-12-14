package com.ineedyourcode.donotes.domain;

import android.content.Context;

import java.util.List;

public interface NotesRepository {
    List<Note> getNotes(Context context);
}
