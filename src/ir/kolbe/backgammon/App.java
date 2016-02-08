package ir.kolbe.backgammon;

import ir.kolbe.utils.LatLong;
import ir.kolbe.utils.PrefHandler;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import org.acra.annotation.ReportsCrashes;
import org.apache.http.conn.util.InetAddressUtils;

import android.annotation.TargetApi;
import android.app.Application;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Looper;
import android.widget.Toast;

@ReportsCrashes(// Enter values from DIALOG section of wiki here!
		 resNotifTickerText = R.string.crash_notif_ticker_text,
        resNotifTitle = R.string.crash_notif_title,
        resNotifText = R.string.crash_notif_text,
       resNotifIcon = android.R.drawable.stat_notify_error // optional. default is a warning sign
, formKey = ""
       )


public class App extends Application {
	
	public static final String TARGETOR_EXTRA_MULTIPLAYER = "multiplayer";
	public static final String TARGETOR_EXTRA_SCORE = "score";
	public static final String TARGETOR_EXTRA_TARGETS_SHOT= "targets_shot";
	public static final String TARGETOR_EXTRA_MISSES = "misses";
	public static final String TARGETOR_EXTRA_LEVEL_ID= "level_id";
	public static final String TARGETOR_EXTRA_OPPONENT_SCORE = "opponent_score";
	public static final String TARGETOR_EXTRA_OPPONENT_TARGETS_SHOT= "opponent_targets_shot";
	public static final String TARGETOR_EXTRA_OPPONENT_MISSES= "opponent_misses";

	public static final String TARGETOR_KEY_SOUND_ON = "sound_on";
	public static final String SHARED_PREFERENCES = "TargetorPreferences";
	
	public static final int LEVELS=20;
	
	public BluetoothSocket btSocket;
	
	
	public static Typeface DEFAULT_Font;
	private static Context context;
	private static App mInstance;
	@Override
	public void onCreate()
	{
		/*ACRA.init(this);
		ACRA.getErrorReporter().removeAllReportSenders();
		ACRA.getErrorReporter().setReportSender(new LocalReportSender(this));*/
		super.onCreate();
		
		mInstance = this;
		App.context = getApplicationContext();	
		PrefHandler.initializeInstance(this);
		DEFAULT_Font = Typeface.createFromAsset(App.getInstance().getAssets(),"fonts/koodak.TTF");
	}
	
	public static synchronized App getInstance() 
	{
		return mInstance;

	}
/*	public static Intent getIntentForStartApp()
	{		
		Intent i;		
		if(PrefHandler.getInstance().getBoolean(PrefHandler.PROPERTY_HAS_SIGNUP, false))		
			i = new Intent(App.getInstance(), FirstPage_Activity.class);						
		else		
			i = new Intent(App.getInstance(), FirstPage_Activity.class);

		return i;

	}*/
	public static void Toast_ItL(String text)
	{
		Toast.makeText(getInstance(), text, Toast.LENGTH_LONG).show();
	}
	public static void Toast_ItS(String text)
	{
		Toast.makeText(getInstance(), text, Toast.LENGTH_SHORT).show();
	}
	public static void showErrorMessage(String ee, Exception ex)
	{
		if(Looper.myLooper() == Looper.getMainLooper())
			Toast.makeText(mInstance,ee + (ex.getMessage() != null ? ex.getMessage():ex.getClass().toString()), Toast.LENGTH_LONG).show();
		    android.util.Log.d("bgm",ee+" : " + (ex.getMessage() != null ? ex.getMessage():ex.getClass().toString()));

	}
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) // API 11
	public static <T> void executeAsyncTask(AsyncTask<T, ?, ?> asyncTask, T... params) 
	{
	    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
	        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
	    else
	        asyncTask.execute(params);
	}
	public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr); 
                        if (useIPv4) {
                            if (isIPv4) 
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                                return delim<0 ? sAddr : sAddr.substring(0, delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
    }
	public  LatLong getLastLocation()
	{
		LocationManager locationManager = (LocationManager)getSystemService
	            (Context.LOCATION_SERVICE); 
	    Location getLastLocation = locationManager.getLastKnownLocation
	            (LocationManager.PASSIVE_PROVIDER);
	    double currentLongitude = getLastLocation.getLongitude();
	    double currentLatitude = getLastLocation.getLatitude();
	    LatLong currentLocation = new LatLong(currentLatitude, currentLongitude); 
	    return currentLocation;
	}
	public static String getMACAddress(String interfaceName) {
        try {
        	//Utils.getMACAddress("wlan0");
        	//Utils.getMACAddress("eth0");
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) continue;
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac==null) return "";
                StringBuilder buf = new StringBuilder();
                for (int idx=0; idx<mac.length; idx++)
                    buf.append(String.format("%02X:", mac[idx]));       
                if (buf.length()>0) buf.deleteCharAt(buf.length()-1);
                return buf.toString();
            }
        } catch (Exception ex) { } // for now eat exceptions
        return "";
        /*try {
            // this is so Linux hack
            return loadFileAsString("/sys/class/net/" +interfaceName + "/address").toUpperCase().trim();
        } catch (IOException ex) {
            return null;
        }*/
    }
	public static boolean isNetworkAvailable()
	{
		try
		{
			ConnectivityManager cm = (ConnectivityManager) App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();

			if (netInfo != null && netInfo.isConnected())
				return true;

		} 
		catch (Exception e)
		{
			showErrorMessage("SmsApp -> isNetworkAvailable", e);
			
		}



		return false;
	}	

}
