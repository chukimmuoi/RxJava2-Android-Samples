package com.rxjava2.android.samples.ui.operators;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.rxjava2.android.samples.R;
import com.rxjava2.android.samples.utils.AppConstant;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.AsyncSubject;

/**
 * Created by amitshekhar on 17/12/16.
 */

public class AsyncSubjectExampleActivity extends AppCompatActivity {

    private static final String TAG = AsyncSubjectExampleActivity.class.getSimpleName();
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

    /* An AsyncSubject emits the last value (and only the last value) emitted by the source
     * Observable, and only after that source Observable completes. (If the source Observable
     * does not emit any values, the AsyncSubject also completes without emitting any values.)
     */
    /**
     * {@link http://reactivex.io/documentation/subject.html#AsyncSubject}
     * */
    private void doSomeWork() {

        AsyncSubject<Integer> source = AsyncSubject.create();

        source.subscribe(getFirstObserver()); // it will emit only 4 and onComplete

        source.onNext(1);
        source.onNext(2);
        source.onNext(3);

        /*
         * it will emit 4 and onComplete for second observer also.
         */
        source.subscribe(getSecondObserver());

        source.onNext(4);
        source.onComplete();

    }


    private Observer<Integer> getFirstObserver() {
        return new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable disposable) {
                Log.e(TAG, "getFirstObserver - onSubscribe: " + disposable.isDisposed());
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.e(TAG, "getFirstObserver - onSubscribe: " + integer);
                textView.append(" First onNext : value : " + integer);
                textView.append(AppConstant.LINE_SEPARATOR);
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                Log.e(TAG, "getFirstObserver - onSubscribe - " + throwable.toString());
                textView.append(" First onError : " + throwable.getMessage());
                textView.append(AppConstant.LINE_SEPARATOR);
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "getFirstObserver - onSubscribe");
                textView.append(" First onComplete");
                textView.append(AppConstant.LINE_SEPARATOR);
            }
        };
    }

    private Observer<Integer> getSecondObserver() {
        return new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable disposable) {
                Log.e(TAG, "getSecondObserver - onSubscribe: " + disposable.isDisposed());
                textView.append(" Second onSubscribe : isDisposed :" + disposable.isDisposed());
                textView.append(AppConstant.LINE_SEPARATOR);
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.e(TAG, "getSecondObserver - onNext: " + integer);
                textView.append(" Second onNext : value : " + integer);
                textView.append(AppConstant.LINE_SEPARATOR);
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                Log.e(TAG, "getSecondObserver - onError");
                textView.append(" Second onError : " + throwable.getMessage());
                textView.append(AppConstant.LINE_SEPARATOR);
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "getSecondObserver - onComplete");
                textView.append(" Second onComplete");
                textView.append(AppConstant.LINE_SEPARATOR);
            }
        };
    }
}