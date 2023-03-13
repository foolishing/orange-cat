package com.zzd.wzqjx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_NewGame;
    private Button btn_Help;
    private Button btn_About;
    private Button btn_End;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView()
    {
        btn_NewGame = findViewById(R.id.btn_NewGame);
        btn_NewGame.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btn_NewGame:
                intent = new Intent(this,ActivityNewGame.class);
                startActivity(intent);
                finish();
                break;

        }
    }
}