package com.haiyunshan.zima.composer;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.util.SortedList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.util.SortedListAdapterCallback;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.haiyunshan.zima.R;
import com.haiyunshan.zima.composer.helper.ParagraphSpanHelper;
import com.haiyunshan.zima.composer.holder.ParagraphHolder;
import com.haiyunshan.zima.composer.observable.SelectionObservable;
import com.haiyunshan.zima.composer.observer.SelectionObserver;
import com.haiyunshan.zima.divider.LeadingMarginDividerItemDecoration;
import com.haiyunshan.zima.font.dataset.FontEntry;
import com.haiyunshan.zima.font.dataset.FontManager;

import java.text.Collator;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParagraphFontFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    Toolbar mToolbar;

    RecyclerView mRecyclerView;
    SortedList<FontEntry> mSortedList;
    FontAdapter mAdapter;

    ParagraphSpanHelper mSpanHelper;

    BottomSheetFragment mBottomSheetFragment;
    ComposerFragment mComposerFragment;

    String mFontId;

    public ParagraphFontFragment() {
        this.mFontId = "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_paragraph_font, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        {
            this.mToolbar = view.findViewById(R.id.toolbar);
            mToolbar.inflateMenu(R.menu.composer_paragraph_format_menu);

            mToolbar.setNavigationIcon(R.drawable.ic_material_arrow_left_dark);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBottomSheetFragment.pop();
                }
            });

            mToolbar.setOnMenuItemClickListener(this);
        }

        {
            this.mRecyclerView = view.findViewById(R.id.recycler_list_view);
            LinearLayoutManager layout = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(layout);

            LeadingMarginDividerItemDecoration decor = new LeadingMarginDividerItemDecoration(getActivity());
            decor.setDrawable(getActivity().getDrawable(R.drawable.shape_divider));
            decor.setMargin(getActivity().getResources().getDimensionPixelSize(R.dimen.font_item_check_size));
            mRecyclerView.addItemDecoration(decor);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        {
            this.mAdapter = new FontAdapter(getActivity());
            FontCallback callback = new FontCallback(mAdapter);
            this.mSortedList = new SortedList<>(FontEntry.class, callback);

            FontManager mgr = FontManager.instance();
            mSortedList.addAll(mgr.getList());

            this.mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        {
            Fragment p = this.getParentFragment();
            while (p != null) {

                if (p instanceof BottomSheetFragment) {
                    this.mBottomSheetFragment = (BottomSheetFragment)p;
                }

                if (p instanceof ComposerFragment) {
                    this.mComposerFragment = (ComposerFragment)p;
                }

                p = p.getParentFragment();
            }
        }

        {
            this.mSpanHelper = mComposerFragment.getSpanHelper();
            if (mSpanHelper != null) {
                mFontId = mSpanHelper.getFont();
            }
        }

        {
            SelectionObservable observable = mComposerFragment.getSelectionObservable();
            observable.registerObserver(mSelectionObserver);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        {
            SelectionObservable observable = mComposerFragment.getSelectionObservable();
            observable.unregisterObserver(mSelectionObserver);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menu_close: {
                mComposerFragment.closeFormat();

                break;
            }
        }

        return true;
    }

    private class FontCallback extends SortedListAdapterCallback<FontEntry> {

        Collator mCollator;

        /**
         * Creates a {@link SortedList.Callback} that will forward data change events to the provided
         * Adapter.
         *
         * @param adapter The Adapter instance which should receive events from the SortedList.
         */
        public FontCallback(RecyclerView.Adapter adapter) {
            super(adapter);

            this.mCollator = Collator.getInstance();
        }

        @Override
        public int compare(FontEntry o1, FontEntry o2) {
            String n1 = o1.getName();
            String n2 = o2.getName();

            return mCollator.compare(n1, n2);
        }

        @Override
        public boolean areContentsTheSame(FontEntry oldItem, FontEntry newItem) {
            return oldItem.getName().equalsIgnoreCase(newItem.getName());
        }

        @Override
        public boolean areItemsTheSame(FontEntry item1, FontEntry item2) {
            return item1.getId().equalsIgnoreCase(item2.getId());
        }
    }

    private class FontAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        LayoutInflater mInflater;

        FontAdapter(Activity context) {
            this.mInflater = context.getLayoutInflater();
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            int resource = R.layout.layout_paragraph_font_item;
            View view = mInflater.inflate(resource, parent, false);
            FontHolder holder = new FontHolder(view);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder h, int position) {
            FontEntry entry = mSortedList.get(position);

            FontHolder holder = (FontHolder)h;
            holder.bind(position, entry);
        }

        @Override
        public int getItemCount() {
            return mSortedList.size();
        }

    }

    void setChecked(int position) {

        int old = -1;
        int count = mRecyclerView.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = mRecyclerView.getChildAt(i);
            RecyclerView.ViewHolder h = mRecyclerView.findContainingViewHolder(child);
            if (h != null && h instanceof FontHolder) {
                FontHolder holder = (FontHolder)h;
                if (holder.mCheckedView.getVisibility() == View.VISIBLE) {
                    old = mRecyclerView.getChildAdapterPosition(child);
                    break;
                }
            }
        }

        if (old >= 0) {
            mAdapter.notifyItemChanged(old);
        }

        mAdapter.notifyItemChanged(position);
    }

    private class FontHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View mCheckedView;
        TextView mNameView;

        int mPosition;
        FontEntry mEntry;

        public FontHolder(View itemView) {
            super(itemView);

            this.mCheckedView = itemView.findViewById(R.id.iv_checked);
            this.mNameView = itemView.findViewById(R.id.tv_name);

            itemView.setOnClickListener(this);
        }

        void bind(int position, FontEntry entry) {
            this.mPosition = position;
            this.mEntry = entry;

            mNameView.setText(entry.getName());

            FontManager mgr = FontManager.instance();
            Typeface tf = mgr.getTypeface(entry);
            mNameView.setTypeface(tf);

            mCheckedView.setVisibility(View.INVISIBLE);

            String fontId = mFontId;
            if (fontId != null) {
                if (entry.getId().equalsIgnoreCase(fontId)) {
                    mCheckedView.setVisibility(View.VISIBLE);
                }
            }

        }

        @Override
        public void onClick(View v) {
            if (v == itemView) {
                if (mSpanHelper != null) {
                    mFontId = mEntry.getId();
                    mSpanHelper.setFont(mFontId);
                }

                if (mCheckedView.getVisibility() != View.VISIBLE) {
                    setChecked(mPosition);
                }
            }
        }
    }

    private SelectionObserver mSelectionObserver = new SelectionObserver() {

        @Override
        public void onSelectionChanged(ParagraphHolder holder, int start, int end) {
            mSpanHelper = holder.getSpanHelper();
            mFontId = mSpanHelper.getFont();

            mAdapter.notifyDataSetChanged();
        }
    };
}
