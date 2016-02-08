package ir.kolbe.backgammon;

import ir.kolbe.utils.Constants;
import ir.kolbe.utils.CpuMove_Location;
import ir.kolbe.utils.LocationModel;
import ir.kolbe.utils.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.shape.IShape;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.LayoutGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.modifier.IModifier;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Play_h2c extends LayoutGameActivity   {

	LinearLayout lnBoard;
	/*private static int CAMERA_WIDTH = 776;
	private static int CAMERA_HEIGHT = 420;*/
	//710-12**710-237
	int save_stat_counter = 0;
	boolean cpuTurn = false;
	int p1DiceRem = 167,p2DiceRem = 167;
	TextView tv_p1Dice,tv_p2Dice;
	Button btnsave,btntest;
	public static final byte BLACK_PIECE = -1;
	public static final byte WHITE_PIECE = 1;
	public static final byte EMPTY_SPACE = 0;
	private byte[] board2 = { 0,2, 2, 4, 2,	2,2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -4,-2, -3, -2, 1, -4 };
	//private byte[] board = { 0, 2, 2, 0, 0,	0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,0, 0, 0, 0, 0, 0, 0, 0, -2 };
	//private byte[] board = { 0, 2, 1, 2, 1,2,6, 0,0, 0, 0, 0, -1, 0, 0, 0, 0,0, 0, -5,-4, -3, -1,0,-1 };
	private byte[] board = { 0, -2, 0, 0, 0,0, 5, 0, 3, 0, 0, 0, -5, 5, 0, 0, 0, -3, 0, -5, 0, 0, 0, 0, 2 };
	boolean customDice = false;
	private static byte[] board_BackedUp = { 0, -2, 0, 0, 0,0, 5, 0, 3, 0, 0, 0, -5, 5, 0, 0, 0, -3, 0, -5, 0, 0, 0, 0, 2 };
	byte forceDice1 =0;
	byte forceDice2 = 0;
	boolean done = false;
	//-----------------------------------------------------Last State Variables------------------------------------------------------
	int p1DiceRem_1 = 0;
	int p2DiceRem_1 = 0;
	private byte firstDice_l = 0;
	private byte secondDice_l = 0;
	private byte hitBlack_l = 0;
	private byte hitWhite_l = 0;
	private byte takenBlack_l = 0;
	private byte takenWhite_l = 0;
	private boolean dice1_l = false;
	private boolean dice2_l = false;
	private byte joft_l = 4;	
	private boolean playerTurn_l = false;
	private byte[] board_l = { 0, -2, 0, 0, 0,	0, 5, 0, 3, 0, 0, 0, -5, 5, 0, 0, 0, -3, 0, -5, 0, 0, 0, 0, 2 };
	//--------------------------------------------Backup 4 Crash Variables
	public static byte firstDice_b = 0;
	public static byte secondDice_b = 0;
	public static byte firstDice_bn = 0;
	public static byte secondDice_bn = 0;
	public static byte hitBlack_b= 0;
	public static byte hitWhite_b = 0;
	public static byte takenBlack_b = 0;
	public static byte takenWhite_b = 0;
	public static boolean dice1_b = false;
	public static boolean dice2_b = false;
	public static byte joft_b = 4;	
	public static boolean playerTurn_b = false;
	public static byte[] board_b = { 0, -2, 0, 0, 0,	0, 5, 0, 3, 0, 0, 0, -5, 5, 0, 0, 0, -3, 0, -5, 0, 0, 0, 0, 2 };
	//------------------------------------------------------------------------------------------------------------------------------------
	public ArrayList<Byte> lst_PossibleCheckers; // ستون دایره های چشمک زن
	public ArrayList<Byte> lst_PossibleMoves; //محل مثلث های سبز رنگ
	public byte hitWhite = 0;
	public byte hitBlack = 0;
	public byte takenWhite = 0; // are those two
	public byte takenBlack = 0;
	public byte f1=-1,f2=-1,t1=-1,t2=-1,f3=-1,t3=-1,f4=-1,t4=-1;
	public byte selectedCheckerBoardPosition = -1; //selected checker from available checkers to move
	public byte selectedTargetBoardPosition = -1; //selected roll to move there, selected checkers
	public byte firstDice,secondDice;
	//boolean rivalisplaying = false;
	public boolean isShowingPossibleCheckers = false;
	public boolean isShowingPossibleMoves = false;
	boolean playerTurn = false;//T means player1 (White) and F means player2 (Black)
	String player1name = "Player1";
	String player2name = "Player2";
	boolean no_move_yet = true;
	boolean dice1 = false,dice2 = false;
	byte joft = 0; // از یک تا چهار میشه با هر بار حرکت یکی اضافه میشه چهار که شد یعنی چهار حرکتشو رفته
	private static int CAMERA_WIDTH = 776;
	private static int CAMERA_HEIGHT = 420;


	//Texture Regions
	ITextureRegion Dialog_cantmoveTextureRegion,mBackgroundTextureRegion,ChekersTextureRegion_w, ChekersTextureRegion_b,ChekersOutTextureRegion_b,ChekersOutTextureRegion_w, possibleMoves_TextureRegion,possibleMoves_TextureRegion2,BringOff_HighLight_TextureRegion;
	ITextureRegion Iw1,Iw2,Iw3,Iw4,Iw5,Iw6,Iw7,Iw8,Iw9,Iw10,Iw11,Iw12,Iw13,Iw14,Iw15;
	ITextureRegion fc;
	ITextureRegion Ib1,Ib2,Ib3,Ib4,Ib5,Ib6,Ib7,Ib8,Ib9,Ib10,Ib11,Ib12,Ib13,Ib14,Ib15;
	Map<Integer, ITextureRegion> Ib,Iw;
	ITiledTextureRegion tt_btnRollDice,tt_dice,tt_dice2,tt_highlight,tt_undo;
	//Sprites
	ArrayList<Checker> lst_Checkers_Black, lst_Checkers_White;//2*15 for white and black checkers
	ArrayList<FakeChecker> lst_Fake_Checkers;//مهره های عدد دار
	ArrayList<Sprite> lst_sprite_PossibleMoves;
	TiledSprite tsprite_Btn_Roll_Dice, tsprite_Btn_Undo;
	AnimatedSprite asprite_dice1,asprite_dice2;
	ArrayList<AnimatedSprite> lst_asprite_possibleCheckers;

	VertexBufferObjectManager vbom;
	Scene scene;
	Music roll1Sound,roll2Sound,moveSoundNormal,moveSoundHitted;
	Random rnd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//setContentView(R.layout.activity_main);
		
		lnBoard = (LinearLayout) findViewById(R.id.lnboard);
		//lnBoard.setVisibility(View.GONE);
		//StartAnimation();
	}
	/*@Override
	protected void onPause()
	{
	    super.onPause();
	    if (this.isGameLoaded())
	        music.pause();
	}

	@Override
	protected synchronized void onResume()
	{
	    super.onResume();
	    System.gc();
	    if (this.isGameLoaded())
	        music.play(); 
	}*/
	@Override
	protected synchronized void onResume()
	{
	    super.onResume();
	    System.gc();
	    if (this.isGameLoaded())
	        StartAnimation();
	}
	boolean animLocked = false;
	private void StartAnimation()
	{
		animLocked = true;
		Resources r = getResources();
		lnBoard.clearAnimation();
		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, r.getDisplayMetrics());

			TranslateAnimation translatee = new TranslateAnimation(Animation.ABSOLUTE,0,Animation.ABSOLUTE,0,Animation.ABSOLUTE,0,Animation.ABSOLUTE,(px));
			//TranslateAnimation translatee2 = new TranslateAnimation(Animation.ABSOLUTE,0,Animation.ABSOLUTE,0,Animation.ABSOLUTE,0,Animation.ABSOLUTE,(px * -1));
			translatee.setDuration(1500);
			//translatee2.setDuration(300);
			translatee.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
					lnBoard.setVisibility(View.VISIBLE);
				}
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				@Override
				public void onAnimationEnd(Animation animation) {
					
					animLocked = false;
				}
			});
			lnBoard.startAnimation(translatee);
			//lnz.startAnimation(translatee2);
		

	}
	@Override
	public EngineOptions onCreateEngineOptions() {
		// TODO Auto-generated method stub
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		EngineOptions eo = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, 
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
		eo.getAudioOptions().setNeedsMusic(true);
		return  eo;
		//return null;
	}




	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		// 1 - Create new scene

		//Creat Background Sprite
		Sprite backgroundSprite = new Sprite(0, 0, this.mBackgroundTextureRegion, vbom);
		//Attach Background Sprite to the scene
		scene.attachChild(backgroundSprite);

		lst_Checkers_Black = new ArrayList<Checker>();
		lst_Checkers_White = new ArrayList<Checker>();
		LocationModel loc = null;
		for(byte i=0;i<=24;i++)
		{
			byte x = board[i];
			if(x > 0)
			{
				boolean updateFake = false;
				if(x > 5) updateFake = true;
				for(byte j=1;j<=x;j++)
				{
					if(j>5)
						loc = Constants.get_Checker_Location_By_(i,5);
					else
						loc = Constants.get_Checker_Location_By_(i,j);
					Checker tmp = new Checker(loc.board_Postion,j,true, loc.x, loc.y, this.ChekersTextureRegion_w.deepCopy(), vbom);
					lst_Checkers_White.add(tmp);
					scene.attachChild(tmp);
					if(j == x)
					{
						if(updateFake)
						{
							UpdateRollAtBeginning(i,true);
						}
					}



				}
			}else if(x < 0)
			{
				boolean updateFake = false;
				x = (byte) Math.abs(x);
				if(x > 5) updateFake = true;
				for(byte j=1;j<=x;j++)
				{
					if(j>5)
						loc = Constants.get_Checker_Location_By_(i,5);
					else
						loc = Constants.get_Checker_Location_By_(i,j);
					Checker tmp = new Checker(loc.board_Postion,j,false, loc.x, loc.y, this.ChekersTextureRegion_b.deepCopy(), vbom);
					lst_Checkers_Black.add(tmp);
					scene.attachChild(tmp);
					if(j == x)
					{
						if(updateFake)
						{
							UpdateRollAtBeginning(i,false);
						}
					}
				}

			}
		}



		ShowRollDiceButton();
		scene.setOnSceneTouchListener(new IOnSceneTouchListener() {

			@Override
			public boolean onSceneTouchEvent(Scene pScene,TouchEvent pSceneTouchEvent) {

				if(pSceneTouchEvent.isActionUp())
				{
					boolean ok = false;
					if(isShowingPossibleCheckers)
					{
						selectedCheckerBoardPosition = Constants.rollClicked(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
						if(selectedCheckerBoardPosition == -100) return true;
						for(int i=0;i<lst_PossibleCheckers.size();i++)
						{
							int r = (int)selectedCheckerBoardPosition;
							if(r == lst_PossibleCheckers.get(i))
							{
								selectedCheckerBoardPosition = (byte)r;
								isShowingPossibleCheckers = false;
								showPossibleMoves(selectedCheckerBoardPosition);
							}
						}
						return true;
					}else if(isShowingPossibleMoves)
					{

						selectedTargetBoardPosition = Constants.rollClicked(pSceneTouchEvent.getX(), pSceneTouchEvent.getY());

						int r = (int)selectedTargetBoardPosition;
						for(int i=0;i<lst_PossibleMoves.size();i++)
						{

							if(r == lst_PossibleMoves.get(i))
							{
								ok = 	runandcalc();
								break;
							}
						}
						if(!ok)
						{
							showPossibleCheckers();
						}
						return true;
					}else
					{
						float x = pSceneTouchEvent.getX();
						float y = pSceneTouchEvent.getY();
						if(x > 680 && y > 215)
						{
							//LoadFake();
						}
					}
					//showMessage("Roll " + res + " Clicked!");
				}
				return false;
			}
		});
		scene.setTouchAreaBindingOnActionDownEnabled(true);

		pOnCreateSceneCallback.onCreateSceneFinished(scene);
	}
	private boolean runandcalc()
	{
		boolean ok = false;
		int toMinus = 0;//عددی که باید از صد و شصت و هفت یا هرچی که الان شده کم بشه
		SetLast();
		isShowingPossibleMoves = false;
		isShowingPossibleCheckers = false;
		if(lst_sprite_PossibleMoves != null)
		{
			for(int j = 0;j<lst_sprite_PossibleMoves.size();j++)//مخفی کردن همه مثلث های سبز رنگ در این حلقه فور
			{
				Sprite s = lst_sprite_PossibleMoves.get(j);
				s.detachSelf();
				if(!s.isDisposed())
					s.dispose();
			}
			lst_sprite_PossibleMoves.clear();
		}

		if(firstDice != secondDice)
		{
			if(playerTurn && hitWhite > 0)
			{
				if((25 - firstDice)  == selectedTargetBoardPosition) {dice1 = true;toMinus = firstDice;}
				if((25 - secondDice)  == selectedTargetBoardPosition) {dice2 = true;toMinus = secondDice;}

			}else
				if(!playerTurn && hitBlack > 0)
				{

					if((firstDice)  == selectedTargetBoardPosition) {dice1 = true;toMinus = firstDice;}
					if((secondDice)  == selectedTargetBoardPosition) {dice2 = true;toMinus = secondDice;}
				}else
				{
					if(selectedTargetBoardPosition <100)
					{
						int diff = Math.abs(selectedCheckerBoardPosition - selectedTargetBoardPosition);
						if(firstDice == diff) {dice1 = true;toMinus = firstDice;}
						if(secondDice == diff) {dice2 = true;toMinus = secondDice;}
						if((firstDice+secondDice) == diff){dice1 = true;dice2 = true;toMinus = (firstDice+secondDice);}
					}else
					{

						if(playerTurn) 
							toMinus = selectedCheckerBoardPosition;
						else 
							toMinus = 25 - selectedCheckerBoardPosition;
						if(d4) {dice1 = true;dice2 = true;}
						else
							if(dice1 == false && dice2 == false && d1 && d2) {dice1 = true;}
							else
								if(d1){dice1 = true;}
								else
									if(d2) {dice2 = true;}
					}
				}
		}else //joft
		{
			if(playerTurn)
			{
				if(  hitWhite == 0)
				{
					if(selectedTargetBoardPosition <100)
					{
						int diff = Math.abs(selectedCheckerBoardPosition - selectedTargetBoardPosition);
						if(diff == firstDice) 	{joft++;toMinus = firstDice;}
						if(diff == (firstDice * 2)) 	{joft+=2;toMinus = (firstDice*2);}
						if(diff == (firstDice * 3)) 	{joft+=3;toMinus = (firstDice*3);}
						if(diff == (firstDice * 4)) 	{joft+=4;toMinus = (firstDice*4);}
					}else
					{
						toMinus = selectedCheckerBoardPosition;
						joft++;
					}
				}else{joft++;toMinus = firstDice;}
			}
			else
			{
				if(  hitBlack == 0)
				{
					if(selectedTargetBoardPosition <100)
					{
						int diff = Math.abs(selectedCheckerBoardPosition - selectedTargetBoardPosition);
						if(diff == firstDice) 	{joft++;toMinus = firstDice;}
						if(diff == (firstDice * 2)) {joft+=2;toMinus = (firstDice*2);}
						if(diff == (firstDice * 3)) 	{joft+=3;toMinus = (firstDice*3);}
						if(diff == (firstDice * 4)) 	{joft+=4;toMinus = (firstDice*4);}
					}
					else
					{
						toMinus = 25 - selectedCheckerBoardPosition;
						joft++;
					}
				}else{joft++;toMinus = firstDice;}
			}
		}
		CreateDicesAgainWithCurrentStatus(false);
		move_a_checker();
		ok = true;
		WriteText(toMinus,0);	
		return ok;

	}
	private void CreateDicesAgainWithCurrentStatus(boolean fromCancel)
	{
		hideDices();
		if(firstDice != secondDice)
		{
			if(dice1 == false) 
			{
				asprite_dice1 = new AnimatedSprite(455, 178, tt_dice.deepCopy(), vbom);
				asprite_dice1.setCurrentTileIndex(firstDice + 11);
				scene.attachChild(asprite_dice1);
			}

			if(dice2 == false) 
			{
				asprite_dice2 = new AnimatedSprite(555, 178, tt_dice.deepCopy(), vbom);
				asprite_dice2.setCurrentTileIndex(secondDice + 11);
				scene.attachChild(asprite_dice2);
			}

		}
		else //joft
		{
			if(joft == 0)
			{
				asprite_dice1 = new AnimatedSprite(455, 178, tt_dice.deepCopy(), vbom);
				asprite_dice1.setCurrentTileIndex(firstDice + 11);
				scene.attachChild(asprite_dice1);

				asprite_dice2 = new AnimatedSprite(555, 178, tt_dice.deepCopy(), vbom);
				asprite_dice2.setCurrentTileIndex(secondDice + 11);
				scene.attachChild(asprite_dice2);
			}

			if(joft == 1)
			{
				//asprite_dice1 = 50%
				/*	asprite_dice1 = new AnimatedSprite(455, 178, tt_dice.deepCopy(), vbom);
				asprite_dice1.setCurrentTileIndex(firstDice + 11);
				asprite_dice1.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
				if(fromCancel)
					asprite_dice1.registerEntityModifier(new AlphaModifier(0,1,0.5f));
				else asprite_dice1.registerEntityModifier(new AlphaModifier(.3f,1,0.5f));*/

				asprite_dice1 = new AnimatedSprite(455, 178, tt_dice2.deepCopy(), vbom);
				asprite_dice1.setCurrentTileIndex(firstDice + 11);
				scene.attachChild(asprite_dice1);

				//asprite_dice2 = 100%
				asprite_dice2 = new AnimatedSprite(555, 178, tt_dice.deepCopy(), vbom);
				asprite_dice2.setCurrentTileIndex(secondDice + 11);
				scene.attachChild(asprite_dice2);
			}
			if(joft == 2)
			{
				//asprite_dice2 = 100%
				asprite_dice2 = new AnimatedSprite(555, 178, tt_dice.deepCopy(), vbom);
				asprite_dice2.setCurrentTileIndex(secondDice + 11);
				scene.attachChild(asprite_dice2);
			}
			if(joft == 3)
			{ //asprite_dice2 = 50%

				/*asprite_dice2 = new AnimatedSprite(555, 178, tt_dice.deepCopy(), vbom);
				asprite_dice2.setCurrentTileIndex(secondDice + 11);
				asprite_dice2.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
				if(fromCancel)
					asprite_dice2.registerEntityModifier(new AlphaModifier(0,1,0.5f));
				else asprite_dice2.registerEntityModifier(new AlphaModifier(.3f,1,0.5f));*/

				asprite_dice2 = new AnimatedSprite(555, 178, tt_dice2.deepCopy(), vbom);
				asprite_dice2.setCurrentTileIndex(secondDice + 11);
				scene.attachChild(asprite_dice2);

			}
		}
	}
	private void HideUndo()
	{
		if(tsprite_Btn_Undo != null)
		{
			scene.detachChild(tsprite_Btn_Undo);
			scene.unregisterTouchArea(tsprite_Btn_Undo);
			tsprite_Btn_Undo.detachSelf();
			if(!tsprite_Btn_Undo.isDisposed())
				tsprite_Btn_Undo.dispose();
		}
	}
	private void ShowUndo()
	{
		HideUndo();
		tsprite_Btn_Undo = new TiledSprite(Constants.Button_Undo_X, Constants.Button_Undo_Y, this.tt_undo.deepCopy(), vbom)
		{
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,final float pTouchAreaLocalX,final float pTouchAreaLocalY) {    
				switch(pSceneTouchEvent.getAction()) {
				case TouchEvent.ACTION_DOWN:
					if(tsprite_Btn_Undo != null)
					{
						tsprite_Btn_Undo.setCurrentTileIndex(1);
					}
					break;
				case TouchEvent.ACTION_MOVE:
					break;
				case TouchEvent.ACTION_UP:

					if(tsprite_Btn_Undo != null)
					{
						scene.detachChild(tsprite_Btn_Undo);
						scene.unregisterTouchArea(tsprite_Btn_Undo);
						tsprite_Btn_Undo.detachSelf();
						if(!tsprite_Btn_Undo.isDisposed())
							tsprite_Btn_Undo.dispose();
						LoadLast();
					}
					break;

				}
				return true;
			}
		};
		tsprite_Btn_Undo.setCurrentTileIndex(0);
		scene.attachChild(tsprite_Btn_Undo);
		scene.registerTouchArea(tsprite_Btn_Undo);
	}
	private void HideRollDiceButton()
	{

		scene.unregisterTouchArea(tsprite_Btn_Roll_Dice);
		tsprite_Btn_Roll_Dice.detachSelf();
		if(!tsprite_Btn_Roll_Dice.isDisposed())
			tsprite_Btn_Roll_Dice.dispose();
	}
	private void ShowRollDiceButton()
	{

		/*	if(no_move_yet == false && playerTurn == cpuTurn)
		{
			HideRollDiceButton();
			Roll_Dice();
		}
		else
		{*/
		//CreateDicesAgainWithCurrentStatus(true);
		tsprite_Btn_Roll_Dice = new TiledSprite(118, 176, this.tt_btnRollDice, vbom)
		{
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,final float pTouchAreaLocalX,final float pTouchAreaLocalY) {    
				switch(pSceneTouchEvent.getAction()) {
				case TouchEvent.ACTION_DOWN:
					HideUndo();
					if(tsprite_Btn_Roll_Dice != null)
					{

						tsprite_Btn_Roll_Dice.setCurrentTileIndex(1);
						//hideDices();
					}
					break;
				case TouchEvent.ACTION_MOVE:
					break;
				case TouchEvent.ACTION_UP:

					if(tsprite_Btn_Roll_Dice != null)
					{
						HideRollDiceButton();
						Roll_Dice();
					}
					break;

				}
				return true;
			}
		};
		tsprite_Btn_Roll_Dice.setCurrentTileIndex(0);
		scene.attachChild(tsprite_Btn_Roll_Dice);
		scene.registerTouchArea(tsprite_Btn_Roll_Dice);
		//}
	}
	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		// TODO Auto-generated method stub
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}


	@Override
	protected int getLayoutID() {
		// TODO Auto-generated method stub
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		return R.layout.activity_main;
	}

	@Override
	protected int getRenderSurfaceViewID() {
		// TODO Auto-generated method stub
		return R.id.xml_rendersurfaceview;
	}
	int c = 1;
	private void Roll_Dice()
	{

		if( playerTurn == cpuTurn)
		{
			HideUndo();

		}
		firstDice_b = firstDice;
		secondDice_b = secondDice;
		hitBlack_b= hitBlack;
		hitWhite_b = hitWhite;
		takenBlack_b = takenBlack;
		takenWhite_b = takenWhite;
		dice1_b = dice1;
		dice2_b = dice2;
		joft_b = joft;	
		playerTurn_b = playerTurn;
		for(byte i = 0;i<=24;i++)
		{
			board_b[i] = board[i];
		}
		/*try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/


		try
		{
			roll1Sound = MusicFactory.createMusicFromAsset(mEngine.getMusicManager(), this,"mfx/roll.ogg");
			roll2Sound = MusicFactory.createMusicFromAsset(mEngine.getMusicManager(), this,"mfx/roll.ogg");

		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		//tsprite_Btn_Roll_Dice.detachSelf();
		//tsprite_Btn_Roll_Dice.dispose();
		int du = 40;


		asprite_dice1 = new AnimatedSprite(455, 178, tt_dice.deepCopy(), vbom);
		asprite_dice2 = new AnimatedSprite(555, 178, tt_dice.deepCopy(), vbom);

		/*	long[] frameDurration = {du,du,du, du, du,du,du,du,du,du,du,du,du,du,du,du,du,du,du,du,du, du, du,du,du,du,du,du};
		int[] frames = {1,0, 2,10,5,11,3,7,6,8,9,4,1,0,2,10,5,11,1,0, 2,10,5,11,3,7,6,8};*/
		long[] frameDurration = {du,du,du, du, du,du,du,du,du,du,du,du,du,du,du,du,du,du,du,du,du, du, du,du};
		int[] frames = {1,0, 2,10,5,11,3,7,6,8,9,4,1,0,2,10,5,11,1,0, 2,10,5,11};
		//as.animate(frameDurration);
		//rnd = new Random(System.currentTimeMillis());
		rnd = new Random();
		int r1 = rnd.nextInt(6 - 1 + 1) + 1;
		int r2 = rnd.nextInt(6 - 1 + 1) + 1;

		if(no_move_yet)
		{
			while (r1 == r2) {
				r2 = rnd.nextInt(6 - 1 + 1) + 1;
			}
		}
		/*else
		{
			while (r1 != r2) {
				r2 = rnd.nextInt(6 - 1 + 1) + 1;
			}
		}*/

		if(forceDice1 != 0) firstDice = forceDice1;
		if(forceDice2 != 0) secondDice = forceDice2;

		firstDice = (byte) r1;
		secondDice = (byte) r2;
		dice1 = false;
		dice2 = false;
		if(!done)
		{
			if(forceDice1 != 0) firstDice = forceDice1;
			if(forceDice2 != 0) secondDice = forceDice2;
			done = true;
		}
		if(customDice)
		{
			if(c == 1)
			{
				firstDice = 1;
				secondDice =3;
			}
			if(c == 2)
			{
				firstDice = 2;
				secondDice =3;
			}
			if(c ==3)
			{
				firstDice = 3;
				secondDice = 2;
			}
			if(c ==4)
			{
				firstDice = 2;
				secondDice = 5;
			}
		}




		firstDice_bn = firstDice;
		secondDice_bn = secondDice;

		if(r1== r2) joft = 0;
		c++;
		roll1Sound.play();
		asprite_dice1.animate(frameDurration, frames, false, new IAnimationListener() {

			@Override
			public void onAnimationStarted(AnimatedSprite pAnimatedSprite,
					int pInitialLoopCount) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite,
					int pRemainingLoopCount, int pInitialLoopCount) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite,
					int pOldFrameIndex, int pNewFrameIndex) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
				// TODO Auto-generated method stub
				asprite_dice1.setCurrentTileIndex(firstDice + 11);
				roll1Sound.stop();
				roll1Sound.release();

			}
		});
		roll2Sound.play();
		asprite_dice2.animate(frameDurration, frames, false, new IAnimationListener() {

			@Override
			public void onAnimationStarted(AnimatedSprite pAnimatedSprite,
					int pInitialLoopCount) {
				// TODO Auto-generated method stub


			}

			@Override
			public void onAnimationLoopFinished(AnimatedSprite pAnimatedSprite,
					int pRemainingLoopCount, int pInitialLoopCount) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationFrameChanged(AnimatedSprite pAnimatedSprite,
					int pOldFrameIndex, int pNewFrameIndex) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationFinished(AnimatedSprite pAnimatedSprite) {
				// TODO Auto-generated method stub
				asprite_dice2.setCurrentTileIndex(secondDice + 11);
				roll2Sound.stop();
				roll2Sound.release();
				if(no_move_yet && !customDice)
				{

					final String startername = "";
					if(firstDice> secondDice)
					{
						playerTurn = true;
						showDiceWinnerDialog(player1name);

					}
					else 
					{
						playerTurn = false;
						showDiceWinnerDialog(player2name);

					}
				}else
				{

					showPossibleCheckers();

				}
			}
		});
		scene.attachChild(asprite_dice1);
		scene.attachChild(asprite_dice2);
	}
	private void move_a_checker()
	{
		boolean Hit_Happened = false;
		Checker checker_sprite = null;
		byte x=0,xx=0,y=0,yy=0;
		if(f1 == -1)
		{
			f1 = selectedCheckerBoardPosition;
			t1 = selectedTargetBoardPosition;
		}
		else if(f2== -1)
		{
			f2 = selectedCheckerBoardPosition;
			t2 = selectedTargetBoardPosition;
		}
		else if(f3== -1)
		{
			f3 = selectedCheckerBoardPosition;
			t3 = selectedTargetBoardPosition;
		}
		else if(f4== -1)
		{
			f4 = selectedCheckerBoardPosition;
			t4 = selectedTargetBoardPosition;
		}
		/*if(selectedTargetBoardPosition < 100)
		{*/
		if(selectedTargetBoardPosition < 100)
		{
			y = (byte)Math.abs(board[selectedTargetBoardPosition]);
			yy = (byte)board[selectedTargetBoardPosition];
		}
		else 
		{
			if(selectedTargetBoardPosition == 100) y = takenWhite;
			if(selectedTargetBoardPosition == 101) y = takenBlack;
		}
		if(selectedCheckerBoardPosition == 0 || selectedCheckerBoardPosition  == -1)//یعنی اگه قراره از قسمت خورده ها مهره حرکت کنه و بیاد داخل
		{
			x = 0;
		}else
		{
			x = (byte) Math.abs(board[selectedCheckerBoardPosition]);
			xx = (byte)board[selectedCheckerBoardPosition];

		}
		if(selectedCheckerBoardPosition > 0)
		{
			if(      (xx * yy) < 0 ) Hit_Happened = true;
		}
		else 
		{
			if(selectedCheckerBoardPosition == 0)
				if(board[selectedTargetBoardPosition] == -1) Hit_Happened = true;
			if(selectedCheckerBoardPosition == -1)
				if(board[selectedTargetBoardPosition]  == 1) Hit_Happened = true;
		}

		if(playerTurn)
		{

			if(Hit_Happened)
			{
				if(selectedCheckerBoardPosition == 0)//یعنی اگه سفیده قبلا خورده بوده و وارد زمین داره میشه
				{
					hitBlack++;
					hitWhite--;
					board[selectedTargetBoardPosition] = 1;
					WriteText(0, selectedTargetBoardPosition);
				}else
				{
					hitBlack++;
					board[selectedCheckerBoardPosition] = (byte) (x-1);
					board[selectedTargetBoardPosition] = 1;
					WriteText(0, selectedTargetBoardPosition);
				}
			}
			else
			{
				if(selectedCheckerBoardPosition == 0)//یعنی اگه سفیده قبلا خورده بوده و وارد زمین داره میشه
				{
					hitWhite--;
					board[selectedTargetBoardPosition] = (byte) (y+1);
				}
				else
				{
					board[selectedCheckerBoardPosition] = (byte) (x-1);
					if(selectedTargetBoardPosition <100)
						board[selectedTargetBoardPosition] = (byte) (y+1);
					else takenWhite++;
				}
			}

			for(int i=0;i<lst_Checkers_White.size();i++)
			{
				checker_sprite = lst_Checkers_White.get(i);
				if(checker_sprite.board_position == selectedCheckerBoardPosition && checker_sprite.roll_position == x)
				{
					//ایجاد یک چکر جدید بجای قبلی که وقتی حرکت میکنه از روی همه حرکت کنه
					Checker newOne = null;
					newOne = new Checker(checker_sprite.board_position, checker_sprite.roll_position, checker_sprite.color_checker, checker_sprite.x, checker_sprite.y, ChekersTextureRegion_w.deepCopy(), vbom);
					lst_Checkers_White.remove(i);
					lst_Checkers_White.add(i, newOne);
					checker_sprite.detachSelf();
					if(!checker_sprite.isDisposed())
						checker_sprite.dispose();
					scene.attachChild(newOne);
					checker_sprite = lst_Checkers_White.get(i);
					// اتمام ایجاد چکر جدید
					if(selectedTargetBoardPosition >=100)
					{
						y++;
						checker_sprite.board_position = selectedTargetBoardPosition;
						LocationModel lm;
						checker_sprite.roll_position = y;
						lm = Constants.get_Checker_Location_By_(selectedTargetBoardPosition, y);
						checker_sprite.x = lm.x;
						checker_sprite.y = lm.y;
						UpdateRollNumber(selectedCheckerBoardPosition,false);
						moveChecker(selectedTargetBoardPosition, checker_sprite,Hit_Happened,true,(byte)i);
					}else
						if(y<5)
						{
							y++;
							checker_sprite.board_position = selectedTargetBoardPosition;

							LocationModel lm;

							if(Hit_Happened)
							{
								checker_sprite.roll_position = 1;
								lm = Constants.get_Checker_Location_By_(selectedTargetBoardPosition, 1);
							}else
							{
								checker_sprite.roll_position = y;
								lm = Constants.get_Checker_Location_By_(selectedTargetBoardPosition, y);
							}


							checker_sprite.x = lm.x;
							checker_sprite.y = lm.y;
							UpdateRollNumber(selectedCheckerBoardPosition,false);
							moveChecker(selectedTargetBoardPosition, checker_sprite,Hit_Happened,false,(byte)i);
						}else
						{
							y++;
							checker_sprite.board_position = selectedTargetBoardPosition;
							checker_sprite.roll_position = y;
							LocationModel lm = Constants.get_Checker_Location_By_(selectedTargetBoardPosition, 5);
							checker_sprite.x = lm.x;
							checker_sprite.y = lm.y;
							UpdateRollNumber(selectedCheckerBoardPosition,false);
							moveChecker(selectedTargetBoardPosition, checker_sprite,false,false,(byte)i);
						}
					break;
				}
			}


		}else
		{
			if(Hit_Happened)
			{

				if(selectedCheckerBoardPosition == -1)//یعنی اگه سیاه قبلا خورده بوده و وارد زمین داره میشه
				{
					hitWhite++;
					hitBlack--;
					board[selectedTargetBoardPosition] = -1;
					WriteText(0, 25 -  selectedTargetBoardPosition);
				}else
				{
					hitWhite++;
					board[selectedCheckerBoardPosition] = (byte) (-1 *  (x-1));
					board[selectedTargetBoardPosition] = -1;
					WriteText(0, 25 -  selectedTargetBoardPosition);
				}
			}
			else
			{
				if(selectedCheckerBoardPosition == -1)//یعنی اگه سیاه قبلا خورده بوده و وارد زمین داره میشه
				{
					hitBlack--;
					board[selectedTargetBoardPosition] = (byte) (-1 *(y+1));
				}
				else
				{
					board[selectedCheckerBoardPosition] = (byte)(-1 *  (x-1));
					if(selectedTargetBoardPosition <100)
						board[selectedTargetBoardPosition] = (byte) (-1 *(y+1));
					else takenBlack++;

				}
			}
			for(int i=0;i<lst_Checkers_Black.size();i++)
			{
				checker_sprite = lst_Checkers_Black.get(i);
				if(checker_sprite.board_position == selectedCheckerBoardPosition && checker_sprite.roll_position == x)
				{
					Checker newOne = null;
					newOne = new Checker(checker_sprite.board_position, checker_sprite.roll_position, checker_sprite.color_checker, checker_sprite.x, checker_sprite.y, ChekersTextureRegion_b.deepCopy(), vbom);
					lst_Checkers_Black.remove(i);
					lst_Checkers_Black.add(i, newOne);
					checker_sprite.detachSelf();
					if(!checker_sprite.isDisposed())
						checker_sprite.dispose();
					scene.attachChild(newOne);
					checker_sprite = lst_Checkers_Black.get(i);

					if(selectedTargetBoardPosition >=100)
					{
						y++;
						checker_sprite.board_position = selectedTargetBoardPosition;
						LocationModel lm;
						checker_sprite.roll_position = y;
						lm = Constants.get_Checker_Location_By_(selectedTargetBoardPosition, y);
						checker_sprite.x = lm.x;
						checker_sprite.y = lm.y;
						UpdateRollNumber(selectedCheckerBoardPosition,false);
						moveChecker(selectedTargetBoardPosition, checker_sprite,Hit_Happened,true,(byte)i);
					}
					else
						if(y<5)
						{
							y++;
							checker_sprite.board_position = selectedTargetBoardPosition;
							LocationModel lm;
							if(Hit_Happened)
							{
								checker_sprite.roll_position = 1;
								lm = Constants.get_Checker_Location_By_(selectedTargetBoardPosition, 1);
							}
							else
							{
								checker_sprite.roll_position = y;
								lm = Constants.get_Checker_Location_By_(selectedTargetBoardPosition, y);
							}
							checker_sprite.x = lm.x;
							checker_sprite.y = lm.y;
							UpdateRollNumber(selectedCheckerBoardPosition,false);
							moveChecker(selectedTargetBoardPosition, checker_sprite,Hit_Happened,false,(byte)i);

							//remove dice and moves and then show roll dice button
						}else
						{
							y++;
							checker_sprite.board_position = selectedTargetBoardPosition;
							checker_sprite.roll_position = y;
							LocationModel lm = Constants.get_Checker_Location_By_(selectedTargetBoardPosition, 5);
							checker_sprite.x = lm.x;
							checker_sprite.y = lm.y;
							UpdateRollNumber(selectedCheckerBoardPosition,false);
							moveChecker(selectedTargetBoardPosition, checker_sprite,false,false,(byte)i);
						}
					break;
				}
			}
		}

	}
	private void UpdateTakenPlace(final byte brd)
	{

	}
	private void UpdateRollNumber(final byte brd,final boolean checker_Move_AnimationEnd_Call_This_Method)
	{

		mEngine.runOnUpdateThread(new Runnable() {

			@Override
			public void run() {

				if(brd < 1) 
				{
					UpdateHitNumber(playerTurn,false);
					return;
				}
				byte num = board[brd];
				num = (byte) Math.abs(num);
				if(num<=4)
				{
					if(checker_Move_AnimationEnd_Call_This_Method) check_if_PlayerTurn_finished();
					return;
				}
				else
				{
					//پاک کردن چکرهای عددی احتمالی
					if(lst_Fake_Checkers != null)
						if(lst_Fake_Checkers.size()>0)
							for(int i=0;i<lst_Fake_Checkers.size();i++)
							{
								FakeChecker f = lst_Fake_Checkers.get(i);
								if(f.board_position == brd)
								{
									f.detachSelf();
									if(!f.isDisposed())
										f.dispose();
									lst_Fake_Checkers.remove(i);
									break;
								}
							}
					if(num == 5) 
					{
						if(checker_Move_AnimationEnd_Call_This_Method)	check_if_PlayerTurn_finished();
						return;
					}
					LocationModel lm = Constants.get_Checker_Location_By_(brd, 5);
					if(lst_Fake_Checkers == null) lst_Fake_Checkers = new ArrayList<FakeChecker>();
					FakeChecker tmp;
					//ساخت چکر عددی لازم
					ITextureRegion it = null;
					if(playerTurn)
					{
						if(num == 6 )it = Iw6.deepCopy();if(num == 7 )it = Iw7.deepCopy();if(num ==8 )it = Iw8.deepCopy();if(num == 9 )it = Iw9.deepCopy();
						if(num == 10 )it = Iw10.deepCopy();if(num == 11 )it = Iw11.deepCopy();if(num ==12 )it = Iw12.deepCopy();if(num ==13 )it = Iw13.deepCopy();
						if(num == 14 )it = Iw14.deepCopy();if(num == 15 )it = Iw15.deepCopy();

					}
					else
					{
						if(num == 6 )it = Ib6.deepCopy();if(num == 7 )it = Ib7.deepCopy();if(num ==8 )it = Ib8.deepCopy();if(num == 9 )it = Ib9.deepCopy();
						if(num == 10 )it = Ib10.deepCopy();if(num == 11 )it = Ib11.deepCopy();if(num ==12 )it = Ib12.deepCopy();if(num ==13 )it = Ib13.deepCopy();
						if(num == 14 )it = Ib14.deepCopy();if(num == 15 )it = Ib15.deepCopy();
					}

					tmp = new FakeChecker(brd,false, lm.x, lm.y,it, vbom);
					lst_Fake_Checkers.add(tmp);
					scene.attachChild(tmp);

				}
				if(checker_Move_AnimationEnd_Call_This_Method) check_if_PlayerTurn_finished();
			}
		});

	}
	private void UpdateRollAtBeginning(final byte brd, final boolean isWhite)
	{
		mEngine.runOnUpdateThread(new Runnable() {

			@Override
			public void run() {


				byte num = board[brd];
				num = (byte) Math.abs(num);

				//پاک کردن چکرهای عددی احتمالی
				if(lst_Fake_Checkers != null)
					if(lst_Fake_Checkers.size()>0)
						for(int i=0;i<lst_Fake_Checkers.size();i++)
						{
							FakeChecker f = lst_Fake_Checkers.get(i);
							if(f.board_position == brd)
							{
								f.detachSelf();
								if(!f.isDisposed())
									f.dispose();
								lst_Fake_Checkers.remove(i);
								break;
							}
						}

				LocationModel lm = Constants.get_Checker_Location_By_(brd, 5);
				if(lst_Fake_Checkers == null) lst_Fake_Checkers = new ArrayList<FakeChecker>();
				FakeChecker tmp;
				//ساخت چکر عددی لازم
				ITextureRegion it = null;
				if(isWhite)
				{
					if(num == 6 )it = Iw6.deepCopy();if(num == 7 )it = Iw7.deepCopy();if(num ==8 )it = Iw8.deepCopy();if(num == 9 )it = Iw9.deepCopy();
					if(num == 10 )it = Iw10.deepCopy();if(num == 11 )it = Iw11.deepCopy();if(num ==12 )it = Iw12.deepCopy();if(num ==13 )it = Iw13.deepCopy();
					if(num == 14 )it = Iw14.deepCopy();if(num == 15 )it = Iw15.deepCopy();

				}
				else
				{
					if(num == 6 )it = Ib6.deepCopy();if(num == 7 )it = Ib7.deepCopy();if(num ==8 )it = Ib8.deepCopy();if(num == 9 )it = Ib9.deepCopy();
					if(num == 10 )it = Ib10.deepCopy();if(num == 11 )it = Ib11.deepCopy();if(num ==12 )it = Ib12.deepCopy();if(num ==13 )it = Ib13.deepCopy();
					if(num == 14 )it = Ib14.deepCopy();if(num == 15 )it = Ib15.deepCopy();
				}

				tmp = new FakeChecker(brd,false, lm.x, lm.y,it, vbom);
				if(lst_Fake_Checkers == null) lst_Fake_Checkers = new ArrayList<FakeChecker>();
				lst_Fake_Checkers.add(tmp);
				scene.attachChild(tmp);


			}
		});

	}



	private void showDiceWinnerDialog(final String name)
	{

		showMessage(name + " "+  getResources().getString(R.string.winnerdialog));
		if(playerTurn == cpuTurn)
		{
			this.getEngine().registerUpdateHandler(new TimerHandler(3, new ITimerCallback()
			{                      
				@Override
				public void onTimePassed(final TimerHandler pTimerHandler)
				{
					no_move_yet = false;
					showPossibleCheckers();
				}
			}));

		}else
		{
			no_move_yet = false;
			showPossibleCheckers();
		}

		//startername + " "+  getResources().getString(R.string.winnerdialog)
	}
	private void showPossibleMoves(byte rc)
	{
		for(int i=0;i<lst_asprite_possibleCheckers.size();i++)
		{
			AnimatedSprite as = lst_asprite_possibleCheckers.get(i);
			as.detachSelf();
			if(!as.isDisposed())
				as.dispose();
		}
		if(lst_asprite_possibleCheckers!= null)
			lst_asprite_possibleCheckers.clear();
		if(lst_sprite_PossibleMoves != null)
			if(lst_sprite_PossibleMoves.size() > 0)
			{
				for(int i=0;i<lst_sprite_PossibleMoves.size();i++)
				{
					Sprite s = lst_sprite_PossibleMoves.get(i);
					s.detachSelf();
					if(!s.isDisposed())
						s.dispose();
				}
			}
		if(lst_sprite_PossibleMoves != null)
			lst_sprite_PossibleMoves.clear();
		if(lst_sprite_PossibleMoves == null)
			lst_sprite_PossibleMoves = new ArrayList<Sprite>();

		if(lst_PossibleMoves != null)
			if(lst_PossibleMoves.size()>0)
				lst_PossibleMoves.clear();

		lst_PossibleMoves = CalcallPossibleMoves((byte)rc);
		for(int i=0;i<lst_PossibleMoves.size();i++)
		{
			int x = lst_PossibleMoves.get(i);
			LocationModel lm = Constants.get_MoveHighlight_Location_By_((byte)x);	
			Sprite sp;

			if(x >=100)//موقعیت صد واسه جایگاه جمع کردن مهره های سفید و صد و یک  مهره های سیاه
			{
				sp = new Sprite(lm.x, lm.y, BringOff_HighLight_TextureRegion, vbom);

			}else
			{
				if(x <= 12)
					sp = new Sprite(lm.x, lm.y, possibleMoves_TextureRegion, vbom);
				else sp = new Sprite(lm.x, lm.y, possibleMoves_TextureRegion2, vbom);
			}
			lst_sprite_PossibleMoves.add(sp);
			scene.attachChild(sp);
			//Add blinking sprite to the scene
		}


		isShowingPossibleCheckers = false;
		isShowingPossibleMoves = true;
	}
	private void cpuGo(byte pChecker,byte pMove)
	{
		selectedCheckerBoardPosition = pChecker;
		selectedTargetBoardPosition = pMove;
		runandcalc();
	}

	CalcBestMove cbm = null;
	List<Node>  res;
	private void CpuMoveit()
	{
		if(res != null && res.size() > 0)
		{
			Node nr = res.get(res.size()-1);
			CpuMove_Location move = new CpuMove_Location();
			move.from_checker =nr.from;
			move.to_board = nr.to;
			List<Byte> ls =  CalcallPossibleMoves(move.from_checker);
			for(int i = 0;i<ls.size(); i++)
			{
				if(ls.get(i) == move.to_board)
				{
					dice1 = nr.dice1;
					dice2 = nr.dice2;
					cpuGo(move.from_checker, move.to_board);
					res.remove(res.size()-1);
					break;
				}
			}
		}else
		{
			cbm = new CalcBestMove();
			Node nd = new Node();
			//Initializing Node
			nd.firstDice = firstDice;
			nd.secondDice = secondDice;
			nd.dice1 = dice1;
			nd.dice2 = dice2;
			nd.hitBlack = hitBlack;
			nd.hitWhite = hitWhite;
			nd.takenBlack = takenBlack;
			nd.takenWhite = takenWhite;
			nd.playerTurn = playerTurn;
			nd.p1DiceRem = p1DiceRem;
			nd.p2DiceRem = p2DiceRem;
			nd.joft = joft;
			nd.d1 = d1;
			nd.d2 = d2;
			nd.d4 = d4;
			nd.isRoot = true;
			nd.board = board.clone();
			res = cbm.Start(nd);

			Node nr = res.get(res.size()-1);
			CpuMove_Location move = new CpuMove_Location();
			move.from_checker =nr.from;
			move.to_board = nr.to;
			List<Byte> ls =  CalcallPossibleMoves(move.from_checker);
			for(int i = 0;i<ls.size(); i++)
			{
				if(ls.get(i) == move.to_board)
				{
					dice1 = nr.dice1;
					dice2 = nr.dice2;
					cpuGo(move.from_checker, move.to_board);
					res.remove(res.size()-1);
					break;
				}
			}

		}
	}

	private void showPossibleCheckers()
	{
		if(canMove() == false)
		{
			final Sprite cantmove =  new Sprite(Constants.Dialog_Cantmove_X, Constants.Dialog_Cantmove_Y, Dialog_cantmoveTextureRegion.deepCopy(), vbom); 
			scene.attachChild(cantmove);

			this.getEngine().registerUpdateHandler(new TimerHandler(2, new ITimerCallback()
			{                      
				@Override
				public void onTimePassed(final TimerHandler pTimerHandler)
				{
					cantmove.detachSelf();
					if(!cantmove.isDisposed())
						cantmove.dispose();
					playerTurn = !playerTurn;
					joft = 0;
					dice1 = true;dice2 = true;


					Play_h2c.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {

							btnsave.performClick();
							f1 = -1;f2 = -1;f3 = -1;f4 = -1;
							t1 = -1;t2 = -1;t3 = -1;t4 = -1;
						}
					});


					hideDices();

					isShowingPossibleCheckers = false;
					ShowRollDiceButton();
				}
			}));
			//hiding dices 2 or 4
			//displaying rolling dice button and etc
		}else
		{

			if(lst_sprite_PossibleMoves != null)
			{
				if(lst_sprite_PossibleMoves.size() > 0)
				{
					for(int i=0;i<lst_sprite_PossibleMoves.size();i++)
					{
						Sprite s = lst_sprite_PossibleMoves.get(i);
						s.detachSelf();
						if(!s.isDisposed())
							s.dispose();
					}
				}
				lst_sprite_PossibleMoves.clear();
			}

			if(lst_asprite_possibleCheckers == null)
				lst_asprite_possibleCheckers = new ArrayList<AnimatedSprite>();
			else lst_asprite_possibleCheckers.clear();
			if(lst_PossibleCheckers != null)
				if(lst_PossibleCheckers.size()>0)
					lst_PossibleCheckers.clear();

			if(playerTurn == cpuTurn)
			{

				getEngine().registerUpdateHandler(new TimerHandler(1, new ITimerCallback()
				{                      
					@Override
					public void onTimePassed(final TimerHandler pTimerHandler)
					{
						CpuMoveit();
						return;
					}
				}));

			}
			else
			{
				lst_PossibleCheckers = CalcallPossibleCheckers();

				for(int i=0;i<lst_PossibleCheckers.size();i++)
				{
					AnimatedSprite as_highlight = null;
					LocationModel lm = null;
					int x = lst_PossibleCheckers.get(i);
					int num;
					if(x == 0 || x == -1)
					{
						if(x == -1)
							lm = Constants.get_Checker_Location_By_(-1,0);
						else lm = Constants.get_Checker_Location_By_(0,0);

					}
					else
					{
						num = board[x];
						num = Math.abs(num);
						if(num >5) num = 5;
						lm = Constants.get_Checker_Location_By_(x, num);	
					}

					as_highlight = new AnimatedSprite(lm.x,lm.y, tt_highlight.deepCopy(), vbom);
					as_highlight.animate(300,true);
					lst_asprite_possibleCheckers.add(as_highlight);
					scene.attachChild(as_highlight);
					//Add blinking sprite to the scene
				}
				isShowingPossibleCheckers = true;
				isShowingPossibleMoves = false;
			}
		}
	}
	boolean d1 = false,d2 = false,d4 = false;
	public ArrayList<Byte> CalcallPossibleMoves(byte fromRoll)
	{
		d1 = false;d2 = false; d4 = false;
		ArrayList<Byte> moves = new ArrayList<Byte>();
		if(playerTurn) ////If white turn
		{
			if(hitWhite == 0)
			{
				if(bringing_Off_Status())
				{
					int last = 6;
					for(int i=6;i>=1;i--)
					{
						if(board[i] <= 0) last--;
						else break;
					}
					if (board[fromRoll] > 0) {
						if(firstDice != secondDice)
						{
							boolean added_hundred = false;

							if(dice1 == false && firstDice == fromRoll)
							{
								d1 = true;
								if(added_hundred == false)
								{
									moves.add((byte)100);
									added_hundred = true;
								}
							}
							if(dice2 == false && secondDice == fromRoll)
							{
								d2= true;
								if(added_hundred == false)
								{
									moves.add((byte)100);
									added_hundred = true;
								}
							}
							if(dice1 == false && firstDice >= last)
							{
								d1 = true;
								if(added_hundred == false)
								{
									moves.add((byte)100);
									added_hundred = true;
								}
							}
							if(dice2 == false && secondDice >= last)
							{
								d2 = true;
								if(added_hundred == false)
								{
									moves.add((byte)100);
									added_hundred = true;
								}
							}
							if(dice1 == false && dice2 == false && (firstDice + secondDice) == fromRoll)
							{
								d4 = true;
								if(added_hundred == false)
								{
									moves.add((byte)100);
									added_hundred = true;
								}
							}
							if (dice1 == false && isValid(fromRoll,firstDice))
							{
								moves.add((byte)(fromRoll - firstDice));
							}
							if (dice2 == false && isValid(fromRoll,secondDice))
							{
								moves.add((byte)(fromRoll - secondDice));
							}
							byte sum = (byte) (firstDice + secondDice);
							if (dice1 == false && dice2 == false && isValid(fromRoll,sum))
								moves.add((byte) (fromRoll - sum));
						}
						else//joft
						{

							boolean added_hundred = false;

							if( firstDice == fromRoll)
							{
								if(added_hundred == false)
								{
									moves.add((byte)100);
									added_hundred = true;
								}
							}

							if(firstDice >= last)
								if(added_hundred == false)
								{
									moves.add((byte)100);
									added_hundred = true;
								}

							int c =1;
							for(int i = joft; i<4;i++)
							{
								byte num = (byte) (firstDice * c);
								c++;
								if (isValid(fromRoll,num))
								{
									moves.add((byte)(fromRoll - num));
									if(board[fromRoll - num] == -1) break;
								} else break;

							}


						}
					}
				}
				else
				{

					if (board[fromRoll] > 0) {
						if(firstDice != secondDice)
						{
							boolean hit = false;
							if (dice1 == false && isValid(fromRoll,firstDice))
							{
								moves.add((byte)(fromRoll - firstDice));
								if(board[fromRoll - firstDice] == -1) hit = true;
							}
							if (dice2 == false && isValid(fromRoll,secondDice))
							{
								moves.add((byte)(fromRoll - secondDice));
								if(board[fromRoll - secondDice] == -1) hit = true;
							}

							byte sum = (byte) (firstDice + secondDice);
							//در خط پایین هیت رو واسه این چک میکنم که اگه مثلا با یکی از دوتا تاس میتونه بزنه خود کاربر انتخاب کنه و گزینه سوم که شامل جمع دوتاس هست رو نشون کاربر نمیدم تا دونه دونه تاس ها رو حرکت بده
							if (!hit && dice1 == false && dice2 == false && isValid(fromRoll,sum))
								moves.add((byte)(fromRoll - sum));
						}else if(firstDice == secondDice)//joft
						{
							int c =1;
							for(int i = joft; i<4;i++)
							{
								byte num = (byte) (firstDice * c);
								c++;
								if (isValid(fromRoll,num))
								{
									moves.add((byte)(fromRoll - num));
									if(board[fromRoll - num] == -1) break;
								} else break;

							}
						}
					}

				}
			}else
			{

				if(firstDice != secondDice)
				{
					if (dice1 == false && board[(25 - firstDice)] >= -1)
					{
						moves.add((byte)(25 - firstDice));
					}
					if (dice2 == false && board[(25 - secondDice)] >= -1)
					{
						moves.add((byte)(25 - secondDice));
					}
				}else
				{
					if (joft < 4 && board[(25 - firstDice)] >= -1)
					{
						moves.add((byte)(25 - firstDice));
					}
				}

				//if(board[25 - firstDice] >= -1) return true;
				//if(board[25 - secondDice] >= -1) return true;
			}
		}else
		{
			if(hitBlack == 0)
			{
				if(bringing_Off_Status())
				{

					int last = 6;
					for(int i=19;i<=24;i++)
					{
						if(board[i] >= 0) last--;
						else break;
					}
					if (board[fromRoll] < 0) {
						if(firstDice != secondDice)
						{
							boolean added_hundred = false;

							if(dice1 == false &&   (25- firstDice) == fromRoll)
							{
								d1 = true;
								if(added_hundred == false)
								{
									moves.add((byte)101);
									added_hundred = true;
								}
							}
							if(dice2 == false && (25- secondDice) == fromRoll)
							{
								d2 = true;
								if(added_hundred == false)
								{
									moves.add((byte)101);
									added_hundred = true;
								}
							}
							if(dice1 == false && firstDice >= last)
							{
								d1 = true;
								if(added_hundred == false)
								{
									moves.add((byte)101);
									added_hundred = true;
								}
							}
							if(dice2 == false && secondDice >= last)
							{
								d2 = true;
								if(added_hundred == false)
								{
									moves.add((byte)101);
									added_hundred = true;
								}
							}
							if(dice1 == false && dice2 == false && (25 - (firstDice + secondDice)) == fromRoll)
							{
								d4 = true;
								if(added_hundred == false)
								{
									moves.add((byte)101);
									added_hundred = true;
								}
							}

							if (dice1 == false && isValid(fromRoll,firstDice))
							{
								moves.add((byte)(fromRoll + firstDice));
							}
							if (dice2 == false && isValid(fromRoll,secondDice))
							{
								moves.add((byte)(fromRoll + secondDice));
							}
							byte sum = (byte) (firstDice + secondDice);
							if (dice1 == false && dice2 == false && isValid(fromRoll,sum))
								moves.add((byte)(fromRoll + sum));
						}
						else//joft
						{

							boolean added_hundred = false;

							if( (25- firstDice) == fromRoll)
							{
								d1 = true;
								if(added_hundred == false)
								{
									moves.add((byte)101);
									added_hundred = true;
								}
							}

							if(firstDice >= last)
							{
								d1 = true;
								if(added_hundred == false)
								{
									moves.add((byte)101);
									added_hundred = true;
								}
							}
							int c =1;
							for(int i = joft; i<4;i++)
							{
								byte num = (byte) (firstDice * c);
								c++;
								if (isValid(fromRoll,num))
								{
									moves.add((byte)(fromRoll + num));
									if(board[fromRoll + num] == -1) break;
								} else break;

							}


						}
					}

				}
				else
				{
					if (board[fromRoll] < 0) {
						if(firstDice != secondDice)
						{
							boolean hit = false;
							if (dice1 == false && isValid(fromRoll,firstDice))
							{
								moves.add((byte)(fromRoll + firstDice));
								if(board[fromRoll + firstDice] == 1) hit = true;
							}
							if (dice2 == false && isValid(fromRoll,secondDice))
							{
								moves.add((byte)(fromRoll + secondDice));
								if(board[fromRoll + secondDice] == 1) hit = true;
							}
							byte sum = (byte) (firstDice + secondDice);
							//در خط پایین هیت رو واسه این چک میکنم که اگه مثلا با یکی از دوتا تاس میتونه بزنه خود کاربر انتخاب کنه و گزینه سوم که شامل جمع دوتاس هست رو نشون کاربر نمیدم تا دونه دونه تاس ها رو حرکت بده
							if (!hit && dice1 == false && dice2 == false && isValid(fromRoll,sum))
								moves.add((byte)(fromRoll + sum));
						}
						else if(firstDice == secondDice)
						{
							int c =1;
							for(int i = joft; i<4;i++)
							{
								byte num = (byte) (firstDice * c);
								c++;
								if (isValid(fromRoll,num))
								{
									moves.add((byte)(fromRoll + num));
									if(board[fromRoll + num] == 1) break;
								} else break;
							}
						}
					}
				}
			}else
			{
				if(firstDice != secondDice)
				{
					if (dice1 == false && board[( firstDice)] <= 1)
					{
						moves.add((byte)( firstDice));
					}
					if (dice2 == false && board[(secondDice)] <= 1)
					{
						moves.add((byte)(secondDice));
					}
				}else
				{
					if (joft < 4 && board[(firstDice)] <= 1)
					{
						moves.add((byte)( firstDice));
					}
				}
			}
		}
		return moves;

	}
	public ArrayList<Byte> CalcallPossibleCheckers()
	{
		ArrayList<Byte> moves = new ArrayList<Byte>();
		if(playerTurn) ////If white turn
		{
			if(hitWhite == 0)
			{
				if(bringing_Off_Status())
				{
					byte last = 6;
					for(int i=6;i>=1;i--)
					{
						if(board[i] <= 0) last--;
						else break;
					}
					if(firstDice != secondDice)
					{
						for(byte i = 1;i<=6;i++)
						{
							if (board[i] > 0) {
								if(dice1 == false && firstDice == i )
								{
									moves.add(i);
									continue;
								}
								if(dice2 == false && secondDice == i )
								{
									moves.add(i);
									continue;
								}
								if(dice1 == false && dice2 == false && (firstDice + secondDice) == i)
								{
									moves.add(i);
									continue;
								}
								if (dice1 == false && isValid(i,firstDice))
								{
									moves.add(i);
									continue;
								}
								if (dice2 == false && isValid(i,secondDice))
								{
									moves.add(i);
									continue;
								}
								if(dice1 == false && firstDice >= last)
								{
									moves.add( last);
									continue;
								}
								if(dice2 == false && secondDice >= last)
								{
									moves.add(last );
									continue;
								}

							}
						}
					}else //joft
					{
						for(byte i = 1;i<=6;i++)
						{
							if (board[i] > 0) {
								if( firstDice == i )
								{
									moves.add(i);
									continue;
								}
								if(secondDice == i )
								{
									moves.add(i);
									continue;
								}
								/*if(dice1 == false && dice2 == false && (firstDice + secondDice) == i)
								{
									moves.add((int)i);
									continue;
								}*/
								if (isValid(i,firstDice))
								{
									moves.add(i);
									continue;
								}

								if( firstDice >= last)
								{
									moves.add(last);
									continue;
								}
							}
						}
					}
				}
				else
				{
					if(firstDice != secondDice)
					{
						for (byte i = 24; i >0; i--) {
							if (board[i] > 0) {
								if (dice1 == false && isValid(i,firstDice))
								{
									moves.add(i);
									continue;
								}
								if (dice2 == false && isValid(i,secondDice))
									moves.add(i);
							}
						}
					}
					else
					{
						for (byte i = 24; i >0; i--) {
							if (board[i] > 0) {
								if (joft < 4 && isValid(i,firstDice))
								{
									moves.add(i);
									continue;
								}
							}
						}
					}
				}
			}else
			{
				moves.add((byte)0);
			}
		}else
		{
			if(hitBlack == 0)
			{
				if(bringing_Off_Status())
				{

					int last = 6;
					for(int i=19;i<= 24;i++)
					{
						if(board[i] >= 0) last--;
						else break;
					}

					if(firstDice != secondDice)
					{
						for(byte i = 19;i<=24;i++)
						{
							if (board[i] < 0) {
								if(dice1 == false && firstDice ==  (25 - i) )
								{
									moves.add(i);
									continue;
								}
								if(dice2 == false && secondDice ==  (25 - i) )
								{
									moves.add(i);
									continue;
								}
								if(dice1 == false && dice2 == false && (firstDice + secondDice) ==  (25 - i))
								{
									moves.add(i);
									continue;
								}
								if (dice1 == false && isValid(i,firstDice))
								{
									moves.add(i);
									continue;
								}
								if (dice2 == false && isValid(i,secondDice))
								{
									moves.add(i);
									continue;
								}
								if(dice1 == false && firstDice >= last)
								{
									moves.add((byte)(25 -  last));
									continue;
								}
								if(dice2 == false && secondDice >= last)
								{
									moves.add((byte)(25 -  last));
									continue;
								}

							}
						}
					}else //joft
					{
						for(byte i = 19;i<=24;i++)
						{
							if (board[i] < 0) {
								if( firstDice == (25 - i) )
								{
									moves.add(i);
									continue;
								}
								/*if(dice1 == false && dice2 == false && (firstDice + secondDice) == i)
								{
									moves.add((int)i);
									continue;
								}*/
								if (isValid(i,firstDice))
								{
									moves.add(i);
									continue;
								}

								if( firstDice >= last)
								{
									moves.add((byte) (25 - last));
									continue;
								}
							}
						}
					}

				}
				else
				{
					if(firstDice != secondDice)
					{
						for (byte i = 1; i < 25; i++) {
							if (board[i] < 0) {
								if (dice1 == false && isValid(i,firstDice))
								{
									moves.add(i);
									continue;
								}
								if (dice2 == false && isValid(i,secondDice))
									moves.add(i);
							}
						}
					}else
					{

						for (byte i = 1; i <25; i++) {
							if (board[i] < 0) {
								if (joft < 4 && isValid(i,firstDice))
								{
									moves.add(i);
									continue;
								}
							}
						}

					}
				}
			}else
			{
				moves.add((byte)-1);
				//if(board[firstDice] <= 1) return true;
				//if(board[secondDice] <= 1) return true;
			}
		}
		return moves;

	}
	public boolean canMove() {

		if(playerTurn) ////If white turn
		{
			if(hitWhite == 0)
			{
				if(bringing_Off_Status())
				{
					//return true;
					if(playerTurn)
					{
						boolean selfexist = false;
						boolean enemyExist = false;
						for(int i=1;i<=6;i++)
						{
							if(board[i] < -1) enemyExist = true;
							if(board[i] >0) selfexist = true;
						}
						if(!enemyExist) return true;
						else
						{
							int last = 6;
							for(int i=6;i>=1;i--)
							{
								if(board[i] <= 0) last--;
								else break;
							}
							ArrayList<Byte> pc = CalcallPossibleCheckers();
							if(selfexist)
							{
								if(firstDice != secondDice)
								{
									if(dice1 == false && dice2 == false && firstDice >= last && secondDice >= last) return true;
									if(dice1 == false && dice2 == true && firstDice >= last) return true;
									if(dice2 == false && dice1 == true && secondDice >= last) return true;
								}else
								{
									if(joft < 4 && firstDice >= last) return true;
								}
								if(pc == null) return false;
								if(pc.size() == 0)return false;
								pc.clear();
								return true;
							}else return true;
						}
					}else
					{
						boolean selfexist = false;
						boolean enemyExist = false;
						for(int i=19;i<=24;i++)
						{
							if(board[i] > 1) enemyExist = true;
							if(board[i] < 0) selfexist = true;
						}
						if(!enemyExist) return true;
						else
						{
							int last = 6;
							for(int i=19;i>=24;i++)
							{
								if(board[i] >= 0) last--;
								else break;
							}
							ArrayList<Byte> pc = CalcallPossibleCheckers();
							if(selfexist)
							{
								if(firstDice != secondDice)
								{
									if(dice1 == false && dice2 == false && firstDice >= last && secondDice >= last) return true;
									if(dice1 == false && dice2 == true && firstDice >= last) return true;
									if(dice2 == false && dice1 == true && secondDice >= last) return true;
								}else
								{
									if(joft < 4 && firstDice >= last) return true;
								}
								if(pc == null) return false;
								if(pc.size() == 0)return false;
								pc.clear();
								return true;
							}else return true;
						}

					}
				}
				else
				{
					for (byte i = 24; i >0; i--) {
						if (board[i] > 0) {
							if(firstDice!= secondDice)
							{
								if (dice1 == false && isValid(i,firstDice))
									return true;
								if (dice2 == false && isValid(i,secondDice))
									return true;
							}
							else
							{
								if (joft <4 && isValid(i,firstDice))
									return true;
							}
						}
					}
				}
			}else
			{
				if(dice1 == false &&  board[25 - firstDice] >= -1) return true;
				if(dice2 == false && board[25 - secondDice] >= -1) return true;
			}
		}else //If black turn
		{
			if(hitBlack == 0)
			{
				if(bringing_Off_Status())
					return true;
				else
				{
					for (byte i = 1; i < 25; i++) {
						if (board[i] < 0) {
							if(firstDice!= secondDice)
							{
								if (dice1 == false && isValid(i,firstDice))
									return true;
								if (dice2 == false && isValid(i,secondDice))
									return true;
							}
							else
							{
								if (joft <4 && isValid(i,firstDice))
									return true;
							}
						}
					}
				}
			}else
			{
				if(dice1 == false && board[firstDice] <= 1) return true;
				if(dice2 == false && board[secondDice] <= 1) return true;
			}
		}


		return false;
	}
	private boolean bringing_Off_Status()
	{

		if(playerTurn)
		{
			if(hitWhite > 0) return false;
			for(int i=7;i<=24;i++)
			{
				if(board[i]>0)return false;
			}
			return true;
		}
		else
		{
			if(hitBlack > 0) return false;
			for(int i=1;i<=18;i++)
			{
				if(board[i] < 0)return false;
			}
			return true;
		}
	}
	private boolean isValid(byte from,byte to) 
	{
		boolean result = false;
		if(playerTurn)//White move check
		{
			if(from - to >= 1)
				if(  (board[from] > 0 && board[from - to] >= -1))
					result = true;
		}else //Black move check
		{
			if(from + to <= 24)
				if(board[from] < 0 && board[from + to] <= 1 )
					result = true;
		}
		return result;
	}
	private void showMessage(final String txt)
	{
		Play_h2c.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AlertDialog.Builder builder = new AlertDialog.Builder(Play_h2c.this);
				builder.setMessage(txt)
				.setCancelable(false)
				.setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//do things
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
			}
		});

	}
	private void showToast(final String txt)
	{
		Play_h2c.this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(Play_h2c.this,txt, Toast.LENGTH_SHORT).show();
			}
		});
	}
	MoveModifier mm;
	private void moveChecker(final byte brd, final Checker chk,final boolean hitHappend,final boolean goOut,byte Checker_Position_In_Array)
	{
		if(mm != null)
			chk.unregisterEntityModifier(mm);

		byte pos=0;
		if(brd < 100)
		{
			pos = (byte) Math.abs(board[brd]);
			if(hitHappend) pos = 1;
			if(pos >4)pos = 5;
		}else
		{
			if(playerTurn) pos = takenWhite;
			else pos = takenBlack;
		}
		final LocationModel lm = Constants.get_Checker_Location_By_(brd, pos);
		float yl = lm.y;
		if(goOut && playerTurn) yl = yl - 17;
		mm = new MoveModifier((float)0.4, chk.getX(), lm.x, chk.getY(), yl, new IEntityModifierListener() {


			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				moveSoundNormal.stop();
				moveSoundNormal.release();
				calcPlayerRemainNumber();

				if(hitHappend)
					moveHitted(playerTurn,brd);
				if(!goOut)
					UpdateRollNumber(brd,true);//بولین که پاس میده یعنی تابع آپدیت اینبار بعد از اتمام انیمیشن فراخوانی شده
				else
				{
					chk.detachSelf();
					if(!chk.isDisposed())
						chk.dispose();

					Checker tmp;
					if(playerTurn)
					{
						tmp = new Checker(chk.board_position,chk.roll_position,true, lm.x, lm.y, ChekersOutTextureRegion_w.deepCopy(), vbom);
						lst_Checkers_White.add(tmp);
					}
					else
					{
						tmp = new Checker(chk.board_position,chk.roll_position,false, lm.x, lm.y, ChekersOutTextureRegion_b.deepCopy(), vbom);
						lst_Checkers_Black.add(tmp);
					}
					scene.attachChild(tmp);
					check_if_PlayerTurn_finished();
				}
				if(playerTurn != cpuTurn)
					ShowUndo();
			}
		});
		chk.registerEntityModifier(mm);
		try {
			moveSoundNormal = MusicFactory.createMusicFromAsset(mEngine.getMusicManager(), this,"mfx/move.ogg");
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		moveSoundNormal.play();
	}
	MoveModifier mm2;
	private void calcPlayerRemainNumber()
	{
		/*int sum1 = 0;
		int sum2 = 0;
			for(int i = 1;i<=24;i++)
			{
				 byte tmp = board[i];
				 if(tmp > 0)
				 {
					 sum1 += (i) * tmp;
				 }
				 if(tmp < 0)
				 {
					 sum2 += (25 - i) * tmp;
				 }
			}
		sum2 = sum2 * -1;
		showToast("White = " + sum1 + " ---- Black = " + sum2);*/
	}
	private void moveHitted(final boolean player_turn,byte board_position)
	{
		if(player_turn)
		{
			Checker checker_sprite = null;
			for(int i=0;i<lst_Checkers_Black.size();i++)//چون نوبت سفید بوده باید اون مهره سیاهی  که خورده رو پیدا کنیم
			{
				checker_sprite = lst_Checkers_Black.get(i);
				if(checker_sprite.board_position == board_position)
					break;
			}
			checker_sprite.board_position = -1;
			checker_sprite.roll_position = 0;
			if(mm2 != null)
				checker_sprite.unregisterEntityModifier(mm2);
			LocationModel lm = Constants.get_Checker_Location_By_(-1, 0);// صفر و صفر موقعیت چکر سیاه که خورده باشه رو بر می گردونه و صفر و یک چکر سفید
			checker_sprite.x = lm.x;
			checker_sprite.y = lm.y;
			mm2 = new MoveModifier((float)0.4, checker_sprite.getX(), lm.x, checker_sprite.getY(), lm.y, new IEntityModifierListener() {

				@Override
				public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {

					moveSoundHitted.stop();
					moveSoundHitted.release();
					/*if(moveSoundHitted != null)
					{
						mEngine.runOnUpdateThread(new Runnable() {
							@Override
							public void run() {
								moveSoundHitted.stop();
								moveSoundHitted.release();
							}
						});
					}*/
					UpdateHitNumber(player_turn,true);
				}
			});
			checker_sprite.registerEntityModifier(mm2);
			try {
				moveSoundHitted = MusicFactory.createMusicFromAsset(mEngine.getMusicManager(), this,"mfx/move.ogg");
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			moveSoundHitted.play();
		}
		else
		{

			Checker checker_sprite = null;
			for(int i=0;i<lst_Checkers_White.size();i++)//چون نوبت سیاهه باید اون مهره سفیدی که خورده رو پیدا کنیم
			{
				checker_sprite = lst_Checkers_White.get(i);
				if(checker_sprite.board_position == board_position)
					break;
			}
			checker_sprite.board_position = 0;
			checker_sprite.roll_position = 0;
			if(mm2 != null)
				checker_sprite.unregisterEntityModifier(mm2);
			LocationModel lm = Constants.get_Checker_Location_By_(0, 0);// صفر و صفر موقعیت چکر سیاه که خورده باشه رو بر می گردونه و صفر و یک چکر سفید
			checker_sprite.x = lm.x;
			checker_sprite.y = lm.y;
			mm2 = new MoveModifier((float)0.4, checker_sprite.getX(), lm.x, checker_sprite.getY(), lm.y, new IEntityModifierListener() {

				@Override
				public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
					moveSoundHitted.stop();
					moveSoundHitted.release();
					UpdateHitNumber(player_turn,true);
				}
			});
			checker_sprite.registerEntityModifier(mm2);
			try {
				moveSoundHitted = MusicFactory.createMusicFromAsset(mEngine.getMusicManager(), this,"mfx/move.ogg");
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			moveSoundHitted.play();
		}
	}
	private void UpdateHitNumber(final boolean turn,final boolean from_moveHited_function)
	{
		mEngine.runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				byte tBoart_Position = 0; //این تابع یه بار وختی فراخوانی میشه که مثلا نوبت سفیده و سیاهو زده و میخواد جای سیاه آپدیت بشه
				//یه بارم وختی فراخوانی میشه که نوبت خود سفیده و از خورده هاشو میاره تو واسه همین جنسش متفاوت میشه


				//پاک کردن چکرهای عددی احتمالی
				if(lst_Fake_Checkers != null)
					if(lst_Fake_Checkers.size()>0)
						for(int i=0;i<lst_Fake_Checkers.size();i++)
						{
							FakeChecker f = lst_Fake_Checkers.get(i);
							if(f.board_position <= 0)
							{
								f.detachSelf();
								if(!f.isDisposed())
									f.dispose();
								lst_Fake_Checkers.remove(i);
								break;
							}
						}

				LocationModel lm = Constants.get_Checker_Location_By_(-1,0);
				if(lst_Fake_Checkers == null) lst_Fake_Checkers = new ArrayList<FakeChecker>();
				FakeChecker tmp;
				//ساخت چکر عددی لازم
				ITextureRegion it = null;
				byte num = hitBlack;
				if(num > 1) 
				{

					if(num == 2 )it = Ib2.deepCopy();if(num ==3 )it = Ib3.deepCopy();if(num == 4 )it = Ib4.deepCopy();if(num == 5 )it = Ib5.deepCopy();
					if(num == 6 )it = Ib6.deepCopy();if(num == 7 )it = Ib7.deepCopy();if(num ==8 )it = Ib8.deepCopy();if(num == 9 )it = Ib9.deepCopy();
					if(num == 10 )it = Ib10.deepCopy();if(num == 11 )it = Ib11.deepCopy();if(num ==12 )it = Ib12.deepCopy();if(num ==13 )it = Ib13.deepCopy();
					if(num == 14 )it = Ib14.deepCopy();if(num == 15 )it = Ib15.deepCopy();//
					tmp = new FakeChecker((byte)0,false, lm.x, lm.y,it, vbom);
					lst_Fake_Checkers.add(tmp);
					scene.attachChild(tmp);
				}
				lm = Constants.get_Checker_Location_By_(0,0);
				if(lst_Fake_Checkers == null) lst_Fake_Checkers = new ArrayList<FakeChecker>();
				//ساخت چکر عددی لازم
				num = hitWhite;
				if(num > 1) 
				{
					if(num == 2 )it = Iw2.deepCopy();if(num ==3 )it = Iw3.deepCopy();if(num == 4 )it = Iw4.deepCopy();if(num == 5 )it = Iw5.deepCopy();
					if(num == 6 )it = Iw6.deepCopy();if(num == 7 )it = Iw7.deepCopy();if(num ==8 )it = Iw8.deepCopy();if(num == 9 )it = Iw9.deepCopy();
					if(num == 10 )it = Iw10.deepCopy();if(num == 11 )it = Iw11.deepCopy();if(num ==12 )it = Iw12.deepCopy();if(num ==13 )it = Iw13.deepCopy();
					if(num == 14 )it = Iw14.deepCopy();if(num == 15 )it = Iw15.deepCopy();//
					tmp = new FakeChecker((byte)0,false, lm.x, lm.y,it, vbom);
					lst_Fake_Checkers.add(tmp);
					scene.attachChild(tmp);
				}

			}
		});
	}
	private void check_if_PlayerTurn_finished()
	{
		if(playerTurn && takenWhite == 15)
		{
			showMessage("Player1 Won the game ");
		}
		else
			if(!playerTurn && takenBlack == 15)
			{
				showMessage("Player1 Won the game ");
			}
			else
			{
				if(firstDice != secondDice)
				{
					if(dice1 == true && dice2 == true)
					{
						//showMessage("1: changed to " + playerTurn);
						Play_h2c.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {

								btnsave.performClick();
								f1 = -1;f2 = -1;f3 = -1;f4 = -1;
								t1 = -1;t2 = -1;t3 = -1;t4 = -1;
							}
						});
						playerTurn = ! playerTurn;
						ShowRollDiceButton();
					}else
						showPossibleCheckers();
				}else
				{
					if(joft >= 4)
					{
						//showMessage("2: changed to " + playerTurn);
						Play_h2c.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {

								btnsave.performClick();
								f1 = -1;f2 = -1;f3 = -1;f4 = -1;
								t1 = -1;t2 = -1;t3 = -1;t4 = -1;
							}
						});
						playerTurn = !playerTurn;
						joft = 0;
						ShowRollDiceButton();
					}else
						showPossibleCheckers();
				}

			}
	}
	private void hideDices()
	{
		if(asprite_dice1 != null)
		{
			asprite_dice1.detachSelf();
			if(!asprite_dice1.isDisposed())
				asprite_dice1.dispose();
		}
		if(asprite_dice2 != null)
		{
			asprite_dice2.detachSelf();
			if(!asprite_dice2.isDisposed())
				asprite_dice2.dispose();
		}
		/*mEngine.runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				if(asprite_dice1 != null)
				{
					asprite_dice1.setIgnoreUpdate(true); 
					asprite_dice1.clearUpdateHandlers();
					asprite_dice1.detachSelf();
					if(!asprite_dice1.isDisposed())
						asprite_dice1.dispose();
				}
				if(asprite_dice2 != null)
				{
					asprite_dice1.setIgnoreUpdate(true);
					asprite_dice2.clearUpdateHandlers();
					asprite_dice2.detachSelf();
					if(!asprite_dice2.isDisposed())
						asprite_dice2.dispose();
				}
			}
		});*/


	}
	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
					throws Exception {
		// TODO Auto-generated method stub
		scene = new Scene();
		vbom = getVertexBufferObjectManager();
		Ib = new HashMap<Integer, ITextureRegion>();
		Iw = new HashMap<Integer, ITextureRegion>();
		Ib.put(5,Ib5);  Ib.put(6,Ib6);  Ib.put(7,Ib7);  Ib.put(8,Ib8);  Ib.put(9,Ib9);  Ib.put(10,Ib10);  Ib.put(11,Ib11);  Ib.put(12,Ib12);  Ib.put(13,Ib13);  Ib.put(14,Ib14);  Ib.put(15,Ib15);
		Iw.put(5,Iw5);  Iw.put(6,Iw6);  Iw.put(7,Iw7);  Iw.put(8,Iw8);  Iw.put(9,Iw9);  Iw.put(10,Iw10);  Iw.put(11,Iw11);  Iw.put(12,Iw12);  Iw.put(13,Iw13);  Iw.put(14,Iw14);  Iw.put(15,Iw15);

		tv_p1Dice = (TextView) findViewById(R.id.tv_p1);
		tv_p2Dice = (TextView) findViewById(R.id.tv_p2);
		btnsave = (Button) findViewById(R.id.btnsave);
		btntest = (Button) findViewById(R.id.btnloadtest);
		btntest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				LoadFake();
			}
		});
		btnsave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				save_stat_counter++;
				String txt = "";
				playerTurn_b = !playerTurn;
				board_b = board.clone();
				firstDice_b = firstDice;
				secondDice_b = secondDice;
				hitBlack_b = hitBlack;
				hitWhite_b = hitWhite;
				takenBlack_b = takenBlack;
				takenWhite_b = takenWhite;
				dice1_b = dice1;
				dice2_b = dice2;
				joft_b = joft;
				String nextline = "\r\n";	
				txt += "Board = {";
				for(int i=0;i<=24;i++)
				{
					if(i < 24)
						txt += Play_h2c.board_b[i] + ",";
					else txt += Play_h2c.board_b[i] + "}";
				}
				if(f1 == -1) return;
				if(f1 != -1)
					txt+= "\n from: " + f1 + " , to: " + t1; 
				if(f2 != -1)
					txt+= "\n from: " + f2 + " , to: " + t2; 
				if(f3 != -1)
					txt+= "\n from: " + f3 + " , to: " + t3; 
				if(f4 != -1)
					txt+= "\n from: " + f4 + " , to: " + t4; 
				txt += "\n" + "firstDice: " + Play_h2c.firstDice_b;
				txt += "\n" + "secondDice: " + Play_h2c.secondDice_b;
				txt += "\n" + "hitBlack: " + Play_h2c.hitBlack_b;
				txt += "\n" + "hitWhite: " + Play_h2c.hitWhite_b;
				txt += "\n" + "takenBlack: " + Play_h2c.takenBlack_b;
				txt += "\n" + "takenWhite: " + Play_h2c.takenWhite_b;
				txt += "\n" + "Dice1: " + Play_h2c.dice1_b;
				txt += "\n" + "Dice2: " + Play_h2c.dice2_b;
				txt += "\n" + "Joft: " + Play_h2c.joft_b;
				txt += "\n" + "playerTurn: " + Play_h2c.playerTurn_b + "\n";

				File root = android.os.Environment.getExternalStorageDirectory(); 
				File dir = new File (root.getAbsolutePath() + "/BackGammonState");
				if(save_stat_counter == 1)
				{
					if (dir.isDirectory()) {
						String[] children = dir.list();
						for (int i = 0; i < children.length; i++) {
							new File(dir, children[i]).delete();
						}
					}
				}
				dir.mkdirs();
				File file = null;


				file = new File(dir, "myState" + save_stat_counter + ".txt");


				try {
					FileOutputStream f = new FileOutputStream(file);
					PrintWriter pw = new PrintWriter(f);
					pw.println(txt);
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
				//Toast.makeText(Play_h2c.this,"Saved",Toast.LENGTH_SHORT).show();
			}
		});
		try
		{
			// 1 - Set up board bitmap textures
			ITexture backgroundTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return getAssets().open("gfx/board.jpg");
				}
			});
			backgroundTexture.load();// 2 - Load bitmap textures into VRAM
			this.mBackgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);// 3- Create texureRegion


			// 1 - Set up Roll Dice Button textures
			//Roll Dice Button  Creation:
			BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
			BitmapTextureAtlas mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 256, 64);
			this.tt_btnRollDice = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas, this, "button_wood_tas.png",0,0, 2, 1);
			mBitmapTextureAtlas.load();

			//Undo Button  Creation:
			BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
			BitmapTextureAtlas mBitmapTextureAtlas_undo = new BitmapTextureAtlas(this.getTextureManager(), 120, 45);
			this.tt_undo = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlas_undo, this, "undo.png",0,0, 2, 1);
			mBitmapTextureAtlas_undo.load();

			//A Dice Creation:
			BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
			BitmapTextureAtlas rd = new BitmapTextureAtlas(this.getTextureManager(), 1656, 92);
			this.tt_dice = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(rd, this, "dice_anim.png",0,0, 18, 1);
			rd.load();

			BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
			BitmapTextureAtlas rdc = new BitmapTextureAtlas(this.getTextureManager(), 1656, 92);
			this.tt_dice2 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(rdc, this, "dice_anim2.png",0,0, 18, 1);
			rdc.load();

			BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
			BitmapTextureAtlas rd2 = new BitmapTextureAtlas(this.getTextureManager(), 92, 30);
			this.tt_highlight = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(rd2, this, "highlight_piece.png",0,0, 2, 1);
			rd2.load();

			//WHITE TEXTURES AND TEXTUREREGIONS
			ITexture checkerTexture_w = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return getAssets().open("gfx/piece_w.png");
				}
			});
			checkerTexture_w.load();
			ChekersTextureRegion_w  = TextureRegionFactory.extractFromTexture(checkerTexture_w);
			//BLACK TEXTURES AND TEXTUREREGIONS
			ITexture checkerTexture_b = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return getAssets().open("gfx/piece_b.png");
				}
			});
			checkerTexture_b.load();
			ChekersTextureRegion_b =TextureRegionFactory.extractFromTexture(checkerTexture_b);

			//BLACK OUT TEXTRURE REGION
			ITexture checkerOutTexture_b = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return getAssets().open("gfx/piece_b_off.png");
				}
			});
			checkerOutTexture_b.load();
			ChekersOutTextureRegion_b =TextureRegionFactory.extractFromTexture(checkerOutTexture_b);


			//WHITE OUT TEXTRURE REGION
			ITexture checkerOutTexture_w = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return getAssets().open("gfx/piece_w_off.png");
				}
			});
			checkerOutTexture_w.load();
			ChekersOutTextureRegion_w =TextureRegionFactory.extractFromTexture(checkerOutTexture_w);



			ITexture possibleMoves = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return getAssets().open("gfx/possible_moves.png");
				}
			});
			possibleMoves.load();
			possibleMoves_TextureRegion  = TextureRegionFactory.extractFromTexture(possibleMoves);


			ITexture cantmove = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return getAssets().open("gfx/cantmove.png");
				}
			});
			cantmove.load();
			Dialog_cantmoveTextureRegion  = TextureRegionFactory.extractFromTexture(cantmove);

			ITexture possibleMoves2 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return getAssets().open("gfx/possible_moves2.png");
				}
			});
			possibleMoves2.load();
			possibleMoves_TextureRegion2  = TextureRegionFactory.extractFromTexture(possibleMoves2);


			ITexture BringOff_HighLight_Texture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
				@Override
				public InputStream open() throws IOException {
					return getAssets().open("gfx/bringaout.png");
				}
			});
			BringOff_HighLight_Texture.load();
			BringOff_HighLight_TextureRegion  = TextureRegionFactory.extractFromTexture(BringOff_HighLight_Texture);



			ITexture ww1 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {@Override public InputStream open() throws IOException {return getAssets().open("gfx/piece_w_1.png");}});ww1.load();Iw1  = TextureRegionFactory.extractFromTexture(ww1);
			ITexture ww2 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {@Override public InputStream open() throws IOException {return getAssets().open("gfx/piece_w_2.png");}});ww2.load();Iw2  = TextureRegionFactory.extractFromTexture(ww2);
			ITexture ww3 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {@Override public InputStream open() throws IOException {return getAssets().open("gfx/piece_w_3.png");}});ww3.load();Iw3  = TextureRegionFactory.extractFromTexture(ww3);
			ITexture ww4 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {@Override public InputStream open() throws IOException {return getAssets().open("gfx/piece_w_4.png");}});ww4.load();Iw4  = TextureRegionFactory.extractFromTexture(ww4);

			ITexture ww5 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {@Override public InputStream open() throws IOException {return getAssets().open("gfx/piece_w_5.png");}});ww5.load();Iw5  = TextureRegionFactory.extractFromTexture(ww5);
			ITexture ww6 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {@Override public InputStream open() throws IOException {return getAssets().open("gfx/piece_w_6.png");}});ww6.load();Iw6  = TextureRegionFactory.extractFromTexture(ww6);
			ITexture ww7 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {@Override public InputStream open() throws IOException {return getAssets().open("gfx/piece_w_7.png");}});ww7.load();Iw7  = TextureRegionFactory.extractFromTexture(ww7);
			ITexture ww8 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {@Override public InputStream open() throws IOException {return getAssets().open("gfx/piece_w_8.png");}});ww8.load();Iw8  = TextureRegionFactory.extractFromTexture(ww8);
			ITexture ww9 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {@Override public InputStream open() throws IOException {return getAssets().open("gfx/piece_w_9.png");}});ww9.load();Iw9  = TextureRegionFactory.extractFromTexture(ww9);
			ITexture ww10 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {@Override public InputStream open() throws IOException {return getAssets().open("gfx/piece_w_10.png");}});ww10.load();Iw10  = TextureRegionFactory.extractFromTexture(ww10);
			ITexture ww11 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {@Override public InputStream open() throws IOException {return getAssets().open("gfx/piece_w_11.png");}});ww11.load();Iw11  = TextureRegionFactory.extractFromTexture(ww11);
			ITexture ww12 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {@Override public InputStream open() throws IOException {return getAssets().open("gfx/piece_w_12.png");}});ww12.load();Iw12  = TextureRegionFactory.extractFromTexture(ww12);
			ITexture ww13 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {@Override public InputStream open() throws IOException {return getAssets().open("gfx/piece_w_13.png");}});ww13.load();Iw13  = TextureRegionFactory.extractFromTexture(ww13);
			ITexture ww14 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {@Override public InputStream open() throws IOException {return getAssets().open("gfx/piece_w_14.png");}});ww14.load();Iw14  = TextureRegionFactory.extractFromTexture(ww14);
			ITexture ww15 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {@Override public InputStream open() throws IOException {return getAssets().open("gfx/piece_w_15.png");}});ww15.load();Iw15  = TextureRegionFactory.extractFromTexture(ww15);

			ITexture bb1 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {@Override public InputStream open() throws IOException {return getAssets().open("gfx/piece_b_1.png");}});bb1.load();Ib1  = TextureRegionFactory.extractFromTexture(bb1);
			ITexture bb2 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {@Override public InputStream open() throws IOException {return getAssets().open("gfx/piece_b_2.png");}});bb2.load();Ib2  = TextureRegionFactory.extractFromTexture(bb2);
			ITexture bb3 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {@Override public InputStream open() throws IOException {return getAssets().open("gfx/piece_b_3.png");}});bb3.load();Ib3  = TextureRegionFactory.extractFromTexture(bb3);
			ITexture bb4 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {@Override public InputStream open() throws IOException {return getAssets().open("gfx/piece_b_4.png");}});bb4.load();Ib4  = TextureRegionFactory.extractFromTexture(bb4);

			ITexture bb5 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {@Override public InputStream open() throws IOException {return getAssets().open("gfx/piece_b_5.png");}});bb5.load();Ib5  = TextureRegionFactory.extractFromTexture(bb5);
			ITexture bb6 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {@Override public InputStream open() throws IOException {return getAssets().open("gfx/piece_b_6.png");}});bb6.load();Ib6  = TextureRegionFactory.extractFromTexture(bb6);
			ITexture bb7 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {@Override public InputStream open() throws IOException {return getAssets().open("gfx/piece_b_7.png");}});bb7.load();Ib7  = TextureRegionFactory.extractFromTexture(bb7);
			ITexture bb8 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {@Override public InputStream open() throws IOException {return getAssets().open("gfx/piece_b_8.png");}});bb8.load();Ib8  = TextureRegionFactory.extractFromTexture(bb8);
			ITexture bb9 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {@Override public InputStream open() throws IOException {return getAssets().open("gfx/piece_b_9.png");}});bb9.load();Ib9  = TextureRegionFactory.extractFromTexture(bb9);
			ITexture bb10 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {@Override public InputStream open() throws IOException {return getAssets().open("gfx/piece_b_10.png");}});bb10.load();Ib10  = TextureRegionFactory.extractFromTexture(bb10);
			ITexture bb11 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {@Override public InputStream open() throws IOException {return getAssets().open("gfx/piece_b_11.png");}});bb11.load();Ib11  = TextureRegionFactory.extractFromTexture(bb11);
			ITexture bb12 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {@Override public InputStream open() throws IOException {return getAssets().open("gfx/piece_b_12.png");}});bb12.load();Ib12  = TextureRegionFactory.extractFromTexture(bb12);
			ITexture bb13 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {@Override public InputStream open() throws IOException {return getAssets().open("gfx/piece_b_13.png");}});bb13.load();Ib13  = TextureRegionFactory.extractFromTexture(bb13);
			ITexture bb14 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {@Override public InputStream open() throws IOException {return getAssets().open("gfx/piece_b_14.png");}});bb14.load();Ib14  = TextureRegionFactory.extractFromTexture(bb14);
			ITexture bb15 = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {@Override public InputStream open() throws IOException {return getAssets().open("gfx/piece_b_15.png");}});bb15.load();Ib15  = TextureRegionFactory.extractFromTexture(bb15);

			pOnCreateResourcesCallback.onCreateResourcesFinished();
		}catch(Exception e)
		{
		}
	}
	private void SetLast()
	{
		p1DiceRem_1 = p1DiceRem;
		p2DiceRem_1 = p2DiceRem;
		hitBlack_l = hitBlack;
		hitWhite_l = hitWhite;
		takenBlack_l = takenBlack;
		takenWhite_l = takenWhite;
		dice1_l = dice1;
		dice2_l = dice2;
		firstDice_l = firstDice;
		secondDice_l = secondDice;
		joft_l = joft;
		playerTurn_l = playerTurn;
		for(byte i = 0;i<=24;i++)
		{
			board_l[i] = board[i];
		}

	}
	private void LoadLast()
	{
		//CLEARING---------------
		HideRollDiceButton();
		if(lst_asprite_possibleCheckers!= null)
			for(int i=0;i<lst_asprite_possibleCheckers.size();i++)
			{
				AnimatedSprite as = lst_asprite_possibleCheckers.get(i);
				as.detachSelf();
				if(!as.isDisposed())
					as.dispose();
			}
		if(lst_asprite_possibleCheckers!= null)
			lst_asprite_possibleCheckers.clear();
		if(lst_sprite_PossibleMoves != null)
			if(lst_sprite_PossibleMoves.size() > 0)
			{
				for(int i=0;i<lst_sprite_PossibleMoves.size();i++)
				{
					Sprite s = lst_sprite_PossibleMoves.get(i);
					s.detachSelf();
					if(!s.isDisposed())
						s.dispose();
				}
			}
		if(lst_sprite_PossibleMoves != null)
			lst_sprite_PossibleMoves.clear();
		if(lst_sprite_PossibleMoves == null)
			lst_sprite_PossibleMoves = new ArrayList<Sprite>();

		if(lst_PossibleMoves != null)
			if(lst_PossibleMoves.size()>0)
				lst_PossibleMoves.clear();


		if(lst_Fake_Checkers!= null)
			for(int i=0;i<lst_Fake_Checkers.size();i++)
			{
				FakeChecker c = lst_Fake_Checkers.get(i);
				c.detachSelf();
				if(!c.isDisposed())
					c.dispose();
			}


		if(lst_Checkers_Black!= null)
			for(int i=0;i<lst_Checkers_Black.size();i++)
			{
				Checker c = lst_Checkers_Black.get(i);
				c.detachSelf();
				if(!c.isDisposed())
					c.dispose();
			}
		if(lst_Checkers_White!= null)
			for(int i=0;i<lst_Checkers_White.size();i++)
			{
				Checker c = lst_Checkers_White.get(i);
				c.detachSelf();
				if(!c.isDisposed())
					c.dispose();
			}
		//EOC-------------------
		//scene = new Scene();
		//vbom = getVertexBufferObjectManager();
		p1DiceRem = p1DiceRem_1;
		p2DiceRem = p2DiceRem_1;
		hitBlack = hitBlack_l;
		hitWhite = hitWhite_l;
		takenBlack = takenBlack_l;
		takenWhite = takenWhite_l;
		dice1 = dice1_l;
		dice2 = dice2_l;
		firstDice = firstDice_l;
		secondDice = secondDice_l;
		joft = joft_l;
		playerTurn = playerTurn_l;

		CreateDicesAgainWithCurrentStatus(true);
		for(byte i = 0;i<=24;i++)
		{
			board[i] = board_l[i];
		}

		lst_Fake_Checkers = new ArrayList<FakeChecker>();
		lst_Checkers_Black = new ArrayList<Checker>();
		lst_Checkers_White = new ArrayList<Checker>();
		LocationModel loc = null;

		tv_p1Dice.post(new Runnable() {
			public void run() {
				tv_p1Dice.setText(String.valueOf(p1DiceRem));
			}

		});

		tv_p2Dice.post(new Runnable() {
			public void run() {
				tv_p2Dice.setText(String.valueOf(p2DiceRem));
			}

		});

		for(byte tb=1;tb<=takenBlack;tb++)
		{
			LocationModel lmtb = Constants.get_Checker_Location_By_(101, tb);
			Checker tmp = new Checker(101,tb,false, lmtb.x, lmtb.y, this.ChekersOutTextureRegion_b.deepCopy(), vbom);
			lst_Checkers_Black.add(tmp);
			scene.attachChild(tmp);
		}
		for(byte tw=1;tw<=takenWhite;tw++)
		{
			LocationModel lmtw = Constants.get_Checker_Location_By_(100, tw);
			Checker tmp = new Checker(101,tw,true, lmtw.x, lmtw.y, this.ChekersOutTextureRegion_w.deepCopy(), vbom);
			lst_Checkers_Black.add(tmp);
			scene.attachChild(tmp);
		}
		LocationModel lmhb = Constants.get_Checker_Location_By_(-1, 0);
		LocationModel lmwb = Constants.get_Checker_Location_By_(0, 0);
		for(byte hb=1;hb<=hitBlack;hb++)
		{
			Checker tmp = new Checker(-1,0,false, lmhb.x, lmhb.y, this.ChekersTextureRegion_b.deepCopy(), vbom);
			lst_Checkers_Black.add(tmp);
			scene.attachChild(tmp);
		}
		if(hitBlack > 1) UpdateHitNumber(false, false);

		for(byte hw=1;hw<=hitWhite;hw++)
		{
			Checker tmp = new Checker(0,0,false, lmwb.x, lmwb.y, this.ChekersTextureRegion_w.deepCopy(), vbom);
			lst_Checkers_White.add(tmp);
			scene.attachChild(tmp);
		}
		if(hitWhite > 1) UpdateHitNumber(false, false);
		for(byte i=0;i<=24;i++)
		{
			byte x = board[i];
			if(x > 0)
			{
				boolean updateFake = false;
				if(x > 5) updateFake = true;
				for(byte j=1;j<=x;j++)
				{
					if(j>5)
						loc = Constants.get_Checker_Location_By_(i,5);
					else
						loc = Constants.get_Checker_Location_By_(i,j);
					Checker tmp = new Checker(loc.board_Postion,j,true, loc.x, loc.y, this.ChekersTextureRegion_w.deepCopy(), vbom);
					lst_Checkers_White.add(tmp);
					scene.attachChild(tmp);
					if(j == x)
					{
						if(updateFake)
						{
							UpdateRollAtBeginning(i,true);
						}
					}
				}
			}else if(x < 0)
			{
				boolean updateFake = false;
				x = (byte) Math.abs(x);
				if(x > 5) updateFake = true;
				for(byte j=1;j<=x;j++)
				{
					if(j>5)
						loc = Constants.get_Checker_Location_By_(i,5);
					else
						loc = Constants.get_Checker_Location_By_(i,j);
					Checker tmp = new Checker(loc.board_Postion,j,false, loc.x, loc.y, this.ChekersTextureRegion_b.deepCopy(), vbom);
					lst_Checkers_Black.add(tmp);
					scene.attachChild(tmp);
					if(j == x)
					{
						if(updateFake)
						{
							UpdateRollAtBeginning(i,false);
						}
					}
				}

			}
		}

		showPossibleCheckers();
	}
	private void WriteText(final int txt,final int txt2)//txt2 واسه کسی که خورده و باید بهش اضافه بشه
	{
		if(playerTurn)
		{
			p1DiceRem = p1DiceRem - txt;
			p2DiceRem = p2DiceRem + txt2;
			tv_p1Dice.post(new Runnable() {
				public void run() {
					tv_p1Dice.setText(String.valueOf(p1DiceRem));
				}

			});
			tv_p2Dice.post(new Runnable() {
				public void run() {
					tv_p2Dice.setText(String.valueOf(p2DiceRem));
				}

			});
		}else
		{
			p2DiceRem = p2DiceRem - txt;
			p1DiceRem = p1DiceRem + txt2;
			tv_p2Dice.post(new Runnable() {
				public void run() {
					tv_p2Dice.setText(String.valueOf(p2DiceRem));
				} 
			});
			tv_p1Dice.post(new Runnable() {
				public void run() {
					tv_p1Dice.setText(String.valueOf(p1DiceRem));
				} 
			});
		}

	}
	private void LoadFake()
	{

		//CLEARING---------------

		if(tsprite_Btn_Roll_Dice != null)
		{
			scene.detachChild(tsprite_Btn_Roll_Dice);
			scene.unregisterTouchArea(tsprite_Btn_Roll_Dice);
			tsprite_Btn_Roll_Dice.detachSelf();
			if(!tsprite_Btn_Roll_Dice.isDisposed())
				tsprite_Btn_Roll_Dice.dispose();
		}
		if(lst_asprite_possibleCheckers!= null)
			for(int i=0;i<lst_asprite_possibleCheckers.size();i++)
			{
				AnimatedSprite as = lst_asprite_possibleCheckers.get(i);
				as.detachSelf();
				if(!as.isDisposed())
					as.dispose();
			}
		if(lst_asprite_possibleCheckers!= null)
			lst_asprite_possibleCheckers.clear();
		if(lst_sprite_PossibleMoves != null)
			if(lst_sprite_PossibleMoves.size() > 0)
			{
				for(int i=0;i<lst_sprite_PossibleMoves.size();i++)
				{
					Sprite s = lst_sprite_PossibleMoves.get(i);
					s.detachSelf();
					if(!s.isDisposed())
						s.dispose();
				}
			}
		if(lst_sprite_PossibleMoves != null)
			lst_sprite_PossibleMoves.clear();
		if(lst_sprite_PossibleMoves == null)
			lst_sprite_PossibleMoves = new ArrayList<Sprite>();

		if(lst_PossibleMoves != null)
			if(lst_PossibleMoves.size()>0)
				lst_PossibleMoves.clear();


		if(lst_Fake_Checkers!= null)
			for(int i=0;i<lst_Fake_Checkers.size();i++)
			{
				FakeChecker c = lst_Fake_Checkers.get(i);
				c.detachSelf();
				if(!c.isDisposed())
					c.dispose();
			}


		if(lst_Checkers_Black!= null)
			for(int i=0;i<lst_Checkers_Black.size();i++)
			{
				Checker c = lst_Checkers_Black.get(i);
				c.detachSelf();
				if(!c.isDisposed())
					c.dispose();
			}
		if(lst_Checkers_White!= null)
			for(int i=0;i<lst_Checkers_White.size();i++)
			{
				Checker c = lst_Checkers_White.get(i);
				c.detachSelf();
				if(!c.isDisposed())
					c.dispose();
			}
		//EOC-------------------
		byte[] board3 = { 0,-4, 0, 3,0, -1 ,0, -4, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,-3, 0,-2, 0, 0 };
		p1DiceRem = 41;
		p2DiceRem = 158;
		forceDice1 =6;//firstDicebN  بشه اون تاسی که بعد از ذخیره کل حالت افتاده
		forceDice2 = 5;
		firstDice = 6;
		secondDice = 5;
		no_move_yet = false;
		hitBlack = 0;
		hitWhite = 0;
		takenBlack = 0;
		takenWhite = 12;
		dice1 = false;
		dice2 = true;
		playerTurn = true;
		lst_Fake_Checkers = new ArrayList<FakeChecker>();
		lst_Checkers_Black = new ArrayList<Checker>();
		lst_Checkers_White = new ArrayList<Checker>();
		LocationModel loc = null;
		CreateDicesAgainWithCurrentStatus(true);
		tv_p1Dice.post(new Runnable() {
			public void run() {
				tv_p1Dice.setText(String.valueOf(p1DiceRem));
			}

		});

		tv_p2Dice.post(new Runnable() {
			public void run() {
				tv_p2Dice.setText(String.valueOf(p2DiceRem));
			}

		});
		LocationModel lmhb = Constants.get_Checker_Location_By_(-1, 0);
		LocationModel lmwb = Constants.get_Checker_Location_By_(0, 0);



		for(byte tb=1;tb<=takenBlack;tb++)
		{
			LocationModel lmtb = Constants.get_Checker_Location_By_(101, tb);
			Checker tmp = new Checker(101,tb,false, lmtb.x, lmtb.y, this.ChekersOutTextureRegion_b.deepCopy(), vbom);
			lst_Checkers_Black.add(tmp);
			scene.attachChild(tmp);
		}
		for(byte tw=1;tw<=takenWhite;tw++)
		{
			LocationModel lmtw = Constants.get_Checker_Location_By_(100, tw);
			Checker tmp = new Checker(101,tw,true, lmtw.x, lmtw.y, this.ChekersOutTextureRegion_w.deepCopy(), vbom);
			lst_Checkers_Black.add(tmp);
			scene.attachChild(tmp);
		}
		for(byte hb=1;hb<=hitBlack;hb++)
		{
			Checker tmp = new Checker(-1,0,false, lmhb.x, lmhb.y, this.ChekersTextureRegion_b.deepCopy(), vbom);
			lst_Checkers_Black.add(tmp);
			scene.attachChild(tmp);
		}
		if(hitBlack > 1) UpdateHitNumber(false, false);

		for(byte hw=1;hw<=hitWhite;hw++)
		{
			Checker tmp = new Checker(0,0,false, lmwb.x, lmwb.y, this.ChekersTextureRegion_w.deepCopy(), vbom);
			lst_Checkers_White.add(tmp);
			scene.attachChild(tmp);
		}
		if(hitWhite > 1) UpdateHitNumber(false, false);


		for(byte i=0;i<=24;i++)
		{
			byte x = board3[i];
			if(x > 0)
			{
				boolean updateFake = false;
				if(x > 5) updateFake = true;
				for(byte j=1;j<=x;j++)
				{
					if(j>5)
						loc = Constants.get_Checker_Location_By_(i,5);
					else
						loc = Constants.get_Checker_Location_By_(i,j);
					Checker tmp = new Checker(loc.board_Postion,j,true, loc.x, loc.y, this.ChekersTextureRegion_w.deepCopy(), vbom);
					lst_Checkers_White.add(tmp);
					scene.attachChild(tmp);
					if(j == x)
					{
						if(updateFake)
						{
							UpdateRollAtBeginning(i,true);
						}
					}



				}
			}else if(x < 0)
			{
				boolean updateFake = false;
				x = (byte) Math.abs(x);
				if(x > 5) updateFake = true;
				for(byte j=1;j<=x;j++)
				{
					if(j>5)
						loc = Constants.get_Checker_Location_By_(i,5);
					else
						loc = Constants.get_Checker_Location_By_(i,j);
					Checker tmp = new Checker(loc.board_Postion,j,false, loc.x, loc.y, this.ChekersTextureRegion_b.deepCopy(), vbom);
					lst_Checkers_Black.add(tmp);
					scene.attachChild(tmp);
					if(j == x)
					{
						if(updateFake)
						{
							UpdateRollAtBeginning(i,false);
						}
					}
				}

			}
		}
		for(byte i = 0;i<=24;i++)
		{
			board[i] = board3[i];
		}
		//ShowRollDiceButton();
		showPossibleCheckers();

	}
}
