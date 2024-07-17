package sfera.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sfera.entity.VideoFile;

import java.util.List;
import java.util.Optional;

public interface VideoFileRepository extends JpaRepository<VideoFile, Long> {
    Optional<VideoFile> findByFileName(String fileName);
    List<VideoFile> findAllByFileName(String fileName);
}
