import http from "../http-common";

class LessonService {
  getAll() {
    return http.get("/lesson");
  }

  getById(id) {
    return http.get(`/lesson/${id}`);
  }

  getByTitle(title) {
    return http.get(`/lesson/title/${title}`);
  }

  create(lesson) {
    return http.post("/lesson", lesson);
  }

  createFull(lesson) {
    return http.post("/lesson/full", lesson);
  }

  addVocabFile(id, vocabFile) {
    return http.put(`/lesson/${id}/vocab`, vocabFile, {
      headers: { "Content-Type": "application/json" }
    });
  }

  addExFile(id, exFile) {
    return http.put(`/lesson/${id}/ex`, exFile, {
      headers: { "Content-Type": "application/json" }
    });
  }

  update(id, lesson) {
    return http.put(`/lesson/${id}`, lesson);
  }

  delete(id) {
    return http.delete(`/lesson/${id}`);
  }
}

export default new LessonService();