package com.univ.chat.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.univ.chat.gcm.MyPushReceiver;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.univ.chat.gcm.NotificationUtils;
import com.univ.chat.push.PushSDK;
import com.univ.chat.R;
import com.univ.chat.adapter.ChatRoomsAdapter;
import com.univ.chat.util.URL;
import com.univ.chat.util.ChatApplication;
import com.univ.chat.helper.SimpleDividerItemDecoration;
import com.univ.chat.model.Chat;
import com.univ.chat.model.Message;
import com.univ.chat.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private ArrayList<Chat> chatArrayList;
    private ChatRoomsAdapter mAdapter;
    private RecyclerView recyclerView;

    static public MyPushReceiver mypushreceiver = null ;
    MyPushReceiver.ReceiverCallback main_activity_handle = new MyPushReceiver.ReceiverCallback(){

        @Override
        public void handle(Message msg) {
            handlePushNotification(msg);
        }
    } ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ChatApplication.getInstance().getPrefManager().getUser() == null) {
            launchLoginActivity();
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        String uuid = ChatApplication.getInstance().
                getPrefManager().getUser().getId() ;
        final Context context = getApplicationContext() ;
        mypushreceiver = new MyPushReceiver(context) ;

        PushSDK.getInstance().registerApp(this , uuid) ;
        PushSDK.getInstance().registerPushListener(mypushreceiver);

        chatArrayList = new ArrayList<>();
        mAdapter = new ChatRoomsAdapter(this, chatArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new ChatRoomsAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new ChatRoomsAdapter.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Chat chat = chatArrayList.get(position);
                Intent intent = new Intent(MainActivity.this, ChatRoomActivity.class);
                intent.putExtra("chat_room_id", chat.getId());
                intent.putExtra("name", chat.getName());
                ///清楚所有得chat_标志.
                chat.clear_unread() ;
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        fetchChatRooms();
        ChatApplication.getInstance().getPrefManager().clearNotifications();
    }

    private void handlePushNotification(Message msg) {

        Message message = msg ;
        String chatRoomId = msg.getSenderId();

        if (message != null && chatRoomId != null) {
            updateRow(chatRoomId, message);
        }
    }

    private void updateRow(String chatRoomId, Message message) {
        for (Chat chat : chatArrayList) {
            if (chat.getId().equals(chatRoomId)) {
                //int index = chatArrayList.indexOf(chat);
                chat.setLastMessage(message.getMessage());
                chat.setUnreadCount(chat.getUnreadCount() + 1);
                //chatArrayList.remove(index);
                //chatArrayList.add(index, chat);
                ChatApplication.getInstance().getPrefManager().storeUserChatInfo(chat);
                break;
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void fetchChatRooms() {

        User user = ChatApplication.getInstance().getPrefManager().getUser();
        if (user == null) {
            Log.e(TAG,"user null in fetch chat rooms");
            launchLoginActivity();
            return;
        }
        String userId = user.getId();
        String url = String.format(URL.GET_ALL_USERS,userId);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "response: " + response);
                try {
                    if (response.getBoolean("success")) {
                        JSONArray rooms = response.getJSONArray("users");
                        for (int i = 0; i < rooms.length(); i++) {
                            JSONObject chatRoomsObj = rooms.getJSONObject(i);
                            String senderId = chatRoomsObj.getString("userId");
                            Chat previousChat = ChatApplication.getInstance().getPrefManager().getUserChatInfo(senderId);
                            Chat cr = new Chat();
                            cr.setId(senderId);
                            cr.setName(chatRoomsObj.getString("name"));
                            if (previousChat != null) {
                                cr.setLastMessage(previousChat.getLastMessage());
                                cr.setUnreadCount(previousChat.getUnreadCount());
                            }
                            else {
                                cr.setUnreadCount(0);
                                cr.setLastMessage("");
                            }
                            cr.setTimestamp(chatRoomsObj.getString("timestamp"));
                            chatArrayList.add(cr);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "" + response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                NetworkResponse networkResponse = error.networkResponse;
                String response = null;
                try {
                    response = (new JSONObject(new String(networkResponse.data, "UTF-8"))).getString("message");
                } catch (Exception exception) {

                }
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + response);
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> hashmap = new HashMap<String,String>() ;
                hashmap.put("charset", "utf8");
                return hashmap;
            }
        } ;

        ChatApplication.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void launchLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mypushreceiver.setHandle(main_activity_handle);
        NotificationUtils.clearNotifications();
    }

    @Override
    protected void onPause() {
        //mypushreceiver.setHandle(null);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int i = menuItem.getItemId();
        if (i == R.id.action_logout) {
            ChatApplication.getInstance().logout();

        }
        return super.onOptionsItemSelected(menuItem);
    }

}
