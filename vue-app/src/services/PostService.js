import http from "../http-common";

class PostService {
  getAll() {
    return http.get("/posts");
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

  removeEvent(id, event) {
    return http.put(`/post/${id}/removeEvent`, event);
  }

  updatePost(post) {
    return http.put(`/post/${id}`, post);
  }

  createPost(post) {
    return http.put(`/post`, post);
  }
}

export default new PostService();