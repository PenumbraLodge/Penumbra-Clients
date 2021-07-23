package me.paolo.manager;

import me.paolo.client.ArtifactStruct;
import me.paolo.client.ClientInfo;
import me.paolo.client.JarInfo;

public class StaticVerifier
{

    private StaticVerifier() { }

    public static boolean verify(ClientInfo clientInfo) {
        if(clientInfo.getJars() == null
                || clientInfo.getName() == null
                || clientInfo.getDescription() == null
                || clientInfo.getDevelopedBy() == null
                || clientInfo.getCrackedBy() == null
                || clientInfo.getPreviewUrls() == null
                || clientInfo.getMinecraftVersions() == null) {
            return false;
        }

        for(JarInfo jarInfo : clientInfo.getJars()) {
            if(!verify(jarInfo)) return false;
            System.out.println(
                    "Imported: \""
                            + clientInfo.getName() + '-' + jarInfo.getVersion()
                            + '\"'
            );
        }
        return true;
    }

    public static boolean verify(JarInfo jarInfo) {
        if(jarInfo.getJvm() == null
                || jarInfo.getJar() == null
                || jarInfo.getJson() == null
                || jarInfo.getVersion() == null) {
            return false;
        }

        return verify(jarInfo.getJvm(), true)
                && verify(jarInfo.getJar(), true)
                && verify(jarInfo.getJson(), true);
    }

    public static boolean verify(ArtifactStruct artifact, boolean force) {
        if(artifact == null) return false;
        if((artifact.getSha1() == null || artifact.getUrl() == null) && force) return false;
        return artifact.getPath() != null;
    }

}
