package ir.kolbe.utils;

import org.apache.http.StatusLine;

public class ResponseModel
{
	public String response;
	public StatusLine statusLine;
	public Exception exception;
	
	public ResponseModel(String response,StatusLine statusLine,Exception exception) 
	{	
	
		this.response = response;	
		this.statusLine = statusLine;
		this.exception = exception;
	}
	
	
	

}
