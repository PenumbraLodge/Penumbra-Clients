package me.paolo;

import me.paolo.client.ArtifactStruct;
import me.paolo.client.ClientInfo;
import me.paolo.client.JarInfo;
import me.paolo.manager.StaticClient;
import me.paolo.util.IOUtil;
import me.paolo.util.JsonUtil;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;

public class ServerMain
{

    public static final String REPOSITORY_URL = "https://raw.githubusercontent.com/PenumbraLodge/Penumbra-Clients/master/server";

    private ServerMain() { }

    public static void main(String[] args) {
        if(args.length < 2) {
            System.out.println("Usage: <versions_dir> <update_file>");
            return;
        }

        { // Import all clients (First part)
            File versionsDir = new File(args[0]);
            if(!versionsDir.exists()) {
                System.err.println("Versions dir not found!");
                return;
            }

            System.out.println("Importing clients...");
            ClientInfo[] clientList = StaticClient.list(versionsDir);
            Arrays.sort(clientList, Comparator.comparing(ClientInfo::getName));

            Arrays.stream(clientList).forEach(clientInfo ->
                    Arrays.sort(clientInfo.getJars(), Comparator.comparing(JarInfo::getVersion)));

            try(FileOutputStream outputStream = new FileOutputStream(
                    IOUtil.createFile(new File(versionsDir, "list.json"), false)
            )) {
                String json = JsonUtil.toJsonStr(clientList);
                outputStream.write(json.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            }
            catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        System.out.println();
        { // Import update file (Second part)
            File updateFile = new File(new File(args[1]).getAbsolutePath());
            if(!updateFile.exists()) {
                System.err.println("Update file not found!");
                return;
            }

            File updateParent = updateFile.getParentFile();
            if(updateParent == null) {
                System.err.println("Parent cannot be null!");
                return;
            }

            System.out.println("Importing update file...");
            ArtifactStruct artifact = getArtifact(updateFile, updateFile);
            try(FileOutputStream outputStream = new FileOutputStream(
                    IOUtil.createFile(new File(updateParent, "Penumbra Launcher.json"), false)
            )) {
                String json = JsonUtil.toJsonStr(artifact);
                outputStream.write(json.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static ArtifactStruct getArtifact(File rootDir, File file) {
        String rootAbsolute = rootDir.getAbsolutePath().replace('\\', '/');
        String fileAbsolute = file.getAbsolutePath().replace('\\', '/');

        String parentAbsolute = new File(rootAbsolute).getParentFile()
                .getAbsolutePath().replace('\\', '/');

        ArtifactStruct artifact = new ArtifactStruct();
        artifact.setPath((rootAbsolute.equals(fileAbsolute) ?
                fileAbsolute.substring(parentAbsolute.length() + 1) : fileAbsolute.substring(rootAbsolute.length() + 1)));
        artifact.setUrl(REPOSITORY_URL + '/' + fileAbsolute.substring(parentAbsolute.length() + 1));
        artifact.setSize(file.length());

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

}
