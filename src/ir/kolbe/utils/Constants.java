package ir.kolbe.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.util.adt.io.in.IInputStreamOpener;

import android.content.Context;
import android.graphics.Point;
import android.text.GetChars;

public class Constants {

	public static int Dialog_Cantmove_X = 95;
	public static int Dialog_Cantmove_Y = 178;
	
	public static int Button_Undo_X = 698;
	public static int Button_Undo_Y = 188;
	public static LocationModel get_Checker_Location_By_(int board_position, int roll_position)
	{
		LocationModel result = new LocationModel();
		if(board_position == 0)// وقتی موقعیت بردپوزیشن صفر باشه یعنی میخوایم مکان هیت شده های سفید رو بهمون بده
		{
			result.x = 335;
			result.y = 80;
			return result;
		}
		if(board_position == -1)// وقتی موقعیت بردپوزیشن منهای یک باشه یعنی میخوایم مکان هیت شده های سیاه رو بهمون بده
		{
			result.x = 335;
			result.y = 292;
			return result;
		}
		
		if(board_position == 101)
		{
			result.x = 711;
			result.y = (12 - 10) +   (roll_position * 10);
			return result;
		}
		if(board_position == 100)
		{
			result.x = 711;
			result.y = (404)  - (roll_position * 10);
			return result;
		}
		
		
		result.board_Postion = board_position;
		result.roll_Position = roll_position;
		if(board_position <= 12)
		{
			int ydec = 30;
			int c = 1;
			result.x = 683 - (board_position * 50);
			result.y = 406 - (roll_position * ydec);
			if(board_position > 6)
				result.x -= 50; 
		}
		if(board_position > 12)
		{
			int ydec = 30;
			int c = 1;
			result.x = -17 + ((board_position-12) * 50);
			result.y = -15 + (roll_position * ydec);
			if(board_position > 18)
				result.x += 50;
		}
		return result;
	}
	public static LocationModel get_MoveHighlight_Location_By_(byte rc)
	{
		LocationModel res = new LocationModel();
		if(rc>=100)
		{
			if(rc == 101)
			{
				res.x = 710;
				res.y = 12;
			}
			else
				if(rc == 100)
				{
					res.x = 710;
					res.y = 237;
				}
			return res;
		}
		
		
		
		int xx = 680;
		int yy = 240;
		int diff = 50;
		if(rc<=6)
		{
			res.x = (xx - (diff * rc));
			res.y = yy;
			return res;
		}
		if(rc<=12)
		{
			res.x = (xx - (diff * rc));
			res.y = yy;
			res.x = res.x - 50;
			return res;
		}
		xx = -20;
		yy = 15;
		if(rc<=18)
		{
			res.x = (xx + (diff * (rc -12) ));
			res.y = yy;
			return res;
		}
		if(rc<=24)
		{
			res.x = (xx + (diff * (rc -12) ));
			res.y = yy;
			res.x = res.x + 50;
			return res;
		}
		return res;		
	}
	public static byte rollClicked(float x,float y)
	{
		byte rc = -100;
		if(y <=110 && y >= 50 && x >= 335 && x <= 380)// is between 13 to 18
		{
			return 0;
		}
		if(y <=345 && y >= 257 && x >= 335 && x <= 380)// is between 13 to 18
		{
			return -1;
		}
		
		if(y <=200 && y >= 0 && x >= 696 && x <= 768)// is briging off place for black
		{
			return 101;
		}
		if(y <=414 && y >= 216 && x >= 697 && x <= 768)// is briging off place for white
		{
			return 100;
		}
		
		
		
		
		
		if(y <=208 && y >= 15 && x >= 30 && x <= 683)// is between 13 to 24
		{
			if(y <=208 && y >= 15 && x >= 30 && x <= 330)// is between 13 to 18
			{
				int ediff = 330;
				int sdiff = 280;
				if(x <= ediff && x >= sdiff) rc = 18;
				sdiff = sdiff - 50;
				ediff = ediff - 50;
				if(x <= ediff && x >= sdiff) rc = 17;
				sdiff = sdiff - 50;
				ediff = ediff - 50;
				if(x <= ediff && x >= sdiff) rc = 16;
				sdiff = sdiff - 50;
				ediff = ediff - 50;
				if(x <= ediff && x >= sdiff) rc = 15;
				sdiff = sdiff - 50;
				ediff = ediff - 50;
				if(x <= ediff && x >= sdiff) rc = 14;
				sdiff = sdiff - 50;
				ediff = ediff - 50;
				if(x <= ediff && x >= sdiff) rc = 13;

			}else // is between 19 to 24
			{
				int ediff = 683;
				int sdiff = 633;
				if(x <= ediff && x >= sdiff) rc = 24;
				sdiff = sdiff - 50;
				ediff = ediff - 50;
				if(x <= ediff && x >= sdiff) rc = 23;
				sdiff = sdiff - 50;
				ediff = ediff - 50;
				if(x <= ediff && x >= sdiff) rc = 22;
				sdiff = sdiff - 50;
				ediff = ediff - 50;
				if(x <= ediff && x >= sdiff) rc = 21;
				sdiff = sdiff - 50;
				ediff = ediff - 50;
				if(x <= ediff && x >= sdiff) rc = 20;
				sdiff = sdiff - 50;
				ediff = ediff - 50;
				if(x <= ediff && x >= sdiff) rc = 19;
			}
		}else if(y >= 209 && y <= 406 && x >= 30 && x <= 683) // is between 1 to 12
		{
			if(y >= 209 && y <= 406 && x >= 30 && x <= 330)// is between 7 to 12
			{
				int ediff = 330;
				int sdiff = 280;
				if(x <= ediff && x >= sdiff) rc = 7;
				sdiff = sdiff - 50;
				ediff = ediff - 50;
				if(x <= ediff && x >= sdiff) rc = 8;
				sdiff = sdiff - 50;
				ediff = ediff - 50;
				if(x <= ediff && x >= sdiff) rc = 9;
				sdiff = sdiff - 50;
				ediff = ediff - 50;
				if(x <= ediff && x >= sdiff) rc = 10;
				sdiff = sdiff - 50;
				ediff = ediff - 50;
				if(x <= ediff && x >= sdiff) rc = 11;
				sdiff = sdiff - 50;
				ediff = ediff - 50;
				if(x <= ediff && x >= sdiff) rc = 12;
			}
			else // is between 1 to 6
			{
				int ediff = 683;
				int sdiff = 633;
				if(x <= ediff && x >= sdiff) rc = 1;
				sdiff = sdiff - 50;
				ediff = ediff - 50;
				if(x <= ediff && x >= sdiff) rc = 2;
				sdiff = sdiff - 50;
				ediff = ediff - 50;
				if(x <= ediff && x >= sdiff) rc = 3;
				sdiff = sdiff - 50;
				ediff = ediff - 50;
				if(x <= ediff && x >= sdiff) rc = 4;
				sdiff = sdiff - 50;
				ediff = ediff - 50;
				if(x <= ediff && x >= sdiff) rc = 5;
				sdiff = sdiff - 50;
				ediff = ediff - 50;
				if(x <= ediff && x >= sdiff) rc = 6;
			}
		}


		return rc;
	}

}
