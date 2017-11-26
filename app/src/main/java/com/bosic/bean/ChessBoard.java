package com.bosic.bean;

public class ChessBoard {
	public final static int CHESS_X_ONBOARD = 9;
	public final static int CHESS_Y_ONBOARD = 13;
	private Chess[][] board;

	public ChessBoard(){
		this.init();
	}

	public void init(){
		this.board = new Chess[CHESS_X_ONBOARD][CHESS_Y_ONBOARD];

		for(int i=0; i<CHESS_X_ONBOARD;i++){
			for(int j=0; j<CHESS_Y_ONBOARD;j++){
				this.board[i][j] = Chess.NO;
			}
		}

		//this.board[4][6] = Chess.WHITE;
		//this.board[7][8] = Chess.BLACK;
	}
	public void print(){
		System.out.println("----------------------------------");
		for(int i=0; i<CHESS_X_ONBOARD;i++){
			for(int j=0;j<CHESS_Y_ONBOARD;j++){
				System.out.print(this.board[i][j].getIcon()+" ");
			}
			System.out.println();
		}
	}
	public Chess[][] getBoard(){
		return this.board;
	}
}
