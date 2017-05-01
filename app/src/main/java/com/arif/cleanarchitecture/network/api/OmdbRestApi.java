package com.arif.cleanarchitecture.network.api;

import com.arif.cleanarchitecture.network.response.Movie;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by arifnadeem
 * * <p>
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

public interface OmdbRestApi {

    @GET("/")
    Single<Movie> getMovie(@Query("t") String title,
                           @Query("plot") String plot,
                           @Query("type") String type,
                           @Query("r") String format);

}

