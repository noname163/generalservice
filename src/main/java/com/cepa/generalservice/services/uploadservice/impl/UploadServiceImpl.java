package com.cepa.generalservice.services.uploadservice.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cepa.generalservice.data.dto.response.CloudinaryUrl;
import com.cepa.generalservice.data.dto.response.FileResponse;
import com.cepa.generalservice.data.object.MediaType;
import com.cepa.generalservice.exceptions.BadRequestException;
import com.cepa.generalservice.exceptions.MediaUploadException;
import com.cepa.generalservice.services.uploadservice.UploadService;
import com.cepa.generalservice.utils.EnvironmentVariables;
import com.cepa.generalservice.utils.StringUtil;
import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class UploadServiceImpl implements UploadService {
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private EnvironmentVariables environmentVariable;
    @Autowired
    private StringUtil stringUtil;

    @Override
    public CloudinaryUrl uploadMedia(MultipartFile file) {
        try {
            // Check if the content type is supported
            String contentType = file.getContentType();
            if (contentType == null || !environmentVariable.initializeAllowedContentTypes().containsKey(contentType)) {
                throw new BadRequestException("Unsupported file type. Supported types are: "
                        + String.join(", ", environmentVariable.initializeAllowedContentTypes().values()));
            }

            MediaType mediaType = stringUtil.convertStringToMediaType(contentType);
            // Define upload options
            Map<String, String> options = new HashMap<>();
            options.put("resource_type", "auto");

            log.info("Start uploading file: {}", file.getOriginalFilename());
            // Perform the upload
            var uploadResult = cloudinary.uploader().upload(file.getBytes(), options);

            // Extract information from the upload result
            String publicId = uploadResult.get("public_id").toString();
            String url = createUrlById(publicId, mediaType.getMediaType(), mediaType.getSubfix());
            log.info("Upload successful. URL: {}", url);

            // Create and return a CloudinaryUrl object
            return CloudinaryUrl.builder()
                    .url(url)
                    .publicId(publicId)
                    .build();
        } catch (IOException e) {
            throw new MediaUploadException("Failed to upload media "+ e.getMessage());
        }
    }


    @Override
    public String createUrlById(String id, String mediaType, String subfixType) {
        return cloudinary.url()
                .resourceType(mediaType)
                .generate(id + "." + subfixType);
    }

    @Override
    public CloudinaryUrl uploadMaterial(FileResponse file) {
        try {
            // Check if the content type is supported
            String contentType = file.getContentType();
            if (contentType == null || !contentType.equals("application/pdf")) {
                throw new BadRequestException("Unsupported file type. Supported types are: PDF, DOCX");
            }

            MediaType mediaType = stringUtil.convertStringToMediaType(contentType);
            // Define upload options
            Map<String, String> options = new HashMap<>();
            options.put("resource_type", "auto");

            log.info("Start uploading file: {}", file.getFileName());
            // Perform the upload
            var uploadResult = cloudinary.uploader().upload(file.getFileStorage(), options);

            // Extract information from the upload result
            String publicId = uploadResult.get("public_id").toString();
            String url = createUrlById(publicId, mediaType.getMediaType(), mediaType.getSubfix());

            log.info("Upload successful. URL: {}", url);

            // Create and return a CloudinaryUrl object
            return CloudinaryUrl.builder()
                    .url(url)
                    .publicId(publicId)
                    .build();
        } catch (IOException e) {
            throw new MediaUploadException("Failed to upload media", e);
        }
    }
}
