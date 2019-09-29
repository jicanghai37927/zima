package com.haiyunshan.zima.test;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Layout;
import android.text.Selection;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.BulletSpan;
import android.text.style.DrawableMarginSpan;
import android.text.style.IconMarginSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.LineHeightSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ReplacementSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.haiyunshan.zima.R;
import com.haiyunshan.zima.composer.style.LineMarginSpan;
import com.haiyunshan.zima.composer.widget.ParagraphEditText;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class TestAlignmentActivity extends AppCompatActivity implements ParagraphEditText.OnSelectionChangeListener {

    ParagraphEditText mEdit;
    RelativeSizeSpan mSpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_alignment);

        this.mEdit = findViewById(R.id.edit);
        mEdit.setOnSelectionChangeListener(this);

        mEdit.postDelayed(new Runnable() {
            @Override
            public void run() {
                testParagraphSpacingSpan();

//                testBackground();
//                testRadiusBackgroundSpan();;
//                testMarginSpan();
//                testSizeSpan();
//
                nullLayouts();
                mEdit.forceLayout();
                mEdit.requestLayout();


            }
        }, 100);

        findViewById(R.id.btn_log).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nullLayouts();
//                Editable text = mEdit.getText();
//
//                Object[] spans = text.getSpans(0, text.length(), Object.class);
//                for (Object obj: spans) {
//                    Log.w("AA", obj.getClass().getName());
//                }

//                String str = Html.toHtml(text, Html.TO_HTML_PARAGRAPH_LINES_INDIVIDUAL);
//                Log.w("BB", str);

//                if (mSpan == null) {
//                    mSpan = new RelativeSizeSpan(1.1f);
//                    text.setSpan(mSpan, 0, text.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//                } else {
//                    text.removeSpan(mSpan);
//                    float pro = mSpan.getSizeChange() + 0.1f;
//                    mSpan = new RelativeSizeSpan(pro);
//                    text.setSpan(mSpan, 0, text.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//                }

//                float px = mEdit.getTextSize();
//                float sp = px / mEdit.getPaint().density;
//                sp += 2;
//                mEdit.setTextSize(sp);

