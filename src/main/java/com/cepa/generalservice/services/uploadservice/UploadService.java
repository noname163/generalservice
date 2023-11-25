package com.cepa.generalservice.services.uploadservice;

import java.util.List;

import javax.mail.Multipart;

import org.springframework.web.multipart.MultipartFile;

import com.cepa.generalservice.data.dto.response.CloudinaryUrl;
import com.cepa.generalservice.data.dto.response.FileResponse;



public interface UploadService {
    public CloudinaryUrl uploadMedia(MultipartFile fileResponse);
    public CloudinaryUrl uploadMaterial(FileResponse file);
    public String createUrlById(String publicId, String mediaType, String subfixType);
}
