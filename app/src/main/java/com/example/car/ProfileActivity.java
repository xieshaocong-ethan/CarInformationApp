package com.example.car;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.car.bean.User;
import com.util.StatusBarUtil;

import org.litepal.crud.DataSupport;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //状态栏透明和间距处理
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this, toolbar);
        StatusBarUtil.setPaddingSmart(this, findViewById(R.id.profile));
        StatusBarUtil.setPaddingSmart(this, findViewById(R.id.blurView));

        List<User> users = DataSupport.findAll(User.class);
        Bitmap b = null;
        b = BitmapFactory.decodeFile(users.get(0).getImgPath());
        CircleImageView circleImageView = findViewById(R.id.img);
        circleImageView.setImageBitmap(b);
        TextView textView1 = findViewById(R.id.name);
        textView1.setText(users.get(0).getUserid());
        TextView textView2 = findViewById(R.id.email);
        textView2.setText(users.get(0).getEmail());
    }

}
