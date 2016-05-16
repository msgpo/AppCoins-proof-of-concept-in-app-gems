/*
 * Copyright (c) 2016.
 * Modified by SithEngineer on 12/05/2016.
 */

package cm.aptoide.pt.v8engine.fragment.implementations;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Locale;

import cm.aptoide.pt.actions.Action1WithWeakRef;
import cm.aptoide.pt.dataprovider.ws.v7.GetAppRequest;
import cm.aptoide.pt.model.v7.GetApp;
import cm.aptoide.pt.model.v7.GetAppMeta;
import cm.aptoide.pt.utils.ObservableUtils;
import cm.aptoide.pt.utils.ShowMessage;
import cm.aptoide.pt.v8engine.R;
import cm.aptoide.pt.v8engine.V8Engine;
import cm.aptoide.pt.v8engine.fragment.GridRecyclerFragment;
import cm.aptoide.pt.v8engine.view.recycler.DisplayableType;
import rx.Observable;

/**
 * Created by sithengineer on 04/05/16.
 */
public class AppViewFragment extends GridRecyclerFragment {

	//
	// constants
	//

	public static final int VIEW_ID = R.layout.fragment_app_view;
	//private static final String TAG = AppViewFragment.class.getName();

	//
	// vars
	//
	private AppViewHeader header;
	private GetAppMeta.App app;
	private long appId;

	//
	// static fragment default new instance method
	//

	public static AppViewFragment newInstance(long appId) {
		Bundle bundle = new Bundle();
		bundle.putLong(BundleKeys.APP_ID.name(), appId);

		AppViewFragment fragment = new AppViewFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void load(boolean refresh) {
		if (refresh) {
			loadAppInfo((int) appId)
					.compose(ObservableUtils.applySchedulers())
					.subscribe(
						new Action1WithWeakRef<GetApp, AppViewFragment>(this) {
						@Override
						public void call(GetApp pojo) {
							AppViewFragment fragment = weakReference.get();
							if(fragment!=null) {
								fragment.setApp(pojo.getNodes().getMeta().getData());
								fragment.showAppInfo();
							}
						}
					});
		}
	}

	@Override
	public void bindViews(View view) {
		super.bindViews(view);
		header = new AppViewHeader(view);
		setHasOptionsMenu(true);
	}

	@Override
	public void setupViews() {
		super.setupViews();
		this.showAppInfo();

		final AppCompatActivity parentActivity = (AppCompatActivity) getActivity();
		ActionBar supportActionBar = parentActivity.getSupportActionBar();
		if (supportActionBar != null) {
			supportActionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	@Override
	public int getContentViewId() {
		return VIEW_ID;
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_appview_fragment, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int i = item.getItemId();

		if (i == android.R.id.home) {
			getActivity().onBackPressed();
			return true;

		} else if (i == R.id.menu_share) {
			ShowMessage.show(item.getActionView(), "TO DO");

			// TODO

			return true;

		} else if (i == R.id.menu_schedule) {
			ShowMessage.show(item.getActionView(), "TO DO");

			// TODO
			return true;

		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void loadExtras(Bundle args) {
		super.loadExtras(args);
		appId = args.getLong(BundleKeys.APP_ID.name());
	}

	private Observable<GetApp> loadAppInfo(int appId) {
		return GetAppRequest.of(appId).observe();
	}

	private void setApp(GetAppMeta.App app) {
		this.app = app;
	}

	private void showAppInfo() {
		if(app==null) return;

		// setup displayables in view
		addDisplayables(DisplayableType.newDisplayables(DisplayableType.Group.APP_VIEW, app));

		// setup header in view
		header.setup(app);
	}

	//
	// bundle keys used internally in this fragment
	//

	private enum BundleKeys {
		APP_ID
	}

	//
	// micro widget for header
	//

	private static final class AppViewHeader {

		// views
		private CollapsingToolbarLayout collapsingToolbar;
		private ImageView featuredGraphic;
		private RelativeLayout badgeLayout;
		private ImageView badge;
		private TextView badgeText;
		private ImageView appIcon;
		private RatingBar ratingBar;
		private TextView fileSize;
		private TextView versionName;
		private TextView downloadsCount;

		// ctor
		public AppViewHeader(@NonNull View view) {
			collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
			featuredGraphic = (ImageView) view.findViewById(R.id.featured_graphic);
			badgeLayout = (RelativeLayout) view.findViewById(R.id.badge_layout);
			badge = (ImageView) view.findViewById(R.id.badge_img);
			badgeText = (TextView) view.findViewById(R.id.badge_text);
			appIcon = (ImageView) view.findViewById(R.id.app_icon);
			ratingBar = (RatingBar) view.findViewById(R.id.rating_bar_top);
			fileSize = (TextView) view.findViewById(R.id.file_size);
			versionName = (TextView) view.findViewById(R.id.version_name);
			downloadsCount = (TextView) view.findViewById(R.id.downloads_count);
		}

		// setup methods
		public void setup(@NonNull GetAppMeta.App pojo) {

			if (pojo.getGraphic() != null) {
				Glide.with(V8Engine.getContext()).load(pojo.getGraphic()).into(featuredGraphic);
			}
			/*
			else if (screenshots != null && screenshots.size() > 0 && !TextUtils.isEmpty
			(screenshots.get(0).url)) {
				Glide.with(V8Engine.getContext()).load(screenshots.get(0).url).into
				(mFeaturedGraphic);
			}
			*/

			if (pojo.getIcon() != null) {
				Glide.with(V8Engine.getContext()).load(pojo.getIcon()).into(appIcon);
			}

			// TODO add placeholders in image loading

			collapsingToolbar.setTitle(pojo.getName());
			ratingBar.setRating(pojo.getStats().getRating().getAvg());
			fileSize.setText(String.format(Locale.ROOT, "%d", pojo.getFile().getFilesize()));
			versionName.setText(pojo.getFile().getVername());
			downloadsCount.setText(String.format(Locale.ROOT, "%d", pojo.getStats()
					.getDownloads()));

			@DrawableRes int badgeResId = 0;
			@StringRes int badgeMessageId = 0;
			switch (pojo.getFile().getMalware().getRank()) {
				case GetAppMeta.GetAppMetaFile.Malware.TRUSTED:
					badgeResId = R.drawable.ic_badge_trusted;
					badgeMessageId = R.string.appview_header_trusted_text;
					break;

				case GetAppMeta.GetAppMetaFile.Malware.WARNING:
					badgeResId = R.drawable.ic_badge_warning;
					badgeMessageId = R.string.warning;
					break;

				default:
				case GetAppMeta.GetAppMetaFile.Malware.UNKNOWN:
					badgeResId = R.drawable.ic_badge_unknown;
					badgeMessageId = R.string.unknown;
					break;
			}

			Glide.with(V8Engine.getContext()).load(badgeResId).into(badge);
			badgeText.setText(badgeMessageId);
		}

	}
}
