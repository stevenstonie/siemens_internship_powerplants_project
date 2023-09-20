import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

import { Injectable } from '@angular/core';
import { Message } from '@stomp/stompjs';
import { Observable } from "rxjs";


@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private socket: any;
  private stompClient: any;
  private messageListeners: ((message: any) => void)[] = [];
  private resolveConnectionPromise: (() => void) | undefined;
  private connectionPromise: Promise<void>;
  private topicSubscriptions: string[] = [];

  constructor() {
    this.connectionPromise = new Promise<void>((resolve) => {
      this.resolveConnectionPromise = resolve;
    });
    this.connect();
  }

  connect(): void {
    this.socket = new SockJS("http://localhost:8082/ws");
    this.stompClient = Stomp.over(this.socket);

    if (this.stompClient) {
      this.stompClient.connect({}, (frame: any) => {
        console.log('Connected: ' + frame);

        if (this.resolveConnectionPromise) {
          this.resolveConnectionPromise();
        }
        this.subscribeToTopics(this.topicSubscriptions, (topic, message) => {
          this.notifyListeners(message);
        });
      });
    } else {
      console.error('StompClient is not initialized.');
    }
  }

  connectionEstablished(): Promise<void> {
    return this.connectionPromise;
  }

  private subscribeToTopics(topics: string[], callback: (topic: string, message: any) => void) {
    topics.forEach((topic: string) => {
      this.stompClient.subscribe(topic, (messageOutput: any) => {
        const message = messageOutput.toString();
        if(!message.includes("{"))
        {
          console.log('Received message from topic', topic, ', with message ', messageOutput.body);
          callback(topic, messageOutput.body);
        } 
        else
        {
        if (messageOutput.body) {
          try {
            const message = JSON.parse(messageOutput.body);
            console.log('Received and parsed message from topic', topic, ', with message', message);
            callback(topic, message);
          } catch (error) {
            console.error('Error parsing JSON:', error);
          }
        }
      }
      });
    });
  }

  public subscribeToTopic(topicName: string, callback: (message: any) => void) {
    this.subscribeToTopics([topicName], (topic, message) => {
      console.log('Handle new topic message:', message);
      callback(message);
    });
  }

  public disconnect() {
    if (this.stompClient) {
      this.stompClient.disconnect();
    }
    console.log('Disconnected');
  }

  private notifyListeners(message: any): void {
    this.messageListeners.forEach(listener => {
      if (typeof listener === 'function') {
        listener(message)
      }
    });
  }

  getObservable(): Observable<any> {
    return new Observable<any>(observer => {
      this.messageListeners.push((message: any) => {
        observer.next(message);
      });
    });
  }

  send(topic: string, message: any): void {
    if (this.stompClient && this.stompClient.connected) {
      this.stompClient.send("/api/powerplants/" + topic, {}, JSON.stringify(message));
    } else {
      console.error('Unable to send message, connection is not established.');
    }
  }

  openGlobalSocket(topic : string) {
    this.stompClient.subscribe(topic, (message: Message) => {
      this.handleResult(message);
    });
  }

  handleResult(message: any) {
    if(typeof message === 'string')
    {
      this.notifyListeners(message);
    } else
   {
      this.notifyListeners(JSON.parse(message.body));
    }
  }

}
