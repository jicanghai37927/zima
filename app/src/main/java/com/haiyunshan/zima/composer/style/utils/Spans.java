package com.haiyunshan.zima.composer.style.utils;

import com.haiyunshan.zima.composer.style.span.AlignmentItem;
import com.haiyunshan.zima.composer.style.span.BoldItem;
import com.haiyunshan.zima.composer.style.span.FontItem;
import com.haiyunshan.zima.composer.style.span.ForegroundColorItem;
import com.haiyunshan.zima.composer.style.span.HighlightItem;
import com.haiyunshan.zima.composer.style.span.LineMarginItem;
import com.haiyunshan.zima.composer.style.span.ParagraphMarginItem;
import com.haiyunshan.zima.composer.style.span.ItalicItem;
import com.haiyunshan.zima.composer.style.span.StrikethroughItem;
import com.haiyunshan.zima.composer.style.span.TextSizeItem;
import com.haiyunshan.zima.composer.style.span.UnderlineItem;

public class Spans {

    // 字符

    public static final BoldItem BOLD_ITEM                      = new BoldItem();
    public static final ItalicItem ITALIC_ITEM                  = new ItalicItem();
    public static final UnderlineItem UNDERLINE_ITEM            = new UnderlineItem();
    public static final StrikethroughItem STRIKETHROUGH_ITEM    = new StrikethroughItem();
    public static final HighlightItem HIGHLIGHT_ITEM            = new HighlightItem();
    public static final TextSizeItem TEXT_SIZE_ITEM             = new TextSizeItem();
    public static final FontItem FONT_ITEM                      = new FontItem();
    public static final ForegroundColorItem FG_COLOR_ITEM       = new ForegroundColorItem();

    // 段落

    public static final AlignmentItem ALIGNMENT_ITEM            = new AlignmentItem();
    public static final LineMarginItem LINE_MARGIN_ITEM         = new LineMarginItem();
    public static final ParagraphMarginItem MARGIN_ITEM         = new ParagraphMarginItem();
}
