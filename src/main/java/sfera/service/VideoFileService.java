package sfera.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sfera.entity.VideoFile;
import sfera.exception.NotFoundException;
import sfera.payload.ApiResponse;
import sfera.repository.VideoFileRepository;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VideoFileService {

    @Value("${file.upload-dir1}")
    private String uploadDir;
    @Value("${file.upload-dir2}")
    private String uploadDir2;


    private final VideoFileRepository videoFileRepository;


    private static final Path root= Paths.get("src/main/resources/");


    public ApiResponse saveFile(MultipartFile file)
    {
        String director = checkingAttachmentType(file);
        if (director == null) {
            return new ApiResponse("File yuklash uchun papka topilmadi", HttpStatus.NOT_FOUND);
        }

        Path resolve = root.resolve(director + "/" + file.getOriginalFilename());
        try {
            Files.copy(file.getInputStream(), resolve);
            VideoFile videoFile = new VideoFile();
            videoFile.setFileName(file.getOriginalFilename());
            videoFile.setFilepath(root.resolve(director + "/" + file.getOriginalFilename()).toString());
            videoFileRepository.save(videoFile);
        }catch (IOException e){
            throw new NotFoundException(e.getMessage());
        }
        return new ApiResponse("Success", HttpStatus.OK);
    }

    public String checkingAttachmentType(MultipartFile file)
    {
        if (Objects.requireNonNull(file.getOriginalFilename()).endsWith(".mp4") ||
                file.getOriginalFilename().endsWith(".avi") ||
                file.getOriginalFilename().endsWith(".mkv")) {
            return "video";
        }else if(file.getOriginalFilename().endsWith(".png") ||
                file.getOriginalFilename().endsWith(".jpg")||
                file.getOriginalFilename().endsWith(".webp")) {
            return "img";
        }
        return null;
    }

//    GetFile uchun
    public Resource loadFileAsResource(Long id) throws MalformedURLException {
        Optional<VideoFile> videoFileOptional = videoFileRepository.findById(id);
        if (videoFileOptional.isPresent()) {
            Path filePath = Paths.get(videoFileOptional.get().getFilepath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            }
        }
        return null;
    }


//    update
public VideoFile updateFile(Long id, MultipartFile file) throws IOException {
    Optional<VideoFile> existingVideoFile = videoFileRepository.findById(id);
    if (existingVideoFile.isPresent()) {
        VideoFile videoFile = existingVideoFile.get();
        Path oldFilePath = Paths.get(videoFile.getFilepath());
        Files.deleteIfExists(oldFilePath);

        String filename = file.getOriginalFilename();
        Path uploadPath;
        if (Objects.requireNonNull(filename).endsWith(".mp4") ||
                filename.endsWith(".avi") ||
                filename.endsWith(".mkv")) {
            uploadPath = Paths.get(uploadDir);
        } else if (filename.endsWith(".png") ||
                filename.endsWith(".jpg") ||
                filename.endsWith(".webp")) {
            uploadPath = Paths.get("root/img");
        } else {
            uploadPath = Paths.get(uploadDir2);
        }

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        videoFile.setFileName(filename);
        videoFile.setFilepath(filePath.toString());

        return videoFileRepository.save(videoFile);
    } else {
        throw new IOException("File not found");
    }
}


//delete file
public ApiResponse deleteFile(Long id) throws IOException {
    Optional<VideoFile> existingVideoFile = videoFileRepository.findById(id);
    if (existingVideoFile.isPresent()) {
        VideoFile videoFile = existingVideoFile.get();
        Path filePath = Paths.get(videoFile.getFilepath());
        Files.deleteIfExists(filePath);
        videoFileRepository.delete(videoFile);
        return new ApiResponse("Successfully deleted",HttpStatus.OK);
    } else {
        throw new IOException("File not found");
    }
}
}
