package com.ineedyourcode.donotes.domain;

import android.content.Context;

import java.util.List;

public interface NotesRepository {
    void getAll(Callback<List<Note>> callback);
}
