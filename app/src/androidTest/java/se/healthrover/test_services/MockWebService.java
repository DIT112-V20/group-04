package se.healthrover.test_services;

import android.app.Activity;

import se.healthrover.conectivity.HealthRoverWebService;
import se.healthrover.conectivity.ResponseHandler;
import se.healthrover.entities.ObjectFactory;

public class MockWebService implements HealthRoverWebService {

    private static final String HTTP_STATUS_RESPONSE = "status";


    @Override
    public void createHttpRequest(final String request, final Activity activity) {
        if (request.contains(HTTP_STATUS_RESPONSE)) {

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ResponseHandler responseHandler = ObjectFactory.getInstance().getResponseHandler();
                    responseHandler.handleSuccess(HTTP_STATUS_RESPONSE,activity,request);
                }
            });
        }
    }

}
