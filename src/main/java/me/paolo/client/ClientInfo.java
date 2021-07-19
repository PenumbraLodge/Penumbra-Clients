package me.paolo.client;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

public class ClientInfo
{

    @SerializedName("jars")
    @Getter @Setter private JarInfo[] jars;

    @SerializedName("name")
    @Getter @Setter private String name;

    @SerializedName("description")
    @Getter @Setter private String description;

    @SerializedName("developed-by")
    @Getter @Setter private String developedBy;

    @SerializedName("cracked-by")
    @Getter @Setter private String crackedBy;

    @SerializedName("preview-urls")
    @Getter @Setter private String[] previewUrls;

    @SerializedName("mc-versions")
    @Getter @Setter private String[] minecraftVersions;

    @Override
    public String toString() {
        return getName();
    }

}
