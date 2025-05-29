import http from "../http-common";

class SongService {
  getAll() {
    return http.get("/song");
  }

  getById(id) {
    return http.get(`/song/${id}`);
  }

  getByTitle(title) {
    return http.get(`/song/title/${title}`);
  }

  create(song) {
    return http.post("/song", song);
  }

  createFull(song) {
    return http.post("/song/full", song);
  }

  update(id, song) {
    return http.put(`/song/${id}`, song);
  }

  delete(id) {
    return http.delete(`/song/${id}`);
  }
}
export default new SongService();