package com.haiyunshan.zima.composer.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import com.haiyunshan.zima.composer.entity.style.SpanEntity;
import com.haiyunshan.zima.composer.item.BaseItem;
import com.haiyunshan.zima.composer.item.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Note {

    @SerializedName("id")
    String mId;

    @SerializedName("body")
    ArrayList<BaseEntity> mBody;

    public static final Note create(File file) {
        if (!file.exists()) {
            return null;
        }

        Note note = null;

        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(fis, "utf-8");

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(BaseEntity.class, new EntityDeserializer())
                    .create();
            note = gson.fromJson(reader, Note.class);

            reader.close();
            fis.close();
        } catch (Exception e) {

        }

        return note;
    }

    public Note(Document doc) {
        this.mId = doc.getId();

        List<BaseItem> list = doc.getBody();

        this.mBody = new ArrayList<>(list.size());
        for (BaseItem item: list) {
            BaseEntity en = item.getEntity();
            mBody.add(en);
        }
    }

    public void save(File file) {

        try {
            file.delete();
            file.createNewFile();

            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(fos, "utf-8");

            Gson gson = new Gson();
            gson.toJson(this, writer);

            writer.close();
            fos.close();
        } catch (Exception e) {

        }

    }

    public String getId() {
        return mId;
    }

    public List<BaseEntity> getBody() {
        return mBody;
    }

    private static class EntityDeserializer implements JsonDeserializer<BaseEntity> {

        static final String PARAGRAPH   = "paragraph";
        static final String PICTURE     = "picture";
        static final String SEPARATE    = "separate";

        Gson mGson;

        EntityDeserializer() {
            this.mGson = new Gson();
        }

        @Override
        public BaseEntity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();

            String name = jsonObject.get("type").getAsString();
            switch (name) {
                case PARAGRAPH:
                    return mGson.fromJson(json, ParagraphEntity.class);
                case PICTURE:
                    return mGson.fromJson(json, PictureEntity.class);
                case SEPARATE:
                    return mGson.fromJson(json, SeparateEntity.class);
                default:
                    return null;
            }
        }
    }
}
