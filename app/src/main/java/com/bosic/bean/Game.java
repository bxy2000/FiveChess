package com.bosic.bean;

public class Game {
	private ChessBoard board;
	private User user;
	private AIPlayer player;
	private boolean gameOver = false;

	public Game(){
	}

	public Game(User user, AIPlayer player, ChessBoard board){
		this.user = user;
		this.player = player;
		this.board = board;
	}

	public boolean getGameOver(){
		return gameOver;
	}

	public void acceptUserChess(int x, int y){
		if(gameOver){
			return;
		}
		// 人下一步棋
		this.board.getBoard()[x][y] = user.getChess();
		// 判断输赢
		if(isWon(x, y, user.getChess())){
			gameOver = true;
			System.out.println("恭喜你，你赢了！");
			return;
		}
		// 机器响应
		Point p = this.player.putChess();
		// 判断输赢
		if(isWon(p.getX(), p.getY(), player.getChess())){
			gameOver = true;
			System.out.println("对不起，机器赢了！");
		}
	}

	private boolean isWon(int x, int y, Chess chess) {
		if(getNumOfHorizontal(x, y, chess) >= 5){
			return true;
		}

		if(getNumOfVertial(x, y, chess) >= 5){
			return true;
		}

		if(getNumOfLBtoRT(x, y, chess) >= 5){
			return true;
		}

		if(getNumOfLTtoRB(x, y, chess) >= 5){
			return true;
		}

		return false;
	}

	private int getNumOfHorizontal(int x, int y, Chess chess) {
		Chess[][] chessBoard = this.board.getBoard();
		int total = 1;
		//向左数子
		for(int i=x-1; i>=0; i--){
			if(chessBoard[i][y] != chess){
				break;
			}
			total++;
		}
		//向右数字
		for(int i=x+1; i<ChessBoard.CHESS_X_ONBOARD;i++){
			if(chessBoard[i][y] != chess){
				break;
			}
			total++;
		}
		return total;
	}

	private int getNumOfVertial(int x, int y, Chess chess) {
		Chess[][] chessBoard = this.board.getBoard();
		int total = 1;
		//向上数子
		for(int i = y-1; i >=0; i--){
			if( chessBoard[x][i] != chess){
				break;
			}
			total++;
		}
		//向下数子
		for(int i = y+1; i < ChessBoard.CHESS_Y_ONBOARD; i++){
			if(chessBoard[x][i] != chess){
				break;
			}
			total++;
		}
		return total;
	}

	private int getNumOfLBtoRT(int x, int y, Chess chess) {
		Chess[][] chessBoard = this.board.getBoard();
		int total = 1;
		//左下数子
		for(int i=x-1, j=y+1;
			i>=0 && j < ChessBoard.CHESS_Y_ONBOARD;
			i--, j++){
			if(chessBoard[i][j] != chess){
				break;
			}
			total++;
		}
		//右上数子
		for(int i=x+1, j=y-1;
			i < ChessBoard.CHESS_X_ONBOARD && j>=0;
			i++,j--){
			if(chessBoard[i][j] != chess){
				break;
			}
			total++;
		}
		return total;
	}

	private int getNumOfLTtoRB(int x, int y, Chess chess) {
		Chess[][] chessBoard = this.board.getBoard();
		int total = 1;
		//左上数子
		for(int i=x-1, j=y-1;
			i>=0 && j >= 0;
			i--, j--){
			if(chessBoard[i][j] != chess){
				break;
			}
			total++;
		}
		//右下数子
		for(int i=x+1, j=y+1;
			i < ChessBoard.CHESS_X_ONBOARD &&
					j < ChessBoard.CHESS_Y_ONBOARD;
			i++,j++){
			if(chessBoard[i][j] != chess){
				break;
			}
			total++;
		}
		return total;
	}
}
