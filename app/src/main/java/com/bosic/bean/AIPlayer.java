package com.bosic.bean;

public abstract class AIPlayer {
	private Chess chess;

	public AIPlayer(Chess chess){
		this.chess = chess;
	}

	public Chess getChess(){
		return chess;
	}

	public abstract Point putChess();
}
