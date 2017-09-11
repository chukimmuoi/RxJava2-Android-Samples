package com.rxjava2.android.samples.ui.networking;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.rx2androidnetworking.Rx2AndroidNetworking;
import com.rxjava2.android.samples.R;
import com.rxjava2.android.samples.model.ApiUser;
import com.rxjava2.android.samples.model.User;
import com.rxjava2.android.samples.model.UserDetail;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by amitshekhar on 04/02/17.
 */

public class NetworkingActivity extends AppCompatActivity {

    public static final String TAG = NetworkingActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_networking);
    }

    /**
     * Map Operator Example
     */
    public void map(View view) {
        Rx2AndroidNetworking.get("https://fierce-cove-29863.herokuapp.com/getAnUser/{userId}")
                .addPathParameter("userId", "2")
                .build()
                .getObjectObservable(ApiUser.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(apiUser -> {
                    User user = new User(apiUser);
                    return user;
                }).subscribe(new Observer<User>() {
            @Override
            public void onSubscribe(@NonNull Disposable disposable) {
                Log.e(TAG, "map - onSubscribe");
            }

            @Override
            public void onNext(@NonNull User user) {
                Log.e(TAG, "map - onNext - " + user.toString());
            }

            @Override
            public void onError(@NonNull Throwable throwable) {
                Log.e(TAG, "map - onError - " + throwable.toString());
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "map - onComplete");
            }
        });
    }


    /**
     * zip Operator Example
     */

    /**
     * This observable return the list of User who loves cricket
     */
    private Observable<List<User>> getCricketFansObservable() {
        return Rx2AndroidNetworking.get("https://fierce-cove-29863.herokuapp.com/getAllCricketFans")
                .build()
                .getObjectListObservable(User.class);
    }

    /*
    * This observable return the list of User who loves Football
    */
    private Observable<List<User>> getFootballFansObservable() {
        return Rx2AndroidNetworking.get("https://fierce-cove-29863.herokuapp.com/getAllFootballFans")
                .build()
                .getObjectListObservable(User.class);
    }

    /*
    * This do the complete magic, make both network call
    * and then returns the list of user who loves both
    * Using zip operator to get both response at a time
    */
    private void findUsersWhoLovesBoth() {
        // here we are using zip operator to combine both request
        Observable.zip(
                getCricketFansObservable(),
                getFootballFansObservable(),
                (cricketFans, footballFans) -> filterUserWhoLovesBoth(cricketFans, footballFans))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<User>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        Log.e(TAG, "zip - onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull List<User> users) {
                        // do anything with user who loves both
                        Log.e(TAG, "zip - onNext - size = " + users.size());
                        for (User user : users) {
                            Log.e(TAG, "zip - onNext - user = " + user.toString());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        Log.e(TAG, "zip - onError - " + throwable.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "zip - onComplete");
                    }
                });
    }

    private List<User> filterUserWhoLovesBoth(List<User> cricketFans, List<User> footballFans) {
        List<User> userWhoLovesBoth = new ArrayList<>();
        for (User cricketFan : cricketFans) {
            for (User footballFan : footballFans) {
                if (cricketFan.id == footballFan.id) {
                    userWhoLovesBoth.add(cricketFan);
                }
            }
        }
        return userWhoLovesBoth;
    }


    public void zip(View view) {
        findUsersWhoLovesBoth();
    }


    /**
     * flatMap and filter Operators Example
     */

    private Observable<List<User>> getAllMyFriendsObservable() {
        return Rx2AndroidNetworking.get("https://fierce-cove-29863.herokuapp.com/getAllFriends/{userId}")
                .addPathParameter("userId", "2")
                .build()
                .getObjectListObservable(User.class);
    }

    public void flatMapAndFilter(View view) {
        getAllMyFriendsObservable()
                .flatMap(users -> Observable.fromIterable(users)) // returning user one by one from usersList.
                .filter(user -> user.isFollowing) // filtering user who follows me.
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        Log.e(TAG, "FlatMap and Filter - onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull User user) {
                        // only the user who is following me comes here one by one
                        Log.e(TAG, "FlatMap and Filter - onNext - " + user.toString());
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        Log.e(TAG, "FlatMap and Filter - onError - " + throwable.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "FlatMap and Filter - onComplete");
                    }
                });
    }


    /**
     * take Operator Example
     */

    public void take(View view) {
        getUserListObservable()
                .flatMap(users -> Observable.fromIterable(users)) // returning user one by one from usersList.
                .take(5) // it will only emit first 5 users out of all
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<User>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        Log.e(TAG, "take - onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull User user) {
                        Log.e(TAG, "take - onNext - " + user.toString());
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        Log.e(TAG, "take - onError - " + throwable.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "take - onComplete");
                    }
                });
    }


    /**
     * flatMap Operator Example
     */

    public void flatMap(View view) {
        getUserListObservable()
                .flatMap(users -> Observable.fromIterable(users))   // returning user one by one from usersList.
                .flatMap(user -> getUserDetailObservable(user.id))  // here we get the user one by one
                                                                    // and returns corresponding getUserDetailObservable
                                                                    // for that userId
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserDetail>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        Log.e(TAG, "flatMap - onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull UserDetail userDetail) {
                        // do anything with userDetail
                        Log.e(TAG, "flatMap - onNext - " + userDetail.toString());
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        Log.e(TAG, "flatMap - onError - " + throwable.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "flatMap - onComplete");
                    }
                });
    }

    /**
     * flatMapWithZip Operator Example
     */

    private Observable<List<User>> getUserListObservable() {
        return Rx2AndroidNetworking.get("https://fierce-cove-29863.herokuapp.com/getAllUsers/{pageNumber}")
                .addPathParameter("pageNumber", "0")
                .addQueryParameter("limit", "10")
                .build()
                .getObjectListObservable(User.class);
    }

    private Observable<UserDetail> getUserDetailObservable(long id) {
        return Rx2AndroidNetworking.get("https://fierce-cove-29863.herokuapp.com/getAnUserDetail/{userId}")
                .addPathParameter("userId", String.valueOf(id))
                .build()
                .getObjectObservable(UserDetail.class);
    }

    public void flatMapWithZip(View view) {
        getUserListObservable()
                .flatMap(users -> Observable.fromIterable(users)) // returning user one by one from usersList.
                // here we get the user one by one and then we are zipping
                // two observable - one getUserDetailObservable (network call to get userDetail)
                // and another Observable.just(user) - just to emit user
                .flatMap(user -> Observable.zip(getUserDetailObservable(user.id),
                        Observable.just(user),
                        ((userDetail, user1) -> new Pair(userDetail, user1))))  // runs when network call completes
                                                                                // we get here userDetail for the corresponding user
                                                                                // returning the pair(userDetail, user)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Observer<? super Pair>) new Observer<Pair<UserDetail, User>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        Log.e(TAG, "flatMapWithZip - onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull Pair<UserDetail, User> userDetailUserPair) {
                        // here we are getting the userDetail for the corresponding user one by one
                        UserDetail userDetail = userDetailUserPair.first;
                        User user = userDetailUserPair.second;
                        Log.e(TAG, "flatMapWithZip - onNext - userDetail: " + userDetail.toString());
                        Log.e(TAG, "flatMapWithZip - onNext - user: " + user.toString());
                    }

                    @Override
                    public void onError(@NonNull Throwable throwable) {
                        // handle error
                        Log.e(TAG, "flatMapWithZip - onError - " + throwable.toString());
                    }

                    @Override
                    public void onComplete() {
                        // do something onCompleted
                        Log.e(TAG, "flatMapWithZip - onComplete");
                    }
                });
    }
}
