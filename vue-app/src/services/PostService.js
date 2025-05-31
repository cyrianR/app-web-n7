import http from "../http-common";

class PostService {
  getAll() {
    return http.get("/posts");
  }

  getLast10() {
    return http.get("/10posts");
  }

  getById(id) {
    return http.get(`/post/${id}`);
  }

  delete(id) {
    return http.delete(`/post/${id}`);
  }

  addEvent(id, event) {
    return http.put(`/post/${id}/addEvent`, event);
  }

  clearEvents(id) {
    return http.put(`/post/${id}/clearEvents`);
  }

  updatePost(id, post) {
    return http.put(`/post/${id}`, post);
  }

  createPost(post) {
    return http.post(`/post`, post);
  }
}

export default new PostService();