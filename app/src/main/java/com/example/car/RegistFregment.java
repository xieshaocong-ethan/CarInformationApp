package com.example.car;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.car.bean.User;
import com.car.util.MyJsonUtil;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import static android.app.Activity.RESULT_OK;

public class RegistFregment extends Fragment implements EasyPermissions.PermissionCallbacks {
    private Context mContext;
    private AlertDialog profilePictureDialog;
    private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    private static final String PERMISSION_WRITE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final int REQUEST_PERMISSION_CAMERA = 0x001;
    private static final int REQUEST_PERMISSION_WRITE = 0x002;
    private static final int CROP_REQUEST_CODE = 0x003;
    static String URLP="http://192.168.43.72:8080/carServer/";
    private ImageView ivAvatar;

    //文件相关
    private File captureFile;
    private File rootFile;
    private File cropFile;
    private EditText id;
    private EditText password;
    private EditText email;
    private EditText yanzhengma;
    private int yanzheng=123;
    private OnUserChangeListener onUserChangeListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.car_register, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        super.onCreate(savedInstanceState);

        mContext = getContext();
        ivAvatar = getActivity().findViewById(R.id.iv_avatar);
        rootFile = new File(MyConstant.PIC_PATH);
        if (!rootFile.exists()) {
            rootFile.mkdirs();
        }

