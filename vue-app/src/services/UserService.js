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

  updateUsername(username) {
    return http.put("user/username", {
      username: username
    });
  }

  updateEmail(email) {
    return http.put("user/email", {
      email: email
    });
  }

  updatePassword(oldPassword, newPassword) {
    return http.put("user/password", {
      oldPassword: oldPassword,
      newPassword: newPassword
    });
  }
}

export default new UserService();