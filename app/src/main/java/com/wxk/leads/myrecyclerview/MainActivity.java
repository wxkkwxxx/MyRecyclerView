package com.wxk.leads.myrecyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.wxk.leads.myrecyclerview.use.BasicActivity;
import com.wxk.leads.myrecyclerview.use.CommonActivity;
import com.wxk.leads.myrecyclerview.use.DragActivity;
import com.wxk.leads.myrecyclerview.use.LoadRefreshActivity;
import com.wxk.leads.myrecyclerview.use.MultiActivity;
import com.wxk.leads.myrecyclerview.use.HeaderFooterActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void basic(View view) {
        Intent intent = new Intent(this, BasicActivity.class);
        startActivity(intent);
    }

    public void common(View view){
        Intent intent = new Intent(this, CommonActivity.class);
        startActivity(intent);
    }

    public void multi(View view){
        Intent intent = new Intent(this, MultiActivity.class);
        startActivity(intent);
    }

    public void headerFooter(View view){
        Intent intent = new Intent(this, HeaderFooterActivity.class);
        startActivity(intent);
    }

    public void dragItemAnimator(View view){
        Intent intent = new Intent(this, DragActivity.class);
        startActivity(intent);
    }

    public void refreshLoad(View view){
        Intent intent = new Intent(this, LoadRefreshActivity.class);
        startActivity(intent);
    }
}
