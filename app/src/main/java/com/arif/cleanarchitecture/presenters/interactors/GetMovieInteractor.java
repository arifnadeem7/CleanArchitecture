package com.arif.cleanarchitecture.presenters.interactors;

import com.arif.cleanarchitecture.network.api.OmdbApiObservables;
import com.arif.cleanarchitecture.network.api.RxSingleSubscriberEvents;
import com.arif.cleanarchitecture.network.response.Movie;
import com.arif.cleanarchitecture.presenters.contracts.GetMovieContract;

import java.io.IOException;

import io.realm.Case;
import io.realm.Realm;
import retrofit2.HttpException;

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

public class GetMovieInteractor extends BaseInteractor implements GetMovieContract.ApiContract, RxSingleSubscriberEvents<Movie> {

    private final OmdbApiObservables mOmdbApiObservables;
    private final Realm mRealm;

    public GetMovieInteractor(Realm realm) {
        mRealm = realm;
        mOmdbApiObservables = new OmdbApiObservables();
    }

    @Override
    public void getMovieForTitle(String title, String type) {
        if (getPresenterContract() != null) {
            Movie movie = mRealm.where(Movie.class).equalTo("Title", title, Case.INSENSITIVE).findFirst();
            if (movie != null) {
                showGetMovieSuccess(movie);
            } else {
                mOmdbApiObservables.getMovieForTitle(title, type).subscribe(getSingleSubscriber(this));
            }
        }
    }

    @Override
    public void onSuccess(Movie movie) {
        if (movie != null) {
            if (movie.getResponse().equalsIgnoreCase("true")) {
                mRealm.beginTransaction();
                mRealm.insertOrUpdate(movie);
                mRealm.commitTransaction();
                showGetMovieSuccess(movie);
            } else
                showGetMovieError(new Exception("Movie not found"));
        }
    }

    @Override
    public void onError(Throwable error) {
        if (error != null) {
            if (error instanceof HttpException) {
                showGetMovieError(error);
            } else if (error instanceof IOException)
                showNetworkError(error);
            else {
                showGetMovieError(error);
            }
        }
    }

    //view helper methods

    private void showGetMovieSuccess(Movie movie) {
        if (getPresenterContract() != null)
            ((GetMovieContract.Presenter) getPresenterContract()).onGetMovieSuccess(movie);
    }

    private void showGetMovieError(Throwable t) {
        if (getPresenterContract() != null)
            getPresenterContract().onError(t);
    }

    private void showNetworkError(Throwable t) {
        if (getPresenterContract() != null)
            getPresenterContract().onNetworkError(t);
    }
}
