package com.ineedyourcode.donotes.ui.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ineedyourcode.donotes.R;
import com.ineedyourcode.donotes.domain.Note;

import java.util.ArrayList;
import java.util.Collection;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private ArrayList<Note> data = new ArrayList<Note>();

    private OnClick onClick;

    public OnClick getOnClick() {
        return onClick;
    }

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    public void setData(Collection<Note> notes) {
        data.clear();
        data.addAll(notes);
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {

        Note note = data.get(position);

        holder.getNoteTitle().setText(note.getNoteTitle());
        holder.getNoteCreateDate().setText(note.getNoteCreateDate());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    interface OnClick {
        void onClick(Note note);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {

        private final TextView noteTitle;

        private final TextView noteCreateDate;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            noteTitle = itemView.findViewById(R.id.note_title);

            noteCreateDate = itemView.findViewById(R.id.note_create_date);

            itemView.findViewById(R.id.card).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Note note = data.get(getAdapterPosition());

                    if (getOnClick() != null) {
                        getOnClick().onClick(note);
                    }
                }
            });
        }

        public TextView getNoteTitle() {
            return noteTitle;
        }

        public TextView getNoteCreateDate() {
            return noteCreateDate;
        }
    }
}