package ir.kolbe.backgammon;

import ir.kolbe.utils.LocationModel;

import java.util.Stack;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.system.CPUUsage;

public class Checker extends Sprite {
	
	    public boolean color_checker;// true = white & false = black
	    public int roll_position;//1 to 15
	    public int board_position; //1 to 24
	    public float x;//x coordinate position
	    public float y;//y coordinate position
	    
	    

		public Checker(int board_Position,int roll_Position,boolean color, float pX, float pY, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
	        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);
	        this.roll_position = roll_Position;
	        this.board_position = board_Position;
	        this.x = pX;
	        this.y = pY;
	        this.color_checker = color;
	    }

		
	   
	 
	   
}
