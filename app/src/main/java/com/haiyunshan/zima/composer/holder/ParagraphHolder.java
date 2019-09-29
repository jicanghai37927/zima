package com.haiyunshan.zima.composer.holder;

import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haiyunshan.zima.R;
import com.haiyunshan.zima.composer.ComposerFragment;
import com.haiyunshan.zima.composer.helper.ParagraphSpanHelper;
import com.haiyunshan.zima.composer.item.BaseItem;
import com.haiyunshan.zima.composer.item.ParagraphItem;
import com.haiyunshan.zima.composer.item.PictureItem;
import com.haiyunshan.zima.composer.item.SeparateItem;
import com.haiyunshan.zima.composer.state.BaseState;
import com.haiyunshan.zima.composer.state.EditState;
import com.haiyunshan.zima.composer.state.FormatState;
import com.haiyunshan.zima.composer.state.ReadState;
import com.haiyunshan.zima.composer.style.utils.SpanUtils;
import com.haiyunshan.zima.composer.widget.ParagraphEditText;
import com.haiyunshan.zima.font.dataset.FontManager;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class ParagraphHolder extends BaseHolder<ParagraphItem> implements View.OnKeyListener {

    ParagraphEditText mEdit;

    ParagraphSpanHelper mSpanHelper;

    public static final ParagraphHolder create(ComposerFragment parent, ViewGroup container) {
        LayoutInflater inflater = parent.getLayoutInflater();
        int resource = R.layout.layout_composer_paragraph_item;
        View view = inflater.inflate(resource, container, false);

        ParagraphHolder holder = new ParagraphHolder(parent, view);
        return holder;
    }

    public ParagraphHolder(ComposerFragment parent, View itemView) {
        super(parent, itemView);

        this.mEdit = itemView.findViewById(R.id.edit_paragraph);
        mEdit.setShowSoftInputOnFocus(false);
        this.mSpanHelper = new ParagraphSpanHelper(this, mEdit);
    }

    @Override
    public void onBind(int position, ParagraphItem item, BaseState state) {
        super.onBind(position, item, state);

        {
            {
                CharSequence text = item.getText();
                mEdit.setText(text);
            }

            if (item.hasSelection()) {
                int start = item.getSelectionStart();
                int end = item.getSelectionEnd();
                mEdit.setSelection(start, end);
                mEdit.requestFocus();

                item.setSelection(-1, -1);
            }

            {
                mEdit.setOnKeyListener(this);
            }

            {

                boolean result = this.nullLayouts();
                if (!result) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        mEdit.setBreakStrategy(Layout.BREAK_STRATEGY_SIMPLE);
                    }
                }
            }
        }

        // 根据不同状态进行绑定
        if (state != null) {
            if (state instanceof ReadState) {
                mEdit.setCursorVisible(false);
                mEdit.setShowSoftInputOnFocus(false);
                mEdit.setOnSelectionChangeListener(null);
                mEdit.setCustomInsertionActionModeCallback(mCustomInsertionActionModeCallback);
                mEdit.setCustomSelectionActionModeCallback(mCustomSelectionActionModeCallback);

            } else if (state instanceof EditState) {
                mEdit.setCursorVisible(true);
                mEdit.setShowSoftInputOnFocus(true);
                mEdit.setOnSelectionChangeListener(null);
                mEdit.setCustomInsertionActionModeCallback(null);
                mEdit.setCustomSelectionActionModeCallback(null);
            } else if (state instanceof FormatState) {
                mEdit.setCursorVisible(true);
                mEdit.setShowSoftInputOnFocus(false);
                mEdit.setOnSelectionChangeListener(mSpanHelper);
                mEdit.setCustomInsertionActionModeCallback(null);
                mEdit.setCustomSelectionActionModeCallback(null);
            }
        }
    }

    @Override
    public void onViewAttachedToWindow() {
        super.onViewAttachedToWindow();

        mSpanHelper.attach();
    }

    @Override
    public void onViewDetachedFromWindow() {
        super.onViewDetachedFromWindow();

        mSpanHelper.detach();
    }

    @Override
    public void save() {
        super.save();

        Editable text = mEdit.getText();
        mItem.setText(text);
    }

    public int getSelectionStart() {
        int start = mEdit.getSelectionStart();
        int end = mEdit.getSelectionEnd();

        int pos = Math.min(start, end);
        return pos;
    }

    public int getSelectionEnd() {
        int start = mEdit.getSelectionStart();
        int end = mEdit.getSelectionEnd();

        int pos = Math.max(start, end);
        return pos;
    }

    public boolean isSelectedAll() {
        int start = this.getSelectionStart();
        int end = this.getSelectionEnd();

        return (start == 0 && end == mEdit.length());
    }

    @Override
    public boolean insertSeparate() {
        int start = this.getSelectionStart();
        int end = this.getSelectionEnd();
        if (start < 0 || end < 0) {
            return false;
        }

        if (start != end) {
            return false;
        }

        if (start == 0) {
            SeparateItem p = SeparateItem.create();

            int index = mDocument.indexOf(mItem);
            mDocument.add(index, p);

            index = mAdapter.indexOf(mItem);
            mAdapter.add(index, p);
            mAdapter.notifyItemInserted(index);

            mRecyclerView.smoothScrollToPosition(mAdapter.indexOf(p));
        } else {

            boolean changed = false;

            ArrayList<BaseItem> list = new ArrayList<>(2);

            // 创建新对象
            {
                {
                    SeparateItem p = SeparateItem.create();
                    list.add(p);
                }

                boolean shouldSplit = true;

                if (false) {
                    int pos = start;
                    if (pos == mEdit.length()) {
                        int index = mDocument.indexOf(mItem);
                        int size = mDocument.size();
                        for (int i = index + 1; i < size; i++) {
                            BaseItem item = mDocument.get(i);
                            if (item instanceof ParagraphItem) {
                                shouldSplit = false;
                                break;
                            }
                        }
                    }
                }

                if (shouldSplit) {

                    changed = true;

                    int pos = start;
                    Editable text = mEdit.getText();
                    CharSequence s1 = text.subSequence(0, pos);
                    CharSequence s2 = text.subSequence(pos, text.length());

                    mItem.setText(s1);
                    mItem.setSelection(pos, pos);

                    ParagraphItem item2 = ParagraphItem.create(s2);
                    item2.setText(s2);

                    list.add(item2);
                }
            }

            // 更新Document
            {
                int position = mDocument.indexOf(this.mItem);
                int index = position + 1;
                for (BaseItem item : list) {
                    mDocument.add(index, item);

                    index++;
                }
            }

            // 更新Adapter
            {
                int position = mAdapter.indexOf(mItem);
                int index = position + 1;

                for (BaseItem p : list) {
                    mAdapter.add(index, p);

                    index++;
                }

                int count = list.size();

                if (changed) {
                    mAdapter.notifyItemChanged(position);
                }

                mAdapter.notifyItemRangeInserted(position + 1, count);
            }

            {
                int pos = mAdapter.indexOf(list.get(0));
                if (pos >= 0) {
                    mRecyclerView.scrollToPosition(pos);
                }
            }
        }

        return true;
    }

    @Override
    public boolean insertPicture(String[] array) {
        int start = this.getSelectionStart();
        int end = this.getSelectionEnd();
        if (start < 0 || end < 0) {
            return false;
        }

        if (start != end) {
            return false;
        }

        boolean changed = false;

        ArrayList<BaseItem> list = new ArrayList<>(array.length * 2 + 1);

        // 创建新对象
        {
            int length = array.length;
            for (int i = 0; i < length; i++) {
                String path = array[i];

                {
                    File file = new File(path);

                    PictureItem p = PictureItem.create(file);
                    list.add(p);
                }

                if ((i + 1) != length) {
                    ParagraphItem p = ParagraphItem.create("");
                    list.add(p);
                }
            }

            boolean shouldSplit = true;

            {
                int pos = start;
                if (pos == mEdit.length()) {
                    int index = mDocument.indexOf(mItem);
                    if (index + 1 < mDocument.size()) {
                        BaseItem item = mDocument.get(index);
                        shouldSplit = (!(item instanceof ParagraphItem));
                    }
                }
            }

            if (shouldSplit) {

                changed = true;

                int pos = start;
                Editable text = mEdit.getText();
                CharSequence s1 = text.subSequence(0, pos);
                CharSequence s2 = text.subSequence(pos, text.length());

                mItem.setText(s1);
                mItem.setSelection(pos, pos);

                ParagraphItem item2 = ParagraphItem.create(s2);
                item2.setText(s2);

                list.add(item2);
            }
        }

        // 更新Document
        {
            int position = mDocument.indexOf(this.mItem);
            int index = position + 1;
            for (BaseItem item : list) {
                mDocument.add(index, item);

                index++;
            }
        }

        // 更新Adapter
        {
            int position = mAdapter.indexOf(mItem);
            int index = position + 1;

            for (BaseItem p : list) {
                mAdapter.add(index, p);

                index++;
            }

            int count = list.size();

            if (changed) {
                mAdapter.notifyItemChanged(position);
            }

            mAdapter.notifyItemRangeInserted(position + 1, count);
        }

        {
            int pos = mAdapter.indexOf(list.get(0));
            if (pos >= 0) {
                mRecyclerView.scrollToPosition(pos);
            }
        }

        return true;
    }

    public ParagraphSpanHelper getSpanHelper() {
        return mSpanHelper;
    }

    public boolean nullLayouts() {
        TextView view = this.mEdit;

        try {
            Method textCanBeSelected = TextView.class.getDeclaredMethod("nullLayouts");
            textCanBeSelected.setAccessible(true);
            textCanBeSelected.invoke(view);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    void delete() {
        int index = mDocument.indexOf(this.mItem);
        if (index == 0) {
            return;
        }

        BaseItem item = mDocument.get(index - 1);

        if (item instanceof ParagraphItem) {

            index = mAdapter.indexOf(item);
            RecyclerView.ViewHolder h = mRecyclerView.findViewHolderForAdapterPosition(index);
            ParagraphHolder holder = (ParagraphHolder)h;
            holder.save();

            {
                ParagraphItem target = (ParagraphItem) item;
                int length = target.getText().length();
                target.setSelection(length, length);
                target.append(mEdit.getText());
            }

            {
                mDocument.remove(mItem);
                index = mAdapter.remove(mItem);
                mAdapter.notifyItemRemoved(index);
            }

            if (holder != null) {
                holder.mEdit.setSelection(holder.mEdit.length());
                holder.mEdit.requestFocus();

                index = mAdapter.indexOf(holder.getItem());
                mAdapter.notifyItemChanged(index);
            }
        } else if (item instanceof SeparateItem) {

            mDocument.remove(item);
            index = mAdapter.remove(item);
            mAdapter.notifyItemRemoved(index);

            if (mEdit.length() == 0) {
                mDocument.remove(mItem);
                index = mAdapter.remove(mItem);
                mAdapter.notifyItemRemoved(index);
            }

        }


    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        boolean result = false;
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                if (this.getSelectionStart() == 0) {
                    this.delete();

                    result = true;
                }
            }
        }

        return result;
    }

    ActionMode.Callback mCustomSelectionActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            menu.clear();

            {
                SpannableString ss = new SpannableString(mContext.getString(R.string.btn_highlight));
                menu.add(Menu.NONE, R.id.highlight, Menu.NONE, ss);
            }

            if (false) {
                SpannableString ss = new SpannableString(mContext.getString(R.string.btn_bold));
                ss.setSpan(new StyleSpan(Typeface.BOLD), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                menu.add(Menu.NONE, R.id.bold, Menu.NONE, ss);
            }

            if (false) {
                SpannableString ss = new SpannableString(mContext.getString(R.string.btn_italic));
                ss.setSpan(new StyleSpan(Typeface.ITALIC), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                menu.add(Menu.NONE, R.id.italic, Menu.NONE, ss);
            }

            if (false) {
                SpannableString ss = new SpannableString(mContext.getString(R.string.btn_underline));
                ss.setSpan(new UnderlineSpan(), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                menu.add(Menu.NONE, R.id.underline, Menu.NONE, ss);
            }

            if (false) {
                SpannableString ss = new SpannableString(mContext.getString(R.string.btn_strikethrough));
                ss.setSpan(new StrikethroughSpan(), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                menu.add(Menu.NONE, R.id.strikethrough, Menu.NONE, ss);
            }

            {
                menu.add(Menu.NONE, android.R.id.copy, Menu.NONE, android.R.string.copy);
            }

            if (!isSelectedAll()) {
                menu.add(Menu.NONE, android.R.id.selectAll, Menu.NONE, android.R.string.selectAll);
            }

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            int itemId = item.getItemId();
            switch (itemId) {
                case R.id.highlight: {
                    SpanUtils.toggleHighlight(mEdit, "brown");

                    break;
                }
                case R.id.bold: {
                    SpanUtils.toggleBold(mEdit);

                    break;
                }
                case R.id.italic: {
                    SpanUtils.toggleItalic(mEdit);

                    break;
                }
                case R.id.underline: {
                    SpanUtils.toggleUnderline(mEdit);

                    break;
                }
                case R.id.strikethrough: {
                    SpanUtils.toggleStrikethrough(mEdit);

                    break;
                }
                case android.R.id.copy: {
                    break;
                }
                case android.R.id.selectAll: {
                    break;
                }
            }

            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    };

    ActionMode.Callback mCustomInsertionActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            menu.clear();
            menu.add(Menu.NONE, android.R.id.selectAll, Menu.NONE, android.R.string.selectAll);

            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    };

}
