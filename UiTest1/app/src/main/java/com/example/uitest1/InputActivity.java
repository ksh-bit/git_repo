package com.example.uitest1;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.uitest1.R;
import com.example.uitest1.helper.FileUtils;
import com.example.uitest1.helper.PhotoHelper;
import com.example.uitest1.helper.RegexHelper;
import com.example.uitest1.model.Member;

import java.io.File;
import java.util.ArrayList;

public class InputActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editTextName, editTextEmail, editTextTel, editTextAddr;
    Button buttonImage, buttonInput, buttonCancel;
    ArrayList<Member> list;
    Intent intent;
    int check_input = 0;
    String filePath;
    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        // 객체 선언
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextTel = findViewById(R.id.editTextTel);
        editTextAddr = findViewById(R.id.editTextAddr);
        buttonImage = findViewById(R.id.buttonImage);
        buttonInput = findViewById(R.id.buttonInput);
        buttonCancel = findViewById(R.id.buttonCancel);
        list = (ArrayList<Member>) getIntent().getSerializableExtra("list");
        intent = new Intent();

        filePath = "";
        // 이벤트 설정
        buttonImage.setOnClickListener(this);
        buttonInput.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        permissionCheck2();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void permissionCheck1() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }
        }
    }

    private void permissionCheck2() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
            }
        }
    }

    @Override
    public void onClick(View v) {
        String msg = null;
        switch (v.getId()) {
            case R.id.buttonImage:
                permissionCheck1();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final String[] items = {"새로 촬영하기", "갤러리에서 가져오기"};
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = null;
                        switch (which) {
                            case 0: // 새로 촬영하기
                                filePath = PhotoHelper.getInstance().getNewPhotoPath();
                                File file = new File(filePath);
                                Uri uri = null;
                                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    uri = FileProvider.getUriForFile(InputActivity.this,
                                            getApplicationContext().getPackageName() + ".fileprovider", file);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                    intent.putExtra(AUDIO_SERVICE, false);
                                    // 카메라앱 호출
                                } else {
                                    uri = Uri.fromFile(file);
                                }
                                Log.d("[INFO]", "uri = " + uri.toString());
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                                startActivityForResult(intent, 100);
                                break;
                            case 1: // 갤러리에서 가져오기
                                // 갤러리를 호출하기 위한 암묵적 인텐트
                                intent = null;
                                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                                } else {
                                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                                }
                                // 이미지 파일만 필터링 => MIME 형태 (KITKAT 이상 버전용)
                                intent.setType("image/*");
                                // 구글 클라우드에 싱크된 파일 제외
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                                }
                                // 갤러리 실행 요청
                                startActivityForResult(intent, 101);
                                break;
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.buttonInput:
                String name = editTextName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String tel = editTextTel.getText().toString().trim();
                String addr = editTextAddr.getText().toString().trim();
                RegexHelper regexHelper = RegexHelper.getInstance();
                if (msg == null && !regexHelper.isValue(name)) {
                    msg = "이름을 입력해주세요.";
                } else if (msg == null && !regexHelper.isEmail(email)) {
                    msg = "이메일을 입력해주세요.";
                } else if (msg == null && !regexHelper.isCellPhone(tel)) {
                    msg = "전화번호를 입력하세요.";
                } else if (msg == null && !regexHelper.isValue(addr)) {
                    msg = "주소를 입력해주세요.";
                }
                if (msg != null) {
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    return;
                }
                list.add(new Member(name, email, tel, addr, filePath));
                intent = new Intent(this, MainActivity.class);
                intent.putExtra("item", list);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                Toast.makeText(this, "입력 완료", Toast.LENGTH_SHORT).show();
                check_input = 1;
                break;
            case R.id.buttonCancel:
                if (check_input == 1) {
                    startActivity(intent);
                } else {
                    finish();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 100:
                    Intent photoIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                            Uri.parse("file://" + filePath));
                    sendBroadcast(photoIntent);
                    Log.d("[INFO]", "filePath = " + filePath);
                    if (bmp != null && bmp.isRecycled()) {
                        bmp.recycle();
                        bmp = null;
                    }
                    bmp = PhotoHelper.getInstance().getThumb(this, filePath);
                    break;
                case 101:
                    Uri photoUri = data.getData();
                    filePath = FileUtils.getPath(this, photoUri);
                    Log.d("[INFO]", "filePath = " + filePath);
                    if (bmp != null && bmp.isRecycled()) {
                        bmp.recycle();
                        bmp = null;
                    }
                    bmp = PhotoHelper.getInstance().getThumb(this, filePath);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bmp != null && bmp.isRecycled()) {
            bmp.recycle();
            bmp = null;
        }
    }
}
