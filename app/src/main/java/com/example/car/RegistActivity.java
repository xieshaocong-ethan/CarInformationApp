package com.example.car;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class RegistActivity extends Activity {
    private EditText id;
    private EditText password;
    private EditText email;
    private EditText yanzhengma;
    private int yanzheng;
   // RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        Button btn_regist = (Button)this.findViewById(R.id.btn_regist);
        Button btn_yanzheng = (Button)this.findViewById(R.id.btn_yanzheng);
        id = this.findViewById(R.id.regist_uname);
        password = this.findViewById(R.id.regist_password);
        yanzhengma = this.findViewById(R.id.regist_yanzheng);
        email = this.findViewById(R.id.regist_email);
        test();
        btn_regist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String loginId = id.getText().toString();
                String pass = password.getText().toString();
                String yanzheng1 = yanzhengma.getText().toString();
                Integer yan = Integer.parseInt(yanzheng1);
                regist(loginId,pass,yan);
            }
        });
        btn_yanzheng.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String emial = email.getText().toString();
                getYanZheng(emial);
            }
        });
    }

    public void regist(String userid ,String password,int yan){
        if(yan==yanzheng){
            String url = "http://192.168.191.1:8080/carServer/Regist";
            Map<String,String> map = new HashMap<>();
            map.put("userid",userid);
            map.put("password",password);
            //将map转化为JSONObject对象
            JSONObject jsonObject = new JSONObject(map);
            Log.i("json","注册（）......");
            JsonObjectRequest request = new JsonObjectRequest(Method.POST, url, jsonObject,
                    new Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {//jsonObject为请求返回的Json格式数据
                            String result = null;
                            try {
                                result = jsonObject.getJSONObject("result").toString();
                                if("true".equals(result)){
                                    Toast.makeText(RegistActivity.this,"注册成功",Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.i("json",result);
                        }
                    },
                    new ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(RegistActivity.this,volleyError.toString()+" 注册失败",Toast.LENGTH_LONG).show();
                        }
                    });
            //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
            request.setTag("testPost");
            MyApplication.getHttpQueues().add(request);
        }else{
            Toast.makeText(RegistActivity.this,"验证码错误",Toast.LENGTH_LONG).show();
        }
    }

    private void test() {
        String url = "http://192.168.191.1:8080/carServer/getOneCategory";
        Map<String,String> map = new HashMap<>();
        map.put("userid","zhangsan");
        map.put("password","112233");
        //将map转化为JSONObject对象
        JSONObject jsonObject = new JSONObject(map);
        Log.i("json","test连接开始");
        JsonObjectRequest request = new JsonObjectRequest(Method.POST, url, jsonObject,
                new Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {//jsonObject为请求返回的Json格式数据
                        Log.i("json",jsonObject.toString() );

                        Toast.makeText(RegistActivity.this,jsonObject.toString(),Toast.LENGTH_LONG).show();
                    }
                },
                new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.i("fail",volleyError.toString() );
                        Toast.makeText(RegistActivity.this,volleyError.toString(),Toast.LENGTH_LONG).show();
                    }
                });
        //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("testPost");
        //将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);
    }

    private void getYanZheng(String email) {
        String url = "http://192.168.191.1:8080/carServer/getYanzheng";
        Map<String,String> map = new HashMap<>();
        map.put("email",email);
        //将map转化为JSONObject对象
        JSONObject jsonObject = new JSONObject(map);
        Log.i("json","请求验证码....");
        JsonObjectRequest request = new JsonObjectRequest(Method.POST, url, jsonObject,
                new Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {//jsonObject为请求返回的Json格式数据
                        Log.i("json",jsonObject.toString() );
                        try {
                            String yanzhengmStr = jsonObject.get("yanzhengma").toString();
                            yanzheng = Integer.valueOf(yanzhengmStr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
        //return Integer.parseInt(jsonObject.toString());
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