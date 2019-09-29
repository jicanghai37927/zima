package com.haiyunshan.zima.composer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.Switch;
import android.widget.TextView;

import com.haiyunshan.zima.R;
import com.haiyunshan.zima.composer.helper.ParagraphSpanHelper;
import com.haiyunshan.zima.composer.holder.ParagraphHolder;
import com.haiyunshan.zima.composer.observable.SelectionObservable;
import com.haiyunshan.zima.composer.observer.SelectionObserver;
import com.haiyunshan.zima.font.dataset.FontEntry;
import com.haiyunshan.zima.font.dataset.FontManager;
import com.haiyunshan.zima.widget.CheckableImageView;
import com.haiyunshan.zima.widget.ColorView;

import java.util.ArrayList;

public class ParagraphFormatFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    static final int ITEM_TITLE             = 101;
    static final int ITEM_SEPARATE          = 102;

    static final int ITEM_PARAGRAPH_STYLE   = 201;
    static final int ITEM_TYPEFACE          = 202;
    static final int ITEM_STYLE             = 203;
    static final int ITEM_SIZE              = 204;
    static final int ITEM_COLOR             = 205;
    static final int ITEM_ALIGNMENT         = 206;
    static final int ITEM_LINE_INDENT       = 207;
    static final int ITEM_INDENT            = 208;
    static final int ITEM_LINE_SPACING      = 209;

    static final int MIN_TEXT_SIZE  = 8;
    static final int MAX_TEXT_SIZE  = 96;
    static final int STEP_TEXT_SIZE = 1;

    static final int MIN_INDENT     = 0;
    static final int MAX_INDENT     = 720;
    static final int STEP_INDENET   = 24;

    Toolbar mToolbar;

    RecyclerView mRecyclerView;
    FormatAdapter mAdapter;

    ParagraphSpanHelper mSpanHelper;

    BottomSheetFragment mBottomSheetFragment;
    ComposerFragment mComposerFragment;

    public ParagraphFormatFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_composer_paragraph_format, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        {
            this.mToolbar = view.findViewById(R.id.toolbar);
            mToolbar.inflateMenu(R.menu.composer_paragraph_format_menu);
            mToolbar.setOnMenuItemClickListener(this);

//            mToolbar.setNavigationIcon(R.drawable.ic_ink_pen_black);
//            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
        }

        this.mRecyclerView = view.findViewById(R.id.recycler_list_view);
        LinearLayoutManager layout = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layout);

        Activity context = getActivity();
        Drawable d = context.getDrawable(R.drawable.shape_divider);
        DividerItemDecoration decor = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        decor.setDrawable(d);
        mRecyclerView.addItemDecoration(decor);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        {
            this.mAdapter = new FormatAdapter(getActivity());
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        {
            SelectionObservable observable = mComposerFragment.getSelectionObservable();
            observable.unregisterObserver(mSelectionObserver);
        }
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
        }

        {
            SelectionObservable observable = mComposerFragment.getSelectionObservable();
            observable.registerObserver(mSelectionObserver);
        }

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
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

    private class FormatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        ArrayList<FormatItem> mList;

        Activity mContext;

        FormatAdapter(Activity context) {
            this.mContext = context;

            this.mList = new ArrayList<>();

//            mList.add(new FormatItem(ITEM_TITLE, "段落样式"));
//            mList.add(new FormatItem(ITEM_PARAGRAPH_STYLE));
//
            mList.add(new FormatItem(ITEM_SEPARATE));

            mList.add(new FormatItem(ITEM_TYPEFACE));
            mList.add(new FormatItem(ITEM_STYLE));
            mList.add(new FormatItem(ITEM_SIZE));
            mList.add(new FormatItem(ITEM_COLOR));

            mList.add(new FormatItem(ITEM_SEPARATE));

            mList.add(new FormatItem(ITEM_ALIGNMENT));

            mList.add(new FormatItem(ITEM_SEPARATE));

            mList.add(new FormatItem(ITEM_LINE_INDENT));
            mList.add(new FormatItem(ITEM_INDENT));

//            mList.add(new FormatItem(ITEM_SEPARATE));
//
//            mList.add(new FormatItem(ITEM_LINE_SPACING));
        }

        @Override
        public int getItemViewType(int position) {
            int type = mList.get(position).mType;
            return type;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder holder = null;
            LayoutInflater inflater = mContext.getLayoutInflater();

            switch (viewType) {
                case ITEM_TITLE: {
                    int resource = R.layout.layout_paragraph_format_title_item;
                    View view = inflater.inflate(resource, parent, false);
                    holder = new TitleHolder(view);
                    break;
                }

                case ITEM_SEPARATE: {
                    int resource = R.layout.layout_paragraph_format_separate_item;
                    View view = inflater.inflate(resource, parent, false);
                    holder = new SeparateHolder(view);

                    break;
                }

                case ITEM_PARAGRAPH_STYLE: {
                    int resource = R.layout.layout_paragraph_format_paragraph_item;
                    View view = inflater.inflate(resource, parent, false);
                    holder = new ParagraphStyleHolder(view);

                    break;
                }

                case ITEM_TYPEFACE: {
                    int resource = R.layout.layout_paragraph_format_font_item;
                    View view = inflater.inflate(resource, parent, false);
                    holder = new FontHolder(view);

                    break;
                }

                case ITEM_STYLE: {
                    int resource = R.layout.layout_paragraph_format_style_item;
                    View view = inflater.inflate(resource, parent, false);
                    holder = new StyleHolder(view);

                    break;
                }

                case ITEM_SIZE: {
                    int resource = R.layout.layout_paragraph_format_size_item;
                    View view = inflater.inflate(resource, parent, false);
                    holder = new TextSizeHolder(view);

                    break;
                }

                case ITEM_COLOR: {
                    int resource = R.layout.layout_paragraph_format_color_item;
                    View view = inflater.inflate(resource, parent, false);
                    holder = new TextColorHolder(view);

                    break;
                }

                case ITEM_ALIGNMENT: {
                    int resource = R.layout.layout_paragraph_format_alignment_item;
                    View view = inflater.inflate(resource, parent, false);
                    holder = new AlignmentHolder(view);

                    break;
                }

                case ITEM_LINE_INDENT: {
                    int resource = R.layout.layout_paragraph_format_line_indent_item;
                    View view = inflater.inflate(resource, parent, false);
                    holder = new LineIndentHolder(view);

                    break;
                }

                case ITEM_INDENT: {
                    int resource = R.layout.layout_paragraph_format_indent_item;
                    View view = inflater.inflate(resource, parent, false);
                    holder = new IndentHolder(view);

                    break;
                }

                case ITEM_LINE_SPACING: {
                    int resource = R.layout.layout_paragraph_format_line_spacing_item;
                    View view = inflater.inflate(resource, parent, false);
                    holder = new LineSpacingHolder(view);

                    break;
                }

                default: {
                    break;
                }
            }

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder h, int position) {
            int viewType = getItemViewType(position);
            FormatItem item = mList.get(position);

            switch (viewType) {
                case ITEM_TITLE: {
                    TitleHolder holder = (TitleHolder) h;
                    holder.bind(position, item);

                    break;
                }

                case ITEM_SEPARATE: {

                    break;
                }

                case ITEM_PARAGRAPH_STYLE: {
                    ParagraphStyleHolder holder = (ParagraphStyleHolder)h;
                    holder.bind(position, item);

                    break;
                }

                case ITEM_TYPEFACE: {
                    FontHolder holder = (FontHolder)h;
                    holder.bind(position, item);

                    break;
                }

                case ITEM_STYLE: {
                    StyleHolder holder = (StyleHolder)h;
                    holder.bind(position, item);

                    break;
                }

                case ITEM_SIZE: {
                    TextSizeHolder holder = (TextSizeHolder)h;
                    holder.bind(position, item);

                    break;
                }

                case ITEM_COLOR: {
                    TextColorHolder holder = (TextColorHolder)h;
                    holder.bind(position, item);

                    break;
                }

                case ITEM_ALIGNMENT: {
                    AlignmentHolder holder = (AlignmentHolder)h;
                    holder.bind(position, item);

                    break;
                }

                case ITEM_LINE_INDENT: {
                    LineIndentHolder holder = (LineIndentHolder)h;
                    holder.bind(position, item);

                    break;
                }

                case ITEM_INDENT: {
                    IndentHolder holder = (IndentHolder)h;
                    holder.bind(position, item);

                    break;
                }
                case ITEM_LINE_SPACING: {
                    LineSpacingHolder holder = (LineSpacingHolder)h;
                    holder.bind(position, item);

                    break;
                }
            }

        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }

    private class FormatItem {

        int mType;
        CharSequence mName;

        FormatItem(int type) {
            this.mType = type;
        }

        FormatItem(int type, CharSequence name) {
            this.mType = type;
            this.mName = name;
        }
    }

    private class TitleHolder extends RecyclerView.ViewHolder {

        TextView mNameView;
        public TitleHolder(View itemView) {
            super(itemView);

            this.mNameView = (TextView)itemView;
        }

        void bind(int position, FormatItem item) {
            mNameView.setText(item.mName);
        }
    }

    private class SeparateHolder extends RecyclerView.ViewHolder {

        public SeparateHolder(View itemView) {
            super(itemView);
        }
    }

    private class ParagraphStyleHolder extends RecyclerView.ViewHolder {

        ParagraphStyleHolder(View itemView) {
            super(itemView);
        }

        void bind(int position, FormatItem item) {

        }
    }


    private class FontHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mValueView;

        FontHolder(View itemView) {
            super(itemView);

            this.mValueView = itemView.findViewById(R.id.tv_value);

            itemView.setOnClickListener(this);
        }

        void bind(int position, FormatItem item) {
            CharSequence name = null;

            if (mSpanHelper != null) {
                String fontId = mSpanHelper.getFont();
                FontManager mgr = FontManager.instance();
                FontEntry e = mgr.obtain(fontId);
                if (e != null) {
                    name = e.getName();
                }
            }

            mValueView.setText(name);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                if (mSpanHelper != null) {
                    ParagraphFontFragment f = new ParagraphFontFragment();
                    mBottomSheetFragment.push(f, "font");
                }
            }
        }
    }

    private class StyleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CheckedTextView mBoldBtn;
        CheckedTextView mItalicBtn;
        CheckedTextView mUnderlineBtn;
        CheckedTextView mStrikethroughBtn;

        StyleHolder(View itemView) {
            super(itemView);

            this.mBoldBtn = itemView.findViewById(R.id.btn_bold);
            mBoldBtn.setOnClickListener(this);

            this.mItalicBtn = itemView.findViewById(R.id.btn_italic);
            mItalicBtn.setOnClickListener(this);

            this.mUnderlineBtn = itemView.findViewById(R.id.btn_underline);
            mUnderlineBtn.getPaint().setUnderlineText(true);
            mUnderlineBtn.setOnClickListener(this);

            this.mStrikethroughBtn = itemView.findViewById(R.id.btn_strikethrough);
            mStrikethroughBtn.getPaint().setStrikeThruText(true);
            mStrikethroughBtn.setOnClickListener(this);
        }

        void bind(int position, FormatItem item) {

            if (mSpanHelper == null) {
                return;
            }

            {
                boolean value = mSpanHelper.isBold();
                mBoldBtn.setChecked(value);
            }

            {
                boolean value = mSpanHelper.isItalic();
                mItalicBtn.setChecked(value);
            }

            {
                boolean value = mSpanHelper.isUnderline();
                mUnderlineBtn.setChecked(value);
            }

            {
                boolean value = mSpanHelper.isStrikethrough();
                mStrikethroughBtn.setChecked(value);
            }
        }

        @Override
        public void onClick(View v) {
            if (v == mBoldBtn) {
                if (mSpanHelper != null) {
                    CheckedTextView view = mBoldBtn;

                    boolean result = mSpanHelper.toggleBold();
                    view.setChecked(result);
                }
            } else if (v == mItalicBtn) {
                if (mSpanHelper != null) {
                    CheckedTextView view = mItalicBtn;

                    boolean result = mSpanHelper.toggleItalic();
                    view.setChecked(result);
                }
            } else if (v == mUnderlineBtn) {
                if (mSpanHelper != null) {
                    CheckedTextView view = mUnderlineBtn;

                    boolean result = mSpanHelper.toggleUnderline();
                    view.setChecked(result);
                }
            } else if (v == mStrikethroughBtn) {
                if (mSpanHelper != null) {
                    CheckedTextView view = mStrikethroughBtn;

                    boolean result = mSpanHelper.toggleStrikethrough();
                    view.setChecked(result);
                }
            }
        }
    }

    /**
     *
     *
     */
    private class TextSizeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mValueView;
        View mMinusView;
        View mPlusView;

        float mTextSize;

        TextSizeHolder(View itemView) {
            super(itemView);

            this.mValueView = itemView.findViewById(R.id.tv_value);
            this.mMinusView = itemView.findViewById(R.id.iv_minus);
            mMinusView.setOnClickListener(this);
            this.mPlusView = itemView.findViewById(R.id.iv_plus);
            mPlusView.setOnClickListener(this);
        }

        void bind(int position, FormatItem item) {
            this.mTextSize = 0;

            if (mSpanHelper != null) {
                mTextSize = mSpanHelper.getTextSize();
            }

            mValueView.setText(this.getTextValue());
        }

        @Override
        public void onClick(View v) {
            if (v == mMinusView) {
                if (mSpanHelper != null && mTextSize > MIN_TEXT_SIZE) {
                    float size = mTextSize;
                    size -= STEP_TEXT_SIZE;
                    size = (size < MIN_TEXT_SIZE) ? MIN_TEXT_SIZE : size;
                    this.mTextSize = size;

                    mValueView.setText(this.getTextValue());

                    mSpanHelper.setTextSize(size);
                }
            } else if (v == mPlusView) {
                if (mSpanHelper != null && mTextSize < MAX_TEXT_SIZE) {
                    float size = mTextSize;
                    size += STEP_TEXT_SIZE;
                    size = (size > MAX_TEXT_SIZE) ? MAX_TEXT_SIZE : size;
                    this.mTextSize = size;

                    mValueView.setText(this.getTextValue());

                    mSpanHelper.setTextSize(size);
                }
            }
        }

        CharSequence getTextValue() {
            CharSequence text = null;
            if (mTextSize <= 0) {
                return text;
            }

            text = ((int)mTextSize) + " 磅";
            return text;
        }
    }


    private class TextColorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View mColorView;

        TextColorHolder(View itemView) {
            super(itemView);

            this.mColorView = itemView.findViewById(R.id.view_text_color);

            itemView.setOnClickListener(this);
        }

        void bind(int position, FormatItem item) {
            int color = Color.BLACK;
            if (mSpanHelper != null) {
                color = mSpanHelper.getForegroundColor();
            }

            if (color == 0) {
                color = Color.BLACK;
            }

            mColorView.setBackgroundColor(color);
        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                if (mSpanHelper != null) {
                    ForegroundColorFragment f = new ForegroundColorFragment();
                    mBottomSheetFragment.push(f, "foreground_color");
                }
            }
        }
    }


    private class AlignmentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CheckableImageView mAlignNormalBtn;
        CheckableImageView mAlignCenterBtn;
        CheckableImageView mAlignOppositeBtn;

        AlignmentHolder(View itemView) {
            super(itemView);

            this.mAlignNormalBtn = itemView.findViewById(R.id.btn_align_normal);
            mAlignNormalBtn.setOnClickListener(this);

            this.mAlignCenterBtn = itemView.findViewById(R.id.btn_align_center);
            mAlignCenterBtn.setOnClickListener(this);

            this.mAlignOppositeBtn = itemView.findViewById(R.id.btn_align_opposite);
            mAlignOppositeBtn.setOnClickListener(this);
        }

        void bind(int position, FormatItem item) {
            if (mSpanHelper == null) {
                return;
            }

            Layout.Alignment align = mSpanHelper.getAlignment();
            this.setChecked(align);
        }

        @Override
        public void onClick(View v) {
            if (v == mAlignNormalBtn) {
                if (!mAlignNormalBtn.isChecked() && mSpanHelper != null) {
                    this.setChecked(mAlignNormalBtn);

                    mSpanHelper.setAlignment(Layout.Alignment.ALIGN_NORMAL);
                }
            } else if (v == mAlignCenterBtn) {
                if (!mAlignCenterBtn.isChecked() && mSpanHelper != null) {
                    this.setChecked(mAlignCenterBtn);

                    mSpanHelper.setAlignment(Layout.Alignment.ALIGN_CENTER);
                }
            } else if (v == mAlignOppositeBtn) {
                if (!mAlignOppositeBtn.isChecked() && mSpanHelper != null) {
                    this.setChecked(mAlignOppositeBtn);

                    mSpanHelper.setAlignment(Layout.Alignment.ALIGN_OPPOSITE);
                }
            }
        }

        void setChecked(CheckableImageView view) {
            mAlignNormalBtn.setChecked(false);
            mAlignCenterBtn.setChecked(false);
            mAlignOppositeBtn.setChecked(false);

            view.setChecked(true);
        }

        void setChecked(Layout.Alignment align) {
            mAlignNormalBtn.setChecked(false);
            mAlignCenterBtn.setChecked(false);
            mAlignOppositeBtn.setChecked(false);

            if (align == Layout.Alignment.ALIGN_NORMAL) {
                mAlignNormalBtn.setChecked(true);
            } else if (align == Layout.Alignment.ALIGN_CENTER) {
                mAlignCenterBtn.setChecked(true);
            } else if (align == Layout.Alignment.ALIGN_OPPOSITE) {
                mAlignOppositeBtn.setChecked(true);
            }
        }
    }

    private class LineIndentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Switch mSwitchBtn;

        public LineIndentHolder(View itemView) {
            super(itemView);

            this.mSwitchBtn = itemView.findViewById(R.id.btn_switch);
            mSwitchBtn.setOnClickListener(this);
        }

        void bind(int position, FormatItem item) {
            boolean checked = false;

            if (mSpanHelper != null) {
                checked = mSpanHelper.isLineMargin();
            }

            mSwitchBtn.setChecked(checked);
        }

        @Override
        public void onClick(View v) {
            if (v == mSwitchBtn) {
                if (mSpanHelper != null) {
                    boolean checked = mSpanHelper.toggleLineMargin();
                    mSwitchBtn.setChecked(checked);
                }
            }
        }
    }

    private class IndentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View mIndentLeftBtn;
        View mIndentRightBtn;

        int mIndent;

        IndentHolder(View itemView) {
            super(itemView);

            this.mIndentLeftBtn = itemView.findViewById(R.id.btn_indent_left);
            mIndentLeftBtn.setOnClickListener(this);

            this.mIndentRightBtn = itemView.findViewById(R.id.btn_indent_right);
            mIndentRightBtn.setOnClickListener(this);

        }

        void bind(int position, FormatItem item) {
            if (mSpanHelper != null) {
                this.mIndent = mSpanHelper.getIndent();
            }
        }

        @Override
        public void onClick(View v) {
            if (v == mIndentLeftBtn) {
                if (mSpanHelper != null && mIndent > MIN_INDENT) {
                    int size = mIndent;
                    size -= STEP_INDENET;
                    size = (size < MIN_INDENT) ? MIN_INDENT : size;
                    this.mIndent = size;

                    mSpanHelper.setIndent(size);
                }
            } else if (v == mIndentRightBtn) {
                if (mSpanHelper != null && mIndent < MAX_INDENT) {
                    int size = mIndent;
                    size += STEP_INDENET;
                    size = (size > MAX_INDENT) ? MAX_INDENT : size;
                    this.mIndent = size;

                    mSpanHelper.setIndent(size);
                }
            }
        }
    }

    private class LineSpacingHolder extends RecyclerView.ViewHolder {

        LineSpacingHolder(View itemView) {
            super(itemView);
        }

        void bind(int position, FormatItem item) {

        }
    }

    private SelectionObserver mSelectionObserver = new SelectionObserver() {

        @Override
        public void onSelectionChanged(ParagraphHolder holder, int start, int end) {
            mSpanHelper = holder.getSpanHelper();

            mAdapter.notifyDataSetChanged();
        }
    };
}
