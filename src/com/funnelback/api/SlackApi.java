package com.funnelback.api;

import java.io.*;
import java.net.InetSocketAddress;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.funnelback.common.config.Config;
import com.funnelback.common.config.NoOptionsConfig;
import com.funnelback.common.io.store.bytes.Push2Store;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.funnelback.api.models.AuthChallenge;
import com.funnelback.api.models.OuterEvent;

public class SlackApi {
    private Config conf;
    private Push2Store push2Store;

    public static void main(String[] args) throws Exception {
        new SlackApi();
    }

    private SlackApi() throws IOException {
        conf = new NoOptionsConfig(new File("/opt/funnelback/"), "sam-test");
        System.out.println("ID: " + conf.getCollectionId());
        System.out.println("Name: " + conf.getCollectionName());
        push2Store = new Push2Store(conf);
        push2Store.open();

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/slackapi", new SlackApiHandler(push2Store));
        server.start();
        push2Store.close();
    }

    static class SlackApiHandler implements HttpHandler {
        private Push2Store push2Store;
        private int count = 0;
        private HashMap<String, String> m = new HashMap<>();


        public SlackApiHandler(Push2Store push2Store) throws IOException {
            this.push2Store = push2Store;
            //m.put("Content-Type", "text/html");
            //push2Store.add(new RawBytesRecord("blabla".getBytes(), "http://www.funnelback.com/" + count), m);

        }

        @Override
        public void handle(HttpExchange t) throws IOException {
            //this is handling the incoming message
            InputStreamReader isr =  new InputStreamReader(t.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            // reading data
            int b;
            StringBuilder buf = new StringBuilder(512);
            while ((b = br.read()) != -1) {
                buf.append((char) b);
            }
            br.close();
            isr.close();
            String input = buf.toString();

            // this is the response
            String output = handleMessage(input);

            t.sendResponseHeaders(200, output.length());
            OutputStream os = t.getResponseBody();
            //os.write(response.getBytes());
            os.write(output.getBytes());
            os.close();
        }

        private String handleMessage(String message) {
            String response;
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                OuterEvent outerEvent = objectMapper.readValue(message, OuterEvent.class);
                response = handleEvent(outerEvent);
            } catch (Exception e) {
                try {
                    AuthChallenge authChallenge = objectMapper.readValue(message, AuthChallenge.class);
                    response = handleAuthChallenge(authChallenge);
                } catch (Exception f) {
                    response = "Unknown message format: " + e.getMessage();
                }

            }
            return response;
        }

        private String handleEvent(OuterEvent outerEvent) {
            return "New event: " + outerEvent.eventId;
            // todo: stuff
        }

        private String handleAuthChallenge(AuthChallenge authChallenge) {
            return authChallenge.challenge;
        }

    }

}