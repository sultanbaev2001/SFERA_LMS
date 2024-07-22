package sfera.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import sfera.entity.File;


import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByFileName(String fileName);
    List<File> findAllById(Long id);
}
