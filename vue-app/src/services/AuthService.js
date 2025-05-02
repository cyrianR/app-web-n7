import http from "../http-common";

class AuthService {
    login(user) {
        return http
            .post("auth/signin", {
                username: user.username,
                password: user.password
            })
            .then(response => {
                if (response.data.token) {
                    localStorage.setItem('user', JSON.stringify(response.data));
                }

                return response.data;
            });
    }

    logout() {
        localStorage.removeItem('user');
    }

    register(user) {
        return http.post("auth/signup", {
            username: user.username,
            email: user.email,
            password: user.password
        });
    }
}

export default new AuthService();
