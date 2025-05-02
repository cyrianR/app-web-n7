import http from "../http-common";

class UserService {
  getAll(username = null) {
    if (username) {
      return http.get(`/users?username=${username}`);
    }
    return http.get("/users");
  }

  getById(id) {
    return http.get(`/user/${id}`);
  }

  updateRoles(id, roles) {
    return http.put(`/user/role/${id}`, roles);
  }

  delete(id) {
    return http.delete(`/user/${id}`);
  }
}

export default new UserService();