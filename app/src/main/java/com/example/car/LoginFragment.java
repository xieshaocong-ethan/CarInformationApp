package com.example.car;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginFragment extends Fragment {
    private EditText id;
    private EditText password;
    private SQLiteDatabase db ;
    private TextView zhuce;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_login, container, false);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button btn_login = (Button)getActivity().findViewById(R.id.btn_login);
        zhuce=(TextView)getActivity().findViewById(R.id.btn_register  );
        id = getActivity().findViewById(R.id.login_edtId);
        password = getActivity().findViewById(R.id.login_edtPwd);
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().removeItem(22);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginId = id.getText().toString();
                String pass = password.getText().toString();
                Toast.makeText(getActivity(), loginId, Toast.LENGTH_SHORT).show();
                login(loginId,pass);
            }
        });

        //注册点击跳转
        String text1="注册";
        SpannableString spannableString1=new SpannableString(text1);
        spannableString1.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                /*Intent intent=new Intent(getActivity(), RegistActivity.class);
                startActivity(intent);*/
                getFragmentManager().beginTransaction()
                        .replace(R.id.test_layout, new RegistFregment()).commit();
            }
        }, 0, text1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        zhuce.setText(spannableString1);
        zhuce.setMovementMethod(LinkMovementMethod.getInstance());
    }


    private void login(String userid,String password) {
        Map<String,String> params = new HashMap();
        params.put("userid",userid);
        params.put("password",password);
        String url = "http://192.168.191.1:8080/carServer/loginByUser";
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
//                            Intent intent = new Intent(getActivity(), testActivity.class);
//                            startActivity(intent);
                            saveUser(json);
                        }else {
                            Toast toast = Toast.makeText(getActivity(),"账号密码错误",Toast.LENGTH_SHORT);
                            toast.show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(),volleyError.toString(),Toast.LENGTH_LONG).show();
                    }
                });
        //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("testPost");
        //将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);
    }

    public static void saveUser(com.alibaba.fastjson.JSONObject jsonObject){
        LitePal.getDatabase();
        User user = new User();
        user.setUserid(jsonObject.getJSONObject("user").get("userid").toString());
        user.setIdentity(jsonObject.getJSONObject("user").get("Identity").toString());
        user.save();
        Log.i("login","ok");
        List<User> books = DataSupport.findAll(User.class);
        for (User book : books) {
            Log.d("MainActivity", "userid is" + book.getUserid());
            Log.d("MainActivity", "Identity is" + book.getIdentity());
        }
    }
}
