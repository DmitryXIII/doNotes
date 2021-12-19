package com.ineedyourcode.donotes.ui.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ineedyourcode.donotes.R;
import com.ineedyourcode.donotes.domain.Note;
import com.ineedyourcode.donotes.ui.adapter.AdapterItem;
import com.ineedyourcode.donotes.ui.adapter.NoteAdapterItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private ArrayList<AdapterItem> data = new ArrayList<>();

    private OnClick onClick;

    public OnClick getOnClick() {
        return onClick;
    }

    public void setOnClick(OnClick onClick) {
        this.onClick = onClick;
    }

    public void setData(Collection<AdapterItem> notes) {
        data.clear();
        data.addAll(notes);
    }

    @NonNull
    @Override
    public NotesAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.NoteViewHolder holder, int position) {

        NoteAdapterItem note = (NoteAdapterItem)data.get(position);

        holder.getNoteTitle().setText(note.getTitle());
        holder.getDate().setText(note.getTime());
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

        private final TextView date;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            noteTitle = itemView.findViewById(R.id.note_title);

            date = itemView.findViewById(R.id.note_create_date);

            itemView.findViewById(R.id.card).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AdapterItem item = data.get(getAdapterPosition());

                    if (item instanceof NoteAdapterItem) {
                        if (getOnClick() != null) {
                            getOnClick().onClick(((NoteAdapterItem) item).getNote());
                        }
                    }
                }
            });
        }

        public TextView getNoteTitle() {
            return noteTitle;
        }

        public TextView getDate() {
            return date;
        }
    }
}
