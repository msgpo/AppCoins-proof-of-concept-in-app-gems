package cm.aptoide.pt.v8engine.repository.request;

import cm.aptoide.accountmanager.AptoideAccountManager;
import cm.aptoide.pt.dataprovider.ws.v7.BodyInterceptor;
import cm.aptoide.pt.dataprovider.ws.v7.store.GetStoreWidgetsRequest;
import cm.aptoide.pt.v8engine.interfaces.StoreCredentialsProvider;

/**
 * Created by neuro on 03-01-2017.
 */
class GetStoreWidgetsRequestFactory {

  private final AptoideAccountManager accountManager;
  private final StoreCredentialsProvider storeCredentialsProvider;
  private final BodyInterceptor bodyInterceptor;

  public GetStoreWidgetsRequestFactory(AptoideAccountManager accountManager,
      StoreCredentialsProvider storeCredentialsProvider, BodyInterceptor bodyInterceptor) {
    this.accountManager = accountManager;
    this.storeCredentialsProvider = storeCredentialsProvider;
    this.bodyInterceptor = bodyInterceptor;
  }

  public GetStoreWidgetsRequest newStoreWidgets(String url) {
    return GetStoreWidgetsRequest.ofAction(url, storeCredentialsProvider.fromUrl(url),
        accountManager.getAccessToken(), bodyInterceptor);
  }
}
