package com.haiyunshan.zima.composer.style.span;

import android.text.Selection;
import android.text.Editable;

public abstract class ParagraphSpanItem<T> extends BaseSpanItem<T> {

    static final char NEW_LINE = '\n';

    ParagraphSpanItem(Class<? extends T> type) {
        super(type);
    }

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

        if (start != 0) {
            start = lastIndexOf(text, NEW_LINE, start - 1);
            start = (start < 0)? 0: (start + 1);
        }

        if (end != text.length()) {
            if (text.charAt(end) != NEW_LINE) {
                end = indexOf(text, NEW_LINE, end + 1);
                end = (end < 0)? text.length(): end;
            }
        }

        return new int[] {start, end};
    }

    public int lastIndexOf(Editable text, int ch, int fromIndex) {
        if (ch < Character.MIN_SUPPLEMENTARY_CODE_POINT) {
            // handle most cases here (ch is a BMP code point or a
            // negative value (invalid code point))
            int i = Math.min(fromIndex, text.length() - 1);
            for (; i >= 0; i--) {
                if (text.charAt(i) == ch) {
                    return i;
                }
            }
            return -1;
        } else {
            return lastIndexOfSupplementary(text, ch, fromIndex);
        }
    }

    int indexOf(Editable text, int ch, int fromIndex) {
        final int max = text.length();
        if (fromIndex < 0) {
            fromIndex = 0;
        } else if (fromIndex >= max) {
            // Note: fromIndex might be near -1>>>1.
            return -1;
        }

        if (ch < Character.MIN_SUPPLEMENTARY_CODE_POINT) {
            // handle most cases here (ch is a BMP code point or a
            // negative value (invalid code point))
            for (int i = fromIndex; i < max; i++) {
                if (text.charAt(i) == ch) {
                    return i;
                }
            }
            return -1;
        } else {
            return indexOfSupplementary(text, ch, fromIndex);
        }

    }

    private int indexOfSupplementary(Editable text, int ch, int fromIndex) {
        if (Character.isValidCodePoint(ch)) {
            final char hi = Character.highSurrogate(ch);
            final char lo = Character.lowSurrogate(ch);
            final int max = text.length() - 1;
            for (int i = fromIndex; i < max; i++) {
                if (text.charAt(i) == hi && text.charAt(i + 1) == lo) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int lastIndexOfSupplementary(Editable text, int ch, int fromIndex) {
        if (Character.isValidCodePoint(ch)) {
            char hi = Character.highSurrogate(ch);
            char lo = Character.lowSurrogate(ch);
            int i = Math.min(fromIndex, text.length() - 2);
            for (; i >= 0; i--) {
                if (text.charAt(i) == hi && text.charAt(i + 1) == lo) {
                    return i;
                }
            }
        }
        return -1;
    }

}
