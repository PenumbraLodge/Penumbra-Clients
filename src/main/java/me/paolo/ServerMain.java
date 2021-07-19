package me.paolo;

import me.paolo.client.ClientInfo;
import me.paolo.client.JarInfo;
import me.paolo.manager.StaticClient;
import me.paolo.util.IOUtil;
import me.paolo.util.JsonUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;

public class ServerMain
{

    public static final String REPOSITORY_URL = "https://raw.githubusercontent.com/PenumbraLodge/Penumbra-Clients/master/server";

    private ServerMain() { }

    public static void main(String[] args) {
        { // Import all clients
            File versionsDir = new File("./versions");
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
            }
        }
    }

}
