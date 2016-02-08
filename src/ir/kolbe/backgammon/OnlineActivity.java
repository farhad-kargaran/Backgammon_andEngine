package ir.kolbe.backgammon;

import ir.kolbe.utils.LatLong;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class OnlineActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_online);
		TextView txt = (TextView) findViewById(R.id.txt);
		LatLong loc =  getLastLocation();
		txt.setText("Latitude: " + loc.lat + ", Longitude: " + loc.lng);
	}

	public  LatLong getLastLocation()
	{
		LocationManager locationManager = (LocationManager)getSystemService
				(Context.LOCATION_SERVICE); 
		Location getLastLocation = locationManager.getLastKnownLocation
				(LocationManager.PASSIVE_PROVIDER);

		try
		{
			double currentLongitude = getLastLocation.getLongitude();
			double currentLatitude = getLastLocation.getLatitude();
			LatLong currentLocation = new LatLong(currentLatitude, currentLongitude); 
			return currentLocation;
		}
		catch(Exception ee)
		{
			return new LatLong(0, 0);
		}
	}
}
