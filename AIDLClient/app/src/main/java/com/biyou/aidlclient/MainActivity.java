package com.biyou.aidlclient;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.biyou.aidlpath.Book;
import com.biyou.aidlpath.BookManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private BookManager mBookManager = null;

    private boolean mBound = false;

    private List<Book> mBooks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void addBook(View view)
    {
        if(!mBound)
        {
            attemptToBindService();
            Toast.makeText(this,"当前与服务端处于未连接状态，正在尝试重连，请稍后再试", Toast.LENGTH_SHORT);
            return;
        }
        Log.e(TAG,"addBook APP研发录In 1");
        if(mBookManager == null)
            return;
        Log.e(TAG,"addBook APP研发录In 2");
        Book book = new Book();
        book.setName("APP研发录In");
        book.setPrice(30);
        try{
            mBookManager.addBook(book);
            Log.e(TAG,book.toString());
        }catch (RemoteException e){
            e.printStackTrace();
        }
    }

    private void attemptToBindService()
    {
        Intent intent = new Intent();
        intent.setAction("com.biyou.aidl");
        intent.setPackage("com.biyou.aidlserver");
        bindService(intent,mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        if(!mBound)
        {
            attemptToBindService();
        }
    }
    @Override
    protected void onStop()
    {
        super.onStop();
        if(mBound)
        {
            unbindService(mServiceConnection);
            mBound = false;
        }
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.e(TAG,"service connected");

            mBookManager = BookManager.Stub.asInterface(iBinder);
            mBound = true;
            if(mBookManager != null)
            {
                try{
                    mBooks = mBookManager.getBooks();
                    Log.e(TAG,mBooks.toString());
                }catch (RemoteException e){
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.e(TAG,"service disconnected");
        }
    };


























}
