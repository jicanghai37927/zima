package com.haiyunshan.zima;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.haiyunshan.zima.note.NoteFragment;

public class NoteActivity extends AppCompatActivity {

    FrameLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        this.mContainer = findViewById(R.id.fragment_container);
        this.showNote();
    }

    public void showNote() {
        FragmentManager fm = this.getSupportFragmentManager();

        NoteFragment f = new NoteFragment();

        FragmentTransaction t = fm.beginTransaction();
        t.replace(mContainer.getId(), f, "note");
        t.commit();
    }

}
