package gd.water.oking.com.cn.wateradministration_gd.main;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import gd.water.oking.com.cn.wateradministration_gd.R;
import gd.water.oking.com.cn.wateradministration_gd.View.SignatureView;
import gd.water.oking.com.cn.wateradministration_gd.bean.Member;

public class SignatureActivity extends Activity {

    private File dir = new File(Environment.getExternalStorageDirectory(), "oking/case_signature");
    private Member member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        String path = getIntent().getStringExtra("dir");
        dir = new File(path);

        if (getIntent().getSerializableExtra("member") != null) {
            member = (Member) getIntent().getSerializableExtra("member");
        }

        final SignatureView signatureView = (SignatureView) findViewById(R.id.signature_View);

        Button clearBtn = (Button) findViewById(R.id.clear_btn);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signatureView.clear();
            }
        });
        Button saveBtn = (Button) findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bitmap bitmap = signatureView.save();

                if (bitmap != null) {

                    if (!dir.exists()) {
                        dir.mkdirs();
                    }

                    File signatureFile = new File(dir, android.text.format.DateFormat
                            .format("yyyyMMdd_HHmmss", System.currentTimeMillis())
                            + ".jpg");

                    try {
                        OutputStream os = new FileOutputStream(signatureFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                        os.flush();
                        os.close();
                        bitmap.recycle();
                        bitmap = null;
                        System.gc();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(signatureFile));
                    sendBroadcast(intent);

                    Intent resultIntent = new Intent();
                    resultIntent.setData(Uri.fromFile(signatureFile));
                    setResult(RESULT_OK, intent);

                    if (member != null) {
                        member.setSignPic(Uri.fromFile(signatureFile));
                    }

                    finish();
                } else {
                    Toast.makeText(SignatureActivity.this, "未签名不能确认！", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
