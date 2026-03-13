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
import java.nio.file.StandardCopyOption;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public class UpdateDownloader {

    private final String project_url = "https://api.modrinth.com/v2/project/kbERwJ64/version";
    private final HttpClient client = HttpClient.newHttpClient();

    public void computeLogged(String destination) {
        Bukkit.getScheduler().runTaskAsynchronously(HomePlugin.getPlugin(HomePlugin.class), () -> {
            System.out.println("Checking for new versions...");

            String currentVersion = HomePlugin.getPlugin(HomePlugin.class)
                    .getDescription()
                    .getVersion();

            String latestVersion = getLatestVersion();
            if (latestVersion == null) {
                System.out.println("No version found!");
                return;
            }

            if (latestVersion.equals(currentVersion)) {
                System.out.println("Plugin is already up to date (" + currentVersion + ")");
                return;
            }

            System.out.println("Latest version: " + latestVersion);

            String fileUrl = getFileUrl(latestVersion);
            String expectedHash = getFileSha512(latestVersion);

            if (fileUrl == null || expectedHash == null) {
                System.out.println("Could not retrieve file URL or hash");
                return;
            }

            try {
                Path tempPath = Path.of(destination + ".tmp");
                downloadFile(fileUrl, tempPath);

                if (checkSum(tempPath, expectedHash)) {
                    Files.move(tempPath, Path.of(destination), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Plugin updated successfully to " + latestVersion);
                } else {
                    System.out.println("Checksum FAILED. Download discarded.");
                    Files.deleteIfExists(tempPath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private String getLatestVersion() {
        List<String> versions = new ArrayList<>();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(project_url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonParser parser = new JsonParser();
                JsonArray jsonVersions = parser.parse(response.body()).getAsJsonArray();

                for (int i = 0; i < jsonVersions.size(); i++) {
                    JsonObject v = jsonVersions.get(i).getAsJsonObject();
                    JsonArray files = v.getAsJsonArray("files");
                    if (files != null && files.size() > 0) {
                        JsonObject file = files.get(0).getAsJsonObject();
                        boolean primary = file.get("primary").getAsBoolean();
                        if (primary) {
                            versions.add(v.get("id").getAsString());
                        }
                    }
                }

                if (!versions.isEmpty()) {
                    return versions.get(0);
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getFileUrl(String versionId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.modrinth.com/v2/version/" + versionId))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

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

    private String getFileSha512(String versionId) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.modrinth.com/v2/version/" + versionId))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonParser parser = new JsonParser();
                JsonObject json = parser.parse(response.body()).getAsJsonObject();
                JsonArray files = json.getAsJsonArray("files");

                if (files != null && files.size() > 0) {
                    JsonObject file = files.get(0).getAsJsonObject();
                    JsonObject hashes = file.getAsJsonObject("hashes");
                    return hashes.get("sha512").getAsString();
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void downloadFile(String fileUrl, Path destination) {
        try {
            Files.createDirectories(destination.getParent());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(fileUrl))
                    .build();

            client.send(request, HttpResponse.BodyHandlers.ofFile(destination));

            System.out.println("Download completed: " + destination);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean checkSum(Path file, String originalHash) {
        String computed = computeSha512(file);
        return computed != null && computed.equalsIgnoreCase(originalHash);
    }

    private String computeSha512(Path file) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            try (DigestInputStream dis = new DigestInputStream(Files.newInputStream(file), digest)) {
                byte[] buffer = new byte[8192];
                while (dis.read(buffer) != -1) ;
            }

            byte[] hash = digest.digest();
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}