package gd.water.oking.com.cn.wateradministration_gd.main;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import gd.water.oking.com.cn.wateradministration_gd.R;

public class ImageViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        Uri imageUri = getIntent().getData();
        if(imageUri != null){
            imageView.setImageURI(imageUri);
        }else{
            this.finish();
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageViewActivity.this.finish();
            }
        });
    }
}
