# Android_Okhttp
简单的OKhttp 同步异步的get 和post 请求地址可以改成自己的接口地址

同步get 和异步get

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
            
            
  同步post  和异步post    以及hanlder更改页面返回值赋值给textview
  
  
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
