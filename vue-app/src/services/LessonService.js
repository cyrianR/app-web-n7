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

  update(id, lesson) {
    return http.put(`/lesson/${id}`, lesson);
  }

  delete(id) {
    return http.delete(`/lesson/${id}`);
  }
}

export default new LessonService();