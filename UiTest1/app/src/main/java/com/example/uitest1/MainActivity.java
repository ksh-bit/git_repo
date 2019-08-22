package com.example.uitest1;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.uitest1.R;
import com.example.uitest1.helper.ObjectInOut;
import com.example.uitest1.model.Member;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button buttonInput, buttonList, buttonSave, buttonLoad;
    ArrayList<Member> list;
    ArrayList<Member> item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 객체 선언
        buttonInput = findViewById(R.id.buttonInput);
        buttonList = findViewById(R.id.buttonList);
        buttonSave = findViewById(R.id.buttonSave);
        buttonLoad = findViewById(R.id.buttonLoad);
        list = new ArrayList<>();
        item = (ArrayList<Member>) getIntent().getSerializableExtra("item");
        // 이벤트 설정
        buttonInput.setOnClickListener(this);
        buttonList.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
        buttonLoad.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        String fname = Environment.getExternalStorageDirectory().getAbsolutePath() + "/member.txt";
        if (item == null) {
            item = new ArrayList<>();
        }
        switch (v.getId()) {
            case R.id.buttonInput:
                intent = new Intent(this, InputActivity.class);
                if (item == null) {
                        intent.putExtra("list", list);

                } else {
                    intent.putExtra("list", item);
                }
                startActivity(intent);
                break;
            case R.id.buttonList:
                intent = new Intent(this, ListActivity.class);
                intent.putExtra("item", item);
                startActivity(intent);
                break;
            case R.id.buttonSave:
                boolean result = ObjectInOut.getInstance().write(fname, item);
                String msg1 = "";
                if (result) msg1 = "저장 성공";
                else msg1 = "저장 실패";
                Toast.makeText(this, msg1, Toast.LENGTH_SHORT).show();
                break;
            case R.id.buttonLoad:
                item.clear();
                item = (ArrayList<Member>) ObjectInOut.getInstance().read(fname);
                String msg2 = "";
                if (item.size() > 0) msg2 = "읽기 성공";
                else msg2 = "읽기 실패";
                Toast.makeText(this, msg2, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
