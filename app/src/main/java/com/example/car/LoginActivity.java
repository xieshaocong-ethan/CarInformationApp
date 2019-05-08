package com.example.car;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.*;
import com.car.bean.admin;
import android.os.Handler;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity {
    private EditText id;
    private EditText password;
    private SQLiteDatabase db;
    private TextView zhuce;
    public static final int SHOW_RESPONSE=1;
    public Handler handler=new Handler() {
        public void handleMessage(Message msg)
        {
            switch (msg.what){
                case SHOW_RESPONSE:
                    String response=(String)msg.obj;
                    Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btn_login = (Button)this.findViewById(R.id.btn_login);
        zhuce=(TextView)findViewById(R.id.btn_register  );
        id = this.findViewById(R.id.login_edtId);
        password = this.findViewById(R.id.login_edtPwd);
        btn_login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                String loginId = id.getText().toString();
                String pass = password.getText().toString();
                Toast.makeText(LoginActivity.this, loginId, Toast.LENGTH_SHORT).show();
                try {
                    SendByHttpClient(loginId,pass);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    public void SendByHttpClient(final String id, final String pw) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost("http://192.168.0.104:8080/carcarcar/LoginServlet");//服务器地址，指向Servlet
                    List<NameValuePair> params = new ArrayList<NameValuePair>();//将id和pw装入list
                    params.add(new BasicNameValuePair("ID", id));
                    params.add(new BasicNameValuePair("PW", pw));
                    final UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, "utf-8");//以UTF-8格式发送
                    httpPost.setEntity(entity);
                    HttpResponse httpResponse = httpclient.execute(httpPost);
                    if (httpResponse.getStatusLine().getStatusCode() == 200)//在200毫秒之内接收到返回值
                    {
                        HttpEntity entity1 = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity1, "utf-8");//以UTF-8格式解析
                        Message message = new Message();
                        message.what=SHOW_RESPONSE;
                        message.obj = response;
                      if(response.equals("true")){
                             Intent intent = new Intent(LoginActivity.this, testActivity.class);
                             startActivity(intent);
                             if(db==null){
                                db = Connector.getDatabase();
                                admin admin = new admin();
                                admin.setAdminname(Integer.valueOf(id));
                                admin.setLogin(1);
                                admin.setLimit("管理员");
                                admin.save();
                            }else{
                                admin admin = new admin();
                                admin.setAdminname(Integer.valueOf(id));
                                admin.setLogin(1);
                                admin.setLimit("管理员");
                                admin.save();
                            }
                       }else {
                         Toast toast = Toast.makeText(LoginActivity.this,"账号密码错误",Toast.LENGTH_SHORT);
                         toast.show();
                      }
                        handler.sendMessage(message);
                       // 使用Message传递消息给线程
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
