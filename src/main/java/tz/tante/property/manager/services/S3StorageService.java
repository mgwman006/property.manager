package tz.tante.property.manager.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import tz.tante.property.manager.exceptions.TanteException;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3StorageService
{
  private static final long MAX_FILE_SIZE_BYTES = 10L * 1024 * 1024;

  private final S3Client s3Client;

  @Value("${app.storage.s3.bucket:property-manager-images}")
  private String bucketName;

  @Value("${app.storage.s3.folder:properties}")
  private String folder;

  @Value("${app.storage.s3.public-url-base:}")
  private String publicUrlBase;

  public S3StorageService(S3Client s3Client)
  {
    this.s3Client = s3Client;
  }

  public StoredImage uploadFile(MultipartFile file, String propertyCode)
  {
    if (file == null || file.isEmpty())
    {
      throw new TanteException("Property image file is required");
    }

    if (file.getSize() > MAX_FILE_SIZE_BYTES)
    {
      throw new TanteException("Property image must be smaller than 10MB");
    }

    String contentType = file.getContentType();
    if (contentType == null || !contentType.startsWith("image/"))
    {
      throw new TanteException("Only image files are allowed");
    }

    String originalFilename = StringUtils
      .cleanPath(file.getOriginalFilename() == null ? "property-image" : file.getOriginalFilename());
    String extension = getFileExtension(originalFilename);
    String objectKey = String.format("%s/%s/%s-%d%s",
      folder,
      propertyCode,
      UUID.randomUUID(),
      System.currentTimeMillis(),
      extension.isBlank() ? "" : "." + extension
    );

    try
    {
      PutObjectRequest request = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(objectKey)
        .contentType(contentType)
        .build();

      s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
    }
    catch (IOException exception)
    {
      throw new TanteException("Unable to upload property image to S3: " + exception.getMessage());
    }

    return new StoredImage(
      objectKey,
      resolveObjectUrl(objectKey),
      originalFilename,
      contentType,
      file.getSize()
    );
  }

  public void deleteFile(String objectKey)
  {
    if (objectKey == null || objectKey.isBlank())
    {
      return;
    }

    s3Client.deleteObject(DeleteObjectRequest.builder()
      .bucket(bucketName)
      .key(objectKey)
      .build());
  }

  private String resolveObjectUrl(String objectKey)
  {
    if (publicUrlBase != null && !publicUrlBase.isBlank())
    {
      return publicUrlBase.endsWith("/")
        ? publicUrlBase + objectKey
        : publicUrlBase + "/" + objectKey;
    }

    return s3Client.utilities().getUrl(builder -> builder.bucket(bucketName).key(objectKey)).toString();
  }

  private String getFileExtension(String fileName)
  {
    if (fileName == null || fileName.isBlank())
    {
      return "";
    }

    int dotIndex = fileName.lastIndexOf('.');
    if (dotIndex < 0 || dotIndex == fileName.length() - 1)
    {
      return "";
    }

    return fileName.substring(dotIndex + 1);
  }

  public record StoredImage(
    String objectKey,
    String url,
    String fileName,
    String contentType,
    long size
  )
  {
  }
}
