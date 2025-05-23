import http from "../http-common";

class EventService {
  getAll() {
    return http.get("/event");
  }

  getBetween(start, end){
    return http.get(`/event/between?start=${encodeURIComponent(start)}&end=${encodeURIComponent(end)}`)
  }

  getById(id) {
    return http.get(`/event/${id}`);
  }

  delete(id) {
    return http.delete(`/event/${id}`);
  }

  addEvent(event) {
    return http.post(`/event/`, event);
  }

  updateEvent(id, event) {
    return http.put(`/event/${id}`, event);
  }
}

export default new EventService();