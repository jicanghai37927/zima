package com.haiyunshan.zima.composer.style.span;

import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;

import com.haiyunshan.zima.composer.entity.style.SpanEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public abstract class BaseSpanItem<T> {

    protected Class<? extends T> mType;
    protected boolean mConcat;
    protected int mFlags;

    BaseSpanItem(Class<? extends T> type) {
        this.mType = type;
        this.mConcat = false;
        this.mFlags = Spanned.SPAN_EXCLUSIVE_INCLUSIVE;

    }

    /**
     * 获取第一个Span
     *
     * @param text
     * @return
     */
    public T peek(Editable text) {
        int[] selection = this.getSelection(text);
        if (selection == null) {
            return null;
        }

        int start = selection[0];

        T[] spans = text.getSpans(start, start + 1, mType);
        for (T s : spans) {
            if (accept(s)) {
                return s;
            }
        }

        return null;
    }

    /**
     * Selection第一个字符
     *
     * @param text
     * @return
     */
    public boolean exist(Editable text) {
        int[] selection = this.getSelection(text);
        if (selection == null) {
            return false;
        }

        int start = selection[0];

        T[] spans = text.getSpans(start, start + 1, mType);
        for (T s : spans) {
            if (accept(s)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Selection所有字符
     *
     * @param text
     * @return
     */
    public boolean match(Editable text) {
        int[] selection = getSelection(text);
        if (isEmpty(selection)) {
            return false;
        }

        ArrayList<SpanObject> spanSet;
        int start = selection[0];
        int end = selection[1];

        T[] spans = text.getSpans(start, end, mType);
        spanSet = new ArrayList<>(spans.length);
        for (T s : spans) {
            if (this.accept(s)) {
                start = text.getSpanStart(s);
                end = text.getSpanEnd(s);
                spanSet.add(new SpanObject(s, start, end));
            }
        }

        if (spanSet.isEmpty()) {
            return false;
        }

        Collections.sort(spanSet, new Comparator<SpanObject>() {
            @Override
            public int compare(SpanObject o1, SpanObject o2) {
                return o1.mStart - o2.mStart;
            }
        });

        boolean result;

        start = selection[0];
        end = selection[1];

        while (true) {
            SpanObject obj = spanSet.remove(0);
            if (obj.mStart > start) {
                result = false;
                break;
            }
            start = (obj.mEnd > start)? obj.mEnd: start;

            if (spanSet.isEmpty()) {
                result = (start >= end);
                break;
            }
        }

        return result;
    }

    /**
     *
     * @param text
     */
    public void set(Editable text) {
        int[] selection = getSelection(text);
        if (isEmpty(selection)) {
            return;
        }

        int start = selection[0];
        int end = selection[1];
        int flags = this.mFlags;

        if (mConcat) {
            int s1 = start - 1;     // 使连续
            s1 = Math.max(0, s1);
            int e1 = end + 1;
            e1 = Math.min(text.length(), e1);

            T[] spans = text.getSpans(s1, e1, mType);
            for (T s : spans) {
                if (this.accept(s)) {
                    int a = text.getSpanStart(s);
                    int b = text.getSpanEnd(s);
                    int f = text.getSpanFlags(s);

                    start = (a < start) ? a : start;
                    end = (b > end) ? b : end;
                    flags = f;

                    text.removeSpan(s);
                }
            }
        }

        text.setSpan(this.create(), start, end, flags);
    }

    /**
     * 清除样式
     *
     * @param text
     */
    public void clear(Editable text) {
        int[] selection = getSelection(text);
        if (isEmpty(selection)) {
            return;
        }

        int start = selection[0];
        int end = selection[1];

        T[] spans = text.getSpans(start, end, mType);
        for (T s : spans) {
            if (this.accept(s)) {
                start = text.getSpanStart(s);
                end = text.getSpanEnd(s);
                int flags = text.getSpanFlags(s);

                if (start < selection[0]) {
                    text.removeSpan(s);

                    if (end <= selection[1]) {
                        text.setSpan(this.create(s), start, selection[0], flags);

                    } else {
                        text.setSpan(this.create(s), start, selection[0], flags);
                        text.setSpan(this.create(s), selection[1], end, flags);
                    }
                } else if (start >= selection[0]){
                    text.removeSpan(s);

                    if (end <= selection[1]) { // 完全在Selection内的Span

                    } else {
                        text.setSpan(this.create(s), selection[1], end, flags);
                    }
                }
            }
        }

    }

    public final boolean toggle(Editable text) {
        boolean value = this.match(text);
        if (value) {
            this.clear(text);
        } else {
            this.set(text);
        }

        return !value;
    }

    protected boolean accept(T span) {
        return true;
    }

    protected abstract T create();
    protected abstract T create(T span);
    public abstract SpanEntity toEntity(Object obj, int start, int end);
    public abstract Object toSpan(SpanEntity entity, Spannable text);

    protected int[] getSelection(Editable text) {
        int start = Selection.getSelectionStart(text);
        int end = Selection.getSelectionEnd(text);
        if (start < 0 || end < 0) {
            return null;
        }

        int a = Math.min(start, end);
        int b = Math.max(start, end);
        start = a;
        end = b;

        return new int[] {start, end};
    }

    final boolean isEmpty(int[] selection) {
        return (selection == null || selection[0] == selection[1]);
    }

    private class SpanObject {

        Object mSpan;
        int mStart;
        int mEnd;

        public SpanObject(Object span, int start, int end) {
            this.mSpan = span;
            this.mStart = start;
            this.mEnd = end;
        }

    }
}
