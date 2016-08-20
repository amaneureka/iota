package com.example.dinesh.iota;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class NotificationCentre extends AppCompatActivity {
    private ImageView warning_sign;
    private TextView warning;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_centre);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        warning_sign=(ImageView)findViewById(R.id.WarningSign);
        warning=(TextView) findViewById(R.id.warning);
        Bundle obj=getIntent().getExtras();
        if(obj != null)
        {
            warning_sign.setImageResource(R.mipmap.attention);
            warning.setText(obj.getString("BUMPY_ROAD"));
        }
    }

}
