package com.rock.han.okhttpstudy;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.UiThread;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends Activity {

    private Button btn_syncget;
    private Button btn_asynget;
    private Button btn_syncpost;
    private Button btn_asynpost;
    private TextView txt_result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_syncget=(Button) findViewById(R.id.btn_syncget);
        btn_asynget=(Button) findViewById(R.id.btn_asynget);
        btn_syncpost=(Button) findViewById(R.id.btn_syncpost);
        btn_asynpost=(Button) findViewById(R.id.btn_asynpost);
        txt_result=(TextView) findViewById(R.id.txt_result);

        btn_syncget.setOnClickListener(new syncGetClickListener());
        btn_asynget.setOnClickListener(new asynGetClickListener());
        btn_syncpost.setOnClickListener(new syncPostClickListener());
        btn_asynpost.setOnClickListener(new asynPostClickListener());
    }

    private class syncGetClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpClient okHttpClient=new OkHttpClient();
                    Request request=new Request.Builder()
                            .url("http://www.baidu.com")
                            .build();


                    try {
                        final Response response=okHttpClient.newCall(request).execute();
                            Log.d("kwwl","response.code()=="+response.code());
                            Log.d("kwwl","response.message()=="+response.message());
                            Log.d("kwwl","res=="+response.body().string());

                            //我们第一个调用UI线程直接用这种方法，下一个用handler
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txt_result.setText(response.body().toString());
                                }
                            });


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }).start();


        }
    }
    private class asynGetClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            OkHttpClient okHttpClient=new OkHttpClient();
            Request request=new  Request.Builder()
                    .url("http://www.baidu.com/")
                    .build();



            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    //做失败的操作
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    Log.d("kwwl","获取数据成功了");
                    Log.d("kwwl","response.code()=="+response.code());
                    Log.d("kwwl","response.body().string()=="+response.body().string());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txt_result.setText(response.body().toString());
                        }
                    });
                }
            });


          //每一步分开写 结果与上面一样
          /*  OkHttpClient okHttpClient=new OkHttpClient();
            Request.Builder builder=new Request.Builder();
            Request request=builder.get().url("http://www.baidu.com").build();
            Call call=okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txt_result.setText(response.body().toString());
                        }
                    });
                }
            });*/


        }
    }
    private class syncPostClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    FormBody.Builder formBody=new FormBody.Builder();
                    formBody.add("18811534193","QB74521");
                    OkHttpClient okHttpClient=new OkHttpClient();
                    Request request=new Request.Builder()
                            .url("http://www.baodu.com/")//可以改成自己的接口
                            .post(formBody.build())
                            .build();
                    try {
                        Response response=okHttpClient.newCall(request).execute();
                        Log.d("kwwl","response.code()=="+response.code());
                        Log.d("kwwl","response.message()=="+response.message());
                        Log.d("kwwl","res=="+response.body().string());
                        //此时的代码执行在子线程，修改UI的操作请使用handler跳转到UI线程。
                        Message message = handler.obtainMessage();
                        message.obj = response.body().toString();
                        message.what=1;
                        handler.sendMessage(message);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1)
            {
                txt_result.setText(msg.toString());
            }
            if(msg.what==2)
            {
                txt_result.setText(msg.toString());
            }
        }
    };

    private class asynPostClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            FormBody.Builder formBody=new FormBody.Builder();
            formBody.add("18811534193","123456");
            OkHttpClient okHttpClient=new OkHttpClient();
            Request request=new Request.Builder()
                    .url("http://www.baidu.com")
                    .post(formBody.build())
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Message message = handler.obtainMessage();
                    message.obj = response.body().toString();
                    message.what=2;
                    handler.sendMessage(message);
                }
            });
        }
    }
}
