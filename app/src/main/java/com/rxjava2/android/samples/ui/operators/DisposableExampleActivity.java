package com.rxjava2.android.samples.ui.operators;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.rxjava2.android.samples.R;
import com.rxjava2.android.samples.utils.AppConstant;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by amitshekhar on 27/08/16.
 */
public class DisposableExampleActivity extends AppCompatActivity {

    private static final String TAG = DisposableExampleActivity.class.getSimpleName();
    Button btn;
    TextView textView;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        btn = (Button) findViewById(R.id.btn);
        textView = (TextView) findViewById(R.id.textView);

        btn.setOnClickListener(v -> doSomeWork());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear(); // do not send event after activity has been destroyed
    }

    /*
     * Example to understand how to use disposables.
     * disposables is cleared in onDestroy of this activity.
     */
    /**
     * {@link http://reactivex.io/RxJava/2.x/javadoc/io/reactivex/disposables/CompositeDisposable.html}
     * */
    void doSomeWork() {
        disposables.add(sampleObservable()
                // Run on a background thread
                .subscribeOn(Schedulers.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onComplete() {
                        textView.append(" onComplete");
                        textView.append(AppConstant.LINE_SEPARATOR);
                        Log.e(TAG, " onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        textView.append(" onError : " + e.getMessage());
                        textView.append(AppConstant.LINE_SEPARATOR);
                        Log.e(TAG, " onError : " + e.getMessage());
                    }

                    @Override
                    public void onNext(String value) {
                        textView.append(" onNext : value : " + value);
                        textView.append(AppConstant.LINE_SEPARATOR);
                        Log.e(TAG, " onNext value : " + value);
                    }
                }));
    }

    static Observable<String> sampleObservable() {
        return Observable.defer(() -> {
            SystemClock.sleep(2000);
            return Observable.just("one", "two", "three", "four", "five");
        });
    }
}
