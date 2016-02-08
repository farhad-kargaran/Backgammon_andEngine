package ir.kolbe.utils;

import ir.kolbe.backgammon.App;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.commons.io.output.ProxyOutputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class ServiceHandler 
{
	String response = null;
	StatusLine statusLine;
	public long mediaId;
    public final static int GET = 1;
    public final static int POST = 2;
    public final static int DELETE = 3;
    public final static int PUT = 4;
    
    final static String HeaderParamToken = "Token";
    
    static final DefaultHttpClient httpClient;
    static 
    {    
        HttpParams httpParameters = new BasicHttpParams();
        //HttpConnectionParams.setConnectionTimeout(httpParameters, 30000);
        //HttpConnectionParams.setSoTimeout(httpParameters, 50000);
        httpClient = new DefaultHttpClient(httpParameters);
    }
 
    public ServiceHandler() 
    {
 
    }
 
    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     * */
    public ResponseModel makeServiceCall(String url, int method) 
    {
        return this.makeServiceCall(url, method, null, false, false);
    }
    
    public ResponseModel makeServiceCall(String url, int method,List<NameValuePair> params,boolean	sendToken) 
    {
    	return this.makeServiceCall(url, method, params, sendToken, false);
    } 
    
    public ResponseModel makeServiceCall(String url, int method,List<NameValuePair> params,boolean	sendToken,boolean sendMultiPart) 
    {
        try 
        {
        	
            HttpEntity httpEntity = null;
            HttpResponse httpResponse = null;
             
            // Checking http request method type
            if (method == POST) 
            {
                HttpPost httpPost = new HttpPost(url);
                
               
                if(sendToken)
                {
                	
                	  httpPost.setHeader(HeaderParamToken, PrefHandler.getInstance().getString(PrefHandler.PROPERTY_TOKEN, ""));                	           	  
                	  //httpPost.setHeader("Accept", "application/json");
                     // httpPost.setHeader("Content-type", "text/html");
                }
                if(sendMultiPart)
                {
                	 MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                	 
                	 builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                	 
                	 for (int index = 0; index < params.size(); index++) 
                	 {
                		 if (params.get(index).getName().startsWith("images") || params.get(index).getName().startsWith("videos"))
                		 {
                			 
                			 File f = new File((params.get(index).getValue()));
                		 } 
                		 else 
                		 {
                			 builder.addTextBody(params.get(index).getName(), params.get(index).getValue(),ContentType.create("text/plain", MIME.UTF8_CHARSET));
                		 }
                	 }                	 
                	
                	 //httpPost.setEntity(builder.build());       
                	 
                	 
                	 ProgressHttpEntityWrapper.ProgressCallback progressCallback = new ProgressHttpEntityWrapper.ProgressCallback() 
                	 {
                		 @Override
                		 public void progress(float progress) 
                		 {
                			int prs = (int)progress;
                			 
                			if(mediaId > 0 && prs > 0 && prs % 10 == 0)
                			{               				
                				//ME Disabled this
                				/* Intent intent = new Intent(App.ACTION_LOCAL_BROADCAST_PROGRESS_MEDIA_UPLOAD);
                				 intent.putExtra("progress", prs);
                				 intent.putExtra("mediaId", mediaId);
                				 LocalBroadcastManager.getInstance(App.getInstance()).sendBroadcast(intent);*/
                				 
                				 android.util.Log.d("rrn progress", ""+progress);                				
                				
                			}
                		 }
                	 };

                	httpPost.setEntity(new ProgressHttpEntityWrapper(builder.build(), progressCallback));
                	 
                }
                else if (params != null)  // adding post params
                {
                	//for (NameValuePair nameValuePair : params){android.util.Log.d("rrn num", nameValuePair.getValue());}
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                }
 
                httpResponse = httpClient.execute(httpPost);
                
                // Now lets see the results of our ACTION
                //org.apache.http.Header[] a = httpResponse.getAllHeaders();
                // The results are collected in this String
                //String value = "";
                // Lets get ALL Headers of the Response
                //for (int i = 0; i < a.length; i++){value = value + " " + i + " " + a[i].getName() + " : " + a[i].getValue() + " \n ";}
            } 
            else if (method == GET) 
            {
                // appending params to url
                if (params != null) 
                {
                    String paramString = URLEncodedUtils.format(params, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);
                if(sendToken)
                {
                	String d = PrefHandler.getInstance().getString(PrefHandler.PROPERTY_TOKEN,"");
                	httpGet.setHeader(HeaderParamToken, PrefHandler.getInstance().getString(PrefHandler.PROPERTY_TOKEN, ""));                	
                }
                	 
                httpResponse = httpClient.execute(httpGet);
            }
            else if (method == DELETE) 
            {
               
                HttpDelete httpDelete = new HttpDelete(url);
                
                if(sendToken)
                	httpDelete.setHeader(HeaderParamToken, PrefHandler.getInstance().getString(PrefHandler.PROPERTY_TOKEN, ""));
 
                httpResponse = httpClient.execute(httpDelete);
            }
            else if (method == PUT) 
            {
               
                HttpPut httpPut = new HttpPut(url);                
                
                if(sendToken)
                	httpPut.setHeader(HeaderParamToken, PrefHandler.getInstance().getString(PrefHandler.PROPERTY_TOKEN, ""));
                
                if (params != null) 
                {                	
                	httpPut.setEntity(new UrlEncodedFormEntity(params));
                }
                 
                httpResponse = httpClient.execute(httpPut);
            }
            
            
            statusLine = httpResponse.getStatusLine();
            
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
            
            android.util.Log.d("rrn url & getStatusLine", url+ "  " + String.valueOf(statusLine));
            android.util.Log.d("rrn response", response);
        } 
        /*catch(UnknownHostException e) // no access to server
        {        	
        	SmsApp.showErrorMessage("ServiceHandler -> makeServiceCall", e);
        }*/        
        catch (Exception e) 
        {
           App.showErrorMessage("ServiceHandler -> makeServiceCall", e);
           return new ResponseModel(response, statusLine, e);
         
           
        }         
      
        return new ResponseModel(response, statusLine, null);
   }
    
    /*public static String getContent(HttpResponse response) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        String body = "";
        String content = "";

        while ((body = rd.readLine()) != null) 
        {
            content += body + "\n";
        }
        return content.trim();
    }*/
}
