package ir.kolbe.backgammon;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.sprite.Sprite;

import android.sax.RootElement;
import android.util.Log;
import ir.kolbe.utils.Constants;
import ir.kolbe.utils.CpuMove_Location;
import ir.kolbe.utils.LocationModel;
import ir.kolbe.utils.Node;

public class CalcBestMove {

	public ArrayList<Byte> lst_PossibleCheckers; // ستون دایره های چشمک زن
	public ArrayList<Byte> lst_PossibleMoves;


	List<Node> childs = new ArrayList<Node>();
	byte[] board;
	byte selectedCheckerBoardPosition;
	byte selectedTargetBoardPosition;
	byte firstDice,secondDice;
	boolean playerTurn;
	boolean dice1,dice2;
	byte hitBlack;
	byte hitWhite;
	byte takenBlack;
	byte takenWhite;
	byte joft;
	public static boolean d1 = false,d2 = false,d4 = false;
	int p1DiceRem;
	int p2DiceRem;
	int names = 0;
	Node root;
	Node Best = null;
	public List<Node> Start(Node rootNode)
	{
		root = rootNode;
		names++;
		//childs.clear();
		List<CpuMove_Location> cpumoves = new ArrayList<CpuMove_Location>();
		loadState(rootNode);
		rootNode.name = names; 
		rootNode.childs = new ArrayList<Node>();
		lst_PossibleCheckers = CalcallPossibleCheckers();
		for(byte i = 0;i< lst_PossibleCheckers.size();i++)
		{
			if(lst_PossibleMoves != null)
				if(lst_PossibleMoves.size()>0)
					lst_PossibleMoves.clear();

			ArrayList<Byte> tmp = new ArrayList<Byte>();
			lst_PossibleMoves = CalcallPossibleMoves((byte)lst_PossibleCheckers.get(i));
			tmp.clear();
			tmp.addAll(lst_PossibleMoves);
			for(int j=0;j<tmp.size();j++)
			{
				//ایجاد یک نود و تنظیم رفرنس پدر و فرزندی
				names++;
				
				Node nd = new Node();
				nd.name = names;
				nd.from = lst_PossibleCheckers.get(i);
				nd.to =  tmp.get(j);
				nd.parent = rootNode;
				nd.parent.childs.add(nd);
				nd.isRoot = false;
				//شروع شبیه سازی بعد از حرکت
				Start_simulating(nd.from,nd.to);
				//خواندن مقادیر بعد حرکت و ذخیره وضعیت در نود
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
				nd.board = board.clone();
				//بازیابی وضعیت پدر برای حرکت سایر بچه ها
				loadState(nd.parent);
				childs.add(nd);
			}
		}
		
		int currentChild = -1;
		boolean skip = false;
		while(childs.size() > currentChild)
		{
			if(skip == false)
			  currentChild++;
			
			if(currentChild == childs.size())continue;
			Node nd = childs.get(currentChild);
			loadState(nd);
			if(is_PlayerTurn_finished() || !canMove())
			{
				skip = false;
				nd.hasChild = false;
				currentChild++;
				continue;
			}
			else
			{
				skip = true;
				childs.remove(currentChild);
				lst_PossibleCheckers.clear();
				lst_PossibleCheckers = CalcallPossibleCheckers();
				for(byte i = 0;i< lst_PossibleCheckers.size();i++)
				{
					if(lst_PossibleMoves != null)
						if(lst_PossibleMoves.size()>0)
							lst_PossibleMoves.clear();

					ArrayList<Byte> tmp = new ArrayList<Byte>();
					lst_PossibleMoves = CalcallPossibleMoves((byte)lst_PossibleCheckers.get(i));
					tmp.clear();
					tmp.addAll(lst_PossibleMoves);
					for(int j=0;j<tmp.size();j++)
					{
						//ایجاد یک نود و تنظیم رفرنس پدر و فرزندی
						names++;
						Node n = new Node();
						n.name = names;
						n.from = lst_PossibleCheckers.get(i);
						n.to =  tmp.get(j);
						n.parent = nd;
						if(n.parent.childs == null)
							n.parent.childs = new ArrayList<Node>();
						n.parent.childs.add(n);
						//شروع شبیه سازی بعد از حرکت
						Start_simulating(n.from,n.to);
						//خواندن مقادیر بعد حرکت و ذخیره وضعیت در نود
						n.firstDice = firstDice;
						n.secondDice = secondDice;
						n.dice1 = dice1;
						n.dice2 = dice2;
						n.takenBlack = takenBlack;
						n.takenWhite = takenWhite;
						n.hitBlack = hitBlack;
						n.hitWhite = hitWhite;
						n.playerTurn = playerTurn;
						n.p1DiceRem = p1DiceRem;
						n.p2DiceRem = p2DiceRem;
						n.joft = joft;
						n.d1 = d1;
						n.d2 = d2;
						n.d4 = d4;
						n.isRoot = false;
						n.board = board.clone();
						//بازیابی وضعیت پدر برای حرکت سایر بچه ها
						loadState(n.parent);
						childs.add(n);
					}
				}

			}
		}//All Done
		
		
		for(int j=0;j<childs.size();j++)
		{
			Node nn = childs.get(j);
			nn.worth = calcWorth(nn);
			if(Best == null) Best = nn;
			else
			{
				if(nn.worth > Best.worth)
					Best = nn;
			}
			/*Log.i("farhad","" + n.name + ", " + n.worth);*/
			if(nn.parent.isRoot == false)
			{
			Log.i(  "eee", "F: " + nn.parent.from + ", T: " + nn.parent.to + " -------->" +  "F: " + nn.from + ", T: " + nn.to + " , W = " + nn.worth);
			}
			else
				Log.i(  "eee", "F: " + nn.from + ", T: " + nn.to + " , W = " + nn.worth);
		   
		}
        Node n = Best;
        //Best.moves = new ArrayList<CpuMove_Location>();
        //cpumoves.clear();
        List<Node> nodes = new ArrayList<Node>();
        while (n.isRoot == false)
        {
        	Node x = n.clone();
        	nodes.add(n);
        	n = n.parent;
        }
		return nodes;
	}
	private int calcWorth(Node n)
	{
		int x = 1;
		byte checkers_dar_Mohavate_az_ghabl = 0;
		byte checkers_dar_Mohavate_ba_een = 0;
		
		byte bishtar_az_yek_shode_ba_een_harkat = 0; 
		byte bishtar_az_yek_shode_az_ghabl = 0; 
		byte takhaye_ghable_harkat = 0;
		byte zade_ghabl=0;
		byte zade_ba_een = 0;
		boolean taken_mishe = false;
		byte takhaye_man = 0;
		boolean tak_dar_mohavate_khodam = false;
		boolean poshte_mahvate_pormishe = false;
		boolean harif_dar_mohavate_man = false;
		if(n.playerTurn)
		{
			zade_ba_een = n.hitBlack;
			zade_ghabl = root.hitBlack;
			if(n.board[7] > 1 && root.board[7] <= 1) poshte_mahvate_pormishe = true;
			for(int i=1;i<=24;i++)
			{
				byte brd = n.board[i];
				if(i < 7)
				 checkers_dar_Mohavate_ba_een = (byte) (checkers_dar_Mohavate_ba_een + brd);
				
				if(brd == 1) takhaye_man++;
				if(brd == 1 && i < 7) tak_dar_mohavate_khodam = true;
				if(i < 7 && brd < 0) harif_dar_mohavate_man = true;
				if(brd > 1) bishtar_az_yek_shode_ba_een_harkat++;
				
				if(n.takenWhite > root.takenWhite) taken_mishe = true;
				brd = root.board[i];
				if(i  < 7)
					 checkers_dar_Mohavate_az_ghabl = (byte) (checkers_dar_Mohavate_az_ghabl + brd);
				if(brd == 1) takhaye_ghable_harkat++;
				if(brd > 1) bishtar_az_yek_shode_az_ghabl++;
			}
		}
		else
		{
			if(n.board[18] < -1 && root.board[18] >= -1) poshte_mahvate_pormishe = true;
			zade_ba_een = n.hitWhite;
			zade_ghabl = root.hitWhite;
			for(int i=1;i<=24;i++)
			{
				byte brd = n.board[i];
				if(i > 18)
					 checkers_dar_Mohavate_ba_een = (byte) (checkers_dar_Mohavate_ba_een + (brd * -1));
				
				if(brd == -1) takhaye_man++;
				if(brd == -1 && i > 18) tak_dar_mohavate_khodam = true;
				if(i > 18 && brd > 1) harif_dar_mohavate_man = true;
				if(brd < -1) bishtar_az_yek_shode_ba_een_harkat++;
				
				
				if(n.takenBlack > root.takenBlack) taken_mishe = true;
				brd = root.board[i];
				if(i > 18)
					 checkers_dar_Mohavate_az_ghabl = (byte) (checkers_dar_Mohavate_az_ghabl + (brd * -1));
				if(brd == -1) takhaye_ghable_harkat++;
				if(brd< -1) bishtar_az_yek_shode_az_ghabl++;
			}
		}
		
		if(bringing_Off_Status(n))
		{
			 //اگه مهره خارج میکنه
		    if(taken_mishe == true) x+=1000 * (n.takenBlack - root.takenBlack) ;
		}
		else if(Warfinished(n))
		{
			if(checkers_dar_Mohavate_ba_een > checkers_dar_Mohavate_az_ghabl)
			{
				x+= 1000 * (checkers_dar_Mohavate_ba_een - checkers_dar_Mohavate_az_ghabl);
			}
			if(checkers_dar_Mohavate_ba_een == checkers_dar_Mohavate_az_ghabl)
			{
				x+= 900;
			}
		}
		else
		{
			//اگه تک در محوطه خودم ندارم، زده های حریف رو امتیاز حساب کنم وگرنه چون خطریه بی خیال امتیاز دادن
			if(zade_ba_een > zade_ghabl)
			{
			if(tak_dar_mohavate_khodam == false) x= x + ((zade_ba_een - zade_ghabl) * 50000);
			else x = x - ((zade_ba_een - zade_ghabl) * 50000);
			}
			//با این حرکت آخر پشت محوطه  پرمیشه و حریفم در محوطه من هست
		    if(poshte_mahvate_pormishe && harif_dar_mohavate_man == true) x+= 50000;
		    //در کل تک ندادم امتیاز داره
		    if(takhaye_man == 0) x+= 30000;
		  //اگه خونه های تک  بعد از این حرکت حرکت کمتر میشه امتیاز خوبی بدم
		    if(takhaye_ghable_harkat > takhaye_man) x+= 1000000;
		    //اگه خونه میبنده با این حرکت
		    if(bishtar_az_yek_shode_ba_een_harkat > bishtar_az_yek_shode_az_ghabl) 
		    { 
		    	x = x + ( (bishtar_az_yek_shode_ba_een_harkat - bishtar_az_yek_shode_az_ghabl) * 60000);
		        
		    }
		}
		/*if(n.parent.isRoot == false)
		{
		Log.i(  "eee", "F: " + n.parent.from + ", T: " + n.parent.to + " -------->" +  "F: " + n.from + ", T: " + n.to + " , W = " + x);
		}
		else
			Log.i(  "eee", "F: " + n.from + ", T: " + n.to + " , W = " + x);
	   */
		
	   
	  
		//if(n.parent.name != 1) x = x+50000;
	   // if(n.board[22] == -2) x = 99000000;
		return x;
	}
	private void loadState(Node n)
	{
		board = n.board.clone();
		//   System.arraycopy(n.board, 0,board, 0, n.board.length);
		selectedCheckerBoardPosition = n.from;
		selectedTargetBoardPosition = n.to;
		firstDice = n.firstDice;
		secondDice = n.secondDice;
		playerTurn = n.playerTurn;
		dice1 = n.dice1;
		dice2 = n.dice2;
		hitBlack = n.hitBlack;
		hitWhite = n.hitWhite;
		takenBlack = n.takenBlack;
		takenWhite = n.takenWhite;
		joft = n.joft;
		d1 = n.d1;
		d2 = n.d2;
		d4 = n.d4;
		p1DiceRem = n.p1DiceRem;
		p2DiceRem = n.p2DiceRem;
	}
	private boolean Start_simulating(byte from, byte to)
	{
		selectedTargetBoardPosition = to;
		selectedCheckerBoardPosition = from;
		boolean ok = false;
		int toMinus = 0;//عددی که باید از صد و شصت و هفت یا هرچی که الان شده کم بشه


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
		move_a_checker();
		ok = true;
		WriteText(toMinus,0);	
		return ok;



	}
	private void move_a_checker()
	{
		boolean Hit_Happened = false;
		byte x=0,xx=0,y=0,yy=0;

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
		}

	}
	private void WriteText(final int txt,final int txt2)//txt2 واسه کسی که خورده و باید بهش اضافه بشه
	{
		if(playerTurn)
		{
			p1DiceRem = p1DiceRem - txt;
			p2DiceRem = p2DiceRem + txt2;
		}else
		{
			p2DiceRem = p2DiceRem - txt;
			p1DiceRem = p1DiceRem + txt2;

		}

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
	private boolean Warfinished(Node n)
	{
		boolean w = false;
		if(bringing_Off_Status()) return false;
		if(n.takenBlack > 0 || n.takenWhite > 0) return false;
		byte maxW = getMaxWhite(n);
		byte minB = getMinBlack(n);
		if(minB > maxW)
			w = true;
		return w;
	}
	private byte getMaxWhite(Node n)
	{
		byte x = 1;
		for(int i=24;i>0;i--)
		{
			byte tmp = n.board[i];
			if(tmp > 0) 
			{
				x = (byte)i;
				break;
			}
		}
		return x;
	}
	private byte getMinBlack(Node n)
	{
		byte x = 1;
		for(int i=1;i<=24;i++)
		{
			byte tmp = n.board[i];
			if(tmp < 0) 
			{
				x = (byte)i;
				break;
			}
		}
		return x;
	}
	private boolean bringing_Off_Status(Node n)
	{

		if(n.playerTurn)
		{
			if(n.hitWhite > 0) return false;
			for(int i=7;i<=24;i++)
			{
				if(n.board[i]>0)return false;
			}
			return true;
		}
		else
		{
			if(n.hitBlack > 0) return false;
			for(int i=1;i<=18;i++)
			{
				if(n.board[i] < 0)return false;
			}
			return true;
		}
	}
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
	private boolean is_PlayerTurn_finished()
	{
		boolean finished = false;
		if(firstDice != secondDice)
		{
			
			if(dice1 == true && dice2 == true)
			{
				finished = true;
				//showMessage("1: changed to " + playerTurn);
			}
		}else
		{
			if(joft >= 4)
			{
				finished = true;
			}
		}
		if(playerTurn)
		{
			if(takenWhite == 15)finished = true;
			
		}else
		{
			if(takenBlack == 15)finished = true;
			
		}
		
		
		return finished;
	}

}
