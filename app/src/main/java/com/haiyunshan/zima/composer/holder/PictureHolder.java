package com.haiyunshan.zima.composer.holder;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.haiyunshan.zima.R;
import com.haiyunshan.zima.composer.ComposerFragment;
import com.haiyunshan.zima.composer.item.BaseItem;
import com.haiyunshan.zima.composer.item.ParagraphItem;
import com.haiyunshan.zima.composer.item.PictureItem;
import com.haiyunshan.zima.composer.state.BaseState;
import com.haiyunshan.zima.utils.SoftInputUtils;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class PictureHolder extends BaseHolder<PictureItem> implements View.OnClickListener, View.OnFocusChangeListener {

    View mContainer;
    ImageView mPictureView;
    EditText mNameEdit;

    ViewGroup mEditLayout;
    View mDeleteBtn;
    View mNameBtn;
    View mPictureBtn;

    boolean mShowAction;

    public static final PictureHolder create(ComposerFragment parent, ViewGroup container) {
        LayoutInflater inflater = parent.getLayoutInflater();
        int resource = R.layout.layout_composer_picture_item;
        View view = inflater.inflate(resource, container, false);

        PictureHolder holder = new PictureHolder(parent, view);
        return holder;
    }

    public PictureHolder(ComposerFragment parent, View itemView) {
        super(parent, itemView);

        this.mContainer = itemView.findViewById(R.id.picture_container);
        this.mPictureView = itemView.findViewById(R.id.iv_picture);
        this.mNameEdit = itemView.findViewById(R.id.edit_name);

        this.mEditLayout = itemView.findViewById(R.id.picture_edit_layout);
        this.mDeleteBtn = itemView.findViewById(R.id.btn_delete);
        mDeleteBtn.setOnClickListener(this);
        this.mNameBtn = itemView.findViewById(R.id.btn_edit_name);
        mNameBtn.setOnClickListener(this);
        this.mPictureBtn = itemView.findViewById(R.id.btn_edit_image);
        mPictureBtn.setOnClickListener(this);

        mNameEdit.setOnFocusChangeListener(this);
    }

    @Override
    public void onBind(int position, PictureItem item, BaseState state) {
        super.onBind(position, item, state);

        {
            this.mShowAction = false;
        }

        {
            mPictureView.setOnClickListener(this);
        }

        {
            CharSequence name = item.getName();
            mNameEdit.setText(name);
            boolean isEmpty = TextUtils.isEmpty(name);
            mNameEdit.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        }

        {
            int[] size = getViewSize(false);
            mPictureView.getLayoutParams().width = size[0];
            mPictureView.getLayoutParams().height = size[1];

            RequestOptions options = new RequestOptions();
            options.placeholder(new ColorDrawable(Color.TRANSPARENT));

            int duration = mContext.getResources().getInteger(R.integer.composer_picture_fade_duration);

            Glide.with(mParent)
                    .load(mItem.getUri())
                    .transition(DrawableTransitionOptions.withCrossFade(duration))
                    .apply(options)
                    .into(mPictureView);
        }

        {
            mEditLayout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mPictureView) {
            this.toogleEdit();
        } else if (v == mDeleteBtn) {
            this.delete();
        } else if (v == mNameBtn) {
            this.editName();
        } else if (v == mPictureBtn) {

        }
    }

    void editName() {
        this.toogleEdit();

        mNameEdit.setVisibility(View.VISIBLE);
        mNameEdit.setEnabled(true);

        mNameEdit.setFocusable(true);
        mNameEdit.setFocusableInTouchMode(true);
        mNameEdit.setSelection(mNameEdit.length());

        mNameEdit.requestFocus();
        SoftInputUtils.show(mContext, mNameEdit);
    }

    void delete() {

        ArrayList<BaseItem> list = new ArrayList<>();

        {
            int index = mDocument.indexOf(mItem);
            if (index >= 0) {
                list.add(mItem);

                if (index + 1 < mDocument.size()) {
                    BaseItem item = mDocument.get(index + 1);
                    if ((item instanceof ParagraphItem)) {
                        RecyclerView.ViewHolder h = mRecyclerView.findViewHolderForAdapterPosition(mAdapter.indexOf(item));
                        BaseHolder holder = (BaseHolder)h;
                        if (holder != null) {
                            holder.save();
                        }

                        if (item.isEmpty()){
                            list.add(item);
                        }
                    }
                }
            }
        }

        {
            for (BaseItem item : list) {
                mDocument.remove(item);
            }
        }

        {
            for (BaseItem item : list) {
                int index = mAdapter.remove(item);
                if (index >= 0) {
                    mAdapter.notifyItemRemoved(index);
                }
            }
        }
    }

    void toogleEdit() {

        if (mNameEdit.hasFocus()) {
            itemView.requestFocus();

            mNameEdit.setEnabled(false);
            mNameEdit.clearFocus();
            SoftInputUtils.hide(mContext, mNameEdit);

            return;
        }

        this.mShowAction = !this.mShowAction;

        boolean showAction = mShowAction;

        {
            int width = mPictureView.getWidth();
            int height = mPictureView.getHeight();

            RequestOptions options = new RequestOptions();

            boolean blur = showAction;
            if (blur) {
                width = width / 6;
                height = height / 6;
                options.override(width, height);

                options.transform(new BlurTransformation(24));
            }

            Glide.with(mParent)
                    .load(mItem.getUri())
                    .apply(options)
                    .transition(new DrawableTransitionOptions().dontTransition())
                    .into(new CrossFadeTarget(width, height));
        }

        {
            if (showAction) {
                mHandler.removeCallbacks(mHideEditLayout);

                mEditLayout.setVisibility(View.VISIBLE);

                //创建一个LayoutAnimationController对象
                LayoutAnimationController controller =
                        new LayoutAnimationController(AnimationUtils.loadAnimation(mContext, R.anim.abc_grow_fade_in_from_bottom));
                controller.setOrder(LayoutAnimationController.ORDER_NORMAL);

                mEditLayout.setLayoutAnimation(controller);
                mEditLayout.startLayoutAnimation();

            } else {
                int delay = mContext.getResources().getInteger(R.integer.abc_config_activityDefaultDur);
                mHandler.postDelayed(mHideEditLayout, delay);

                LayoutAnimationController controller =
                        new LayoutAnimationController(AnimationUtils.loadAnimation(mContext, R.anim.abc_shrink_fade_out_from_bottom));
                controller.setOrder(LayoutAnimationController.ORDER_REVERSE);
                mEditLayout.setLayoutAnimation(controller);
                mEditLayout.startLayoutAnimation();

            }

        }
    }

    int[] getViewSize(boolean scale) {
        int width = mRecyclerView.getMeasuredWidth();
        width -= (itemView.getPaddingLeft() + itemView.getPaddingRight());
        width -= (mContainer.getPaddingLeft() + mContainer.getPaddingRight());

        if (scale) {
            width = (mItem.getWidth() < width) ? mItem.getWidth() : width;
        }

        int height = width * mItem.getHeight() / mItem.getWidth();

        return new int[] { width, height};
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == mNameEdit) {
            if (!hasFocus) {
                boolean isEmpty = (mNameEdit.length() == 0);
                mNameEdit.setVisibility(isEmpty? View.GONE: View.VISIBLE);

                mNameEdit.setEnabled(false);
                mNameEdit.setFocusable(false);
                mNameEdit.setFocusableInTouchMode(false);

                this.save();
            }
        }
    }

    private class CrossFadeTarget extends SimpleTarget<Drawable> {

        public CrossFadeTarget(int width, int height) {
            super(width, height);
        }

        @Override
        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {

            TransitionDrawable t;

            Drawable current = mPictureView.getDrawable();
            if (current != null) {
                if (current instanceof TransitionDrawable) {
                    t = (TransitionDrawable) current;
                    current = t.findDrawableByLayerId(t.getId(1));
                }
            } else {
                current = new ColorDrawable(Color.TRANSPARENT);
            }

            t = new TransitionDrawable(new Drawable[] { current, resource });
            t.setId(0,0);
            t.setId(1,1);

            int duration = mContext.getResources().getInteger(R.integer.composer_picture_fade_duration);
            t.startTransition(duration);

            mPictureView.setImageDrawable(t);
        }
    }

    @Override
    public void save() {
        super.save();

        CharSequence name = mNameEdit.getText();
        mItem.setName(name);
    }

    private Runnable mHideEditLayout = new Runnable() {

        @Override
        public void run() {
            mEditLayout.setVisibility(View.INVISIBLE);
        }
    };
}
