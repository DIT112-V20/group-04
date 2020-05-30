package se.healthrover.conectivity;

/*
 * This class implementation was originally written by @platisd https://github.com/platisd
 * The original code can be Viewed at https://github.com/DIT112-V20/kotlin-app-arduino-sketch-ci
 */

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;

import java.net.InetAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class LocalNetworkDeviceNameResolver {
    private final String mServiceName;
    private final String mServiceType;
    private final int mPort;

    private InetAddress mAddress;
    private CountDownLatch mLatch;
    private AddressResolutionListener mAddressResolutionListener;

    /**
     * Constructs a new class instance meant to be used synchronously (not suggested)
     * **Warning**: Due to the way the NsdManager is fetched (i.e. via a singleton) instantiating
     * this class multiple times to discover the *same* service, will induce unpredictable behavior.
     * Stick to one instance for each of the services you want to discover and prefer the
     * asynchronous way by passing a callback through the constructor.
     *
     * @param context     The application context
     * @param serviceName The service name to discover without (.local) suffixes
     * @param serviceType The service type (e.g. _http._tcp.)
     * @param port        The port
     */
    public LocalNetworkDeviceNameResolver(Context context, String serviceName, String serviceType,
                                          int port) {
        this(context, serviceName, serviceType, port, null);
    }

    /**
     * Constructs a new class instance meant to be used asynchronously (suggested)
     * **Warning**: Due to the way the NsdManager is fetched (i.e. via a singleton) instantiating
     * this class multiple times to discover the *same* service, will induce unpredictable behavior.
     * Stick to one instance for each of the services you want to discover and prefer the
     * asynchronous way by passing a callback through the constructor.
     *
     * @param context                   The application context
     * @param serviceName               The service name to discover without (.local) suffixes
     * @param serviceType               The service type (e.g. _http._tcp.)
     * @param port                      The port
     * @param addressResolutionListener The callback to be invoked when the name is resolved
     */
    public LocalNetworkDeviceNameResolver(Context context, String serviceName, String serviceType,
                                          int port, AddressResolutionListener addressResolutionListener) {
        mServiceName = serviceName;
        mServiceType = serviceType;
        mPort = port;
        mAddressResolutionListener = addressResolutionListener;

        NsdServiceInfo serviceInfo = new NsdServiceInfo();
        serviceInfo.setServiceName(serviceName);
        serviceInfo.setServiceType(serviceType);
        serviceInfo.setPort(port);

        NsdManager mNsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);

        mLatch = new CountDownLatch(1);

        assert mNsdManager != null;
        mNsdManager.resolveService(serviceInfo, new NsdManager.ResolveListener() {
            @Override
            public void onResolveFailed(NsdServiceInfo serviceInfo, int errorCode) {
            }

            @Override
            public void onServiceResolved(NsdServiceInfo serviceInfo) {
                mAddress = serviceInfo.getHost();
                mLatch.countDown();

                if (mAddressResolutionListener != null) {
                    mAddressResolutionListener.onAddressResolved(mAddress);
                }
            }
        });
    }

    /**
     * Get the resolved address synchronously
     *
     * @param timeOut  How much to wait for the address to be resolved
     * @param timeUnit The unit of the timeout
     * @return The address of the specified name
     * @throws Exception Thrown if name not resolved within timeout
     */
    public InetAddress getAddress(int timeOut, TimeUnit timeUnit) throws Exception {

        boolean timeoutExpired = true;
        try {
            timeoutExpired = !mLatch.await(timeOut, timeUnit);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (timeoutExpired) {
            throw new Exception("Could not resolve device name: \"" + mServiceName
                    + "\" with service type: \"" + mServiceType
                    + "\" at port: " + mPort + "\"");
        }
        return mAddress;
    }

    public interface AddressResolutionListener {
        void onAddressResolved(InetAddress address);
    }
}
