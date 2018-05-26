package com.univ.chat.gcm;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.univ.chat.helper.GsonHelper;
import com.univ.chat.model.Message;
import com.univ.chat.model.User;
import com.univ.chat.util.ChatApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Observable;
import java.util.Observer;

public class MyPushReceiver implements Observer {
    private static final String TAG = MyPushReceiver.class.getSimpleName();
    private NotificationUtils notificationUtils;
    private Context context ;
    private Class<?>notification_activity;

    public MyPushReceiver (Context c , Class<?>activity) {
        context = c ;
        notification_activity = activity ;
    }

    public Message onMessageReceived(String data) {

        String title = "ChatMessage";
        Log.d(TAG, "title: " + title);
        Log.d(TAG, "data: " + data);

        if (ChatApplication.getInstance().getPrefManager().getUser() == null) {
            Log.e(TAG, "user is not logged in, skipping push notification");
            return null ;
        }
        return processMessage(title, data);
        //processTypingState(data);
    }

    public Message processMessage(String title, String data) {
        Message result = null ;
        try {
            JSONObject dataJSON = new JSONObject(data);
            JSONObject messageJSON = dataJSON.getJSONObject("message");
            Message message = (Message) GsonHelper.fromJson(messageJSON,Message.class);
            result = message ;
            JSONObject userJSON = dataJSON.getJSONObject("user");
            User user = (User) GsonHelper.fromJson(userJSON,User.class);
            message.setUser(user);

            NotificationUtils notificationUtils = new NotificationUtils();
            notificationUtils.playNotificationSound();

            if (!NotificationUtils.isAppInBackground(context)) {
            } else {
                Intent resultIntent = new Intent(context, notification_activity);
                showNotificationMessage(context, title, user, message, resultIntent);
            }
        } catch (JSONException | ClassNotFoundException e) {
            Log.e(TAG, "json parsing error: " + e.getMessage());
            Toast.makeText(context, "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return result ;
    }

    private void showNotificationMessage(Context context, String title, User user, Message message, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, user, message, intent);
    }

    static public abstract class ReceiverCallback {
        abstract public void handle(Message msg)  ;
    }
    private ReceiverCallback handle = null ;
    public void setHandle(ReceiverCallback callback) {
        handle = callback ;
    }
    @Override
    public void update(Observable observable, Object o) {
        String json_message = (String) o ;
        Message msg = onMessageReceived(json_message) ;
        if(handle != null) handle.handle(msg);
    }
}
