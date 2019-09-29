package com.haiyunshan.zima.composer.holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.haiyunshan.zima.composer.ComposerFragment;
import com.haiyunshan.zima.composer.item.BaseItem;
import com.haiyunshan.zima.composer.item.Document;
import com.haiyunshan.zima.composer.item.ParagraphItem;
import com.haiyunshan.zima.composer.item.PictureItem;
import com.haiyunshan.zima.composer.item.SeparateItem;
import com.haiyunshan.zima.composer.state.BaseState;

import java.util.ArrayList;

public class DocumentAdapter extends RecyclerView.Adapter<BaseHolder> {

    private static final int PARAGRAPH  = 101;
    private static final int PICTURE    = 102;
    private static final int SEPARATE   = 103;

    ArrayList<BaseItem> mList;

    ComposerFragment mParent;

    public DocumentAdapter(ComposerFragment parent) {
        this.mParent = parent;

        Document doc = parent.getDocument();
        this.mList = new ArrayList<>(doc.getBody());
    }

    public int indexOf(BaseItem item) {
        return mList.indexOf(item);
    }

    public void add(int index, BaseItem item) {
        mList.add(index, item);
    }

    public void add(BaseItem item) {
        mList.add(item);
    }

    public int remove(BaseItem item) {
        int index = this.indexOf(item);
        if (index >= 0) {
            mList.remove(index);
        }

        return index;
    }

    @Override
    public int getItemViewType(int position) {
        BaseItem item = mList.get(position);

        int type = -1;
        if (item instanceof ParagraphItem) {
            type = PARAGRAPH;
        } else if (item instanceof PictureItem) {
            type = PICTURE;
        } else if (item instanceof SeparateItem) {
            type = SEPARATE;
        }

        return type;
    }

    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseHolder holder = null;

        if (viewType == PARAGRAPH) {
            holder = ParagraphHolder.create(mParent, parent);
        } else if (viewType == PICTURE) {
            holder = PictureHolder.create(mParent, parent);
        } else if (viewType == SEPARATE) {
            holder = SeparateHolder.create(mParent, parent);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        BaseItem item = mList.get(position);
        BaseState state = mParent.getStateMachine().peek();

        holder.onBind(position, item, state);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull BaseHolder holder) {
        holder.onViewAttachedToWindow();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull BaseHolder holder) {
        holder.onViewDetachedFromWindow();
    }
}
