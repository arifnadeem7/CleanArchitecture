package com.arif.cleanarchitecture.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arif.cleanarchitecture.R;
import com.arif.cleanarchitecture.presenters.contracts.GetMovieContract;
import com.arif.cleanarchitecture.presenters.presenters.GetMoviePresenter;
import com.arif.cleanarchitecture.utils.Utils;
import com.arif.cleanarchitecture.views.activities.GetMovieActivity;
import com.bumptech.glide.Glide;

import java.util.Locale;

import io.realm.Realm;

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
public class GetMovieFragment extends BaseFragment implements GetMovieContract.View, SearchView.OnQueryTextListener {

    private GetMoviePresenter mGetMoviePresenter;
    private TextView mTvMovieDescription, mTvMovieTitle, mTvMovieRating, mTvError;
    private ImageView mImgMovie;
    private ProgressBar mProgressBar;
    private Realm mRealm;
    private LinearLayout mLLMovieDetail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((GetMovieActivity) getActivity()).getSupportActionBar().setTitle("IMDB Search");
        ((GetMovieActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setHasOptionsMenu(true);
        mRealm = Realm.getDefaultInstance();
        mGetMoviePresenter = new GetMoviePresenter(mRealm);
        mGetMoviePresenter.subscribeView(this);
        initViews(view);
    }

    @Override
    protected void initViews(View base) {
        mLLMovieDetail = (LinearLayout) base.findViewById(R.id.llMovieDetail);
        mProgressBar = (ProgressBar) base.findViewById(R.id.pbMovieLoad);
        mTvMovieTitle = (TextView) base.findViewById(R.id.tvMovieTitle);
        mTvMovieRating = (TextView) base.findViewById(R.id.tvMovieRating);
        mImgMovie = (ImageView) base.findViewById(R.id.imgMovie);
        mTvMovieDescription = (TextView) base.findViewById(R.id.tvMovieDescription);
        mTvError = (TextView) base.findViewById(R.id.tvError);
    }

    @Override
    protected void bindViewListeners() {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        try {
            SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(item);
            mSearchView.setQueryHint(getString(R.string.search_by_title));
            mSearchView.setInputType(InputType.TYPE_CLASS_TEXT);
            mSearchView.setOnQueryTextListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Utils.hideSoftKeyboard((GetMovieActivity) getActivity());
        if (query.length() > 2) {
            try {
                mGetMoviePresenter.getMovieForTitle(query, getString(R.string.movie).toLowerCase());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            showShortToast(getString(R.string.error_min_char_limit));
            return false;
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void showGetMovieSuccess(String title, String imdbRating, String plot, String imageUrl) {
        if (isFragmentAlive()) {
            toggleViewVisibilty(true);
            handleSuccessViewState();
            mTvMovieTitle.setText(title != null ? String.format(Locale.getDefault(), "%s: %s", getString(R.string.movie), title) : getString(R.string.no_title));
            mTvMovieRating.setText(imdbRating != null ? String.format(Locale.getDefault(), "%s: %s", getString(R.string.imdb_rating), imdbRating) : getString(R.string.no_rating));
            mTvMovieDescription.setText(plot != null ? String.format(Locale.getDefault(), "%s: %s", getString(R.string.plot), plot) : getString(R.string.no_plot));
            Glide.with(this).load(imageUrl).crossFade().into(mImgMovie);
        }
    }

    @Override
    public void showGetMovieError(Throwable t) {
        if (isFragmentAlive()) {
            handleErrorViewState();
            showShortToast(t.getMessage());
            mTvError.setText(t.getMessage() != null ? t.getMessage() : getString(R.string.error_unknown));
            toggleViewVisibilty(false);
        }
    }

    @Override
    public void networkError(Throwable t) {
        if (isFragmentAlive()) {
            mTvMovieDescription.setText(t.getMessage() != null ? t.getMessage() : getString(R.string.error_network));
            toggleViewVisibilty(false);
        }
    }

    @Override
    public void showLoading(boolean loading) {
        if (isFragmentAlive()) {
            if (loading) {
                mLLMovieDetail.setVisibility(View.GONE);
                mTvError.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
            } else {
                mProgressBar.setVisibility(View.GONE);
                mLLMovieDetail.setVisibility(View.VISIBLE);
            }
        }
    }

    private void toggleViewVisibilty(boolean show) {
        if (show) {
            mTvMovieTitle.setVisibility(View.VISIBLE);
            mTvMovieRating.setVisibility(View.VISIBLE);
        } else {
            mTvMovieTitle.setVisibility(View.GONE);
            mTvMovieRating.setVisibility(View.GONE);
        }
    }

    private void handleErrorViewState() {
        mLLMovieDetail.setVisibility(View.GONE);
        mTvError.setVisibility(View.VISIBLE);
    }

    private void handleSuccessViewState() {
        mTvError.setVisibility(View.GONE);
        mLLMovieDetail.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        if (mGetMoviePresenter.wasSubscribed(this))
            mGetMoviePresenter.unsubscribeView(this);
        if (!mRealm.isClosed())
            mRealm.close();
        super.onDestroyView();
    }
}
