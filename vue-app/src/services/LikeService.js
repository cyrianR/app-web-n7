import http from "../http-common";

class LikeService {

    getLikesByEvent(eventId) {
        return http.get(`/like/event/${eventId}`);
    }

    getLikesByEvent(eventId) {
        return http.get(`/like/event/${eventId}`);
    }

    getLikeByUserAndEvent(userId, eventId) {
        return http.get(`/like/user/${userId}/event/${eventId}`);
    }

    addLike(userId, eventId) {
        return http.post(`/like/user/${userId}/event/${eventId}`);
    }

    deleteLike(userId, eventId) {
        return http.delete(`/like/user/${userId}/event/${eventId}`);
    }

}

export default new LikeService();