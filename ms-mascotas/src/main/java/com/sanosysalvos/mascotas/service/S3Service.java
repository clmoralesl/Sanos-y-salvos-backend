package com.sanosysalvos.mascotas.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class S3Service {

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.credentials.access-key-id}")
    private String accessKeyId;

    @Value("${aws.credentials.secret-access-key}")
    private String secretAccessKey;

    private S3Presigner getPresigner() {
        return S3Presigner.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKeyId, secretAccessKey)
                ))
                .build();
    }

    public Map<String, String> generarUrlFirmada(String tipo, String contentType) {
        String carpeta = "otros/";
        if ("mascota".equalsIgnoreCase(tipo)) {
            carpeta = "mascotas/";
        } else if ("perfil".equalsIgnoreCase(tipo)) {
            carpeta = "perfiles/";
        }

        String extension = "jpg";
        if (contentType != null && contentType.contains("/")) {
            extension = contentType.split("/")[1];
        }

        String fileName = carpeta + UUID.randomUUID().toString() + "." + extension;

        try (S3Presigner presigner = getPresigner()) {
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(contentType)
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(15))
                    .putObjectRequest(objectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = presigner.presignPutObject(presignRequest);
            String uploadUrl = presignedRequest.url().toString();
            
            // Public URL of the object on S3 once uploaded
            String publicUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName);

            Map<String, String> response = new HashMap<>();
            response.put("uploadUrl", uploadUrl);
            response.put("publicUrl", publicUrl);
            response.put("key", fileName);

            log.info("Generada URL pre-firmada para subir a: {}", fileName);
            return response;
        } catch (Exception e) {
            log.error("Error al generar URL pre-firmada: {}", e.getMessage());
            throw new RuntimeException("No se pudo generar la URL de subida", e);
        }
    }
}
