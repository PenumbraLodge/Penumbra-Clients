package me.paolo.manager;

import me.paolo.ServerMain;
import me.paolo.client.ArtifactStruct;
import me.paolo.client.ClientInfo;
import me.paolo.client.JarInfo;
import me.paolo.util.IOUtil;
import me.paolo.util.JsonUtil;

import java.io.ByteArrayOutputStream;
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

    private static ArtifactStruct createArtifact(ClientInfo clientInfo, String version, File file) {
        ArtifactStruct artifact = new ArtifactStruct();
        artifact.setPath(clientInfo.getName() + '/' + file.getName());
        artifact.setSize(file.length());
        artifact.setUrl(String.format("%s/versions/%s/%s/%s",
                ServerMain.REPOSITORY_URL, clientInfo.getName(), version, file.getName()
        ));

        try(FileInputStream inputStream = new FileInputStream(file);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            IOUtil.storeSha1Checksum(inputStream, outputStream);
            artifact.setSha1(IOUtil.toHexString(outputStream.toByteArray()));
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return artifact;
    }

    private static ClientInfo get(File clientDir) {
        try(FileInputStream inputStream = new FileInputStream(new File(clientDir, "config.json"))) {
            ClientInfo clientInfo = JsonUtil.fromJson(inputStream, ClientInfo.class);

            String[] previews = clientInfo.getPreviewUrls();
            for(int i = 0; i < previews.length; i++) {
                previews[i] = String.format("%s/versions/%s/%s",
                        ServerMain.REPOSITORY_URL, clientDir.getName(), previews[i]);
            }

            List<JarInfo> jars = new ArrayList<>();
            Arrays.stream(Objects.requireNonNull(clientDir.listFiles()))
                    .filter(File::isDirectory)
                    .forEach(file -> {
                        JarInfo jarInfo = new JarInfo();
                        jarInfo.setJar(createArtifact(clientInfo, file.getName(), new File(
                                file, clientInfo.getName() + ".jar"
                        )));

                        jarInfo.setJson(createArtifact(clientInfo, file.getName(), new File(
                                file, clientInfo.getName() + ".json"
                        )));
                        jarInfo.setVersion(file.getName());
                        jars.add(jarInfo);
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
