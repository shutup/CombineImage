package com.shutup.combineimage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @InjectView(R.id.combineTwoImageMenu)
    Button mCombineTwoImageMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

    }


    @OnClick(R.id.combineTwoImageMenu)
    public void onClick() {
        Intent intent = new Intent(MainActivity.this, CombineImageActivity.class);
        startActivity(intent);
    }
}
