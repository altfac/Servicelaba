import axios from 'axios';

const UrlNames = "http://localhost:8090/audio/names";

class AudioService {
  constructor() {
    this.api = axios.create({
      baseURL: 'http://localhost:8090/audio/',
      headers: { "ngrok-skip-browser-warning": "true" }
    });
  }

  getSongs() {
    return this.api.get(UrlNames);
  }
}

export default new AudioService();