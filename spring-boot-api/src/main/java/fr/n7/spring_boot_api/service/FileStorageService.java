package fr.n7.spring_boot_api.service;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import fr.n7.spring_boot_api.model.File;
import fr.n7.spring_boot_api.repository.FileRepository;

@Service
public class FileStorageService {

  @Autowired
  private FileRepository fileRepository;

  public File store(MultipartFile file) throws IOException {
    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
    File File = new File(fileName, file.getContentType(), file.getBytes());

    return fileRepository.save(File);
  }

  public File getFile(String id) {
    Optional<File> file = fileRepository.findById(id);
    if (!file.isPresent()) {
      throw new RuntimeException("File not found with id " + id);
    }
    return file.get();
  }
  
  public Stream<File> getAllFiles() {
    return fileRepository.findAll().stream();
  }
}
