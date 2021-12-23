package com.ineedyourcode.donotes.ui.notecontent;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.DrawableRes;

import com.ineedyourcode.donotes.domain.Note;

public interface AddNoteView {

    void showProgress();

    void hideProgress();

    void setFabIcon(int icon);

    void setTitle(String title);

    void setMessage(String message);

    void actionCompleted(String key, Bundle bundle);
}
