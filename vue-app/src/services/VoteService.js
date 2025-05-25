import http from "../http-common";

class VoteService {

    getById(id) {
        return http.get(`/vote/${id}`);
    }

    getVotesByEvent(eventId) {
        return http.get(`/vote/event/${eventId}`);
    }

    getVotesByUser(userId) {
        return http.get(`/vote/user/${userId}`);
    }

    getVoteByUserAndEvent(userId, eventId) {
        return http.get(`/vote/user/${userId}/event/${eventId}`);
    }

    addVote(data) {
        return http.post("/vote", data);
    }

    deleteVote(id) {
        return http.delete(`/vote/${id}`);
    }

}

export default new VoteService();