package com.haiyunshan.zima.composer.helper;

import android.text.Layout;
import android.text.Selection;

import com.haiyunshan.zima.composer.ComposerFragment;
import com.haiyunshan.zima.composer.holder.ParagraphHolder;
import com.haiyunshan.zima.composer.observable.SelectionObservable;
import com.haiyunshan.zima.composer.style.utils.SpanUtils;
import com.haiyunshan.zima.composer.widget.ParagraphEditText;

public class ParagraphSpanHelper implements ParagraphEditText.OnSelectionChangeListener {

    ParagraphHolder mHolder;
    ParagraphEditText mEdit;

    public ParagraphSpanHelper(ParagraphHolder holder, ParagraphEditText editText) {
        this.mHolder = holder;
        this.mEdit = editText;
    }

    public void attach() {
        SpanUtils.attach(mEdit);
    }

    public void detach() {
        SpanUtils.detach(mHolder.getItem().getText());
    }

    public boolean isBold() {
        int start = mHolder.getSelectionStart();
        if (start < 0) {
            return false;
        }

        boolean result = SpanUtils.isBold(mEdit);

        return result;
    }

    public boolean toggleBold() {
        int start = mHolder.getSelectionStart();
        int end = mHolder.getSelectionEnd();
        if (start < 0 || end < 0) {
            return false;
        }

        if (start == end) {
            return isBold();
        }

        boolean result = SpanUtils.toggleBold(mEdit);
        return result;
    }

    public boolean isItalic() {
        int start = mHolder.getSelectionStart();
        if (start < 0) {
            return false;
        }

        boolean result = SpanUtils.isItalic(mEdit);

        return result;
    }

    public boolean toggleItalic() {
        int start = mHolder.getSelectionStart();
        int end = mHolder.getSelectionEnd();
        if (start < 0 || end < 0) {
            return false;
        }

        if (start == end) {
            return isBold();
        }

        boolean result = SpanUtils.toggleItalic(mEdit);
        return result;
    }

    public boolean isUnderline() {
        int start = mHolder.getSelectionStart();
        if (start < 0) {
            return false;
        }

        boolean result = SpanUtils.isUnderline(mEdit);
        return result;
    }

    public boolean toggleUnderline() {
        int start = mHolder.getSelectionStart();
        int end = mHolder.getSelectionEnd();
        if (start < 0 || end < 0) {
            return false;
        }

        if (start == end) {
            return isBold();
        }

        boolean result = SpanUtils.toggleUnderline(mEdit);
        return result;
    }

    public boolean isStrikethrough() {
        int start = mHolder.getSelectionStart();
        if (start < 0) {
            return false;
        }

        boolean result = SpanUtils.isStrikethrough(mEdit);
        return result;
    }

    public boolean toggleStrikethrough() {
        int start = mHolder.getSelectionStart();
        int end = mHolder.getSelectionEnd();
        if (start < 0 || end < 0) {
            return false;
        }

        if (start == end) {
            return isBold();
        }

        boolean result = SpanUtils.toggleStrikethrough(mEdit);
        return result;
    }

    public Layout.Alignment getAlignment() {

        int start = mHolder.getSelectionStart();
        int end = mHolder.getSelectionEnd();
        if (start < 0 || end < 0) {
            return mEdit.getLayout().getAlignment();
        }

        Layout.Alignment align = SpanUtils.getAlignment(mEdit);
        if (align == null) {
            align = Layout.Alignment.ALIGN_NORMAL;

            if (mEdit.getLayout() != null) {
                align = mEdit.getLayout().getAlignment();
            }
        }

        return align;
    }

    public void setAlignment(Layout.Alignment align) {
        int start = mHolder.getSelectionStart();
        int end = mHolder.getSelectionEnd();
        if (start < 0 || end < 0) {
            return;
        }

        SpanUtils.setAlignment(mEdit, align);
    }


    public float getTextSize() {

        int start = mHolder.getSelectionStart();
        int end = mHolder.getSelectionEnd();
        if (start < 0 || end < 0) {
            float size = mEdit.getTextSize();
            float density = mEdit.getPaint().density;;
            size = size / density;
            return size;
        }

        float size = SpanUtils.getTextSize(mEdit);
        return size;
    }

    public void setTextSize(float textSize) {
        final int start = mHolder.getSelectionStart();
        final int end = mHolder.getSelectionEnd();
        if (start < 0 || end < 0) {
            return;
        }

        SpanUtils.setTextSize(mEdit, textSize);

        // 重新布局，避免文字重叠
        Selection.setSelection(mEdit.getText(), start, end);
        mHolder.nullLayouts();
        Selection.setSelection(mEdit.getText(), start, end);
    }


    public String getFont() {

        int start = mHolder.getSelectionStart();
        int end = mHolder.getSelectionEnd();
        if (start < 0 || end < 0) {
            return "";
        }

        String font = SpanUtils.getFont(mEdit);
        return font;
    }

    public void setFont(String font) {
        final int start = mHolder.getSelectionStart();
        final int end = mHolder.getSelectionEnd();
        if (start < 0 || end < 0) {
            return;
        }

        SpanUtils.setFont(mEdit, font);

        // 重新布局，避免文字重叠
        Selection.setSelection(mEdit.getText(), start, end);
        mHolder.nullLayouts();
        Selection.setSelection(mEdit.getText(), start, end);
    }

    public int getForegroundColor() {

        int start = mHolder.getSelectionStart();
        int end = mHolder.getSelectionEnd();
        if (start < 0 || end < 0) {
            return 0;
        }

        int color = SpanUtils.getForegroundColor(mEdit);
        return color;
    }

    public void setForegroundColor(int color) {
        final int start = mHolder.getSelectionStart();
        final int end = mHolder.getSelectionEnd();
        if (start < 0 || end < 0) {
            return;
        }

        SpanUtils.setForegroundColor(mEdit, color);
    }

    public boolean isLineMargin() {
        int start = mHolder.getSelectionStart();
        if (start < 0) {
            return false;
        }

        boolean result = SpanUtils.isLineMargin(mEdit);

        return result;
    }

    public boolean toggleLineMargin() {
        int start = mHolder.getSelectionStart();
        int end = mHolder.getSelectionEnd();
        if (start < 0 || end < 0) {
            return false;
        }

        boolean result = SpanUtils.toggleLineMargin(mEdit);

        // 重新布局，避免文字重叠
        Selection.setSelection(mEdit.getText(), start, end);
        mHolder.nullLayouts();
        Selection.setSelection(mEdit.getText(), start, end);

        return result;
    }

    public int getIndent() {

        int start = mHolder.getSelectionStart();
        int end = mHolder.getSelectionEnd();
        if (start < 0 || end < 0) {
            return 0;
        }

        int size = SpanUtils.getIndent(mEdit);
        return size;
    }

    public void setIndent(int indent) {
        final int start = mHolder.getSelectionStart();
        final int end = mHolder.getSelectionEnd();
        if (start < 0 || end < 0) {
            return;
        }

        SpanUtils.setIndent(mEdit, indent);

        // 重新布局，避免文字重叠
        Selection.setSelection(mEdit.getText(), start, end);
        mHolder.nullLayouts();
        Selection.setSelection(mEdit.getText(), start, end);
    }

    @Override
    public void onSelectionChanged(ParagraphEditText view, int start, int end) {
        ComposerFragment parent = mHolder.getParent();

        SelectionObservable observable = parent.getSelectionObservable();
        observable.notifySelectionChanged(mHolder, start, end);
    }
}
