package com.haiyunshan.zima.composer.state;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.haiyunshan.zima.R;
import com.haiyunshan.zima.composer.BottomSheetFragment;
import com.haiyunshan.zima.composer.ComposerFragment;
import com.haiyunshan.zima.composer.ParagraphFormatFragment;

public class FormatState extends BaseState implements Toolbar.OnMenuItemClickListener {

    FormatState(ComposerFragment parent) {
        super(StateMachine.FORMAT, parent);
    }

    @Override
    public void onEnter() {
        super.onEnter();

        {
            mToolbar.getMenu().clear();
            mToolbar.inflateMenu(R.menu.composer_format_menu);

            mToolbar.setNavigationIcon(R.drawable.ic_done_black_24dp);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mParent.closeFormat();
                }
            });

            mToolbar.setOnMenuItemClickListener(this);
        }

        {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)(mRecyclerView.getLayoutParams());
            params.topMargin = 0;
            params.bottomMargin = mContext.getResources().getDimensionPixelSize(R.dimen.composer_bottom_sheet_size);

            mToolbar.setVisibility(View.GONE);

            BottomSheetFragment f = mParent.getBottomSheet();
            f.setContent(new ParagraphFormatFragment(), "paragraph_format");

            FragmentTransaction t = mParent.getChildFragmentManager().beginTransaction();
            t.show(mParent.getBottomSheet());
            t.commit();

            mParent.getView().findViewById(R.id.composer_bottom_bar_container).setVisibility(View.GONE);
        }

        {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onExit() {
        super.onExit();
    }

    @Override
    public boolean onBackPressed() {
        if (mBottomSheet.onBackPressed()) {
            return true;
        }

        return mParent.closeFormat();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {

            default: {
                break;
            }

        }

        return false;
    }
}
