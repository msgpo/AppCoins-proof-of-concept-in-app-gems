package cm.aptoide.pt.v8engine.social.view.viewholder;

import android.support.v4.util.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cm.aptoide.pt.dataprovider.model.v7.listapp.App;
import cm.aptoide.pt.v8engine.R;
import cm.aptoide.pt.v8engine.networking.image.ImageLoader;
import cm.aptoide.pt.v8engine.social.data.CardTouchEvent;
import cm.aptoide.pt.v8engine.social.data.SocialStore;
import cm.aptoide.pt.v8engine.social.data.StoreAppCardTouchEvent;
import cm.aptoide.pt.v8engine.util.DateCalculator;
import cm.aptoide.pt.v8engine.view.recycler.displayable.SpannableFactory;
import java.util.HashMap;
import java.util.Map;
import rx.subjects.PublishSubject;

/**
 * Created by jdandrade on 28/06/2017.
 */

public class SocialStoreViewHolder extends CardViewHolder<SocialStore> {
  private final PublishSubject<CardTouchEvent> cardTouchEventPublishSubject;
  private final DateCalculator dateCalculator;
  private final SpannableFactory spannableFactory;
  private final LayoutInflater inflater;
  private final ImageView headerPrimaryAvatar;
  private final ImageView headerSecondaryAvatar;
  private final TextView headerPrimaryName;
  private final TextView headerSecondaryName;
  private final TextView timestamp;
  private final TextView storeNameBodyHeader;
  private final LinearLayout appsContainer;
  private final TextView storeNameFollow;
  private final ImageView storeAvatarFollow;
  private final TextView storeNumberFollowers;
  private final TextView storeNumberApps;
  private final Button followStoreButton;

  public SocialStoreViewHolder(View view,
      PublishSubject<CardTouchEvent> cardTouchEventPublishSubject, DateCalculator dateCalculator,
      SpannableFactory spannableFactory) {
    super(view);
    this.inflater = LayoutInflater.from(itemView.getContext());
    this.cardTouchEventPublishSubject = cardTouchEventPublishSubject;
    this.dateCalculator = dateCalculator;
    this.spannableFactory = spannableFactory;

    this.headerPrimaryAvatar = (ImageView) view.findViewById(R.id.card_image);
    this.headerSecondaryAvatar = (ImageView) view.findViewById(R.id.card_user_avatar);
    this.headerPrimaryName = (TextView) view.findViewById(R.id.card_title);
    this.headerSecondaryName = (TextView) view.findViewById(R.id.card_subtitle);
    this.timestamp = (TextView) view.findViewById(R.id.card_date);
    this.storeNameBodyHeader = (TextView) view.findViewById(R.id.social_shared_store_name);
    this.appsContainer = (LinearLayout) view.findViewById(
        R.id.displayable_social_timeline_store_latest_apps_container);
    this.storeAvatarFollow = (ImageView) view.findViewById(R.id.social_shared_store_avatar);
    this.storeNameFollow = (TextView) view.findViewById(R.id.store_name);
    this.storeNumberFollowers = (TextView) view.findViewById(R.id.social_number_of_followers_text);
    this.storeNumberApps = (TextView) view.findViewById(R.id.social_number_of_apps_text);
    this.followStoreButton = (Button) view.findViewById(R.id.follow_btn);
  }

  @Override public void setCard(SocialStore card, int position) {
    ImageLoader.with(itemView.getContext())
        .loadWithShadowCircleTransform(card.getPoster()
            .getPrimaryAvatar(), this.headerPrimaryAvatar);
    ImageLoader.with(itemView.getContext())
        .loadWithShadowCircleTransform(card.getPoster()
            .getSecondaryAvatar(), this.headerSecondaryAvatar);
    this.headerPrimaryName.setText(card.getPoster()
        .getPrimaryName());
    this.headerSecondaryName.setText(card.getPoster()
        .getSecondaryName());
    this.timestamp.setText(
        dateCalculator.getTimeSinceDate(itemView.getContext(), card.getLatestUpdate()));
    this.storeNameBodyHeader.setText(card.getStoreName());
    ImageLoader.with(itemView.getContext())
        .load(card.getStoreAvatar(), storeAvatarFollow);
    this.storeNameFollow.setText(card.getStoreName());
    this.storeNumberFollowers.setText(card.getSubscribers());
    this.storeNumberApps.setText(card.getAppsNumber());
    showStoreLatestApps(card);
  }

  private void showStoreLatestApps(SocialStore card) {
    Map<View, Long> apps = new HashMap<>();
    LongSparseArray<String> appsPackages = new LongSparseArray<>();

    appsContainer.removeAllViews();
    View latestAppView;
    ImageView latestAppIcon;
    TextView latestAppName;
    for (App latestApp : card.getApps()) {
      latestAppView = inflater.inflate(R.layout.social_timeline_latest_app, appsContainer, false);
      latestAppIcon = (ImageView) latestAppView.findViewById(R.id.social_timeline_latest_app_icon);
      latestAppName = (TextView) latestAppView.findViewById(R.id.social_timeline_latest_app_name);
      ImageLoader.with(itemView.getContext())
          .load(latestApp.getIcon(), latestAppIcon);
      latestAppName.setText(latestApp.getName());
      appsContainer.addView(latestAppView);
      apps.put(latestAppView, latestApp.getId());
      appsPackages.put(latestApp.getId(), latestApp.getPackageName());
    }

    setStoreLatestAppsListeners(card, apps, appsPackages);
  }

  private void setStoreLatestAppsListeners(SocialStore card, Map<View, Long> apps,
      LongSparseArray<String> appsPackages) {
    for (View app : apps.keySet()) {
      app.setOnClickListener(click -> cardTouchEventPublishSubject.onNext(
          new StoreAppCardTouchEvent(card, CardTouchEvent.Type.BODY,
              appsPackages.get(apps.get(app)))));
    }
  }
}
