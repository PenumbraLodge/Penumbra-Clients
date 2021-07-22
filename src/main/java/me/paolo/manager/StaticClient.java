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

    private static ClientInfo get(File clientDir) {
        File rootDir = clientDir.getParentFile();
        if(rootDir == null) {
            System.err.println("Parent directory is null. This shouldn't happen!");
            return null;
        }

        try(FileInputStream inputStream = new FileInputStream(new File(clientDir, "config.json"))) {
            ClientInfo clientInfo = JsonUtil.fromJson(inputStream, ClientInfo.class);

            String[] previews = clientInfo.getPreviewUrls();
            for(int i = 0; i < previews.length; i++) {
                previews[i] = Objects.requireNonNull(
                        ServerMain.getArtifact(rootDir, new File(clientDir, previews[i]))
                ).getUrl();
            }

            List<JarInfo> jars = new ArrayList<>();
            Arrays.stream(Objects.requireNonNull(clientDir.listFiles()))
                    .filter(File::isDirectory)
                    .forEach(verionDir -> {
                        JarInfo jarInfo = new JarInfo();
                        jarInfo.setJar(ServerMain.getArtifact(rootDir, new File(
                                verionDir, clientInfo.getName() + ".jar"
                        )));
                        jarInfo.setJson(ServerMain.getArtifact(rootDir, new File(
                                verionDir, clientInfo.getName() + ".json"
                        )));
                        jarInfo.setVersion(verionDir.getName());
                        jars.add(jarInfo);

                        System.out.println("Imported: \"" + clientInfo.getName() + '-' + jarInfo.getVersion() + "\"");
                    });
            clientInfo.setJars(jars.toArray(new JarInfo[0]));
            return clientInfo;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ClientInfo[] list(File versionsDir) {
        List<ClientInfo> infoList = new ArrayList<>();
        Arrays.stream(Objects.requireNonNull(versionsDir.listFiles()))
                .filter(File::isDirectory)
                .forEach(file -> infoList.add(get(file)));
        return infoList.toArray(new ClientInfo[0]);
    }

}
