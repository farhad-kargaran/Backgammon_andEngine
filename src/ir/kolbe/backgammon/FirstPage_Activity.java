package ir.kolbe.backgammon;

import ir.kolbe.utils.PrefHandler;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class FirstPage_Activity extends Activity {

	Button btn_cpu,btn_offline;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_first_page_);
		
		btn_cpu = (Button) findViewById(R.id.btn_cpu);
		btn_offline = (Button) findViewById(R.id.btn_offline);
		btn_cpu.setTypeface(App.DEFAULT_Font);
		btn_offline.setTypeface(App.DEFAULT_Font);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	public void bluetooth(View v)
	{
		Intent i = new Intent(FirstPage_Activity.this, BTFindActivity.class);
		//Intent i = new Intent(FirstPage_Activity.this, Play_h2hBT.class);
		startActivity(i);
	} 
	public void twoPlayer_h2hOff(View v)
	{
		Intent i = new Intent(FirstPage_Activity.this, Play_h2hOff.class);
		startActivity(i);
	}
	public void twoPlayer_h2hOn(View v)
	{
		if(PrefHandler.getInstance().getBoolean(PrefHandler.PROPERTY_HAS_SIGNUP,false))
		{
			Intent i = new Intent(FirstPage_Activity.this, OnlineActivity.class);
			startActivity(i);
			
		}else
		{
			Intent i = new Intent(FirstPage_Activity.this, RegisterActivity.class);
			startActivity(i);
		}
		
	}
	public void twoPlayer_h2c(View v)
	{
		/*Intent i = new Intent(FirstPage_Activity.this, Play_h2c.class);
		startActivity(i);*/
		
		Intent i = new Intent(FirstPage_Activity.this, Play_h2c.class);
		startActivity(i);
	}
	
}
