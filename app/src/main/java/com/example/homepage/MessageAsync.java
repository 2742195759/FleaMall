package com.example.homepage;

import android.os.AsyncTask;
import Message.Message ;
import Respond.Respond ;

public abstract class MessageAsync <rst> {
    Message gms ;
    public MessageAsync(Message ms) {
        gms = ms ;
    }
    public void excute() {
        new AsyncTask<Message , Void , rst> () {
            @Override
            protected rst doInBackground(Message... paras) {
                /// 目前只支持1个参数，以后可能可以多个一起增加。
                for (Message p : paras){
                    return (rst) p.sendAndReturn() ;
                }
                return null ;
            }

            @Override
            protected void onPostExecute(rst rst) {
                handle_result(rst);
            }
        }.execute(gms) ;
    }
    public abstract void handle_result(rst result) ;
}
