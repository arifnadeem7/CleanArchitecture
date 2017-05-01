package com.arif.cleanarchitecture.presenters.contracts;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.arif.cleanarchitecture.presenters.BaseView;

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

public interface GetMovieContract {

    interface View extends BaseView {

        void showGetMovieSuccess(@Nullable String title, @Nullable String imdbRating, @Nullable String plot, @Nullable String imageUrl);

        void showGetMovieError(@Nullable Throwable t);

        void networkError(@Nullable Throwable t);

    }

    interface ApiContract {
        void getMovieForTitle(@NonNull String title, @NonNull String type) throws IllegalAccessException;
    }

    interface Presenter<Movie> extends BasePresenterContract {
        void onGetMovieSuccess(Movie data);
    }

}
