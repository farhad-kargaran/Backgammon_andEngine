package ir.kolbe.backgammon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class Single_player_options extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_single_player_options);
		TextView txt_chooseColor = (TextView) findViewById(R.id.txt_chooseColor);
		txt_chooseColor.setTypeface(App.DEFAULT_Font);
		TextView txt_b = (TextView) findViewById(R.id.txt_b);
		txt_b.setTypeface(App.DEFAULT_Font);
		TextView txt_w = (TextView) findViewById(R.id.txt_w);
		txt_w.setTypeface(App.DEFAULT_Font);
		TextView txt_play = (TextView) findViewById(R.id.txt_play);
		txt_play.setTypeface(App.DEFAULT_Font);
	}
	public void start(View v)
	{
		Intent i = new Intent(Single_player_options.this, Play_h2c.class);
		startActivity(i);
	}
}
