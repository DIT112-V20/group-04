package se.healthrover.conectivity;

import android.app.Activity;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MyWebSocket {


        public void createWebSocket(String url, Activity activity){

            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            SocketListener socketListener = new SocketListener(activity);
            WebSocket webSocket =  okHttpClient.newWebSocket(request, socketListener);

        }



    public class SocketListener extends WebSocketListener {

        private Activity activity;
        public SocketListener(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onClosed(@NotNull okhttp3.WebSocket webSocket, int code, @NotNull String reason) {
            super.onClosed(webSocket, code, reason);
        }

        @Override
        public void onClosing(@NotNull okhttp3.WebSocket webSocket, int code, @NotNull String reason) {
            super.onClosing(webSocket, code, reason);
        }

        @Override
        public void onFailure(@NotNull okhttp3.WebSocket webSocket, @NotNull Throwable t, @Nullable Response response) {
            super.onFailure(webSocket, t, response);
        }

        @Override
        public void onMessage(@NotNull okhttp3.WebSocket webSocket, @NotNull String text) {
            super.onMessage(webSocket, text);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "Connected", Toast.LENGTH_LONG).show();
                    System.out.println("Connected...............");
                }
            });
        }

        @Override
        public void onMessage(@NotNull okhttp3.WebSocket webSocket, @NotNull ByteString bytes) {
            super.onMessage(webSocket, bytes);
        }

        @Override
        public void onOpen(@NotNull okhttp3.WebSocket webSocket, @NotNull Response response) {
            super.onOpen(webSocket, response);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, "Connected", Toast.LENGTH_LONG).show();
                    System.out.println("Connected...............");
                }
            });
        }
    }
}
