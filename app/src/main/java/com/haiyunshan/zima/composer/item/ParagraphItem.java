package com.haiyunshan.zima.composer.item;

import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;

import com.haiyunshan.zima.composer.entity.ParagraphEntity;
import com.haiyunshan.zima.composer.style.utils.StyleUtils;

public class ParagraphItem extends BaseItem<ParagraphEntity> {

    CharSequence mText;

    int mSelectionStart = -1;
    int mSelectionEnd = -1;

    public static final ParagraphItem create(CharSequence text) {
        ParagraphItem item = new ParagraphItem(text);
        return item;
    }

    ParagraphItem(CharSequence text) {
        this.mEntity = new ParagraphEntity();

        SpannableStringBuilder ss = new SpannableStringBuilder(text);

        this.mText = this.applyStyle(ss, mEntity);
    }

    ParagraphItem(ParagraphEntity entity) {
        super(entity);

        String text = entity.getText();
        text = (text == null)? "": text;
        SpannableStringBuilder ss = new SpannableStringBuilder(text);

        this.mText = this.applyStyle(ss, mEntity);
    }

    @Override
    public ParagraphEntity getEntity() {
        {
            mEntity.setText(mText.toString());
            mEntity.getSpans().clear();
        }

        if (mText instanceof Spanned) {
            StyleUtils.getSpans(mEntity.getSpans(), (Spanned)mText);
        }

        return super.getEntity();
    }

    @Override
    public boolean isEmpty() {
        return TextUtils.isEmpty(mText);
    }

    public CharSequence getText() {
        return mText;
    }

    public void setText(CharSequence text) {
        this.mText = text;
    }

    public boolean hasSelection() {
        return (mSelectionStart >= 0) && (mSelectionEnd >= 0);
    }

    public void setSelection(int start, int end) {
        this.mSelectionStart = start;
        this.mSelectionEnd = end;
    }

    public int getSelectionStart() {
        return mSelectionStart;
    }

    public int getSelectionEnd() {
        return mSelectionEnd;
    }

    public void append(CharSequence cs) {
        if (TextUtils.isEmpty(cs)) {
            return;
        }

        if (!(mText instanceof Editable)) {
            return;
        }

        Editable text = (Editable)mText;
        text.append(cs);

    }

    Spannable applyStyle(Spannable ss, ParagraphEntity entity) {

        {
            StyleUtils.toSpans(entity.getSpans(), ss);
        }

        {

        }

        return ss;
    }
}
