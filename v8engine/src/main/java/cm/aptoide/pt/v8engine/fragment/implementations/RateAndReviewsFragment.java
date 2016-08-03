/*
 * Copyright (c) 2016.
 * Modified by SithEngineer on 03/08/2016.
 */

package cm.aptoide.pt.v8engine.fragment.implementations;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import cm.aptoide.pt.database.Database;
import cm.aptoide.pt.database.realm.Installed;
import cm.aptoide.pt.dataprovider.ws.v7.GetAppRequest;
import cm.aptoide.pt.dataprovider.ws.v7.ListFullReviewsRequest;
import cm.aptoide.pt.logger.Logger;
import cm.aptoide.pt.model.v7.FullReview;
import cm.aptoide.pt.model.v7.GetAppMeta;
import cm.aptoide.pt.utils.AptoideUtils;
import cm.aptoide.pt.v8engine.R;
import cm.aptoide.pt.v8engine.fragment.GridRecyclerFragment;
import cm.aptoide.pt.v8engine.interfaces.FragmentShower;
import cm.aptoide.pt.v8engine.view.recycler.displayable.Displayable;
import cm.aptoide.pt.v8engine.view.recycler.displayable.implementations.grid.RateAndReviewCommentDisplayable;
import io.realm.Realm;
import lombok.Cleanup;
import rx.Subscription;

/**
 * Created by sithengineer on 13/05/16.
 */
public class RateAndReviewsFragment extends GridRecyclerFragment {

	private static final String TAG = RateAndReviewsFragment.class.getSimpleName();

	private static final String APP_ID = "app_id";
	private static final String PACKAGE_NAME = "package_name";
	private static final String STORE_NAME = "store_name";
	private static final String REVIEW_ID = "review_id";
	private long appId;
	private String packageName;
	private String storeName;
	private long reviewId;

	private TextView emptyData;
	private Subscription subscription;

	private MenuItem installButton;
	private RatingTotalsLayout ratingTotalsLayout;
	private RatingBarsLayout ratingBarsLayout;

	private ProgressBar progressBar;
	private MenuItem installMenuItem;

	public static RateAndReviewsFragment newInstance(long appId, String storeName, String packageName) {
		RateAndReviewsFragment fragment = new RateAndReviewsFragment();
		Bundle args = new Bundle();
		args.putLong(APP_ID, appId);
		args.putString(STORE_NAME, storeName);
		args.putString(PACKAGE_NAME, packageName);
		fragment.setArguments(args);
		return fragment;
	}

