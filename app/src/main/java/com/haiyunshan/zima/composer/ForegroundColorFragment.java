package com.haiyunshan.zima.composer;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haiyunshan.zima.R;
import com.haiyunshan.zima.color.ColorPanel;
import com.haiyunshan.zima.composer.helper.ParagraphSpanHelper;
import com.haiyunshan.zima.composer.holder.ParagraphHolder;
import com.haiyunshan.zima.composer.observable.SelectionObservable;
import com.haiyunshan.zima.composer.observer.SelectionObserver;
import com.haiyunshan.zima.widget.ColorLayout;
import com.haiyunshan.zima.widget.ColorView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForegroundColorFragment extends Fragment implements View.OnClickListener, Toolbar.OnMenuItemClickListener {

    TextView mTitleView;
    Toolbar mToolbar;

    View mBtnPanel;
    ColorView mBtnNoColor;

    ArrayList<ColorView> mList;
    LinearLayout mContainer;

    ParagraphSpanHelper mSpanHelper;

    BottomSheetFragment mBottomSheetFragment;
    ComposerFragment mComposerFragment;

    int mColor;

    public ForegroundColorFragment() {
        this.mList = new ArrayList<>();

        this.mColor = Color.TRANSPARENT;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_foreground_color, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        {
            this.mBtnPanel = view.findViewById(R.id.btn_color_panel);
            mBtnPanel.setOnClickListener(this);

            this.mBtnNoColor = view.findViewById(R.id.btn_no_color);
            mBtnNoColor.setOnClickListener(this);

            this.mContainer = view.findViewById(R.id.color_container);
        }

        {
            this.mTitleView = view.findViewById(R.id.tv_title);
            mTitleView.setText("文本色");

            this.mToolbar = view.findViewById(R.id.toolbar);
            mToolbar.inflateMenu(R.menu.composer_paragraph_format_menu);

            mToolbar.setNavigationIcon(R.drawable.ic_material_arrow_left_dark);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBottomSheetFragment.pop();
                }
            });

            mToolbar.setOnMenuItemClickListener(this);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        this.ensurePanel();
        this.setChecked(mColor);
        for (ColorView v : mList) {
            v.setOnClickListener(this);
        }

        this.setChecked(mColor);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        {
            Fragment p = this.getParentFragment();
            while (p != null) {

                if (p instanceof BottomSheetFragment) {
                    this.mBottomSheetFragment = (BottomSheetFragment)p;
                }

                if (p instanceof ComposerFragment) {
                    this.mComposerFragment = (ComposerFragment)p;
                }

                p = p.getParentFragment();
            }
        }

        {
            this.mSpanHelper = mComposerFragment.getSpanHelper();
            if (mSpanHelper != null) {
                mColor = mSpanHelper.getForegroundColor();
            }
        }

        {
            SelectionObservable observable = mComposerFragment.getSelectionObservable();
            observable.registerObserver(mSelectionObserver);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        {
            SelectionObservable observable = mComposerFragment.getSelectionObservable();
            observable.unregisterObserver(mSelectionObserver);
        }
    }

    void ensurePanel() {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        LinearLayout container = this.mContainer;
        ColorPanel panel = ColorPanel.instance();
        ArrayList<ColorView> list = this.mList;

        String[][] colors = panel.getColors();
        for (String[] c : colors) {
            ColorLayout group = createLayout(inflater, container, c, list);
            mContainer.addView(group);
        }
    }

    ColorLayout createLayout(LayoutInflater inflater, LinearLayout container, String[] colors, List<ColorView> list) {
        ColorLayout layout = (ColorLayout)(inflater.inflate(R.layout.layout_color_group, container, false));
        LinearLayout group = layout.getContainer();

        int length = colors.length;
        for (int i = 0; i < length; i++) {
            {
                String c = colors[i];
                ColorView view = createView(inflater, group, c);
                layout.addChild(view);

                list.add(view);
            }

            if ((i + 1) != length) {
                View view = inflater.inflate(R.layout.layout_color_separate, group, false);
                layout.addChild(view);
            }
        }

        return layout;
    }

    ColorView createView(LayoutInflater inflater, LinearLayout container, String color) {
        ColorView view = (ColorView)(inflater.inflate(R.layout.layout_color_item, container, false));

        int c = Color.parseColor(color);
        view.setColor(c);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnPanel) {

        } else if (v == mBtnNoColor) {
            this.setChecked(mBtnNoColor);
        } else if (mList.contains(v)) {
            this.setChecked(v);

            if (mSpanHelper != null) {
                ColorView view = (ColorView)v;
                mSpanHelper.setForegroundColor(view.getColor());
            }
        }
    }

    void setChecked(View view) {
        this.clear();

        for (ColorView v : mList) {
            if (v == view) {
                this.mColor = v.getColor();

                v.setChecked(true);
                break;
            }
        }

        if (view == mBtnNoColor) {
            this.mColor = Color.TRANSPARENT;

            mBtnNoColor.setChecked(true);
        }
    }

    void setChecked(int color) {
        this.clear();

        for (ColorView v : mList) {
            if (v.getColor() == color) {
                v.setChecked(true);
                break;
            }
        }

        if (color == Color.TRANSPARENT) {
            mBtnNoColor.setChecked(true);
        }
    }

    void clear() {
        mBtnNoColor.setChecked(false);

        for (ColorView v : mList) {
            v.setChecked(false);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menu_close: {
                mComposerFragment.closeFormat();

                break;
            }
        }

        return true;
    }

    private SelectionObserver mSelectionObserver = new SelectionObserver() {

        @Override
        public void onSelectionChanged(ParagraphHolder holder, int start, int end) {
            mSpanHelper = holder.getSpanHelper();
            mColor = mSpanHelper.getForegroundColor();

            setChecked(mColor);
        }
    };
}
