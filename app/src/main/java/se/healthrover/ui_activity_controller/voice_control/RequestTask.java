package se.healthrover.ui_activity_controller.voice_control;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;

import com.google.cloud.dialogflow.v2.DetectIntentRequest;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;


// This class processes user requests in an Asynchronous way
// the request query is sent to the API and
// the response is captured

class RequestTask extends AsyncTask<Void, Void, DetectIntentResponse> {
    @SuppressLint("StaticFieldLeak")
    private Activity activity;
    private SessionName session;
    private SessionsClient sessionsClient;
    private QueryInput queryInput;

    RequestTask(Activity activity, SessionName session, SessionsClient sessionsClient, QueryInput queryInput){
        this.activity = activity;
        this.session = session;
        this.sessionsClient = sessionsClient;
        this.queryInput = queryInput;
    }


    @Override
    protected DetectIntentResponse doInBackground(Void... voids) {
        try {
            DetectIntentRequest detectIntentRequest = DetectIntentRequest.newBuilder()
                    .setSession(session.toString())
                    .setQueryInput(queryInput)
                    .build();
            return sessionsClient.detectIntent(detectIntentRequest);
        } catch (Exception e) {
            System.out.println("Oops something went wrong..");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(DetectIntentResponse result){
        ((SpeechRecognition) activity).processResponse(result);
    }
}
