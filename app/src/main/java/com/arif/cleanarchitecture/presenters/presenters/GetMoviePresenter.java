package com.arif.cleanarchitecture.presenters.presenters;

import com.arif.cleanarchitecture.network.response.Movie;
import com.arif.cleanarchitecture.presenters.contracts.GetMovieContract;
import com.arif.cleanarchitecture.presenters.interactors.GetMovieInteractor;

import io.realm.Realm;

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

public class GetMoviePresenter extends BasePresenter<GetMovieContract.View, Movie> implements GetMovieContract.Presenter<Movie> {

    private GetMovieInteractor mGetMovieInteractor;

    public GetMoviePresenter(Realm realm) {
        mGetMovieInteractor = new GetMovieInteractor(realm);
    }

    @Override
    public void subscribeInteractor() {
        subscribeInteractor(mGetMovieInteractor, this);
    }

    public void getMovieForTitle(String title, String type) throws IllegalAccessException {
        if (getViewContract() == null)
            throw new IllegalAccessException("Cannot call this method before subscribing the View");
        if (getViewContract() != null) {
            getViewContract().showLoading(true);
            mGetMovieInteractor.getMovieForTitle(title, type);
        }
    }

    @Override
    public void onGetMovieSuccess(Movie movie) {
        if (getViewContract() != null) {
            getViewContract().showLoading(false);
            getViewContract().showGetMovieSuccess(movie.getTitle(), movie.getImdbRating(), movie.getPlot(), movie.getPoster());
        }
    }

    @Override
    public void onError(Throwable error) {
        if (getViewContract() != null) {
            getViewContract().showLoading(false);
            getViewContract().showGetMovieError(error);
        }
    }

    @Override
    public void onNetworkError(Throwable error) {
        if (getViewContract() != null) {
            getViewContract().showLoading(false);
            getViewContract().networkError(error);
        }
    }

    @Override
    public void onComplete() {

    }
}
