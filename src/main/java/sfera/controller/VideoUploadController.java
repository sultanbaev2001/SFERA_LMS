package sfera.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import sfera.entity.VideoFile;
import sfera.payload.ApiResponse;
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


    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER')")
    @PostMapping(value = "/upload",consumes = {"multipart/form-data"})
    public ResponseEntity<ApiResponse> uploadVideo(@RequestParam("file") MultipartFile file) {
        try {
            ApiResponse videoFile = videoFileService.saveFile(file);
            return ResponseEntity.status(videoFile.getStatus()).body(videoFile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER')")
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


    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER')")
    @PutMapping(value = "/update/{id}",consumes = {"multipart/form-data"})
    public ResponseEntity<VideoFile> updateFile(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            VideoFile updatedFile = videoFileService.updateFile(id, file);
            return ResponseEntity.ok(updatedFile);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PreAuthorize("hasRole('ROLE_STUDENT') or hasRole('ROLE_TEACHER')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteFile(@PathVariable Long id) {
        try {
            ApiResponse apiResponse = videoFileService.deleteFile(id);
            return ResponseEntity.status(apiResponse.getStatus()).body(apiResponse);
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }
}
