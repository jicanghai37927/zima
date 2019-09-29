package com.haiyunshan.zima.composer;


import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.haiyunshan.zima.R;
import com.haiyunshan.zima.composer.entity.Note;
import com.haiyunshan.zima.composer.helper.ParagraphSpanHelper;
import com.haiyunshan.zima.composer.holder.BaseHolder;
import com.haiyunshan.zima.composer.holder.DocumentAdapter;
import com.haiyunshan.zima.composer.holder.ParagraphHolder;
import com.haiyunshan.zima.composer.item.BaseItem;
import com.haiyunshan.zima.composer.item.Document;
import com.haiyunshan.zima.composer.item.ParagraphItem;
import com.haiyunshan.zima.composer.item.PictureItem;
import com.haiyunshan.zima.composer.item.SeparateItem;
import com.haiyunshan.zima.composer.observable.SelectionObservable;
import com.haiyunshan.zima.composer.state.BaseState;
import com.haiyunshan.zima.composer.state.StateMachine;
import com.haiyunshan.zima.note.dataset.NoteEntry;
import com.haiyunshan.zima.note.dataset.NoteManager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 *
 */
public class ComposerFragment extends Fragment implements Toolbar.OnMenuItemClickListener, View.OnClickListener {

    static final int REQUEST_PHOTO = 1001;

    Toolbar mToolbar;
    RecyclerView mRecyclerView;

    View mSaveBtn;

    Document mDocument;
    DocumentAdapter mAdapter;

    BottomSheetFragment mBottomSheet;

    StateMachine mStateMachine;
    SelectionObservable mSelectionObservable;

