package org.example.controller;

import com.google.gson.Gson;
import org.example.dao.ItemDAO;
import org.example.Item;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@WebSocket
public class PriceWebSocket {
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());
    private static ItemDAO itemDAO;                     
    private static final Gson gson = new Gson();

    public static void setItemDAO(ItemDAO dao) {
        itemDAO = dao;
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        sessions.add(session);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        sessions.remove(session);
    }


    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
    }

    public static void broadcastPriceUpdate(Item item) {
        if (item == null) return;
        String payload = gson.toJson(new PriceUpdateMessage(item.getId(), item.getPrice()));
        synchronized (sessions) {
            for (Session s : sessions) {
                try {
                    s.getRemote().sendString(payload);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class PriceUpdateMessage {
        public final String type = "updatePrice";
        public final String itemId;
        public final double price;

        public PriceUpdateMessage(String itemId, double price) {
            this.itemId = itemId;
            this.price = price;
        }
    }
}
