package me.paolo.client;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

public class JarInfo
{

    @SerializedName("jvm")
    @Getter @Setter private ArtifactStruct jvm;

    @SerializedName("jar")
    @Getter @Setter private ArtifactStruct jar;

    @SerializedName("json")
    @Getter @Setter private ArtifactStruct json;

    @SerializedName("version")
    @Getter @Setter private String version;

    @Override
    public String toString() {
        return getVersion();
    }

}
