package com.example.car;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.BaseRecyclerAdapter;
import com.adapter.SmartViewHolder;
import com.forum.model.entity.CarDetail;
import com.forum.model.entity.Parameter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class CarDetailActivity extends AppCompatActivity {


    private static boolean isFirstEnter = true;
    private BaseRecyclerAdapter<String> itemAdapter;
    private BaseRecyclerAdapter<Parameter> dialogAdapter;
    Intent intent;
    String dicarid;
    int cindex;
    int sindex;
    int ai;
    int ii;
    int inai;
    List<CarDetail> carDetails= MyConstant.carDetails;
    ArrayList<Parameter> ar = getparameter(carDetails.get(sindex));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);
        intent = getIntent();
        dicarid = intent.getStringExtra("dicarid");
        cindex = intent.getIntExtra("cindex",0);
        sindex = intent.getIntExtra("sindex",0);
        View view = findViewById(R.id.recyclerView1);

        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            ArrayList<String> picar = new ArrayList<>();
            try {

                picar.add(MyConstant.cars.get(cindex).getPurl1());
                picar.add(MyConstant.cars.get(cindex).getPurl());
                picar.add(MyConstant.cars.get(cindex).getPurl2());
                picar.add(MyConstant.cars.get(cindex).getPurl3());
                picar.add(MyConstant.cars.get(cindex).getPurl4());

            }catch (Exception e){e.printStackTrace();}
            ArrayList<Parameter> carpArrayList = new ArrayList<>();
            ArrayList<Parameter> carArrayList = new ArrayList<>();
            try {
                for (ii = 0; ii < ar.size(); ii++) {
                    carpArrayList.add(new Parameter() {{
                        this.setPname(ar.get(ii).getPname());
                        this.setPvalue(ar.get(ii).getPvalue());
                    }});
                }
                for (Parameter pa : ar) {
                    ArrayList<Parameter> inar;
                    Method getsomeclass = CarDetail.class.getMethod("get"+pa.getPname());
                    Log.d("method",getsomeclass.getName());
                    inar = getparameter(getsomeclass.invoke(carDetails.get(sindex),null));
                    for (inai = 0; inai < inar.size(); inai++) {
                        carArrayList.add(new Parameter() {{
                            this.setPvalue(inar.get(inai).getPvalue());
                            this.setPname(inar.get(inai).getPname());
                        }});
                    }

                }
            }catch (Exception e){e.printStackTrace();}

            recyclerView.setAdapter(itemAdapter = new BaseRecyclerAdapter<String>(picar,R.layout.item_detail_car) {
                @Override
                protected void onBindViewHolder(SmartViewHolder holder, String model, int position) {
                    holder.setimage(R.id.imageView2,model+".jpg",10*1024*1024,true);
                }
            });

            final Toolbar toolbar = findViewById(R.id.toolbar);
            CircleImageView civ = findViewById(R.id.avatar);
            MyConstant.setCarImg(MyConstant.cars.get(cindex).getBrandPurl()+".png",10*1024*1024,civ);
            TextView title = findViewById(R.id.title1);
            title.setText(MyConstant.cars.get(cindex).getBrand());
            TextView as = findViewById(R.id.assistant);
            as.setText(MyConstant.carDetails.get(cindex).getbasic_parameter().getModelname());
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(CarDetailActivity.this,"\uD83D\uDE0F",Toast.LENGTH_SHORT).show();
                }
            });
                    itemAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            BottomSheetDialog dialog=new BottomSheetDialog(CarDetailActivity.this);
                            View dialogView = View.inflate(getBaseContext(), R.layout.activity_car_detail, null);
                            RefreshLayout refreshLayout = dialogView.findViewById(R.id.refreshLayout2);
                            RecyclerView recyclerView = new RecyclerView(getBaseContext());
                            recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                            recyclerView.setAdapter(dialogAdapter = new BaseRecyclerAdapter<Parameter>(carArrayList,R.layout.item_dialog) {
                                @Override
                                protected void onBindViewHolder(SmartViewHolder holder, Parameter model, int position) {
                                    holder.text(R.id.textView5,model.getPname());
                                    holder.text(R.id.textView6,model.getPvalue());
                                }
                            });
                            refreshLayout.setEnableRefresh(false);
                            refreshLayout.setEnableNestedScroll(false);
                            refreshLayout.setRefreshContent(recyclerView);
                            dialog.setContentView(dialogView);
                            dialog.show();
                        }
                    });
        }




    }


    private static String[] getFiledName(Object o){
        Field[] fields=o.getClass().getDeclaredFields();
        String[] fieldNames=new String[fields.length];
        for(int i=0;i<fields.length;i++){
            fieldNames[i]=fields[i].getName();
        }
        return fieldNames;
    }
    private static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(o, new Object[] {});
            return value;
        } catch (Exception e) {

            return null;
        }
    }

    private ArrayList<Parameter> getparameter(Object clas){
        ArrayList<Parameter> ar = new ArrayList<>();
        ArrayList<String> aName = new ArrayList();
        ArrayList<String> aValue = new ArrayList();
        String[] fieldNames = getFiledName(clas);

        //遍历所有属性
        for(int j=0 ; j<fieldNames.length ; j++) {
            //获取属性的名字
            String name = fieldNames[j];
            Object value = getFieldValueByName(name,clas);
            aName.add(name);
            aValue.add(String.valueOf(value));
        }
        for(ai=0 ; ai<aName.size() ; ai++){
            ar.add( new Parameter(){{
                this.setPname(aName.get(ai));
                this.setPvalue(aValue.get(ai));
            }});
        }
        return ar;
    }

}
