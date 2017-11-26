package com.bosic.bean;

public class SmartPlayer extends AIPlayer {
	private final static int FIVE = 5000;
	private final static int FOUR_LIVE = 2000;
	private final static int FOUR = 900;
	private final static int THREE_LIVE = 400;
	private final static int THREE = 100;
	private final static int TWO_LIVE = 40;
	private final static int TWO = 10;
	private final static int ONE = 4;
	private final static int DEAD = 0;

	private ChessBoard board;

	public SmartPlayer(Chess chess, ChessBoard board) {
		super(chess);
		this.board = board;
	}

	@Override
	public Point putChess() {
		int total = 0;
		Point point = new Point();
		// 计算每个棋子的价值，找出最大价值的棋子。
		for (int i = 0; i < ChessBoard.CHESS_X_ONBOARD; i++) {
			for (int j = 0; j < ChessBoard.CHESS_Y_ONBOARD; j++) {
				// 如果有棋子
				if (this.board.getBoard()[i][j] != Chess.NO) {
					continue;
				}
				// 估值
				int t = evaluate(i, j);
				// 保留最大值位置
				if (t > total) {
					total = t;
					point.setX(i);
					point.setY(j);
				}
			}
		}

		//System.out.println("score="+total);

		// 落子
		if(total == 0){
			point = new Point(
					ChessBoard.CHESS_X_ONBOARD / 2,
					ChessBoard.CHESS_Y_ONBOARD / 2);
		}

		this.board.getBoard()[point.getX()][point.getY()] = this.getChess();
		return point;
	}

	private int evaluate(int x, int y) {
		// 计算对手的价值
		int total = getScoreByChessType(x, y,
				this.getChess() == Chess.BLACK ? Chess.WHITE : Chess.BLACK);
		// 计算自己的价值
		total += getScoreByChessType(x, y, this.getChess());

		return total;
	}

	private int getScoreByChessType(int x, int y, Chess chess) {
		int total = scoreOfH(x, y, chess) + // 横向
				scoreOfV(x, y, chess) + // 垂直
				scoreLBtoRT(x, y, chess) + // 左下到右上
				scoreLTtoRB(x, y, chess); // 左上到右下
		return total;
	}

	private int scoreOfH(int x, int y, Chess chess) {
		boolean begin = false;
		boolean end = false;
		if (x == 0 || this.board.getBoard()[x - 1][y] != chess) {
			begin = true;
		}
		if (x == ChessBoard.CHESS_X_ONBOARD - 1
				|| this.board.getBoard()[x + 1][y] != chess) {
			end = true;
		}
		Chess[][] chessBoard = this.board.getBoard();

		int total = 1;
		for (int i = x - 1; i >= 0; i--) {
			if (chessBoard[i][y] != chess) {
				break;
			}
			total++;
		}
		for (int i = x + 1; i < ChessBoard.CHESS_X_ONBOARD; i++) {
			if (chessBoard[i][y] != chess) {
				break;
			}
			total++;
		}
		return getScoreByChessNum(begin, end, total);
	}

	private int getScoreByChessNum(boolean begin, boolean end, int total) {
		if (total >= 5) {
			return FIVE;
		}
		if (begin && end) {
			return DEAD;
		}
		switch (total) {
			case 4:
				if (begin || end) {
					return FOUR;
				}
				return FOUR_LIVE;
			case 3:
				if (begin || end) {
					return THREE;
				}
				return THREE_LIVE;
			case 2:
				if (begin || end) {
					return TWO;
				}
				return TWO_LIVE;
			case 1:
				return ONE;
		}
		return 0;
	}

	private int scoreOfV(int x, int y, Chess chess) {
		boolean begin = false;
		boolean end = false;
		if (y == 0 || this.board.getBoard()[x][y-1] != chess) {
			begin = true;
		}
		if (y == ChessBoard.CHESS_Y_ONBOARD - 1
				|| this.board.getBoard()[x][y+1] != chess) {
			end = true;
		}
		Chess[][] chessBoard = this.board.getBoard();
		int total = 1;
		for (int i = y - 1; i >= 0; i--) {
			if (chessBoard[x][i] != chess) {
				break;
			}
			total++;
		}
		for (int i = y + 1; i < ChessBoard.CHESS_Y_ONBOARD; i++) {
			if (chessBoard[x][i] != chess) {
				break;
			}
			total++;
		}
		return getScoreByChessNum(begin, end, total);
	}

	private int scoreLBtoRT(int x, int y, Chess chess) {
		boolean begin = false;
		boolean end = false;
		//左下端
		if(x==0 || y==ChessBoard.CHESS_Y_ONBOARD-1 ||
				this.board.getBoard()[x-1][y+1] != chess){
			begin = true;
		}
		//右上端
		if(y==0 || x==ChessBoard.CHESS_X_ONBOARD-1 ||
				this.board.getBoard()[x+1][y-1] != chess){
			end = true;
		}
		int total = 1;
		Chess[][] chessBoard = this.board.getBoard();
		//左下方向数子
		for(int i=x-1,j=y+1;
			i>=0 && j <ChessBoard.CHESS_Y_ONBOARD;
			i--,j++){
			if(chessBoard[i][j] != chess){
				break;
			}
			total++;
		}
		//右上方向数子
		for(int i=x+1, j=y-1;
			i<ChessBoard.CHESS_X_ONBOARD && j >= 0;
			i++, j--){
			if(chessBoard[i][j] != chess){
				break;
			}
			total++;
		}
		return this.getScoreByChessNum(begin, end, total);
	}

	private int scoreLTtoRB(int x, int y, Chess chess) {
		boolean begin = false;
		boolean end = false;
		//左上端
		if(x == 0 || y==0 || this.board.getBoard()[x-1][y-1] != chess){
			begin = true;
		}
		//右下端
		if(x == ChessBoard.CHESS_X_ONBOARD-1 ||
				y == ChessBoard.CHESS_Y_ONBOARD-1 ||
				this.board.getBoard()[x+1][y+1] != chess){
			end = true;
		}
		int total = 1;
		Chess[][] chessBoard = this.board.getBoard();
		//左上方向数子
		for(int i = x-1, j=y-1;
			i >=0 && j >=0;
			i--,j--){
			if(chessBoard[i][j] != chess){
				break;
			}
			total++;
		}
		//右下方向数子
		for(int i=x+1, j=y+1;
			i < ChessBoard.CHESS_X_ONBOARD && j < ChessBoard.CHESS_Y_ONBOARD;
			i++,j++){
			if(chessBoard[i][j] != chess){
				break;
			}
			total++;
		}
		return this.getScoreByChessNum(begin, end, total);
	}
}
