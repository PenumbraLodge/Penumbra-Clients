package me.paolo.client;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

public class ArtifactStruct
{

    @SerializedName("path")
    @Getter @Setter private String path;

    @SerializedName("sha1")
    @Getter @Setter private String sha1;

    @SerializedName("size")
    @Getter @Setter private long size;

    @SerializedName("url")
    @Getter @Setter private String url;

    @Override
    public String toString() {
        return getPath();
    }

}
