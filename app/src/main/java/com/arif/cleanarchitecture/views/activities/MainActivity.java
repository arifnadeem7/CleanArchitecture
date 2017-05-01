package com.arif.cleanarchitecture.views.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.arif.cleanarchitecture.R;
import com.arif.cleanarchitecture.utils.Logger;
import com.arif.cleanarchitecture.utils.Navigator;

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
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Logger.init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Navigator.startGetMovieActivity(this, null);
        finish();
    }
}
