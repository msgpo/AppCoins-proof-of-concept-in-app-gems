/*
 * Copyright (c) 2016.
 * Modified by SithEngineer on 12/05/2016.
 */

package cm.aptoide.pt.v8engine.fragment.implementations;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cm.aptoide.pt.model.v7.Event;
import cm.aptoide.pt.v8engine.R;

/**
 * Created by neuro on 10-05-2016.
 */
public class StoreGridRecyclerFragment extends StoreTabGridRecyclerFragment {

	public static StoreGridRecyclerFragment newInstance(Event event, String
			title) {
		Bundle args = buildBundle(event, title);

		StoreGridRecyclerFragment fragment = new StoreGridRecyclerFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void setupToolbar() {
		// It's not calling super cause it does nothing in the middle class}
		// StoreTabGridRecyclerFragment.
		if (toolbar != null) {
			((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
			((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(title);
			((AppCompatActivity) getActivity()).getSupportActionBar()
					.setDisplayHomeAsUpEnabled(true);
			toolbar.setLogo(R.drawable.ic_aptoide_toolbar);
		}
	}

	@Override
	public int getContentViewId() {
		return R.layout.recycler_fragment_with_toolbar;
	}

	@Override
	public void setupViews() {
		super.setupViews();
		setupToolbar();
	}

	@Override
	public void bindViews(View view) {
		super.bindViews(view);
	}
}