package com.biyou.aidlserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.biyou.aidlpath.Book;
import com.biyou.aidlpath.BookManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dLi on 2017/10/18.
 */

public class AIDLService extends Service {
    public final String TAG = this.getClass().getSimpleName();

    private List<Book> mBooks = new ArrayList<>();

    private final BookManager.Stub mBookManager = new BookManager.Stub()
    {

        @Override
        public List<Book> getBooks() throws RemoteException {
            synchronized (this)
            {
                Log.e(TAG,"invoking getBooks() method,now the list is : " + mBooks.toString());
                if(mBooks != null)
                {
                    return mBooks;
                }
                return new ArrayList<>();
            }
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            synchronized (this)
            {
                if(mBooks == null)
                {
                    mBooks = new ArrayList<>();
                }
                if(book == null)
                {
                    Log.e(TAG,"Book is null in In");
                    book = new Book();
                }
                book.setPrice(2333);
                if(!mBooks.contains(book))
                {
                    mBooks.add(book);
                }
                Log.e(TAG,"invoking addBooks() method , now the list is : " + mBooks.toString());
            }
        }
    };

    @Override
    public void onCreate()
    {
        super.onCreate();
        Book book = new Book();
        book.setName("Android开发艺术探索");
        book.setPrice(28);
        mBooks.add(book);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e(TAG,String.format("on bind,intent = %s", intent.toString()));

        return mBookManager;
    }

























}
