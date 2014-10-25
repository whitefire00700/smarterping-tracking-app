package com.track.smarterping;

import java.util.LinkedList;
import java.util.Queue;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;



public class ClientController implements Connection.ConnectionHandler {

    public static final long RECONNECT_DELAY = 10 * 1000;

    private Context context;

    private Handler handler;
    private Queue<String> messageQueue;

    private Connection connection;

    private String address;
    private int port;
    private String loginMessage;

    public ClientController(Context context, String address, int port, String loginMessage) {
        this.context = context;
        messageQueue = new LinkedList<String>();
        this.address = address;
        this.port = port;
        this.loginMessage = loginMessage;
        
    }

    private BroadcastReceiver connectivityListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            StatusActivity.addMessage(context.getString(R.string.status_connectivity_change));
            /*if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)) {
                handler.removeCallbacksAndMessages(null);
            } else {
                reconnect();
            }*/
        }
    };

    public void start() {
        handler = new Handler();
        connection = new Connection(this);
        connection.connect(address, port);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(connectivityListener, filter);
    }

    public void stop() {
        context.unregisterReceiver(connectivityListener);

        connection.close();
        handler.removeCallbacksAndMessages(null);
    }

    private void reconnect() {
        handler.removeCallbacksAndMessages(null);
        connection.close();
        connection = new Connection(this);
        connection.connect(address, port);
    }

    private void delayedReconnect() {
        connection.close();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                connection = new Connection(ClientController.this);
                connection.connect(address, port);
            }
        }, RECONNECT_DELAY);
    }

    public void setNewServer(String address, int port) {
        this.address = address;
        this.port = port;
        reconnect();
    }

    public void setNewLogin(String loginMessage) {
        this.loginMessage = loginMessage;
        reconnect();
    }

    public void setNewLocation(String locationMessage) {
        messageQueue.offer(locationMessage);
        if (!connection.isClosed() && !connection.isBusy()) {
            connection.send(messageQueue.poll());
        }
    }

    @Override
    public void onConnected(boolean result) {
        if (result) {
            StatusActivity.addMessage(context.getString(R.string.status_connection_success));
            connection.send(loginMessage);
        } else {
            StatusActivity.addMessage(context.getString(R.string.status_connection_fail));
            delayedReconnect();
        }
    }

    @Override
    public void onSent(boolean result) {
        if (result) {
            if (!messageQueue.isEmpty()) {
                connection.send(messageQueue.poll());
            }
        } else {
            StatusActivity.addMessage(context.getString(R.string.status_send_fail));
            delayedReconnect();
        }
    }

}
