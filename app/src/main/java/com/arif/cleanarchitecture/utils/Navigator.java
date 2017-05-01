package com.arif.cleanarchitecture.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.arif.cleanarchitecture.views.activities.GetMovieActivity;

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

public final class Navigator {

    private Navigator() {

    }

    public static void startGetMovieActivity(Context from, Bundle extras) {
        Intent intent = new Intent(from, GetMovieActivity.class);
        startActivity(from, extras, intent);
    }

    private static void startActivity(Context from, Bundle extras, Intent intent) {
        if (extras != null)
            intent.putExtras(extras);
        from.startActivity(intent);
    }

    public static void clearBackStackAndStartActivity(Context context, Class name, Bundle bundle) {
        Intent intent = new Intent(context, name);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(context, bundle, intent);
    }

}
