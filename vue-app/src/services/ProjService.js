import http from "../http-common";

class ProjService {
  getAnimeById(id) {
    return http.get(`/anime/${id}`);
  }

  getAllFinishedAnimes() {
    return http.get("/anime/finished");
  }

  getAllNotFinishedAnimes() {
    return http.get("/anime/not-finished");
  }

  createAnime(data) {
    return http.post("/anime", data);
  }

  updateAnime(id, data) {
    return http.put(`/anime/${id}`, data);
  }

  deleteAnime(id) {
    return http.delete(`/anime/${id}`);
  }
}

export default new ProjService();
