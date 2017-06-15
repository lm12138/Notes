package com.edu.notes.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.notes.R;
import com.edu.notes.adapter.DynamicAdapter;
import com.edu.notes.bean.Dynamic;
import com.edu.notes.bean.MyUser;
import com.edu.notes.utlis.SmartisanRefreshableLayout;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import static cn.bmob.v3.Bmob.getApplicationContext;


public class InfoFragment extends BaseFragment {

    View view;
    private SmartisanRefreshableLayout mSmartisanRefreshableLayout;
    private ListView mListView;
    DynamicAdapter adapter;


    public static InfoFragment newInstance() {

        InfoFragment fragment = new InfoFragment();

        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_info, container, false);

        //test
        mSmartisanRefreshableLayout = (SmartisanRefreshableLayout) view.findViewById(R.id.refreshable_view);


        mSmartisanRefreshableLayout.setOnRefreshListener(new SmartisanRefreshableLayout.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRefreshFinished() {
                mSmartisanRefreshableLayout.finishRefreshing();
                getDynamic();
            }
        });


        mListView = (ListView) view.findViewById(R.id.list_view);


//长按监听
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setView(R.layout.dialog_delete_layout);

                builder.setTitle("删除提示");

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });


                //确认之后删除
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Dynamic dynamic=new Dynamic();
                        dynamic= adapter.getid(position);
                        dynamic.setObjectId(dynamic.getObjectId().toString());
                        dynamic.delete(getContext(), new DeleteListener() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(getContext(),"删除成功",Toast.LENGTH_SHORT).show();

                                getDynamic();
                            }

                            @Override
                            public void onFailure(int i, String s) {
                                Toast.makeText(getContext(),"删除失败"+s,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });


                builder.setCancelable(true);
                builder.show();


                return false;
            }
        });
        getDynamic();


        return view;
    }

    private void getDynamic() {

        BmobQuery<Dynamic> bmobQuery=new BmobQuery();

        MyUser user= BmobUser.getCurrentUser(getApplicationContext(),MyUser.class);


        bmobQuery.addWhereEqualTo("userid",user.getObjectId());


        bmobQuery.order("-createdAt").findObjects(getActivity(), new FindListener<Dynamic>() {
            @Override
            public void onSuccess(List<Dynamic> list) {


                adapter=new DynamicAdapter(getActivity(),R.layout.info_item,list);
                mListView.setAdapter(adapter);
            }
            @Override
            public void onError(int i, String s) {

                showSnackar(getView(), "笔记获取失败");

                //Toast.makeText(getContext(), "失败", Toast.LENGTH_SHORT).show();
            }
        });


    }


}
