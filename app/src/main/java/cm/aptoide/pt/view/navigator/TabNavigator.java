package cm.aptoide.pt.view.navigator;

import rx.Observable;

/**
 * Created by marcelobenites on 01/03/17.
 */
public interface TabNavigator {

  void navigate(TabNavigation tabNavigation);

  Observable<TabNavigation> navigation();

  void clearNavigation();
}
