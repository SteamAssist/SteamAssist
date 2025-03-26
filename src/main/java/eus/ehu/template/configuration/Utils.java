package eus.ehu.template.configuration;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class Utils {



    public static String query(String url) throws IOException {

        final OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static String readFile(String fileName) throws IOException {
        URL fileURL = Utils.class.getResource("/eus/ehu/" + fileName);
        if (fileURL == null) {
            throw new IOException("File not found: " + fileName);
        }
        try {
            return new String(Files.readAllBytes(Paths.get(fileURL.toURI())));
        } catch (URISyntaxException e) {
            throw new IOException("Invalid URI syntax: " + fileURL, e);
        }
    }
}
