package me.paolo.util;

import lombok.experimental.UtilityClass;

import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@UtilityClass
public class IOUtil
{

    public static String toHexString(byte[] data) {
        StringBuilder builder = new StringBuilder();
        for (byte b : data) builder.append(String.format("%02x", b));
        return builder.toString();
    }

    public static void storeSha1Checksum(InputStream inputStream, OutputStream outputStream) throws IOException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            store(new DigestInputStream(inputStream, messageDigest), null);
            outputStream.write(messageDigest.digest());
            outputStream.flush();
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static File createFile(File file, boolean directory) throws IOException {
        createFileIfNotExists(file, directory);
        return file;
    }

    public static File createFileIfNotExists(File file, boolean directory) throws IOException {
        if(file.exists() && file.isDirectory() == directory) return null;
        if(directory) return file.mkdirs() ? file : null;

        File parent = file.getParentFile();
        if(parent != null && !parent.exists()) parent.mkdirs();
        return file.createNewFile() ? file : null;
    }

    public static String getStackTrace(Throwable t) {
        try(StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter)) {
            t.printStackTrace(printWriter);
            return stringWriter.toString();
        }
        catch (IOException e) {
            return "null";
        }
    }

    public static byte[] readFully(InputStream inputStream) throws IOException {
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            IOUtil.store(inputStream, outputStream);
            return outputStream.toByteArray();
        }
    }

    public static void store(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] data = new byte[1024];

        int read;
        while((read = inputStream.read(data)) != -1) {
            if(outputStream != null)
                outputStream.write(data, 0, read);
        }

        if(outputStream != null)
            outputStream.flush();
    }

}
