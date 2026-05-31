package com.example.demo.service.image;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageService {
   @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * Guarda el archivo y devuelve el nombre (o ruta) de la imagen.
     */
    public String storeImage(MultipartFile file) throws IOException {
        // Asegúrate de que el directorio existe.
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        // Genera un nombre único para el archivo.
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return fileName; // O puedes retornar filePath.toString() si prefieres la ruta completa.
    }

    /**
     * Descarga una imagen desde una URL remota y la almacena en disco.
     * Devuelve el nombre local generado.
     */
    public String storeImageFromUrl(String imageUrl) throws IOException {
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        URL url = new URL(imageUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setInstanceFollowRedirects(true);
        connection.setConnectTimeout(10_000);
        connection.setReadTimeout(10_000);

        int responseCode = connection.getResponseCode();
        if (responseCode < 200 || responseCode >= 300) {
            throw new IOException("No se pudo descargar la imagen. HTTP " + responseCode);
        }

        String contentType = connection.getContentType();
        String extension = resolveExtension(contentType, imageUrl);
        String fileName = UUID.randomUUID().toString() + extension;

        Path filePath = Paths.get(uploadDir, fileName);
        try (InputStream inputStream = connection.getInputStream()) {
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } finally {
            connection.disconnect();
        }

        return fileName;
    }

    private String resolveExtension(String contentType, String fallbackUrl) {
        if (contentType != null) {
            String normalized = contentType.toLowerCase();
            if (normalized.contains("png")) {
                return ".png";
            }
            if (normalized.contains("webp")) {
                return ".webp";
            }
            if (normalized.contains("gif")) {
                return ".gif";
            }
            if (normalized.contains("jpeg") || normalized.contains("jpg")) {
                return ".jpg";
            }
        }

        String urlLower = fallbackUrl != null ? fallbackUrl.toLowerCase() : "";
        if (urlLower.contains(".png")) return ".png";
        if (urlLower.contains(".webp")) return ".webp";
        if (urlLower.contains(".gif")) return ".gif";
        return ".jpg";
    }
}
