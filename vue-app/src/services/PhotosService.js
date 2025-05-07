import http from "../http-common";

class PhotosService {
  getById(id) {
    return http.get('/getfile/${id}');
  }
  uploadFile() {
    return http.get('/file/upload', {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    });
  }
  getListFiles() {
    return http.get('/files');
  }
  deleteFile(id) {
    return http.delete('/file/${id}');
  }
}

export default new PhotosService();

