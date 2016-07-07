/*
 * Copyright (c) 2016.
 * Modified by SithEngineer on 04/07/2016.
 */

package cm.aptoide.pt.v8engine.view.recycler.displayable.implementations.appView;

import cm.aptoide.pt.model.v7.GetApp;
import cm.aptoide.pt.model.v7.Type;
import cm.aptoide.pt.v8engine.R;

/**
 * Created by sithengineer on 30/06/16.
 */
public class AppViewRateAndCommentsDisplayable extends AppViewDisplayable {

	public AppViewRateAndCommentsDisplayable() {
	}

	public AppViewRateAndCommentsDisplayable(GetApp getApp) {
		super(getApp);
	}

	public AppViewRateAndCommentsDisplayable(GetApp getApp, boolean fixedPerLineCount) {
		super(getApp, fixedPerLineCount);
	}

	@Override
	public Type getType() {
		return Type.APP_VIEW_RATE_AND_COMMENT;
	}

	@Override
	public int getViewLayout() {
		return R.layout.displayable_app_view_rate_and_comment;
	}
}