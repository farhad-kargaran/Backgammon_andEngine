package ir.kolbe.backgammon;

import ir.kolbe.utils.PrefHandler;
import ir.kolbe.utils.ResponseModel;

import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.widget.EditText;

public class RegisterActivity extends Activity {


	EditText edt_password, edt_confirmPassword, edt_email ,edt_nickName;


	String email,password,confirmPassword,nickName, model, screenSize,version,imei,date_;

	final static String Url_SignUp = "http://jamal.farhadi.ir/users/signup.json";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);




		edt_password = (EditText) findViewById(R.id.edt_password);
		edt_confirmPassword = (EditText) findViewById(R.id.edt_confirm);
		edt_email = (EditText) findViewById(R.id.edt_email);
		edt_nickName = (EditText) findViewById(R.id.edt_nickname);
		edt_email.setText("a1@a1.com");
		edt_password.setText("123456");
		edt_confirmPassword.setText("123456");
		edt_nickName.setText("a1");

		model = android.os.Build.BRAND;
		if(TextUtils.isEmpty(model))
			model="unknown";
		screenSize = getScreenSize();
       version =  String.valueOf(android.os.Build.VERSION.SDK_INT);
       imei = getImei();
       date_ =  String.valueOf(System.currentTimeMillis()/1000);
	}
	
	public void signup(View v)
	{
		password = edt_password.getText().toString();
		confirmPassword = edt_confirmPassword.getText().toString();
		email = edt_email.getText().toString();
		nickName = edt_nickName.getText().toString();


		if(!password.equals(confirmPassword))
		{
			App.Toast_ItS(getResources().getString(R.string.passwords_not_match));
			return;
		}
		if(email.length() < 3)
		{
			App.Toast_ItS(getResources().getString(R.string.email_is_needed));
			return;
		}
		if(nickName.length() < 1)
		{
			App.Toast_ItS(getResources().getString(R.string.nickname_is_needed));
			return;
		}
		if(App.isNetworkAvailable() == false)
		{
			App.Toast_ItS(getResources().getString(R.string.no_ineternet));
			return;
		}
		App.executeAsyncTask(new AsyncCaller());
	}



	private class AsyncCaller extends AsyncTask<Void, Void, Void>
	{
		HttpEntity httpEntity = null;
		HttpResponse httpResponse = null;
		String responseString = null;
		StatusLine statusLine;

		final ProgressDialog pDialog = new ProgressDialog(RegisterActivity.this);

		ResponseModel resp;

		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();

			pDialog.setMessage("Sending Data to Server...");
			//pDialog.setCancelable(false);
			pDialog.show(); 
			pDialog.setOnCancelListener(new ProgressDialog.OnCancelListener() 
			{

				@Override
				public void onCancel(DialogInterface dialog) 
				{
					(AsyncCaller.this).cancel(true);	               
				}

			});
			pDialog.setOnDismissListener(new OnDismissListener() 
			{

				@Override
				public void onDismiss(DialogInterface dialog) 
				{
					(AsyncCaller.this).cancel(true);	               
				}
			});


		}
		@Override
		protected Void doInBackground(Void... params) 
		{

			HttpClient client = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
			JSONObject json = new JSONObject();
			try 
			{
				HttpPost post = new HttpPost(Url_SignUp);
				json.put("email", email);
				json.put("password", password);
				json.put("nickname", nickName);
				json.put("model", model);
				json.put("size", screenSize);
				json.put("version", version);
				json.put("imei", imei);
				//json.put("date", date_);
				//json.put("ip", App.getIPAddress(true));
				
				StringEntity se = new StringEntity( json.toString());  
				se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
				post.setEntity(se);
				post.setHeader("Token", SplashActivity.token); 
				httpResponse = client.execute(post);
				statusLine = httpResponse.getStatusLine();
				httpEntity = httpResponse.getEntity();
				responseString = EntityUtils.toString(httpEntity);
				responseString = responseString;
			}
			catch(Exception e) 
			{
				e.printStackTrace();
				//App.Toast_ItL(getResources().getString(R.string.can_not_connect_to_server));
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result)
		{
			super.onPostExecute(result);
			pDialog.dismiss();
			try 
			{

				if(httpResponse != null)
				{
					try 
					{
						JSONObject jsonResp =new JSONObject(responseString);
						
						String statusCode = jsonResp.getString("status");	
						String message = jsonResp.getString("message");	
						String data =  jsonResp.getString("data");
						int code = jsonResp.getInt("code");
						if(code == 201)
						{
							JSONObject object = new JSONObject(data);
							PrefHandler.getInstance().setPreference(PrefHandler.PROPERTY_TOKEN,object.getString("token"));
							PrefHandler.getInstance().setPreference(PrefHandler.PROPERTY_ID,object.getString("id"));
							finish();
							Intent i = new Intent(RegisterActivity.this, OnlineActivity.class);
							PrefHandler.getInstance().setPreference(PrefHandler.PROPERTY_HAS_SIGNUP, true);
							PrefHandler.getInstance().setPreference(PrefHandler.PROPERTY_EMAIL, edt_email.getText().toString());
							PrefHandler.getInstance().setPreference(PrefHandler.PROPERTY_PASSWORD, edt_password.getText().toString());
							PrefHandler.getInstance().setPreference(PrefHandler.PROPERTY_NICKNAME, edt_nickName.getText().toString());
							App.Toast_ItS(getResources().getString(R.string.registed_successfuly));
							startActivity(i);
						}else if(code == 400)
						{
							String title = message;
							String Details = "";
							JSONObject object = new JSONObject(data);
							Iterator keys =  object.keys();
							while(keys.hasNext())
							{
								String key = (String) keys.next();
								Details += object.getString(key) + "\n";	
							}
							//Details = Details.replaceAll(String.valueOf('['),"");
							//Details = Details.replaceAll(String.valueOf(']'),"");
							//Details = Details.replaceAll(String.valueOf('"'),"");
							App.Toast_ItL(message + "\n\n" + Details); 

						}
						else
						{
							App.Toast_ItS(getResources().getString(R.string.unknown_error));

						}
						// App.Toast_ItL("as");
					} 
					catch (Exception ee) 
					{
						App.Toast_ItL(ee.toString());
					} 
				}
			} 
			catch (Exception e) 
			{
				App.showErrorMessage("RegisterActivity -> AsyncCaller -> OnPost", e);
			}
		}
	}
	private String getImei()
	{
		String str = "null";
		try
		{
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		str = telephonyManager.getDeviceId();
		}
		catch(Exception ee)
		{
		}
		return str;
	}
	private  String getScreenSize()
	{
		String str = "0,0";
		try
		{
			Display display = getWindowManager().getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			int width = size.x;
			int height = size.y;
			str = "w: " + width + ", x: " + height;
		}
		catch(Exception ee)
		{
		}
		return str;
	}
}
