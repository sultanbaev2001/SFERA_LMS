package sfera.controller;

import sfera.entity.VideoFile;
import sfera.service.VideoFileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@RequestMapping("/api/videos")
public class VideoUploadController {

    private final VideoFileService videoFileService;

    public VideoUploadController(VideoFileService videoFileService) {
        this.videoFileService = videoFileService;
    }

    @PostMapping(value = "/upload",consumes = {"multipart/form-data"})
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file) {
        try {
            VideoFile videoFile = videoFileService.saveFile(file);
            return new ResponseEntity<>("File uploaded successfully: " + videoFile.getFileName(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error occurred while uploading file", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/files/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        try {
            Optional<Resource> resourceOptional = videoFileService.loadFileAsResource(filename);
            if (resourceOptional.isPresent()) {
                Resource resource = resourceOptional.get();
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(Files.probeContentType(Paths.get(resource.getURI()))))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                        .body(resource);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
