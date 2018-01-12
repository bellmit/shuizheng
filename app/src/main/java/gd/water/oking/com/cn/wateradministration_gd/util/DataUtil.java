package gd.water.oking.com.cn.wateradministration_gd.util;

import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhao on 2016/11/14.
 */

public class DataUtil {

    public static <T> T praseJson(String jsonData, TypeToken<T> tTypeToken) {
        T object = null;
        Gson g = new GsonBuilder().serializeNulls().registerTypeAdapter(Uri.class, new UristampAdapter()).
                registerTypeAdapter(Long.class, new LongstampAdapter()).create();
        final java.lang.reflect.Type type = tTypeToken.getType();
        try {
            object = g.fromJson(jsonData, type);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return object;
    }

    public static <T> String toJson(T object) {
        Gson g = new GsonBuilder().serializeNulls().registerTypeAdapter(Uri.class, new UristampAdapter()).create();
        return g.toJson(object);
    }

    public static class UristampAdapter implements JsonSerializer<Uri>, JsonDeserializer<Uri> {

        @Override
        public Uri deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            if (jsonElement == null) {
                return null;
            } else {
                return Uri.fromFile(new File(jsonElement.getAsString()));
            }
        }

        @Override
        public JsonElement serialize(Uri uri, Type type, JsonSerializationContext jsonSerializationContext) {
            String value = "";
            if (uri != null) {
                value = uri.getPath();
            }
            return new JsonPrimitive(value);
        }
    }

    public static class LongstampAdapter implements JsonSerializer<Long>, JsonDeserializer<Long> {

        @Override
        public Long deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            String str = jsonElement.getAsString();
            if ("".equals(str)) {
                return null;
            } else {
                return jsonElement.getAsLong();
            }
        }


        @Override
        public JsonElement serialize(Long aLong, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(aLong);
        }
    }

    public static String getSystemTime() {
        DateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(new Date());
    }
}
