package cm.aptoide.pt.v8engine.repository;

import cm.aptoide.accountmanager.AptoideAccountManager;
import cm.aptoide.pt.dataprovider.DataProvider;
import cm.aptoide.pt.dataprovider.repository.IdsRepositoryImpl;
import cm.aptoide.pt.dataprovider.ws.v7.ListAppsRequest;
import cm.aptoide.pt.preferences.secure.SecurePreferencesImplementation;
import cm.aptoide.pt.v8engine.util.StoreUtils;

/**
 * Created by neuro on 26-12-2016.
 */

public class RequestRepositoty {
  public ListAppsRequest getListAppsFromUrl(String url) {
    return ListAppsRequest.ofAction(url, StoreUtils.getStoreCredentialsFromUrl(url),
        AptoideAccountManager.getAccessToken(), AptoideAccountManager.getUserEmail(),
        new IdsRepositoryImpl(SecurePreferencesImplementation.getInstance(),
            DataProvider.getContext()).getAptoideClientUUID());
  }
}
