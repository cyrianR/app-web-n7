package fr.n7.spring_boot_api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.n7.spring_boot_api.model.Lesson;
import fr.n7.spring_boot_api.repository.LessonRepository;

// filter authorized origin
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class LessonController {
    
    @Autowired
    LessonRepository lessonRepository;

    // Get lesson by ID
    @GetMapping("/lesson/{id}")
    public ResponseEntity<Lesson> getLessonById(@PathVariable("id") long id) {
        Optional<Lesson> lessonData = lessonRepository.findById(id);
        if (lessonData.isPresent()) {
            return new ResponseEntity<>(lessonData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Get lessons by title
    @GetMapping("/lesson/title/{title}")
    public ResponseEntity<List<Lesson>> getLessonsByTitle(@PathVariable("title") String title) {
        List<Lesson> lessons = lessonRepository.findByTitle(title);
        if (lessons.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(lessons, HttpStatus.OK);
        }
    }

    // Get all lessons
    @GetMapping("/lesson")
    public ResponseEntity<List<Lesson>> getAllLessons() {
        List<Lesson> lessons = lessonRepository.findAll();
        if (lessons.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(lessons, HttpStatus.OK);
        }
    }

    // Get all lessons ordered by orderNum
    @GetMapping("/lesson/order")
    public ResponseEntity<List<Lesson>> getAllLessonsOrdered() {
        List<Lesson> lessons = lessonRepository.findAllByOrderByOrderNumAsc();
        lessons.sort((l1, l2) -> { // this is a comparator
            if (l1.getOrderNum() == 0 && l2.getOrderNum() == 0) {
                return 0;
            } else if (l1.getOrderNum() == 0) {
                return 1;
            } else if (l2.getOrderNum() == 0) {
                return -1;
            } else {
                return Integer.compare(l1.getOrderNum(), l2.getOrderNum());
            }
        });
        if (lessons.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(lessons, HttpStatus.OK);
        }
    }

    // Create a new lesson (without vocab and ex files)
    @PostMapping("/lesson")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LESSON_ADMIN')")
    public ResponseEntity<Lesson> createLesson(@RequestBody Lesson lesson) {
        try {
            Lesson newLesson = lessonRepository.save(new Lesson(lesson.getTitle(), lesson.getFile(), lesson.getCulturalFile()));
            return new ResponseEntity<>(newLesson, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Create a new lesson (with everything)
    @PostMapping("/lesson/full")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LESSON_ADMIN')")
    public ResponseEntity<Lesson> createFullLesson(@RequestBody Lesson lesson) {
        try {
            Lesson newLesson = lessonRepository.save(new Lesson(lesson.getTitle(), lesson.getFile(), lesson.getVocabFile(), lesson.getExFile(), lesson.getCulturalFile(), lesson.getOrderNum()));
            return new ResponseEntity<>(newLesson, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update a lesson
    @PutMapping("/lesson/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LESSON_ADMIN')")
    public ResponseEntity<Lesson> updateLesson(@PathVariable("id") long id, @RequestBody Lesson lesson) {
        Optional<Lesson> lessonData = lessonRepository.findById(id);
        if (lessonData.isPresent()) {
            Lesson _lesson = lessonData.get();
            _lesson.setTitle(lesson.getTitle());
            _lesson.setFile(lesson.getFile());
            _lesson.setVocabFile(lesson.getVocabFile());
            _lesson.setExFile(lesson.getExFile());
            _lesson.setCulturalFile(lesson.getCulturalFile());
            _lesson.setOrderNum(lesson.getOrderNum());
            return new ResponseEntity<>(lessonRepository.save(_lesson), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a lesson
    @DeleteMapping("/lesson/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('LESSON_ADMIN')")
    public ResponseEntity<HttpStatus> deleteLesson(@PathVariable("id") long id) {
        try {
            lessonRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
