package fr.n7.spring_boot_api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.security.access.prepost.PreAuthorize;

import fr.n7.spring_boot_api.model.File;
import fr.n7.spring_boot_api.service.FileStorageService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class FileController {

  @Autowired
  private FileStorageService storageService;

  // Upload file
  @PostMapping("/file/upload")
  @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")

  public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
    try {
      storageService.store(file);
      return new ResponseEntity<>(file .getOriginalFilename(), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(file.getOriginalFilename(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  // Get all files
  @GetMapping("/files")
  @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
  public ResponseEntity<List<File>> getListFiles() {
    List<File> files = storageService.getAllFiles().collect(Collectors.toList());
    return new ResponseEntity<>(files, HttpStatus.OK);
    
  }
  
  @DeleteMapping("/file/delete/{id}")
  @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
  public ResponseEntity<String> deleteFile(@PathVariable String id) {
      try {
          boolean isDeleted = storageService.deleteFile(id);
          if (isDeleted) {
              return new ResponseEntity<>("File deleted successfully", HttpStatus.OK);
          } else {
              return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);
          }
      } catch (Exception e) {
          return new ResponseEntity<>("Error deleting file", HttpStatus.INTERNAL_SERVER_ERROR);
      }
  }

  // Get file by ID
  @GetMapping("/getfile/{id}")
  @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
  public ResponseEntity<byte[]> getFile(@PathVariable String id) {
    File file = storageService.getFile(id);

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
        .body(file.getData());
  }
}