    public ComposerFragment() {
        this.mSelectionObservable = new SelectionObservable();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_composer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        {
            this.mToolbar = view.findViewById(R.id.composer_toolbar);
        }

        {
            this.mRecyclerView = view.findViewById(R.id.composer_list_view);

            LinearLayoutManager layout = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(layout);
        }

        {
            this.mSaveBtn = view.findViewById(R.id.iv_save);
            mSaveBtn.setOnClickListener(this);
        }

        {
            this.mBottomSheet = (BottomSheetFragment)(getChildFragmentManager().findFragmentById(R.id.composer_bottom_sheet_fragment));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        {
            this.mDocument = this.readDoc();
        }

        {
            this.mAdapter = new DocumentAdapter(this);
            mRecyclerView.setAdapter(mAdapter);
        }

        {
            this.mStateMachine = new StateMachine(this);
            mStateMachine.push(StateMachine.READ);
        }

        {
            mStateMachine.peek().onEnter();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PHOTO) {
            if (resultCode == RESULT_OK) {
                if (data != null) {

                    ArrayList<Uri> list = new ArrayList<>();

                    {
                        ClipData clipData = data.getClipData();
                        if (clipData != null) {
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                ClipData.Item item = clipData.getItemAt(i);
                                Uri uri = item.getUri();
                                list.add(uri);
                            }
                        }

                        if (list.isEmpty()) {
                            Uri uri = data.getData();
                            if (uri != null) {
                                list.add(uri);
                            }
                        }
                    }

                    {
                        String[] array = new String[list.size()];
                        int index = 0;

                        for (Uri uri : list) {
                            String uriString = uri.toString();
                            String path;

                            if (uriString.contains("content")) {
                                path = getRealPathFromURI(uri);
                            } else {
                                path = uriString.replace("file://", "");
                            }

                            path = (path == null) ? uriString : path;
                            array[index++] = path;
                        }

                        this.insertPictures(array);
                    }
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        {
            this.saveRecyclerView();
        }

        {
            NoteManager dm = NoteManager.instance();
            String id = mDocument.getId();

            NoteEntry entry = dm.put(id);
            entry.setTitle(mDocument.getTitle());
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        this.saveDoc();

    }

    public BottomSheetFragment getBottomSheet() {
        return mBottomSheet;
    }

    public void closeEdit() {
        mStateMachine.pop();
    }

    public void startEdit() {
        mStateMachine.push(StateMachine.EDIT);
    }

    public boolean closeFormat() {
        BaseState state = mStateMachine.pop();

        mBottomSheet.reset();

        return (state != null);
    }

    public void startFormat() {
        mStateMachine.push(StateMachine.FORMAT);
    }

    public boolean onBackPressed() {
        boolean result = mStateMachine.peek().onBackPressed();

        return result;
    }

    void insertPictures(String[] array) {
        View focus = this.getActivity().getCurrentFocus();
        if (focus == null) {
            this.addPictures(array);
            return;
        }

        RecyclerView.ViewHolder h = mRecyclerView.findContainingViewHolder(focus);
        if (h == null) {
            this.addPictures(array);
            return;
        }

        BaseHolder holder = (BaseHolder)h;
        holder.insertPicture(array);
    }

    void addSeparate() {

        ArrayList<BaseItem> list = new ArrayList<>();

        // 创建新对象
        {
            {
                {
                    SeparateItem p = SeparateItem.create();
                    list.add(p);
                }

                {
                    ParagraphItem p = ParagraphItem.create("");
                    list.add(p);
                }
            }

        }


        // 更新Document
        {
            for (BaseItem item : list) {
                mDocument.add(item);
            }
        }

        // 更新Adapter
        {
            int position = mAdapter.getItemCount();

            for (BaseItem p : list) {
                mAdapter.add(p);
            }

            int count = list.size();

            mAdapter.notifyItemChanged(position);
            mAdapter.notifyItemRangeInserted(position, count);

            mRecyclerView.scrollToPosition(position);
        }
    }

    void addPictures(String[] array) {

        ArrayList<BaseItem> list = new ArrayList<>(array.length * 2 + 1);

        // 创建新对象
        {
            int length = array.length;
            for (int i = 0; i < length; i++) {
                String path = array[i];

                {
                    File file = new File(path);

                    PictureItem p = PictureItem.create(file);
                    list.add(p);
                }

                {
                    ParagraphItem p = ParagraphItem.create("");
                    list.add(p);
                }
            }

        }


        // 更新Document
        {
            for (BaseItem item : list) {
                mDocument.add(item);
            }
        }

        // 更新Adapter
        {
            int position = mAdapter.getItemCount();

            for (BaseItem p : list) {
                mAdapter.add(p);
            }

            int count = list.size();

            mAdapter.notifyItemChanged(position);
            mAdapter.notifyItemRangeInserted(position, count);

            mRecyclerView.scrollToPosition(position);
        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    public Document getDocument() {
        return mDocument;
    }

    public StateMachine getStateMachine() {
        return mStateMachine;
    }

    public DocumentAdapter getAdapter() {
        return mAdapter;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    Document readDoc() {
        String id = getArguments().getString("id", "zima");

        File file = this.getFile();

        Note note = Note.create(file);
        if (note == null) {
            this.mDocument = new Document(id);

            {
                NoteManager dm = NoteManager.instance();
                dm.put(mDocument.getId());
                dm.save();
            }

        } else {
            this.mDocument = new Document(note);
        }

        return mDocument;
    }

    void saveRecyclerView() {
        int count = mRecyclerView.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = mRecyclerView.getChildAt(i);
            RecyclerView.ViewHolder h = mRecyclerView.getChildViewHolder(child);
            BaseHolder holder = (BaseHolder)h;
            holder.save();
        }
    }

    void saveDoc() {
        if (mDocument == null) {
            return;
        }

        File file = this.getFile();
        Note note = new Note(mDocument);
        note.save(file);
    }

    File getFile() {

        File file = new File(Environment.getExternalStorageDirectory(), "zima/note");
        file.mkdirs();

        String id = getArguments().getString("id", "zima");
        file = new File(file, id + ".json");

        return file;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.menu_separate: {
                this.separate();
                break;
            }
            case R.id.menu_picture: {
                this.selectPhoto();
                break;
            }
        }

        return true;
    }

    public void separate() {
        View focus = this.getActivity().getCurrentFocus();
        if (focus == null) {
            this.addSeparate();
            return;
        }

        RecyclerView.ViewHolder h = mRecyclerView.findContainingViewHolder(focus);
        if (h == null) {
            this.addSeparate();
            return;
        }

        BaseHolder holder = (BaseHolder)h;
        holder.insertSeparate();
    }

    public void selectPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        this.startActivityForResult(intent, REQUEST_PHOTO);
    }

    public String getRealPathFromURI(Uri contentURI) {
        String path = null;
        String[] projection = { MediaStore.Images.Media.DATA };

        Cursor cursor = getActivity().getContentResolver().query(contentURI, projection, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();

            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            path = cursor.getString(idx);

            cursor.close();
        }

        return path;
    }

    public ParagraphSpanHelper getSpanHelper() {
        RecyclerView recyclerView = this.mRecyclerView;
        View child = recyclerView.getFocusedChild();
        if (child == null) {
            return null;
        }

        RecyclerView.ViewHolder h = recyclerView.findContainingViewHolder(child);
        if (h == null || !(h instanceof ParagraphHolder)) {
            return null;
        }

        ParagraphHolder holder = (ParagraphHolder)h;
        return holder.getSpanHelper();
    }

    public SelectionObservable getSelectionObservable() {
        return mSelectionObservable;
    }

    @Override
    public void onClick(View v) {
        if (v == mSaveBtn) {
            this.saveRecyclerView();

            File file = new File(Environment.getExternalStorageDirectory(), "zima.html");
            save(mDocument, file);
        }
    }

    File save(Document doc, File file) {

        List<BaseItem> list = doc.getBody();
        for (BaseItem item : list) {

            if (item instanceof ParagraphItem) {

                ParagraphItem pItem = (ParagraphItem)item;
                String text = Html.toHtml((Spanned)pItem.getText(), Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE);
                text = StringEscapeUtils.unescapeHtml4(text);

                try {
                    FileUtils.writeStringToFile(file, text, "utf-8");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            }


        }

        return file;
    }
}
