package me.paolo.manager;

import me.paolo.ServerMain;
import me.paolo.client.ClientInfo;
import me.paolo.client.JarInfo;
import me.paolo.util.JsonUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class StaticClient
{

    private StaticClient() { }

    private static ClientInfo get(File runtimeDir, File versionsDir, File clientDir) {
        try(FileInputStream inputStream = new FileInputStream(new File(clientDir, "config.json"))) {
            ClientInfo clientInfo = JsonUtil.fromJson(inputStream, ClientInfo.class);

            String[] previews = clientInfo.getPreviewUrls();
            for(int i = 0; i < previews.length; i++) {
                previews[i] = Objects.requireNonNull(ServerMain.getArtifact(
                        versionsDir, new File(clientDir, previews[i])
                )).getUrl();
            }

            JarInfo[] jars = clientInfo.getJars();
            for (JarInfo jarInfo : jars) {
                File versionDir = new File(clientDir, jarInfo.getVersion());
                jarInfo.setJvm(ServerMain.getArtifact(runtimeDir, new File(
                        runtimeDir, jarInfo.getJvm().getPath()
                )));
                jarInfo.setJar(ServerMain.getArtifact(versionDir, new File(
                        versionDir, clientInfo.getName() + ".jar"
                )));
                jarInfo.setJson(ServerMain.getArtifact(versionDir, new File(
                        versionDir, clientInfo.getName() + ".json"
                )));
            }
            return StaticVerifier.verify(clientInfo) ? clientInfo : null;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ClientInfo[] list(File runtimeDir, File versionsDir) {
        List<ClientInfo> infoList = new ArrayList<>();
        Arrays.stream(Objects.requireNonNull(versionsDir.listFiles()))
                .filter(File::isDirectory)
                .forEach(clientDir -> infoList.add(get(
                        runtimeDir, versionsDir, clientDir
                )));
        return infoList.toArray(new ClientInfo[0]);
    }

}