	public static RateAndReviewsFragment newInstance(long appId, String storeName, String packageName, long reviewId) {
		RateAndReviewsFragment fragment = new RateAndReviewsFragment();
		Bundle args = new Bundle();
		args.putLong(APP_ID, appId);
		args.putString(STORE_NAME, storeName);
		args.putString(PACKAGE_NAME, packageName);
		args.putLong(REVIEW_ID, reviewId);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void loadExtras(Bundle args) {
		super.loadExtras(args);
		appId = args.getLong(APP_ID);
		packageName = args.getString(PACKAGE_NAME);
		storeName = args.getString(STORE_NAME);
		reviewId = args.getLong(REVIEW_ID, -1);
	}

	@Override
	public void load(boolean refresh, Bundle savedInstanceState) {
		Logger.d(TAG, "Other versions should refresh? " + refresh);
		fetchRating(refresh);
		fetchReviews();
	}

	@Override
	public int getContentViewId() {
		return R.layout.fragment_rate_and_reviews;
	}

	@Override
	public void bindViews(View view) {
		super.bindViews(view);
		emptyData = (TextView) view.findViewById(R.id.empty_data);
		setHasOptionsMenu(true);

		ratingTotalsLayout = new RatingTotalsLayout(view);
		ratingBarsLayout = new RatingBarsLayout(view);
		progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
	}

	@Override
	public void setupToolbar() {
		super.setupToolbar();
		if (toolbar != null) {
			ActionBar bar = ((AppCompatActivity) getActivity()).getSupportActionBar();
			bar.setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.menu_install, menu);
		installMenuItem = menu.findItem(R.id.menu_install);

		@Cleanup
		Realm realm = Database.get();
		Installed installed = Database.InstalledQ.get(packageName, realm);

		//check if the app is installed
		if (installed != null) {
			// app installed... update text
			installMenuItem.setTitle(R.string.open);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			getActivity().onBackPressed();
			return true;
		}
		if (itemId == R.id.menu_install) {
			((FragmentShower) getContext()).pushFragmentV4(AppViewFragment.newInstance(appId));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void fetchRating(boolean refresh) {
		GetAppRequest.of(appId).execute(getApp -> {
			GetAppMeta.App data = getApp.getNodes().getMeta().getData();
			setupTitle(data.getName());
			setupRating(data);
			finishLoading();
		}, refresh);
	}

	private void setupRating(GetAppMeta.App data) {
		ratingTotalsLayout.setup(data);
		ratingBarsLayout.setup(data);
	}

	private void fetchReviews() {
		ListFullReviewsRequest.of(storeName, packageName).execute(reviewsResponse -> {
			List<FullReview> reviews = reviewsResponse.getDatalist().getList();
			List<Displayable> displayables = new LinkedList<>();
			int index = 0;
			int count = 0;
			for (final FullReview review : reviews) {
				displayables.add(new RateAndReviewCommentDisplayable(review));
				if (review.getId() == reviewId) {
					index = count;
				}
				count++;
			}
			setDisplayables(displayables);
			finishLoading();
			progressBar.setVisibility(View.GONE);
			recyclerView.scrollToPosition(index);
			installMenuItem.setVisible(reviewId >= 0);
		});
	}

	public void setupTitle(String title) {
		super.setupToolbar();
		if (toolbar != null) {
			ActionBar bar = ((AppCompatActivity) getActivity()).getSupportActionBar();
			bar.setTitle(title);
		}
	}

	private static class RatingTotalsLayout {

		private TextView usersVoted;
		private TextView ratingValue;
		private AppCompatRatingBar ratingBar;

		public RatingTotalsLayout(View view) {
			usersVoted = (TextView) view.findViewById(R.id.users_voted);
			ratingValue = (TextView) view.findViewById(R.id.rating_value);
			ratingBar = (AppCompatRatingBar) view.findViewById(R.id.rating_bar);
		}

		public void setup(GetAppMeta.App data) {
			GetAppMeta.Stats stats = data.getStats();
			usersVoted.setText(AptoideUtils.StringU.withSuffix(stats.getDownloads()));
			ratingValue.setText(String.format(Locale.getDefault(), "%.1f", stats.getRating().getAvg()));
			ratingBar.setRating(stats.getRating().getAvg());
		}
	}

	private static class RatingBarsLayout {

		private ProgressAndTextLayout[] progressAndTextLayouts;

		public RatingBarsLayout(View view) {
			progressAndTextLayouts = new ProgressAndTextLayout[5];
			progressAndTextLayouts[0] = new ProgressAndTextLayout(R.id.one_rate_star_progress, R.id.one_rate_star_count, view);
			progressAndTextLayouts[1] = new ProgressAndTextLayout(R.id.two_rate_star_progress, R.id.two_rate_star_count, view);
			progressAndTextLayouts[2] = new ProgressAndTextLayout(R.id.three_rate_star_progress, R.id.three_rate_star_count, view);
			progressAndTextLayouts[3] = new ProgressAndTextLayout(R.id.four_rate_star_progress, R.id.four_rate_star_count, view);
			progressAndTextLayouts[4] = new ProgressAndTextLayout(R.id.five_rate_star_progress, R.id.five_rate_star_count, view);
		}

		public void setup(GetAppMeta.App data) {
			GetAppMeta.Stats.Rating rating = data.getStats().getRating();
			final int total = rating.getTotal();
			for (final GetAppMeta.Stats.Rating.Vote vote : rating.getVotes()) {
				progressAndTextLayouts[vote.getValue() - 1].setup(total, vote.getCount());
			}
		}
	}

	private static class ProgressAndTextLayout {

		private ProgressBar progressBar;
		private TextView text;

		public ProgressAndTextLayout(int progressId, int textId, View view) {
			progressBar = (ProgressBar) view.findViewById(progressId);
			text = (TextView) view.findViewById(textId);
		}

		public void setup(int total, int count) {
			progressBar.setMax(total);
			progressBar.setProgress(count);
			text.setText(AptoideUtils.StringU.withSuffix(count));
		}
	}
}
