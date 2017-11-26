package com.bosic.fivechess;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Process;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.bosic.bean.Chess;
import com.bosic.bean.SmartPlayer;
import com.bosic.bean.StupidPlayer;
import com.bosic.bean.User;
import com.bosic.view.ChessPanel;

public class MainActivity extends Activity {
    private ChessPanel panel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.panel = (ChessPanel)findViewById(R.id.chessPanel);

//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		builder.setTitle("选择你是用的棋子");
//		String[] choiceItems = {"黑棋","白棋"};

        panel.user = new User(Chess.BLACK);
        panel.player = new SmartPlayer(Chess.WHITE, panel.getBoard());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.game_options, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.ai_stupid:
                this.panel.player = new StupidPlayer(this.panel.player.getChess(),
                        this.panel.getBoard());
                break;
            case R.id.ai_smart:
                this.panel.player = new SmartPlayer(this.panel.player.getChess(),
                        this.panel.getBoard());
                break;
            case R.id.restart:
                this.panel.initPanel();
                break;
            case R.id.exit:
                Process.killProcess(Process.myPid());
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
