package com.bosic.bean;

import java.util.Random;

public class StupidPlayer extends AIPlayer {
	private ChessBoard board;

	public StupidPlayer(Chess chess, ChessBoard board) {
		super(chess);
		this.board = board;
	}

	@Override
	public Point putChess() {
		Random rand = new Random();
		int x = rand.nextInt(ChessBoard.CHESS_X_ONBOARD);
		int y = rand.nextInt(ChessBoard.CHESS_Y_ONBOARD);

		while(this.board.getBoard()[x][y] != Chess.NO){
			x = rand.nextInt(ChessBoard.CHESS_X_ONBOARD);
			y = rand.nextInt(ChessBoard.CHESS_Y_ONBOARD);
		}

		this.board.getBoard()[x][y] = this.getChess();

		return new Point(x, y);
	}

}
