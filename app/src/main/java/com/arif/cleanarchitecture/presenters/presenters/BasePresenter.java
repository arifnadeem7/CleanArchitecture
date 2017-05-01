package com.arif.cleanarchitecture.presenters.presenters;

import com.arif.cleanarchitecture.presenters.contracts.BasePresenterContract;
import com.arif.cleanarchitecture.presenters.interactors.BaseInteractor;
import com.arif.cleanarchitecture.utils.Logger;

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
 *
 * @param <V> View to be attached
 * @param <M> Model to be got from Server or DB
 */

public abstract class BasePresenter<V, M> {

    private V viewContract;
    private BaseInteractor<M> mBaseInteractor;
    private BasePresenterContract mInteractorContract;

    /**
     * Call this when View is being created
     * <p/>
     * This binds the view to the listener
     *
     * @param updateListener
     */
    public void subscribeView(V updateListener) {
        subscribeInteractor();
        final V previousListener = viewContract;
        if (previousListener != null) {
            throw new IllegalStateException("A view was already bound to this listener, please unbind it before binding any other view = " + previousListener);
        }
        viewContract = updateListener;
        mBaseInteractor.subscribeView(mInteractorContract);
        Logger.e(BasePresenter.class.getSimpleName(), "subscribeView");
    }

    /**
     * Check if View was previously subscribed.
     *
     * @param updateListener
     * @return
     */
    public boolean wasSubscribed(V updateListener) {
        if (updateListener != null) {
            if (viewContract == null)
                return false;
            if (updateListener == viewContract)
                return true;
        }
        return false;
    }

    /**
     * Call this when View is being finished or destroyed
     *
     * @param updateListener
     */
    public void unsubscribeView(V updateListener) {
        final V previousListener = viewContract;
        if (previousListener == updateListener) {
            this.viewContract = null;
        } else {
            throw new IllegalStateException("No such listener was bound previously.");
        }
        mBaseInteractor.unsubscribeView(mInteractorContract);
        Logger.e(BasePresenter.class.getSimpleName(), "unsubscribeView");
    }

    public void subscribeInteractor(BaseInteractor<M> interactor, BasePresenterContract contract) {
        mBaseInteractor = interactor;
        mInteractorContract = contract;
    }

    public abstract void subscribeInteractor();

    public V getViewContract() {
        return viewContract;
    }

}
