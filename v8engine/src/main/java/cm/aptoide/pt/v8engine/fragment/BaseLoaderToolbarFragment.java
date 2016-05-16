/*
 * Copyright (c) 2016.
 * Modified by SithEngineer on 12/05/2016.
 */

package cm.aptoide.pt.v8engine.fragment;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import cm.aptoide.pt.v8engine.R;

/**
 * Created by neuro on 06-05-2016.
 */
public abstract class BaseLoaderToolbarFragment extends BaseLoaderFragment {

	protected Toolbar toolbar;

	@Override
	public void setupViews() {
		setupToolbar();
	}

	/**
	 * Setup the toolbar, if present.
	 */
	public void setupToolbar() {
		if (toolbar != null) {
			((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
		}
	}

	@Override
	public void bindViews(View view) {
		super.bindViews(view);
		toolbar = (Toolbar) view.findViewById(R.id.toolbar);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		toolbar = null;
	}
}