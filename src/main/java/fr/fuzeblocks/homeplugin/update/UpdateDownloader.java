package fr.fuzeblocks.homeplugin.update;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.fuzeblocks.homeplugin.HomePlugin;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UpdateDownloader {

    private final String project_url = "https://api.modrinth.com/v2/project/kbERwJ64";
    private final String version_url = "https://api.modrinth.com/v2/version/";

    private final HttpClient client = HttpClient.newHttpClient();

    public UpdateDownloader() {

        Bukkit.getScheduler().runTaskAsynchronously(HomePlugin.getPlugin(HomePlugin.class), () -> {
            System.out.println("List of available versions:");
            List<String> versions = getVersions();
            System.out.println(versions);
            String latestVersion = getVersion(versions);
             System.out.println(latestVersion);
            if (latestVersion == null) return;

            String fileUrl = getFileUrl(latestVersion);
            System.out.println(fileUrl);
            if (fileUrl != null) {
                downloadFile(fileUrl, "plugins/download/HomePlugin.jar");
            }
        });
    }

    public List<String> getVersions() {
        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(project_url))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {

                JsonParser parser = new JsonParser();
                JsonObject json = parser.parse(response.body()).getAsJsonObject();
                JsonArray versionsArray = json.getAsJsonArray("versions");

                if (versionsArray != null && versionsArray.size() > 0) {
                    List<String> versions = new ArrayList<>();
                    for (int i = 0; i < versionsArray.size(); i++) {
                        versions.add(versionsArray.get(i).getAsString());
                    }
                    return versions;
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getVersion(List<String> versions) {
        if (versions == null || versions.isEmpty()) return null;
        Collections.reverse(versions);
        for (String version : versions) {
            if (isPrimary(version)) {
                return version;
            }
        }
        return null;
    }

    public boolean isPrimary(String version) {

        try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(version_url + version))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {

                JsonParser parser = new JsonParser();
                JsonObject json = parser.parse(response.body()).getAsJsonObject();

                JsonArray files = json.getAsJsonArray("files");

                if (files != null && files.size() > 0) {
                    JsonObject file = files.get(0).getAsJsonObject();
                    return file.get("primary").getAsBoolean();
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }

    public String getFileUrl(String version) {
         try {

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(version_url + version))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {

                JsonParser parser = new JsonParser();
                JsonObject json = parser.parse(response.body()).getAsJsonObject();

                JsonArray files = json.getAsJsonArray("files");

                if (files != null && files.size() > 0) {
                    JsonObject file = files.get(0).getAsJsonObject();
                    return file.get("url").getAsString();
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void downloadFile(String fileUrl, String destination) {

        try {

            Path path = Path.of(destination);

            Files.createDirectories(path.getParent());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(fileUrl))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofFile(path));

            System.out.println("Download completed: " + destination);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}