package sfera.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sfera.entity.VideoFile;
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
    @Value("${file.upload-dir3}")
    private String uploadDir3;

    private final VideoFileRepository videoFileRepository;


//    SaveFile uchun
    public VideoFile saveFile(MultipartFile file) throws IOException {
        if (Objects.requireNonNull(file.getOriginalFilename()).endsWith(".mp4") ||
                file.getOriginalFilename().endsWith(".avi") ||
                file.getOriginalFilename().endsWith(".mkv")) {


            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            VideoFile videoFile = new VideoFile();
            videoFile.setFileName(file.getOriginalFilename());
            videoFile.setFilepath(filePath.toString());

            return videoFileRepository.save(videoFile);
        } else if (file.getOriginalFilename().endsWith(".png") ||
                file.getOriginalFilename().endsWith(".jpg")||
                file.getOriginalFilename().endsWith(".webp")) {


            Path uploadPath = Paths.get(uploadDir3);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            VideoFile videoFile = new VideoFile();
            videoFile.setFileName(file.getOriginalFilename());
            videoFile.setFilepath(filePath.toString());

            return videoFileRepository.save(videoFile);
        }else {
            Path uploadPath = Paths.get(uploadDir2);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            VideoFile videoFile = new VideoFile();
            videoFile.setFileName(file.getOriginalFilename());
            videoFile.setFilepath(filePath.toString());

            return videoFileRepository.save(videoFile);
        }

    }

//    GetFile uchun
    public Optional<Resource> loadFileAsResource(String filename) throws MalformedURLException {
        Optional<VideoFile> videoFileOptional = videoFileRepository.findByFileName(filename);
        if (videoFileOptional.isPresent()) {
            Path filePath = Paths.get(videoFileOptional.get().getFilepath()).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return Optional.of(resource);
            }
        }
        return Optional.empty();
    }
}
