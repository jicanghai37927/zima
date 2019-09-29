package com.haiyunshan.zima.note;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.haiyunshan.zima.ComposerActivity;
import com.haiyunshan.zima.R;
import com.haiyunshan.zima.note.dataset.NoteEntry;

public class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    View mPhotoContainer;
    ImageView mPhotoView;
    TextView mTitleView;
    TextView mSourceView;

    NoteEntry mEntry;

    NoteFragment mParent;

    public static NoteHolder create(NoteFragment parent, ViewGroup container) {
        LayoutInflater inflater = parent.getLayoutInflater();
        int resource = R.layout.layout_note_item;
        View view = inflater.inflate(resource, container, false);

        NoteHolder item = new NoteHolder(parent, view);
        return item;
    }

    public NoteHolder(NoteFragment parent, View itemView) {
        super(itemView);

        this.mParent = parent;

        {
            itemView.findViewById(R.id.story_interest_container).setVisibility(View.GONE);
            itemView.findViewById(R.id.story_topic_large_image).setVisibility(View.GONE);
            itemView.findViewById(R.id.story_large_image).setVisibility(View.GONE);
            itemView.findViewById(R.id.story_expand_more).setVisibility(View.GONE);
            itemView.findViewById(R.id.story_topic).setVisibility(View.GONE);
        }

        {
            this.mPhotoContainer = itemView.findViewById(R.id.story_photo_container);
            this.mPhotoView = itemView.findViewById(R.id.story_photo);
            this.mTitleView = itemView.findViewById(R.id.story_title);
            this.mSourceView = itemView.findViewById(R.id.story_source);
        }

        {
            itemView.setOnClickListener(this);
        }
    }

    void bind(int position, NoteEntry entry) {

        this.mEntry = entry;

        mPhotoContainer.setVisibility(View.GONE);
        mTitleView.setText(entry.getTitle());
        mSourceView.setText(entry.getSubtitle());


    }

    @Override
    public void onClick(View v) {
        if (v == itemView) {
            String id = mEntry.getId();

            ComposerActivity.startForResult(mParent, id, NoteFragment.REQUEST_COMPOSE, v);
            mParent.mComposeId = id;
        }
    }
}
