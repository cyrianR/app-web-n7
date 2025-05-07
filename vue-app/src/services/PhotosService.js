import http from "../http-common";

class PhotosService {
  getById(id) {
    return http.get(`/getfile/${id}`);
  }
  uploadFile(formData) {
    return http.post(`/file/upload`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
  }
  getListFiles() {
    return http.get('/files').then(response => response.data);
  }
  deleteFile(id) {
    return http.delete(`/file/delete/${id}`);
  }
}

export default new PhotosService();

