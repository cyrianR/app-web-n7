import http from "../http-common";

class EventService {
  getAll() {
    return http.get("/event");
  }

  getBetween(start, end){
    return http.get(`/event/between?start=${encodeURIComponent(start)}&end=${encodeURIComponent(end)}`)
  }

  getFutureEvents(eventType) {
    return http.get(`/event/future?eventType=${encodeURIComponent(eventType)}`);
  }

  getById(id) {
    return http.get(`/event/${id}`);
  }

  delete(id) {
    return http.delete(`/event/${id}`);
  }

  addEvent(event) {
    return http.post(`/event`, event);
  }

  updateEvent(id, event) {
    return http.put(`/event/${id}`, event);
  }

  formatEventType(eventType) {
    const frenchMap = {
      PROJO: '📺 Projo',
      LESSON: '📖 Cours',
      COOKING: '🍳 Atelier cuisine',
      KARAOKE: '🎤 Karaoke'
    }
    return frenchMap[eventType] || 'Autre'
  }

  colorEvent(eventType) {
    const colorMap = {
      PROJO: '#f6e6fa',
      LESSON: '#e0f7fa',
      COOKING: '#fff9c4',
      KARAOKE: '#e0ffe0'
    }
    return colorMap[eventType] || '#cccccc'
  }
}

export default new EventService();