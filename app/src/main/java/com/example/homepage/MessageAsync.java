package com.example.homepage;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import java.util.LinkedList;

import Message.Message ;
import Respond.Respond ;

/*
    运行的顺序是:一个接一个的运行:
        1.先处理Message里面的
 */
public abstract class MessageAsync <rst> {
    /// Task pool make the cntRuning is less than MaxRuning;
    static LinkedList< MessageAsync >list =
            new LinkedList<MessageAsync >();
    static int maxRuning = 5 ;
    static int cntRuning = 0 ;
    static void checkRun() {
        while(cntRuning < maxRuning && list.size() > 0) {
            ++ cntRuning ;
            list.removeFirst().runTask();
        }
    }
    static void addTask (MessageAsync task) {
        list.addLast(task);
        checkRun();
    }
    static void endTask(MessageAsync task) {
        -- cntRuning ;
        checkRun();
    }



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
        addTask(this);
    }
    private void runTask() {
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
                if(cnt_msg == gms.length) {
                    endTask(mtask);
                }
                else mtask.excute() ;
            }
        } ;
        task.execute(gms) ;
    }
    public abstract void handle_result(rst result , int cnt) ;
}
