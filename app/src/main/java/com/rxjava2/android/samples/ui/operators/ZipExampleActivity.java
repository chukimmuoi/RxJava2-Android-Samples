package com.rxjava2.android.samples.ui.operators;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.rxjava2.android.samples.R;
import com.rxjava2.android.samples.model.User;
import com.rxjava2.android.samples.utils.AppConstant;
import com.rxjava2.android.samples.utils.Utils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by amitshekhar on 27/08/16.
 */
public class ZipExampleActivity extends AppCompatActivity {

    private static final String TAG = ZipExampleActivity.class.getSimpleName();
    Button btn;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        btn = (Button) findViewById(R.id.btn);
        textView = (TextView) findViewById(R.id.textView);

        btn.setOnClickListener(v -> doSomeWork());
    }

    /*
    * Here we are getting two user list
    * One, the list of cricket fans
    * Another one, the list of football fans
    * Then we are finding the list of users who loves both
    */
    /**
     * http://reactivex.io/documentation/operators/zip.html
     * */
    private void doSomeWork() {
        Observable.zip(getFootballFansObservable(), getCricketFansObservable(),
                (users, users2) -> Utils.filterUserWhoLovesBoth(users, users2))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    private Observable<List<User>> getCricketFansObservable() {
        return Observable.create(observableEmitter -> {
           if(!observableEmitter.isDisposed()){
               observableEmitter.onNext(Utils.getUserListWhoLovesCricket());
               observableEmitter.onComplete();
           }
        });
    }

    private Observable<List<User>> getFootballFansObservable() {
        return Observable.create(observableEmitter -> {
           if(!observableEmitter.isDisposed()) {
               observableEmitter.onNext(Utils.getUserListWhoLovesFootball());
               observableEmitter.onComplete();
           }
        });
    }

    private Observer<List<User>> getObserver() {
        return new Observer<List<User>>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, " onSubscribe : " + d.isDisposed());
            }

            @Override
            public void onNext(List<User> userList) {
                textView.append(" onNext");
                textView.append(AppConstant.LINE_SEPARATOR);
                for (User user : userList) {
                    textView.append(" firstname : " + user.firstname);
                    textView.append(AppConstant.LINE_SEPARATOR);
                }
                Log.e(TAG, " onNext : " + userList.size());
            }

            @Override
            public void onError(Throwable e) {
                textView.append(" onError : " + e.getMessage());
                textView.append(AppConstant.LINE_SEPARATOR);
                Log.e(TAG, " onError : " + e.getMessage());
            }

            @Override
            public void onComplete() {
                textView.append(" onComplete");
                textView.append(AppConstant.LINE_SEPARATOR);
                Log.e(TAG, " onComplete");
            }
        };
    }


}