package com.example.car;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.car.bean.User;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText id;
    private EditText password;
    private TextView zhuce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        Button btn_login = (Button)this.findViewById(R.id.btn_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        zhuce=(TextView)findViewById(R.id.btn_register  );
        id = this.findViewById(R.id.login_edtId);
        password = this.findViewById(R.id.login_edtPwd);
        btn_login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                String loginId = id.getText().toString();
                String pass = password.getText().toString();
                Toast.makeText(LoginActivity.this, loginId, Toast.LENGTH_SHORT).show();
                login(loginId,pass);
            }
        });


        String text1="注册";
        SpannableString spannableString1=new SpannableString(text1);
        spannableString1.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, RegistActivity.class);
                startActivity(intent);

            }
        }, 0, text1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        zhuce.setText(spannableString1);
        zhuce.setMovementMethod(LinkMovementMethod.getInstance());

    }

    private void login(String userid, String password) {

        Map<String,String> params = new HashMap();
        params.put("userid",userid);
        params.put("password",password);
        String url = MyConstant.url+"loginByUser";
        //将map转化为JSONObject对象
        JSONObject jsonObject = new JSONObject(params);
        Log.i("json","登录中....");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {//jsonObject为请求返回的Json格式数据
                        String responseJson = jsonObject.toString();
                        Log.i("json",responseJson );
                        com.alibaba.fastjson.JSONObject json = JSON.parseObject(responseJson);
                        String result = json.get("result").toString();
                        if(result.equals("true")){

                            Bitmap img = stringToBitmap("data:image/jpeg;base64,"+json.get("img").toString());
                            String imgPath = saveImageToGallery(img,json.getJSONObject("user").get("userid").toString()+"_touxiang");
                            String userid = id.getText().toString();
                            String email = json.getJSONObject("user").get("email").toString();
                            String iden = json.getJSONObject("user").get("identity").toString();

                            User user = new User();
                            user.setUserid(userid);
                            user.setImgPath(imgPath);
                            user.setEmail(email);
                            user.setIdentity(iden);

                            saveUser(user);
                            Intent intent = new Intent(LoginActivity.this, CarMainActivity.class);
//                            intent.putExtra("userid", json.get("userid").toString());
//                            intent.putExtra("email", json.get("email").toString());
//                            intent.putExtra("img", img);
                            startActivity(intent);
                        }else {
                            Toast toast = Toast.makeText(LoginActivity.this,"账号密码错误",Toast.LENGTH_SHORT);
                            toast.show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(LoginActivity.this,volleyError.toString(),Toast.LENGTH_LONG).show();
                    }
                });
        //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("testPost");
        //将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);
    }


    public static void saveUser(User user){
        DataSupport.deleteAll(User.class);
        LitePal.getDatabase();
        user.save();
        Log.i("saveUser","ok");
        List<User> users = DataSupport.findAll(User.class);
        for (User user1 : users) {
            Log.d("user", "userid is" + " "+user1.getUserid());
            Log.d("user", "userEmail is" + " "+user1.getEmail());
            Log.d("user", "userImgPath is" + " "+user1.getImgPath());
            Log.d("user", "Identity is" + " "+user1.getIdentity());
        }
    }


    public static String saveImageToGallery(Bitmap bitmap, String fileName) {
        // 保存图片至指定路径  Context context,
        String storePath = MyConstant.PIC_PATH;
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        File file = new File(appDir,fileName +".jpg");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Log.e("createNewFile", ": ",e);
            }
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片(80代表压缩20%)
            boolean isSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
            fos.flush();
            fos.close();

            //发送广播通知系统图库刷新数据
//            Uri uri = Uri.fromFile(file);
//            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
//            if (isSuccess) {
//                return true;
//            } else {
//                return false;
//            }
        } catch (IOException e) {
            Log.e("??", "saveImageToGallery: ",e);

        }
        String s = file.getAbsolutePath();
        return s;
    }
    public static Bitmap stringToBitmap(String string) {
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray = Base64.decode(string.split(",")[1], Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
