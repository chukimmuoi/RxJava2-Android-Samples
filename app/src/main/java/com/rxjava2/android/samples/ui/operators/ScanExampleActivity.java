package com.rxjava2.android.samples.ui.operators;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.rxjava2.android.samples.R;
import com.rxjava2.android.samples.utils.AppConstant;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ScanExampleActivity extends AppCompatActivity {

    private static final String TAG = ScanExampleActivity.class.getSimpleName();
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

    /* Using scan operator, it sends also the previous result */
    /**
     * http://reactivex.io/documentation/operators/scan.html
     * */
    private void doSomeWork() {
        getObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .scan((integer, integer2) -> integer + integer2)
                .subscribe(getObserver());
    }

    private Observable<Integer> getObservable() {
        return Observable.just(1, 2, 3, 4, 5);
    }

    private Observer<Integer> getObserver() {
        return new Observer<Integer>() {

            @Override
            public void onSubscribe(Disposable d) {
                Log.e(TAG, " onSubscribe : " + d.isDisposed());
            }

            @Override
            public void onNext(Integer value) {
                textView.append(" onNext : value : " + value);
                textView.append(AppConstant.LINE_SEPARATOR);
                Log.e(TAG, " onNext value : " + value);
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