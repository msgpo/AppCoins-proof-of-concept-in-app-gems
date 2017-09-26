package cm.aptoide.pt.view.search;

import cm.aptoide.pt.database.realm.MinimalAd;
import cm.aptoide.pt.dataprovider.model.v7.search.SearchApp;
import cm.aptoide.pt.presenter.View;
import java.util.List;
import rx.Observable;

public interface SearchView extends View {
  void showFollowedStoresResult();

  void showAllStoresResult();

  Observable<Void> clickFollowedStoresSearchButton();

  Observable<Void> clickEverywhereSearchButton();

  Observable<String> clickNoResultsSearchButton();

  void showNoResultsImage();

  void showResultsLayout();

  void showLoading();

  void hideLoading();

  void changeFollowedStoresButtonVisibility(boolean visible);

  void changeAllStoresButtonVisibility(boolean visible);

  void addFollowedStoresResult(List<SearchApp> dataList);

  void addAllStoresResult(List<SearchApp> dataList);

  Model getViewModel();

  void addFollowedStoresAdsResult(List<MinimalAd> ads);

  void addAllStoresAdsResult(List<MinimalAd> ads);

  void setFollowedStoresAdsEmpty();

  void setAllStoresAdsEmpty();

  void showPopup(boolean hasVersions, String appName, String appIcon, String packageName,
      String storeName, String theme, android.view.View anchor);

  String getDefaultTheme();

  interface Model {

    String getCurrentQuery();

    String getStoreName();

    boolean isOnlyTrustedApps();

    boolean isAllStoresSelected();
  }
}
