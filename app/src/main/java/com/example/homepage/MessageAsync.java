package com.example.homepage;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import Message.Message ;
import Respond.Respond ;
/*
    运行的顺序是:一个接一个的运行:
        1.先处理Message里面的
 */
public abstract class MessageAsync <rst> {
    Message[] gms ;
    MessageAsync mtask = this ;
    public MessageAsync(Message ... ms) {
        gms = ms;
    }
    int cnt_msg = 0 ;
    public void reset() {
        cnt_msg = 0 ;
    }
    public void excute() {
        AsyncTask task = new AsyncTask<Message , Void , rst> () {
            @Override
            protected rst doInBackground(Message ... paras) {
                /// 目前只支持1个参数，以后可能可以多个一起增加。
                return (rst)(paras[cnt_msg].sendAndReturn()) ;
            }

            @Override
            protected void onPostExecute(rst rst) {
                handle_result(rst , cnt_msg) ;
                ++ cnt_msg ;
                if(cnt_msg == gms.length) return ;
                else mtask.excute() ;
            }
        } ;
        task.execute(gms) ;
    }
    public abstract void handle_result(rst result , int cnt) ;
}
