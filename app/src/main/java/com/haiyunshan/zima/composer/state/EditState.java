package com.haiyunshan.zima.composer.state;

import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.haiyunshan.zima.R;
import com.haiyunshan.zima.composer.ComposerFragment;

public class EditState extends BaseState implements Toolbar.OnMenuItemClickListener {

    EditState(ComposerFragment parent) {
        super(StateMachine.EDIT, parent);
    }

    @Override
    public void onEnter() {
        super.onEnter();

        {
            mToolbar.getMenu().clear();
            mToolbar.inflateMenu(R.menu.composer_edit_menu);

            mToolbar.setNavigationIcon(R.drawable.ic_done_black_24dp);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mParent.closeEdit();
                }
            });

            mToolbar.setOnMenuItemClickListener(this);
        }

        {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onExit() {
        super.onExit();
    }

    public boolean onBackPressed() {
        mParent.closeEdit();

        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menu_separate: {
                mParent.separate();
                break;
            }
            case R.id.menu_picture: {
                mParent.selectPhoto();
                break;
            }
        }

        return true;
    }
}
