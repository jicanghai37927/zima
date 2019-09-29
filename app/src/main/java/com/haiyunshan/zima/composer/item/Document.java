package com.haiyunshan.zima.composer.item;

import android.text.TextUtils;

import com.haiyunshan.zima.composer.entity.BaseEntity;
import com.haiyunshan.zima.composer.entity.Note;
import com.haiyunshan.zima.composer.entity.ParagraphEntity;
import com.haiyunshan.zima.composer.entity.PictureEntity;
import com.haiyunshan.zima.composer.entity.SeparateEntity;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Document {

    String mId;
    ArrayList<BaseItem> mBody;

    public Document(String id) {
        this.mId = id;
        this.mBody = new ArrayList<>(11);

        if (mBody.isEmpty()) {
            mBody.add(ParagraphItem.create(""));
        }
    }

    public String getId() {
        return mId;
    }

    public Document(Note note) {
        this.mId = note.getId();

        List<BaseEntity> list = note.getBody();

        this.mBody = new ArrayList<>(list.size() + 1);
        for (BaseEntity en : list) {
            BaseItem item = create(en);
            mBody.add(item);
        }

        if (mBody.isEmpty()) {
            mBody.add(ParagraphItem.create(""));
        }
    }

    public String getTitle() {
        for (BaseItem item : mBody) {
            if (item instanceof ParagraphItem) {
                String text = ((ParagraphItem)item).getText().toString();

                if (!TextUtils.isEmpty(text)) {
                    int pos = text.indexOf('\n');
                    if (pos > 0) {
                        text = text.substring(0, pos);
                    }

                    return text;
                }
            }
        }

        return "";
    }

    public List<BaseItem> getBody() {
        return mBody;
    }

    public void add(BaseItem item) {
        mBody.add(item);
    }

    public void add(int index, BaseItem item) {
        mBody.add(index, item);
    }

    public int indexOf(BaseItem item) {
        return mBody.indexOf(item);
    }

    public int remove(BaseItem item) {
        int index = this.indexOf(item);
        if (index >= 0) {
            mBody.remove(index);
        }

        return index;
    }

    public int size() {
        return mBody.size();
    }

    public BaseItem get(int index) {
        return mBody.get(index);
    }

    private BaseItem create(BaseEntity en) {
        BaseItem item = null;

        if (en instanceof ParagraphEntity) {
            item = new ParagraphItem((ParagraphEntity)en);
        } else if (en instanceof PictureEntity) {
            item = new PictureItem((PictureEntity)en);
        } else if (en instanceof SeparateEntity) {
            item = new SeparateItem((SeparateEntity)en);
        }

        return item;
    }
}
