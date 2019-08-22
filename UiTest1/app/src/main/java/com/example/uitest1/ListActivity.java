package com.example.uitest1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.example.uitest1.R;
import com.example.uitest1.adapter.MemberAdapter;
import com.example.uitest1.model.Member;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements View.OnClickListener {
    ArrayList<Member> list;
    MemberAdapter adapter;
    ListView listView;
    Button buttonBack;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        // 객체 선언
        list = (ArrayList<Member>) getIntent().getSerializableExtra("item");
        adapter = new MemberAdapter(this, R.layout.list_item, list);
        listView = findViewById(R.id.listView);
        buttonBack = findViewById(R.id.buttonBack);
        intent = new Intent();
        // listView 설정
        listView.setAdapter(adapter);
        // 이벤트 설정
        buttonBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonBack:
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("item", list);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP );
                startActivity(intent);
                break;
        }
    }
}
