package com.haiyunshan.zima.note;


import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haiyunshan.zima.ComposerActivity;
import com.haiyunshan.zima.R;
import com.haiyunshan.zima.note.dataset.NoteManager;
import com.haiyunshan.zima.utils.UUIDUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteFragment extends Fragment implements View.OnClickListener {

    static final int REQUEST_COMPOSE = 1001;

    Toolbar mToolbar;

    RecyclerView mRecyclerView;
    NoteAdapter mAdapter;

    TextView mNewNoteBtn;

    String mComposeId;

    public NoteFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        {
            this.mToolbar = view.findViewById(R.id.toolbar);
        }

        {
            mRecyclerView = view.findViewById(R.id.note_list_view);
            LinearLayoutManager layout = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(layout);

        }

        {
            this.mNewNoteBtn = view.findViewById(R.id.new_note_button);
            mNewNoteBtn.setOnClickListener(this);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        {
            this.mAdapter = new NoteAdapter(this);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.w("AA", "onActivityResult");

        if (requestCode == REQUEST_COMPOSE) {
            NoteManager.instance().save();

            mAdapter.update(mComposeId);
            mComposeId = null;
        }
    }

    NoteAdapter getAdapter() {
        return mAdapter;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public void onClick(View v) {
        if (v == mNewNoteBtn) {
            String id = UUIDUtils.next();
            ComposerActivity.startForResult(this, id, REQUEST_COMPOSE, v);

            this.mComposeId = id;
        }
    }
}
