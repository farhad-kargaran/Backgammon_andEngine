package ir.kolbe.utils;

import java.util.ArrayList;
import java.util.List;

public class Node {
	
	public byte from;
	public byte to;
	
	public boolean hasChild;
	public boolean isRoot;
	public int name;
	public Node parent;
	
	public int worth;
	public List<Node> childs;
	public  byte[] board = { 0, -2, 0, 0, 0,	0, 5, 0, 3, 0, 0, 0, -5, 5, 0, 0, 0, -3, 0, -5, 0, 0, 0, 0, 2 };
	public boolean playerTurn;
	public byte firstDice;
	public byte secondDice;
	public boolean dice1;
	public boolean dice2;
	public byte hitBlack;
	public byte hitWhite;
	public byte takenBlack;
	public byte takenWhite;
	public int p1DiceRem;
	public int p2DiceRem;
	public byte joft;
	public boolean d1;
	public boolean d2;
	public boolean d4;
	
	public Node clone()
	{
		Node x = new Node();
		x.board = board.clone();
		x.playerTurn = playerTurn;
		x.firstDice = firstDice;
		x.secondDice = secondDice;
		x.dice1 = dice1;
		x.dice2 = dice2;
		x.hitBlack = hitBlack;
		x.hitWhite = hitWhite;
		x.takenBlack = takenBlack;
		x.takenWhite = takenWhite;
		x.p1DiceRem = p1DiceRem;
		x.p2DiceRem = p2DiceRem;
		x.joft = joft;
		x.d1 = d1;
		x.d2 = d2;
		x.d4 = d4;
		x.from = from;
		x.to = to;
		x.hasChild = hasChild;
		x.isRoot = isRoot;
		x.name = name;
		x.parent = parent;
		x.worth = worth;
		if(childs!=null)
		{
			x.childs = new ArrayList<Node>();
			for(int i=0;i<childs.size();i++)
			{
				Node c = childs.get(i);
				x.childs.add(c);
			}
		}
		return x;
	}
}
