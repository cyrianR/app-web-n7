import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import store from "./../store";

const backend_url = import.meta.env.VITE_BACKEND_URL;

class WebSocketService {
  constructor() {
    this.client = null;
  }

  connect() {
    const user = store.state.auth.user;
    if (user && user.token) {
      this.client = new Client({
        brokerURL: 'ws://' + backend_url + '/ws',
        connectHeaders: {
          Authorization: `Bearer ${user.token}`,
        },
        connectHeaders: {},
        debug: (str) => console.log(str),
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
        webSocketFactory: () => new SockJS('http://' + backend_url + '/ws'),
      });

      this.client.activate();
    }
  }

  subscribeToRoleUpdates(callback) {
    if (this.client) {
      this.client.onConnect = () => {
        this.client.subscribe('/topic/roles', (message) => {
          callback(JSON.parse(message.body));
        });
      };
    }
  }

  disconnect() {
    if (this.client) {
      this.client.deactivate();
    }
  }
}

export default new WebSocketService();