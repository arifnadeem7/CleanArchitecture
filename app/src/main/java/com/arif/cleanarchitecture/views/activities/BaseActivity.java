package com.arif.cleanarchitecture.views.activities;

import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    protected void showShortToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void showShortSnackbar(String msg) {
        try {
            ViewGroup vg = (ViewGroup) this.findViewById(android.R.id.content);
            if (vg != null) {
                Snackbar.make(vg.getChildAt(0), msg, Snackbar.LENGTH_SHORT).show();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    protected void showLongToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    protected void showLongSnackbar(String msg) {
        try {
            ViewGroup vg = (ViewGroup) this.findViewById(android.R.id.content);
            if (vg != null) {
                Snackbar.make(vg.getChildAt(0), msg, Snackbar.LENGTH_LONG).show();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    protected boolean isNullOrEmpty(String str) {
        return str == null || str.equals("");
    }

    protected void changeFragment(int id, Fragment fragment) {
        try {
            if (!isFinishing()) {
                String backStateName = fragment.getClass().getName();
                boolean fragmentPopped = getSupportFragmentManager().popBackStackImmediate(backStateName, 0);
                if (!fragmentPopped) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(id, fragment)
                            .addToBackStack(backStateName)
                            .commit();
                }
            }
        } catch (IllegalStateException ilex) {
            ilex.printStackTrace();
        }
    }

    protected void changeFragmentNoBackstack(int id, Fragment fragment) {
        try {
            if (!isFinishing()) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(id, fragment)
                        .commit();
            }
        } catch (IllegalStateException ilEx) {
            ilEx.printStackTrace();
        }
    }

    protected void showShortSnackBar(View view, String message) {
        if (view != null) {
            Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT).setAction("DISMISS", new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            TextView tv = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
            tv.setTypeface(Typeface.createFromAsset(
                    view.getContext().getAssets(),
                    "fonts/Roboto-Bold.ttf"));
            snackbar.show();
        }
    }
}
