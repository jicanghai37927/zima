package com.haiyunshan.zima.composer.style;

import android.text.style.RelativeSizeSpan;

public class TextSizeSpan extends RelativeSizeSpan {

    public TextSizeSpan(float proportion) {
        super(proportion);
    }

    public final float getProportion() {
        return super.getSizeChange();
    }
}
