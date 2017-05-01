package com.arif.cleanarchitecture;

import android.app.Application;

import com.arif.cleanarchitecture.utils.Constants;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by arifnadeem
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
public class CleanArchApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initRealm();
    }

    private void initRealm() {
        Realm.init(getApplicationContext());
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().schemaVersion(Constants.RealmVersion.CURRENT_VERSION).build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
