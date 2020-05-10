package se.healthrover.conectivity;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.loader.content.CursorLoader;
import java.util.concurrent.CountDownLatch;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import se.healthrover.ui_activity_controller.ManualControl;

public class HealthRoverWebSocket implements HealthRoverWebService {

        private OkHttpClient okHttpClient;

    public HealthRoverWebSocket(Activity activity){

         okHttpClient = new OkHttpClient();
       }

        public void createHttpRequest(String url, Activity activity){


            Request request = new Request.Builder()
                    .url(url)
                    .build();
            WebSocket webSocket =  okHttpClient.newWebSocket(request, new SocketListener(activity));
            webSocket.send("status");
            okHttpClient.dispatcher().executorService().shutdown();
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
        public void onMessage(@NotNull okhttp3.WebSocket webSocket, @NotNull final String text) {
            super.onMessage(webSocket, text);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    System.out.println("on text response " + text);
                }
            });
        }

        @Override
        public void onMessage(@NotNull okhttp3.WebSocket webSocket, @NotNull ByteString bytes) {
            super.onMessage(webSocket, bytes);
            System.out.println("on byte Response " + bytes.hex());
        }

        @Override
        public void onOpen(@NotNull final okhttp3.WebSocket webSocket, @NotNull final Response response) {
            super.onOpen(webSocket, response);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (response.message().equals("status")){
                        Intent intent = new Intent(activity, ManualControl.class);
                        activity.startActivity(intent);
                    }
                    System.out.println("on open Sending " + response.message());

                }
            });
        }
    }
}
