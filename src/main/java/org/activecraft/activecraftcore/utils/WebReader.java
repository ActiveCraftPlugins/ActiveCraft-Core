package org.activecraft.activecraftcore.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebReader {

    public static HashMap<String, Integer> acVersionMap = new HashMap<>();
    public static long lastACVersionUpdate = 0;

    public static HashMap<String, Integer> getACVersionMap() {
        if (System.currentTimeMillis() - lastACVersionUpdate < 600000) return acVersionMap;
        HashMap<String, Integer> map = new HashMap<>();
        Map<?, ?> rawMap;
        try {
            rawMap = readAsMap(new URL("https://raw.githubusercontent.com/CPlaiz/ActiveCraft-Core/master/plugins.json"));
        } catch (IOException e) {
            return acVersionMap;
        }
        for (Object key : rawMap.keySet())
            map.put(key.toString(), (Integer) rawMap.get(key.toString()));
        lastACVersionUpdate = System.currentTimeMillis();
        return acVersionMap = map;
    }

    public static Map<?, ?> readAsMap(URL url) throws IOException {
        return new ObjectMapper().readValue(url, Map.class);
    }

    public static List<?> readAsList(URL url) throws IOException {
        return new ObjectMapper().readValue(url, List.class);
    }

    public static String readAsString(URL url) throws IOException {
        URLConnection conn = url.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        conn.getInputStream()));
        StringBuffer buffer = new StringBuffer();
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            buffer.append(inputLine);
        in.close();
        return buffer.toString();
    }

    public static void downloadFile(URL url, String localFilename) throws IOException {
        ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(localFilename);
        fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
    }
}