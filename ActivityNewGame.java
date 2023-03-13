package com.zzd.wzqjx;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ActivityNewGame extends AppCompatActivity implements View.OnClickListener {

    private Button btn_pvp;
    private Button btn_pvi;
    private Button btn_return;
    private ChessView ChessView;
    private Win isWin;
    private Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_new_game);

        initView();
    }

    private void initView() {
        ChessView = findViewById(R.id.ChessView);

        btn_pvp = findViewById(R.id.btn_pvp);
        btn_pvp.setOnClickListener(this);

        btn_pvi = findViewById(R.id.btn_pvi);
        btn_pvi.setOnClickListener(this);

        btn_return = findViewById(R.id.btn_return);
        btn_return.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_pvp:
                ChessView.restart();
                ChessView.setMode(false);
                break;
            case R.id.btn_pvi:
                aiDialog();
                ChessView.setMode(true);
                break;
            case R.id.btn_return:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;

        }


    }

    private void aiDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder =new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage("选择先手方");
        builder.setPositiveButton("玩家先手", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ChessView.restart(true);
            }
        });
        builder.setNegativeButton("电脑先手", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ChessView.restart(false);
            }
        });
        AlertDialog dialog =builder.create();
        dialog.show();
    }

}
