package me.paolo.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

@UtilityClass
public class JsonUtil
{

    private static final Gson GSON_INST = new GsonBuilder().setPrettyPrinting().create();

    public static <T> T fromJson(Reader reader, Class<T> type) {
        return GSON_INST.fromJson(reader, type);
    }

    public static <T> T fromJson(InputStream inputStream, Class<T> type) throws IOException {
        try(InputStreamReader streamReader = new InputStreamReader(inputStream)) {
            return GSON_INST.fromJson(streamReader, type);
        }
    }

    public static <T> T fromJson(JsonElement jsonElement, Class<T> type) {
        return GSON_INST.fromJson(jsonElement, type);
    }

    public static JsonElement toJson(Object obj) {
        return GSON_INST.toJsonTree(obj);
    }

    public static String toJsonStr(Object obj) {
        return GSON_INST.toJson(obj);
    }

}
