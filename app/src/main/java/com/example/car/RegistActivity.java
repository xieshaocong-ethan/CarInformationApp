package com.example.car;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class RegistActivity extends Activity {
    private EditText id;
    private EditText password;
    private String yanzheng;

    private void volleyPostWithJsonObjectRequest() {
        String url = "http://www.kuaidi100.com/query";
        Map<String,String> map = new HashMap<>();
        map.put("type","yuantong");
        map.put("postid","229728279823");
        //将map转化为JSONObject对象
        JSONObject jsonObject = new JSONObject(map);

        JsonObjectRequest request = new JsonObjectRequest(Method.POST, url, jsonObject,
                new Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {//jsonObject为请求返回的Json格式数据
                        Toast.makeText(RegistActivity.this,jsonObject.toString(),Toast.LENGTH_LONG).show();
                    }
                },
                new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(RegistActivity.this,volleyError.toString(),Toast.LENGTH_LONG).show();
                    }
                });
        //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("testPost");
        //将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        Button btn_regist = (Button)this.findViewById(R.id.btn_regist);
        id = this.findViewById(R.id.regist_uname);
        password = this.findViewById(R.id.regist_password);
        id = this.findViewById(R.id.regist_uname);
        password = this.findViewById(R.id.regist_password);
        volleyPostWithJsonObjectRequest();
        btn_regist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String loginId = id.getText().toString();
                String pass = password.getText().toString();

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}