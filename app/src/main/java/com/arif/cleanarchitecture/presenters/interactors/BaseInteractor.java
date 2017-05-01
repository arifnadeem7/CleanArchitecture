package com.arif.cleanarchitecture.presenters.interactors;

import com.arif.cleanarchitecture.network.api.RxSingleSubscriberEvents;
import com.arif.cleanarchitecture.network.api.RxSubscriberEvents;
import com.arif.cleanarchitecture.presenters.contracts.BasePresenterContract;
import com.arif.cleanarchitecture.utils.Logger;

import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by arifnadeem on 3/21/17.
 * <p>
 * Copyright 2017 - Arif Nadeem
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public abstract class BaseInteractor<T> {

    private static final String TAG = BaseInteractor.class.getSimpleName();

    private BasePresenterContract presenterContract;

    private final CompositeDisposable subscriptionQueue = new CompositeDisposable();

    /**
     * Call this when View is being created
     * <p/>
     * This binds the view to the listener
     *
     * @param updateListener
     */
    public void subscribeView(BasePresenterContract updateListener) {
        final BasePresenterContract previousListener = presenterContract;
        if (previousListener != null) {
            throw new IllegalStateException("A view was already bound to this listener, please unbind it before binding any other view = " + previousListener);
        }
        presenterContract = updateListener;
        Logger.e(TAG, "subscribeView");
    }

    /**
     * Check if View was previously subscribed.
     *
     * @param updateListener
     * @return
     */
    public boolean wasSubscribed(BasePresenterContract updateListener) {
        if (updateListener != null) {
            if (presenterContract == null)
                return false;
            if (updateListener == presenterContract)
                return true;
        }
        return false;
    }

    /**
     * Call this when View is being finished or destroyed
     *
     * @param updateListener
     */
    public void unsubscribeView(BasePresenterContract updateListener) {
        final BasePresenterContract previousListener = presenterContract;
        if (previousListener == updateListener) {
            this.presenterContract = null;
        } else {
            throw new IllegalStateException("No such listener was bound previously.");
        }
        clearSubscriptionQueue();
        Logger.e(TAG, "unsubscribeView");
    }

    public BasePresenterContract getPresenterContract() {
        return presenterContract;
    }

    public void queueSubscriptionForDisposal(Disposable subscription) {
        subscriptionQueue.add(subscription);
        Logger.e(TAG, "Added subscription, count = " + subscriptionQueue.size());
    }

    //Queue multiple subscriptions for removing
    public void queueSubscriptionsForDisposal(Disposable... subscriptions) {
        for (Disposable subscription : subscriptions) {
            subscriptionQueue.add(subscription);
        }
    }

    public void clearSubscriptionQueue() {
        Logger.e(TAG, "Clearing subscription queue!");
        Logger.e(TAG, "count before clearing = " + subscriptionQueue.size());
        subscriptionQueue.clear();
        Logger.e(TAG, "count after clearing = " + subscriptionQueue.size());
    }

    protected <E> Observer<E> getSubscriber(final RxSubscriberEvents subscriberEvents) {
        return new Observer<E>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                queueSubscriptionForDisposal(d);
            }

            @Override
            public void onError(Throwable e) {
                subscriberEvents.onError(e);
            }

            @Override
            public void onComplete() {
                subscriberEvents.onCompleted();
            }

            @Override
            public void onNext(E next) {
                subscriberEvents.onNext(next);
            }
        };
    }

    //SingleSubscriber for better performance!!!
    protected <E> SingleObserver<E> getSingleSubscriber(final RxSingleSubscriberEvents singleSubscriberEvents) {
        return new SingleObserver<E>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {
                queueSubscriptionForDisposal(d);
            }

            @Override
            public void onSuccess(E value) {
                singleSubscriberEvents.onSuccess(value);
            }

            @Override
            public void onError(Throwable error) {
                singleSubscriberEvents.onError(error);
            }
        };
    }
}
