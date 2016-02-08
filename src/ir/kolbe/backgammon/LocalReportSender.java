package ir.kolbe.backgammon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

import android.content.Context;
import android.widget.Toast;

public class LocalReportSender implements ReportSender {

	Context c;
	public LocalReportSender(Context cx)
	{
		c = cx;
	}

	@Override
	public void send(Context arg0, CrashReportData crashReportData) throws ReportSenderException
	{
		// TODO Auto-generated method stub
		 try {
			 writeToSDFile(getError(crashReportData));
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		
	}
	public static String humanReadableByteCount(long bytes, boolean si) {
	    int unit = si ? 1000 : 1024;
	    if (bytes < unit) return bytes + " B";
	    int exp = (int) (Math.log(bytes) / Math.log(unit));
	    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	    return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
	}
	private String getError(CrashReportData c)
	{
		
		String txt = "";
		
		String nextline = "\r\n";	
		txt += "Board = {";
		for(int i=0;i<=24;i++)
		{
			if(i < 24)
			txt += Play_h2hOff.board_b[i] + ",";
			else txt += Play_h2hOff.board_b[i] + "}";
		}
		txt += "\n" + "firstDice: " + Play_h2hOff.firstDice_b;
		txt += "\n" + "secondDice: " + Play_h2hOff.secondDice_b;
		txt += "\n" + "firstDiceN: " + Play_h2hOff.firstDice_bn;
		txt += "\n" + "secondDiceN: " + Play_h2hOff.secondDice_bn;
		txt += "\n" + "hitBlack: " + Play_h2hOff.hitBlack_b;
		txt += "\n" + "hitWhite: " + Play_h2hOff.hitWhite_b;
		txt += "\n" + "takenBlack: " + Play_h2hOff.takenBlack_b;
		txt += "\n" + "takenWhite: " + Play_h2hOff.takenWhite_b;
		txt += "\n" + "Dice1: " + Play_h2hOff.dice1_b;
		txt += "\n" + "Dice2: " + Play_h2hOff.dice2_b;
		txt += "\n" + "Joft: " + Play_h2hOff.joft_b;
		txt += "\n" + "playerTurn: " + Play_h2hOff.playerTurn_b + "\n";
		
		txt += "Android Version: " + c.get(ReportField.ANDROID_VERSION);
		txt += nextline + "App Version Name: " + c.get(ReportField.APP_VERSION_NAME);
		txt += nextline + "App Version Code: " + c.get(ReportField.APP_VERSION_CODE);
		long bytes = Long.parseLong(c.get(ReportField.AVAILABLE_MEM_SIZE));
		txt += nextline + "Available RAM: " + humanReadableByteCount(bytes, true);
		txt += nextline + "Phone Model: " + c.get(ReportField.PHONE_MODEL);
		txt +=nextline + nextline+  "--------------------LOG CAT--------------------";
		txt += nextline + c.get(ReportField.LOGCAT);
        return txt;
		
	}
	private void writeToSDFile(String logg){

		// Find the root of the external storage.
		// See http://developer.android.com/guide/topics/data/data-  storage.html#filesExternal

		File root = android.os.Environment.getExternalStorageDirectory(); 

		// See http://stackoverflow.com/questions/3551821/android-write-to-sd-card-folder

		File dir = new File (root.getAbsolutePath() + "/BackGammonCrash");
		dir.mkdirs();
		int c = 1;
		File file = null;
		boolean loop = true;
		while(loop)
		{
		    file = new File(dir, "myData" + c + ".txt");
		    if(file.exists() == false)loop = false;
		    else c++;
		}

		try {
			FileOutputStream f = new FileOutputStream(file);
			PrintWriter pw = new PrintWriter(f);
			pw.println(logg);
			pw.flush();
			pw.close();
			try {
				f.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		catch (FileNotFoundException e) {
		} 

	}

	//private final Map<ReportField, String> mMapping = new HashMap<ReportField, String>() ;





}
