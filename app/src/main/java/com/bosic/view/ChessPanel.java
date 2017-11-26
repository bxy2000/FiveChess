package com.bosic.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.bosic.bean.AIPlayer;
import com.bosic.bean.Chess;
import com.bosic.bean.ChessBoard;
import com.bosic.bean.Point;
import com.bosic.bean.User;
import com.bosic.fivechess.MainActivity;
import com.bosic.fivechess.R;

public class ChessPanel extends View {
    private int BORDER_LENGTH = 160;//36;
    private final static int PADDING_LEFT = 80;//15;
    private final static int PADDING_TOP = 80;//15;

    private Bitmap black;
    private Bitmap white;
    private ChessBoard board;

    private boolean isStop = false;

    private AlertDialog.Builder builder;

    public User user;
    public AIPlayer player;

    public ChessPanel(Context context) {
        super(context);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;
        //BORDER_LENGTH = (w_screen - 2 * PADDING_LEFT) / ChessBoard.CHESS_X_ONBOARD;

        Log.v("ChessPanel", ""+BORDER_LENGTH);

        System.out.println(BORDER_LENGTH);
    }

    public ChessBoard getBoard() {
        return this.board;
    }

    public void initPanel() {
        this.board.init();
        this.invalidate();
        this.isStop = false;
    }

    public ChessPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.board = new ChessBoard();

        this.black = BitmapFactory.decodeResource(getResources(), R.drawable.black);
        this.white = BitmapFactory.decodeResource(getResources(), R.drawable.white);

        this.builder = new AlertDialog.Builder(getContext());
        this.builder.setMessage("是否需要重新开始？");
        this.builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                ChessPanel.this.initPanel();
            }
        });
        this.builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //什么都不做
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.LTGRAY);
        paint.setStyle(Style.STROKE);
        //画棋盘
        for (int i = 0; i < ChessBoard.CHESS_X_ONBOARD - 1; i++) {
            for (int j = 0; j < ChessBoard.CHESS_Y_ONBOARD - 1; j++) {
                canvas.drawRect(
                        PADDING_LEFT + i * BORDER_LENGTH,
                        PADDING_TOP + j * BORDER_LENGTH,
                        PADDING_LEFT + (i + 1) * BORDER_LENGTH,
                        PADDING_TOP + (j + 1) * BORDER_LENGTH,
                        paint);
            }
        }
        //画棋子
        for (int i = 0; i < ChessBoard.CHESS_X_ONBOARD; i++) {
            for (int j = 0; j < ChessBoard.CHESS_Y_ONBOARD; j++) {
                if (this.board.getBoard()[i][j] == Chess.BLACK) {
                    canvas.drawBitmap(black,
                            PADDING_LEFT + i * BORDER_LENGTH - BORDER_LENGTH / 2,
                            PADDING_TOP + j * BORDER_LENGTH - BORDER_LENGTH / 2,
                            paint);
                }

                if (this.board.getBoard()[i][j] == Chess.WHITE) {
                    canvas.drawBitmap(white,
                            PADDING_LEFT + i * BORDER_LENGTH - BORDER_LENGTH / 2,
                            PADDING_TOP + j * BORDER_LENGTH - BORDER_LENGTH / 2,
                            paint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (isStop) {
                Toast.makeText(getContext(), "", Toast.LENGTH_LONG).show();
                return true;
            }
            float x = event.getX();
            float y = event.getY();

            int col;
            int row;

            if ((x - PADDING_LEFT) % BORDER_LENGTH < BORDER_LENGTH / 2) {
                col = (int) ((x - PADDING_LEFT) / BORDER_LENGTH);
            } else {
                col = (int) ((x - PADDING_LEFT) / BORDER_LENGTH) + 1;
            }

            if ((y - PADDING_TOP) % BORDER_LENGTH < BORDER_LENGTH / 2) {
                row = (int) ((y - PADDING_TOP) / BORDER_LENGTH);
            } else {
                row = (int) ((y - PADDING_TOP) / BORDER_LENGTH) + 1;
            }

            col = col < 0 ? 0 : col;
            col = col >= ChessBoard.CHESS_X_ONBOARD ?
                    ChessBoard.CHESS_X_ONBOARD - 1 : col;

            row = row < 0 ? 0 : row;
            row = row >= ChessBoard.CHESS_Y_ONBOARD ?
                    ChessBoard.CHESS_Y_ONBOARD - 1 : row;

            if (this.board.getBoard()[col][row] != Chess.NO) {
                Toast.makeText(getContext(), "无效位置，已有棋子！", Toast.LENGTH_SHORT).show();
                return true;
            }

            this.board.getBoard()[col][row] = this.user.getChess();

            this.invalidate();

            if (isWon(col, row, this.user.getChess())) {
                this.builder.setTitle("太厉害了，你赢了！").create().show();
                this.isStop = true;
            }

            Point p = this.player.putChess();

            this.invalidate();

            if (isWon(p.getX(), p.getY(), this.player.getChess())) {
                this.builder.setTitle("你输了，真是可惜！").create().show();
                this.isStop = true;
            }
        }
        return true;
    }

    private boolean isWon(int x, int y, Chess chess) {
        if (getNumOfHorizontal(x, y, chess) >= 5) {
            return true;
        }

        if (getNumOfVertial(x, y, chess) >= 5) {
            return true;
        }

        if (getNumOfLBtoRT(x, y, chess) >= 5) {
            return true;
        }

        if (getNumOfLTtoRB(x, y, chess) >= 5) {
            return true;
        }

        return false;
    }

    private int getNumOfHorizontal(int x, int y, Chess chess) {
        Chess[][] chessBoard = this.board.getBoard();
        int total = 1;
        //向左数子
        for (int i = x - 1; i >= 0; i--) {
            if (chessBoard[i][y] != chess) {
                break;
            }
            total++;
        }
        //向右数字
        for (int i = x + 1; i < ChessBoard.CHESS_X_ONBOARD; i++) {
            if (chessBoard[i][y] != chess) {
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
        for (int i = y - 1; i >= 0; i--) {
            if (chessBoard[x][i] != chess) {
                break;
            }
            total++;
        }
        //向下数子
        for (int i = y + 1; i < ChessBoard.CHESS_Y_ONBOARD; i++) {
            if (chessBoard[x][i] != chess) {
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
        for (int i = x - 1, j = y + 1;
             i >= 0 && j < ChessBoard.CHESS_Y_ONBOARD;
             i--, j++) {
            if (chessBoard[i][j] != chess) {
                break;
            }
            total++;
        }
        //右上数子
        for (int i = x + 1, j = y - 1;
             i < ChessBoard.CHESS_X_ONBOARD && j >= 0;
             i++, j--) {
            if (chessBoard[i][j] != chess) {
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
        for (int i = x - 1, j = y - 1;
             i >= 0 && j >= 0;
             i--, j--) {
            if (chessBoard[i][j] != chess) {
                break;
            }
            total++;
        }
        //右下数子
        for (int i = x + 1, j = y + 1;
             i < ChessBoard.CHESS_X_ONBOARD &&
                     j < ChessBoard.CHESS_Y_ONBOARD;
             i++, j++) {
            if (chessBoard[i][j] != chess) {
                break;
            }
            total++;
        }
        return total;
    }
}
