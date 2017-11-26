package com.bosic.bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Program {
	public static void main(String[] args){
		ChessBoard board = new ChessBoard();

		//请选择使用什么棋子
		Chess chess = getUserChess();

		User user = new User(Chess.WHITE);
		AIPlayer player  = new SmartPlayer(Chess.BLACK, board);

		if(chess == Chess.BLACK){
			user = new User(Chess.BLACK);
			player = new StupidPlayer(Chess.WHITE, board);
		}

		Game game = new Game(user, player, board);

		while(!game.getGameOver()){
			//等待用户录入
			Point p = inputUser();
			game.acceptUserChess(p.getX(), p.getY());
			board.print();
		}

	}
	//用户选择棋子
	private static Chess getUserChess() {
		System.out.println("您想使用什么棋子？(0:黑棋  1:白起)");
		String input = getInput();

		if(input.equals("0")){
			return Chess.BLACK;
		}
		else{
			return Chess.WHITE;
		}
	}

	private static String getInput() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String result="";
		try {
			result = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}
	//获得用户落子
	private static Point inputUser() {
		System.out.println("请输入你要下棋的位置:");
		String input = getInput();
		String[] token = input.split(",");
		int x = Integer.parseInt(token[0]);
		int y = Integer.parseInt(token[1]);
		return new Point(x, y);
	}
}
