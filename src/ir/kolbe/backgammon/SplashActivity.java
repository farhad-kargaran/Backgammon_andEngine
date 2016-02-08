package ir.kolbe.backgammon;

import ir.kolbe.utils.PrefHandler;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;

public class SplashActivity extends Activity {

	private static int SPLASH_TIME_OUT = 10;
	
	public static String token = "3f3b9d45e3fe8d87c7ae56217aa872c1";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		new Handler().postDelayed(new Runnable() 
		{
			@Override
			public void run() 
			{
				goToNextActivity();		               
			}
		}, SPLASH_TIME_OUT); 
	}
	private void goToNextActivity()
	{
		Intent i = new Intent(SplashActivity.this, FirstPage_Activity.class);
		startActivity(i);
		finish();		
	}
}
