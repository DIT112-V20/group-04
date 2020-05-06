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

        private OkHttpClient okHttpClient = new OkHttpClient();
        private SocketListener socketListener;
        //Testing TODO remove counter
        public static int socketInt = 0;
        public static int socketRestp =  0;
        private boolean status;


    public HealthRoverWebSocket(Activity activity){
         socketListener = new SocketListener(activity);
       }

        public void createWebSocket(String url, Activity activity){

            Request request = new Request.Builder()
                    .url(url)
                    .build();
            WebSocket webSocket =  okHttpClient.newWebSocket(request, socketListener);
            //webSocket.send(command);
            System.out.println("request " + request);
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
                    if (text.contains("200")){
                        Intent intent = new Intent(activity.getApplication(), ManualControl.class);
                        intent.putExtra("carName", "HealthRover");
                        activity.startActivity(intent);
                        status = true;
                        System.out.println("response " + text);
                    }
                    else {
                        System.out.println("response " + text);
                        status = false;
                    }
                    //Testing TODO remove counter
                    socketRestp++;
                }
            });
        }

        @Override
        public void onMessage(@NotNull okhttp3.WebSocket webSocket, @NotNull ByteString bytes) {
            super.onMessage(webSocket, bytes);
        }

        @Override
        public void onOpen(@NotNull final okhttp3.WebSocket webSocket, @NotNull Response response) {
            super.onOpen(webSocket, response);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("Sending");
                    //webSocket.send("");
                    //Testing TODO remove counter
                    socketInt++;
                    System.out.println("\n" + "Send");
                }
            });
        }
    }
}
