package com.edu.notes.fragment;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.notes.R;
import com.edu.notes.bean.Weather;

import org.xutils.view.annotation.ViewInject;

import java.util.Arrays;

import hdl.com.myhttputils.CommCallback;
import hdl.com.myhttputils.MyHttpUtils;

import static cn.bmob.v3.Bmob.getApplicationContext;


public class HomeFragment extends Fragment {


    @ViewInject(R.id.forecast_txt)
    TextView forecast_txt;

    @ViewInject(R.id.forecast_txt2)
    TextView forecast_txt2;


    @ViewInject(R.id.forecast_temp)
    TextView forecast_temp;

    @ViewInject(R.id.forecast_temp2)
    TextView forecast_temp2;


    @ViewInject(R.id.forecast_icon)
    ImageView forecast_icon;

    @ViewInject(R.id.forecast_icon2)
    ImageView forecast_icon2;


    @ViewInject(R.id.cloth_brief)
    TextView cloth_brief;

    @ViewInject(R.id.sport_brief)
    TextView sport_brief;

    @ViewInject(R.id.travel_brief)
    TextView travel_brief;

    @ViewInject(R.id.flu_brief)
    TextView flu_brief;



    @ViewInject(R.id.cloth_txt)
    TextView cloth_txt;

    @ViewInject(R.id.sport_txt)
    TextView sport_txt;

    @ViewInject(R.id.travel_txt)
    TextView travel_txt;

    @ViewInject(R.id.flu_txt)
    TextView flu_txt;

    @ViewInject(R.id.tv_weather)
    TextView tv_city;

    public String Location ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        init(view);

        Location=getLocation(getApplicationContext());