//                Log.w("AA", px + ", " + sp);
            }
        });
    }

    void testParagraphSpacingSpan() {
        Editable text = mEdit.getText();

        if (true) {
            int start = 0;
            int end = text.length();
            int flags = Spanned.SPAN_INCLUSIVE_INCLUSIVE;
            text.setSpan(new ParagraphSpacingSpan(), start, end, flags);
        }
    }

    void testBackground() {
        Editable text = mEdit.getText();

        if (true) {
            int start = 0;
            int end = text.length();
            int flags = Spanned.SPAN_INCLUSIVE_INCLUSIVE;
            text.setSpan(new BackgroundColorSpan(Color.YELLOW), start, end, flags);
        }
    }

    void testRadiusBackgroundSpan() {
        Editable text = mEdit.getText();

        if (true) {
            int start = 0;
            int end = text.length();
            int flags = Spanned.SPAN_INCLUSIVE_INCLUSIVE;
            text.setSpan(new RadiusBackgroundSpan(Color.YELLOW, 16), start, end, flags);
        }
    }

    void testMarginSpan() {
        Editable text = mEdit.getText();

        if (true) {
            int start = 0;
            int end = text.length();
            int flags = Spanned.SPAN_INCLUSIVE_INCLUSIVE;
            text.setSpan(new LeadingMarginSpan.Standard(200, 200), start, end, flags);
        }

        if (true) {
            int start = 0;
            int end = text.length();
            int flags = Spanned.SPAN_INCLUSIVE_INCLUSIVE;
            LineMarginSpan span = new LineMarginSpan();
            span.setView(mEdit);
            text.setSpan(span, start, end, flags);
        }

        nullLayouts();
    }

    void testSizeSpan() {
        Editable text = mEdit.getText();

        {
            int start = 0;
            int end = text.length();
            int flags = Spanned.SPAN_INCLUSIVE_INCLUSIVE;
            text.setSpan(new AbsoluteSizeSpan((int)(30 * 2.5f)), start, end, flags);
        }

        {
            int start = 0;
            int end = 5;
            int flags = Spanned.SPAN_EXCLUSIVE_INCLUSIVE;
            text.setSpan(new RelativeSizeSpan((1.5f)), start, end, flags);
        }
    }

    String getGravity(int gravity) {
        return "" + gravity;
    }

    String getTextAlignment(int align) {
        String[] array = new String[] {
                "TEXT_ALIGNMENT_INHERIT",
                "TEXT_ALIGNMENT_GRAVITY",
                "TEXT_ALIGNMENT_TEXT_START",
                "TEXT_ALIGNMENT_TEXT_END",
                "TEXT_ALIGNMENT_CENTER",
                "TEXT_ALIGNMENT_VIEW_START",
                "TEXT_ALIGNMENT_VIEW_END"
        };

        return array[align];
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

    @Override
    public void onSelectionChanged(ParagraphEditText view, int start, int end) {
        if (start != end && view.getLayout() != null) {
            return;
        }

        Rect padding = getPadding(view);
        if (padding != null) {
            boolean linebreak = false;
            int line = view.getLayout().getLineForOffset(start);
            start = view.getLayout().getLineStart(line);
            end = view.getLayout().getLineEnd(line);
            for (int i = start; i < end; i++) {
                if (view.getText().charAt(i) == '\n') {
                    linebreak = true;
                    break;
                }
            }

            padding.bottom = (linebreak)? -150: 0;
        }
    }

    class LineSpacingSpan implements LineHeightSpan.WithDensity {

        @Override
        public void chooseHeight(CharSequence text, int start, int end, int spanstartv, int v, Paint.FontMetricsInt fm, TextPaint paint) {
            if (start == ((Spanned)text).getSpanStart(this)) {
                fm.top -= 50;
            } else {
                fm.ascent -= 50;
            }
//            fm.ascent -= 50;
//            fm.descent += 50;

//            fm.bottom += 50;
//            fm.top -= 50;

//            fm.leading += 50;
        }

        @Override
        public void chooseHeight(CharSequence text, int start, int end, int spanstartv, int v, Paint.FontMetricsInt fm) {
            Log.w("AA", "chooseHeight");
        }
    }

    class LineLeadingSpan implements LeadingMarginSpan {

        @Override
        public int getLeadingMargin(boolean first) {
            if (first) {
                return 100;
            }

            return 0;
        }

        @Override
        public void drawLeadingMargin(Canvas c, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {

        }
    }

    class RadiusBackgroundSpan extends ReplacementSpan {

        private int mSize;
        private int mColor;
        private int mRadius;

        /**
         * @param color  背景颜色
         * @param radius 圆角半径
         */
        public RadiusBackgroundSpan(int color, int radius) {
            mColor = color;
            mRadius = radius;
        }

        @Override
        public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
            mSize = (int) (paint.measureText(text, start, end) + 2 * mRadius);
            //mSize就是span的宽度，span有多宽，开发者可以在这里随便定义规则
            //我的规则：这里text传入的是SpannableString，start，end对应setSpan方法相关参数
            //可以根据传入起始截至位置获得截取文字的宽度，最后加上左右两个圆角的半径得到span宽度
            return mSize;
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
            int color = paint.getColor();//保存文字颜色
            paint.setColor(mColor);//设置背景颜色
            paint.setAntiAlias(true);// 设置画笔的锯齿效果
            RectF oval = new RectF(x, y + paint.ascent(), x + mSize, y + paint.descent());
            //设置文字背景矩形，x为span其实左上角相对整个TextView的x值，y为span左上角相对整个View的y值。paint.ascent()获得文字上边缘，paint.descent()获得文字下边缘
            canvas.drawRoundRect(oval, mRadius, mRadius, paint);//绘制圆角矩形，第二个参数是x半径，第三个参数是y半径
            paint.setColor(color);//恢复画笔的文字颜色
            canvas.drawText(text, start, end, x + mRadius, y, paint);//绘制文字
        }
    }

    private class ParagraphSpacingSpan implements LineHeightSpan {

        @Override
        public void chooseHeight(CharSequence text, int start, int end, int istartv, int v, Paint.FontMetricsInt fm) {
//            Log.w("AA", "start = " + start + ", end = " + end + ", spanstartv = " + spanstartv + ", v = " + v);


            if ((end > 0 && text.charAt(end - 1) == '\n'))  {
//                fm.bottom += 200;
                fm.descent += 200;

                Log.w("AA", "line break = " + start);
            }

//            if (start == ((Spanned)text).getSpanStart(this)) {
//                fm.top -= 50;
//                fm.leading -= 200;
//            } else {
//                fm.leading -= 200;
////                fm.ascent -= 50;
//            }

//            if (end > 0 && text.charAt(end - 1) == '\n') {
////                fm.bottom += 200;
////                fm.top -= 200;
////                fm.descent += 200;
////                fm.ascent -= 200;
//                fm.leading += 200;
//                Log.w("AA", "line break");
//            }

//            fm.leading += 120;
//            fm.descent += 40;
//            fm.ascent -= 40;
//            fm.top -= 40;
//            fm.bottom += 40;
        }
    }

    Drawable[] getCursorDrawable(EditText editText) {
        Drawable[] drawables = null;

        try {
            Field fEditor = TextView.class.getDeclaredField("mEditor");
            fEditor.setAccessible(true);
            Object editor = fEditor.get(editText);

            Class<?> clazz = editor.getClass();
            Field fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
            fCursorDrawable.setAccessible(true);

            Object cursorDrawable = fCursorDrawable.get(editor);
            drawables = (Drawable[])cursorDrawable;

        } catch (Exception e) {

        }

        return drawables;
    }

    Rect getPadding(EditText editText) {

        Drawable[] drawables = getCursorDrawable(editText);
        if (drawables != null && drawables.length > 0 && drawables[0] != null) {
            Drawable d = drawables[0];
            if (d instanceof GradientDrawable) {
                GradientDrawable g = (GradientDrawable)d;
                Rect padding = getPadding(g);
                if (padding != null) {
                    return padding;
                }
            }
        }

        return null;
    }

    Rect getPadding(GradientDrawable drawable) {
        Rect rect = null;

        try {
            Field fEditor = GradientDrawable.class.getDeclaredField("mPadding");
            fEditor.setAccessible(true);
            Object editor = fEditor.get(drawable);

            rect = (Rect)editor;
        } catch (Exception e) {

        }

        return rect;
    }

    public static void setCursorDrawableColor(EditText editText, int color) {
        try {

            Field fEditor = TextView.class.getDeclaredField("mEditor");
            fEditor.setAccessible(true);
            Object editor = fEditor.get(editText);

            Class<?> clazz = editor.getClass();
            Field fCursorDrawable = clazz.getDeclaredField("mCursorDrawable");
            fCursorDrawable.setAccessible(true);

            Drawable[] drawables = new Drawable[2];
            drawables[0].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            drawables[1].setColorFilter(color, PorterDuff.Mode.SRC_IN);
            fCursorDrawable.set(editor, drawables);
        } catch (final Throwable ignored) {
        }
    }
}
