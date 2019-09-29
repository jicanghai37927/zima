package com.haiyunshan.zima;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.haiyunshan.zima.composer.ComposerFragment;

public class ComposerActivity extends AppCompatActivity {

    FrameLayout mContainer;

    public static final void startForResult(Fragment fragment, String id, int requestCode, View trigger) {
        Intent intent = new Intent(fragment.getContext(), ComposerActivity.class);
        intent.putExtra("id", id);

        Bundle bundle = null;
//        if (trigger != null) {
//            bundle = ActivityOptions.makeSceneTransitionAnimation(fragment.getActivity(), trigger, "sharedView").toBundle();
//        }

//        bundle = ActivityOptions.makeSceneTransitionAnimation(fragment.getActivity()).toBundle();

        fragment.startActivityForResult(intent, requestCode, bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_composer);

        this.mContainer = findViewById(R.id.fragment_container);
        this.showNote();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = this.getSupportFragmentManager();
        Fragment f = fm.findFragmentByTag("composer");
        if (f != null) {
            ComposerFragment fragment = (ComposerFragment)f;
            if (fragment.onBackPressed()) {
                return;
            }
        }

        super.onBackPressed();
    }

    public void showNote() {
        FragmentManager fm = this.getSupportFragmentManager();

        ComposerFragment f = new ComposerFragment();
        f.setArguments(getIntent().getExtras());

        FragmentTransaction t = fm.beginTransaction();
        t.replace(mContainer.getId(), f, "composer");
        t.commit();
    }
}