                onGetWeather(Location);
        return view;
    }
    public void init(View view){

        tv_city=(TextView)getActivity().findViewById(R.id.tv_weather) ;
        forecast_txt = (TextView) view.findViewById(R.id.forecast_txt);
        forecast_txt2= (TextView) view.findViewById(R.id.forecast_txt2);

        forecast_temp=(TextView)view.findViewById(R.id.forecast_temp);
        forecast_temp2=(TextView)view.findViewById(R.id.forecast_temp2);

        forecast_icon=(ImageView)view.findViewById(R.id.forecast_icon);
        forecast_icon2=(ImageView)view.findViewById(R.id.forecast_icon2);

        cloth_brief=(TextView)view.findViewById(R.id.cloth_brief);
        sport_brief=(TextView)view.findViewById(R.id.sport_brief);
        travel_brief=(TextView)view.findViewById(R.id.travel_brief);
        flu_brief=(TextView)view.findViewById(R.id.flu_brief);

        cloth_txt=(TextView)view.findViewById(R.id.cloth_txt);
        sport_txt=(TextView)view.findViewById(R.id.sport_txt);
        travel_txt=(TextView)view.findViewById(R.id.travel_txt);
        flu_txt=(TextView)view.findViewById(R.id.flu_txt);

    }
    public void onGetWeather(String Location ) {
        String Apipath;
        if(Location.isEmpty()){
            Toast.makeText(getActivity(),"GPS定位失败加载默认城市"+"上海",Toast.LENGTH_LONG).show();
             Apipath = "https://free-api.heweather.com/v5/weather?city=shanghai&key=e170417d640c4d9a8d0b92c57ebe4eb7";

        }else{
            //分割字符串
            String[] str = null;
            str = Location.split(",");
            Toast.makeText(getActivity(),"经度"+str[0].toString()+"纬度"+str[1].toString(),Toast.LENGTH_LONG).show();
             Apipath = "https://free-api.heweather.com/v5/weather?city="+Location+"&key=e170417d640c4d9a8d0b92c57ebe4eb7";
        }

        new MyHttpUtils()
                .url(Apipath)//请求的url
                .setJavaBean(Weather.class)//设置需要解析成的javabean对象
                .setReadTimeout(60000)//设置读取超时时间,不设置的话默认为30s(30000)
                .setConnTimeout(6000)//设置连接超时时间,不设置的话默认5s(5000)
                .onExecute(new CommCallback<Weather>() {
                    @Override
                    public void onSucess(Weather weather) {
                        tv_city.setText(weather.getHeWeather5().get(0).getBasic().getCity());
                        String s=weather.getHeWeather5().get(0).getDaily_forecast().get(0).getCond().getCode_d();
                        setImage(s,forecast_icon);
                        String s2 =weather.getHeWeather5().get(0).getDaily_forecast().get(1).getCond().getCode_d();
                        setImage(s2,forecast_icon2);

                        forecast_txt.setText(weather.getHeWeather5().get(0).getDaily_forecast().get(0).getCond().getTxt_d()+"。"+
                                weather.getHeWeather5().get(0).getDaily_forecast().get(0).getWind().getSc()+
                                weather.getHeWeather5().get(0).getDaily_forecast().get(0).getWind().getDir()+
                                weather.getHeWeather5().get(0).getDaily_forecast().get(0).getWind().getSpd()+"km/h。"+
                                weather.getHeWeather5().get(0).getDaily_forecast().get(0).getPop()+"%。");


                        forecast_txt2.setText(weather.getHeWeather5().get(0).getDaily_forecast().get(1).getCond().getTxt_d()+"。"+
                                weather.getHeWeather5().get(0).getDaily_forecast().get(1).getWind().getSc()+
                                weather.getHeWeather5().get(0).getDaily_forecast().get(1).getWind().getDir()+
                                weather.getHeWeather5().get(0).getDaily_forecast().get(1).getWind().getSpd()+"km/h。"+
                                weather.getHeWeather5().get(0).getDaily_forecast().get(1).getPop()+"%。");


                        forecast_temp.setText(weather.getHeWeather5().get(0).getDaily_forecast().get(0).getTmp().getMin()+"℃"+"-"+
                                weather.getHeWeather5().get(0).getDaily_forecast().get(0).getTmp().getMax()+"℃");
                        forecast_temp2.setText(weather.getHeWeather5().get(0).getDaily_forecast().get(2).getTmp().getMin()+"℃"+"-"+
                                weather.getHeWeather5().get(0).getDaily_forecast().get(0).getTmp().getMax()+"℃");



                        cloth_brief.setText(cloth_brief.getText()+weather.getHeWeather5().get(0).getSuggestion().getDrsg().getBrf());
                        sport_brief.setText(cloth_brief.getText()+weather.getHeWeather5().get(0).getSuggestion().getSport().getBrf());
                        travel_brief.setText(cloth_brief.getText()+weather.getHeWeather5().get(0).getSuggestion().getTrav().getBrf());
                        flu_brief.setText(cloth_brief.getText()+weather.getHeWeather5().get(0).getSuggestion().getFlu().getBrf());



                        cloth_txt.setText(weather.getHeWeather5().get(0).getSuggestion().getDrsg().getTxt());
                        sport_txt.setText(weather.getHeWeather5().get(0).getSuggestion().getSport().getTxt());
                        travel_txt.setText(weather.getHeWeather5().get(0).getSuggestion().getTrav().getTxt());
                        flu_txt.setText(weather.getHeWeather5().get(0).getSuggestion().getFlu().getTxt());

                    }

                    @Override
                    public void onFailed(String msg) {

                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void setImage(String s ,ImageView view){
        switch (s){
            case "100": view.setImageDrawable(getResources().getDrawable(R.mipmap.qing));
                break;
            case "101": view.setImageDrawable(getResources().getDrawable(R.mipmap.duoyun));
                break;
            case "102": view.setImageDrawable(getResources().getDrawable(R.mipmap.shaoyun));
                break;
            case "103": view.setImageDrawable(getResources().getDrawable(R.mipmap.qingzhuanduoyun));
                break;
            case "104": view.setImageDrawable(getResources().getDrawable(R.mipmap.yin));
                break;
            case "300":view.setImageDrawable(getResources().getDrawable(R.mipmap.zhneyu));
                break;
            case "302":view.setImageDrawable(getResources().getDrawable(R.mipmap.leizhenyu));
                break;
            case "303":view.setImageDrawable(getResources().getDrawable(R.mipmap.qiangleizhenyu));
                break;
            case "305":view.setImageDrawable(getResources().getDrawable(R.mipmap.xiaoxue));
                break;
            case "306":view.setImageDrawable(getResources().getDrawable(R.mipmap.zhongyu));
                break;
            case "307":view.setImageDrawable(getResources().getDrawable(R.mipmap.dayu));
                break;
            case "400":view.setImageDrawable(getResources().getDrawable(R.mipmap.xiaoxue));
                break;
            case "401":view.setImageDrawable(getResources().getDrawable(R.mipmap.daxue));
                break;
            case "402":view.setImageDrawable(getResources().getDrawable(R.mipmap.daxue));
                break;
            default :view.setImageDrawable(getResources().getDrawable(R.mipmap.weizhi));
                break;
        }
    }

    public static HomeFragment newInstance( ) {

        HomeFragment fragment = new HomeFragment();


        return fragment;
    }
    //定位
    public String getLocation(Context context){
        LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setCostAllowed(false);
        //设置位置服务免费
        criteria.setAccuracy(Criteria.ACCURACY_COARSE); //设置水平位置精度
        //getBestProvider 只有允许访问调用活动的位置供应商将被返回
        String  providerName = lm.getBestProvider(criteria, true);

        if (providerName != null)
        {
            Location location = lm.getLastKnownLocation(providerName);
            if(location!=null){
                //获取维度信息
                double latitude = location.getLatitude();
                //获取经度信息
                double longitude = location.getLongitude();
                return longitude+","+latitude;
            }
        }
        return "";
    }
}