        id = getActivity().findViewById(R.id.regist_uname);
        password = getActivity().findViewById(R.id.regist_password);
        yanzhengma = getActivity().findViewById(R.id.regist_yanzheng);
        email = getActivity().findViewById(R.id.regist_email);
        Button btn_regist = (Button)getActivity().findViewById(R.id.btn_regist);
        Button btn_yanzheng = (Button)getActivity().findViewById(R.id.btn_yanzheng);
        test();
        //注册点击事件
        btn_regist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String loginId = id.getText().toString();
                String pass = password.getText().toString();
                String yanzheng1 = yanzhengma.getText().toString();
                /*if(cropFile==null){
                    Toast.makeText(getActivity(),"请上传图片",Toast.LENGTH_SHORT).show();
                    return;
                }*/
                if(TextUtils.isEmpty(loginId)){
                    Toast.makeText(getActivity(),"账号不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(yanzheng1)){
                    Toast.makeText(getActivity(),"验证码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    Toast.makeText(getActivity(),"密码不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(loginId.length()>20){
                    Toast.makeText(getActivity(),"您输入的账号过长",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(loginId.length()>20){
                    Toast.makeText(getActivity(),"您输入的密码过长",Toast.LENGTH_SHORT).show();
                    return;
                }
                Integer yan = Integer.parseInt(yanzheng1);
                if(cropFile!=null){
                    String img = getImageStr(cropFile.getPath());
                    regist(email.getText().toString(),loginId,pass,yan,img);
                }else{regist(email.getText().toString(),loginId,pass,yan,null);}
            }
        });
        btn_yanzheng.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String emial = email.getText().toString();
                getYanZheng(emial);
            }
        });
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (profilePictureDialog == null) {
                    @SuppressLint("InflateParams") View rootView = LayoutInflater.from(getContext()).inflate(R.layout.item_profile_picture, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    rootView.findViewById(R.id.tv_take_photo).setOnClickListener(onTakePhotoListener);
                    rootView.findViewById(R.id.tv_choose_photo).setOnClickListener(onChoosePhotoListener);
                    builder.setView(rootView);
                    profilePictureDialog = builder.create();
                    profilePictureDialog.show();
                } else {
                    profilePictureDialog.show();
                }
            }
        });
    }

    private void dismissProfilePictureDialog() {
        if (profilePictureDialog != null && profilePictureDialog.isShowing()) {
            profilePictureDialog.dismiss();
        }
    }

    private View.OnClickListener onTakePhotoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismissProfilePictureDialog();
            if (EasyPermissions.hasPermissions(mContext, PERMISSION_CAMERA, PERMISSION_WRITE)) {
                takePhoto();
            } else {
                EasyPermissions.requestPermissions(RegistFregment.this, "need camera permission", REQUEST_PERMISSION_CAMERA, PERMISSION_CAMERA, PERMISSION_WRITE);
            }
        }
    };
    //测试与服务器连接
    private void test() {
        String url = URLP+"getOneCategory";
        Map<String,String> map = new HashMap<>();
        map.put("userid","zhangsan");
        map.put("password","112233");
        //将map转化为JSONObject对象
        JSONObject jsonObject = new JSONObject(map);
        Log.i("json","test连接开始");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {//jsonObject为请求返回的Json格式数据
                        Log.i("json",jsonObject.toString() );

                        Toast.makeText(getActivity(),jsonObject.toString(),Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.i("fail",volleyError.toString() );
                        Toast.makeText(getActivity(),volleyError.toString(),Toast.LENGTH_LONG).show();
                    }
                });
        //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
        request.setTag("testPost");
        request.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        //将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);
    }
    //从服务器得到验证码
    private void getYanZheng(String email) {
        String url = URLP+"getYanzheng";
        Map<String,String> map = new HashMap<>();
        map.put("email",email);
        //将map转化为JSONObject对象
        JSONObject jsonObject = new JSONObject(map);
        Log.i("json","请求验证码....");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {//jsonObject为请求返回的Json格式数据
                        Log.i("json",jsonObject.toString() );
                        try {
                            String yanzhengmStr = jsonObject.get("yanzhengma").toString();
                            yanzheng = Integer.valueOf(yanzhengmStr);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getActivity(),jsonObject.toString(),Toast.LENGTH_LONG).show();
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
        request.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,1));
        //将请求加入全局队列中
        MyApplication.getHttpQueues().add(request);
        //return Integer.parseInt(jsonObject.toString());
    }
    //注册功能
    public void regist(final String email, final String userid , String password, int yan, String img){
        if(yan==yanzheng){
            String url = URLP+"Regist";
            Map<String,String> map = new HashMap<>();
            map.put("userid",userid);
            map.put("password",password);
            map.put("imgPath",img);
            //将map转化为JSONObject对象
            JSONObject jsonObject = new JSONObject(map);
            Log.i("json","注册（）......");
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {//jsonObject为请求返回的Json格式数据
                            String result = null;
                            com.alibaba.fastjson.JSONObject json = MyJsonUtil.VolleyJsonToAlibabaJson(jsonObject);
                            result = json.get("result").toString();
                            if("true".equals(result)){
                                Toast.makeText(getActivity(),"注册成功",Toast.LENGTH_LONG).show();
                                User user = new User();
                                user.setUserid(userid);
                                user.setEmail(email);
                                user.setIdentity("user");
                                testActivity.saveUser(user);
                                //修改侧边栏属性
                                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                                //navigationView.getMenu().removeItem(22);
                                View headerView = navigationView.getHeaderView(0);
                                TextView TextViewuserid = (TextView)headerView.findViewById(R.id.user_id);
                                TextViewuserid.setText(userid);
                                TextView TextViewemial = (TextView)headerView.findViewById(R.id.user_email);
                                TextViewemial.setText(email);
                                ImageView imageView = (ImageView)headerView.findViewById(R.id.imageView);
                                imageView.setImageBitmap(BitmapFactory.decodeFile(cropFile.getAbsolutePath()));
                                navigationView.getMenu().findItem(R.id.login).setVisible(false);
                                //user传递给testActivity
                                onUserChangeListener.onUserChange(user);
                                //保存到本地数据库
                                testActivity.saveUser(user);
                            }
                            Log.i("json",result);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(getActivity(),volleyError.toString()+" 注册失败",Toast.LENGTH_LONG).show();
                        }
                    });
            //设置请求的Tag标签，可以在全局请求队列中通过Tag标签进行请求的查找
            request.setTag("testPost");
            request.setRetryPolicy(new DefaultRetryPolicy(500000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MyApplication.getHttpQueues().add(request);
        }else{
            Toast.makeText(getActivity(),"验证码错误",Toast.LENGTH_LONG).show();
        }
    }

    //拍照
    private void takePhoto() {
        //用于保存调用相机拍照后所生成的文件
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }
        captureFile = new File(rootFile, "temp.jpg");
        //跳转到调用系统相机

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断版本 如果在Android7.0以上,使用FileProvider获取Uri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(mContext,mContext.getApplicationContext().getPackageName() + ".provider", captureFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
        } else {
            //否则使用Uri.fromFile(file)方法获取Uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(captureFile));
        }
        startActivityForResult(intent, REQUEST_PERMISSION_CAMERA);
    }

    private View.OnClickListener onChoosePhotoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismissProfilePictureDialog();
            if (EasyPermissions.hasPermissions(mContext, PERMISSION_WRITE)) {
                choosePhoto();
            } else {
                EasyPermissions.requestPermissions(RegistFregment.this, "need camera permission", REQUEST_PERMISSION_WRITE, PERMISSION_WRITE);
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    //从相册选择
    private void choosePhoto() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_PERMISSION_WRITE);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == REQUEST_PERMISSION_CAMERA) {
            takePhoto();
        } else if (requestCode == REQUEST_PERMISSION_WRITE) {
            choosePhoto();
        }
    }

    private void cropPhoto(Uri uri) {
        cropFile = new File(rootFile, "img.jpg");
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cropFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, CROP_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_PERMISSION_CAMERA:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Uri contentUri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", captureFile);
                        cropPhoto(contentUri);
                    } else {
                        cropPhoto(Uri.fromFile(captureFile));
                    }
                    break;
                case REQUEST_PERMISSION_WRITE:
                    cropPhoto(data.getData());
                    break;
                case CROP_REQUEST_CODE:
                    saveImage(cropFile.getAbsolutePath());
                    ivAvatar.setImageBitmap(BitmapFactory.decodeFile(cropFile.getAbsolutePath()));
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String saveImage(String path) {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        try {
            FileOutputStream fos = new FileOutputStream(cropFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return cropFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getImageStr(String filePath) {
        //将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in =null;
        byte[] data =null;
        // 读取图片字节数组
        try{
            in =new FileInputStream(filePath);
            data =new byte[in.available()];
            in.read(data);in.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }//对字节数组Base64编码//返回Base64编码过的字节数组字符串
        String s = Base64.encodeToString(data, Base64.DEFAULT);
        return s;
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    public interface OnUserChangeListener
    {
        public void onUserChange(User user);
    }

    //兼容低版本（api<23）
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try {
            onUserChangeListener = (OnUserChangeListener) getActivity();
        } catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString() +" must implement OnHeadlineSelectedListener");
        }
    }
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        try {
            onUserChangeListener = (OnUserChangeListener) getActivity();
        } catch (ClassCastException e)
        {
            throw new ClassCastException(context.toString() +" must implement OnHeadlineSelectedListener");
        }
    }
}
