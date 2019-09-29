package com.haiyunshan.zima.note;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.haiyunshan.zima.R;
import com.haiyunshan.zima.note.dataset.NoteEntry;
import com.haiyunshan.zima.note.dataset.NoteManager;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteHolder> {

    ArrayList<NoteEntry> mList;

    int mMargin;
    int mDivider;

    NoteFragment mParent;

    NoteAdapter(NoteFragment parent) {
        this.mParent = parent;

        {
            this.mList = new ArrayList<>();

            NoteManager dm = NoteManager.instance();
            mList.addAll(dm.getList());
        }

        {
            Resources resources = mParent.getActivity().getResources();
            int margin = resources.getDimensionPixelSize(R.dimen.digest_card_margin_top);
            int divider = resources.getDimensionPixelSize(R.dimen.divider_size);

            this.mMargin = margin;
            this.mDivider = divider;
        }

    }

    public int indexOf(String id) {
        int size = mList.size();
        for (int i = 0; i < size; i++) {
            NoteEntry e = mList.get(i);
            if (e.getId().equalsIgnoreCase(id)) {
                return i;
            }
        }

        return -1;
    }

    void update(String id) {
        if (!TextUtils.isEmpty(id)) {
            int index = this.indexOf(id);
            if (index >= 0) {
                this.notifyItemChanged(index);
            } else {
                NoteEntry entry = NoteManager.instance().obtain(id);
                if (entry != null) {
                    if (!mList.isEmpty()) {
                        this.notifyItemChanged(0);
                    }
                    mList.add(0, entry);
                    this.notifyItemInserted(0);

                    mParent.getRecyclerView().scrollToPosition(0);
                }
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        NoteHolder holder = NoteHolder.create(mParent, parent);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {

        {
            NoteEntry entry = mList.get(position);
            (holder).bind(position, entry);
        }

        {
            View itemView = holder.itemView;

            int margin = this.mMargin;
            int divider = this.mDivider;

            int top = divider;
            int bottom = 0;

            int count = mParent.getAdapter().getItemCount();
            if (count == 1) {
                top = bottom = margin;
            } else {
                if (position == 0) {
                    top = margin;
                } else if (position + 1 == count) {
                    bottom = margin;
                } else {

                }
            }

            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)itemView.getLayoutParams();
            params.topMargin = top;
            params.bottomMargin = bottom;
        }

        {
            View itemView = holder.itemView;

            int resId = R.drawable.card_background_single_light;

            int count = mParent.getAdapter().getItemCount();
            if (count == 1) {

            } else {
                if (position == 0) {
                    resId = R.drawable.card_background_top_light;
                } else if (position + 1 == count) {
                    resId = R.drawable.card_background_bottom_light;
                } else {
                    resId = R.drawable.card_background_center_light;
                }
            }

            itemView.setBackgroundResource(resId);
        }
    }

    @Override
    public int getItemCount() {
        int size = mList.size();

        return size;
    }
}
