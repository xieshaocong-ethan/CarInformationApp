package com.example.car;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.car.bean.User;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;

public class testActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RegistFregment.OnUserChangeListener {
    FragmentManager fragmentManager = getSupportFragmentManager();
    LoginFragment loginFragment = new LoginFragment();
    RegistFregment registFregment = new RegistFregment();
    static User user;
    @Override
    public void onUserChange(User user1) {
        user = user1;
        Toast.makeText(this,"test:"+user.toString(),Toast.LENGTH_LONG).show();
    }
        MainFragment mainFragment = new MainFragment();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        setTitle("主页");
        if(user==null){
            navigationView.getMenu().findItem(R.id.login).setVisible(true);
        }else{
            navigationView.getMenu().findItem(R.id.login).setVisible(false);
        }
//        navigationView.getMenu().add(22,22,22,"login");
//        navigationView.getMenu().getItem(22).setIcon(R.mipmap.ic_launcher);
        fragmentManager.beginTransaction().replace(R.id.test_layout, mainFragment).commit();



    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.login) {
            // Handle the camera action
            setTitle("login");
            fragmentManager.beginTransaction()
                    .replace(R.id.test_layout, loginFragment).commit();
        } else if (id == R.id.regist) {
            fragmentManager.beginTransaction()
                    .replace(R.id.test_layout, registFregment).commit();
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        registFregment.onActivityResult(requestCode, resultCode, data);
    }

    public static void saveUser(com.alibaba.fastjson.JSONObject jsonObject){
        LitePal.getDatabase();
        user = new User();
        user.setUserid(jsonObject.getJSONObject("user").get("userid").toString());
        user.setIdentity(jsonObject.getJSONObject("user").get("Identity").toString());
        user.save();
        Log.i("saveUser","ok");
        List<User> books = DataSupport.findAll(User.class);
        for (User book : books) {
            Log.d("user", "userid is" + " "+book.getUserid());
            Log.d("user", "Identity is" + " "+book.getIdentity());
        }
    }
    public static void saveUser(User user){
        LitePal.getDatabase();
        user.save();
        Log.i("saveUser","ok");
        List<User> books = DataSupport.findAll(User.class);
        for (User book : books) {
            Log.d("user", "userid is" + " "+book.getUserid());
            Log.d("user", "Identity is" + " "+book.getIdentity());
        }
    }

}
