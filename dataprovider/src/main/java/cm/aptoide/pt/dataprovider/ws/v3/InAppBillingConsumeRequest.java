/*
 * Copyright (c) 2016.
 * Modified by Marcelo Benites on 12/08/2016.
 */

package cm.aptoide.pt.dataprovider.ws.v3;

import cm.aptoide.accountmanager.AptoideAccountManager;
import cm.aptoide.accountmanager.ws.BaseBody;
import cm.aptoide.pt.model.v3.BaseV3Response;
import rx.Observable;

/**
 * Created by marcelobenites on 8/12/16.
 */
public class InAppBillingConsumeRequest extends V3<BaseV3Response> {

  private InAppBillingConsumeRequest(String baseHost, BaseBody baseBody) {
    super(baseHost, baseBody);
  }

  public static InAppBillingConsumeRequest of(int apiVersion, String packageName,
      String purchaseToken) {
    BaseBody args = new BaseBody();
    args.put("mode", "json");
    args.put("package", packageName);
    args.put("apiversion", String.valueOf(apiVersion));
    args.put("reqtype", "iabconsume");
    args.put("purchasetoken", purchaseToken);
    args.put("access_token", AptoideAccountManager.getAccessToken());
    return new InAppBillingConsumeRequest(BASE_HOST, args);
  }

  @Override protected Observable<BaseV3Response> loadDataFromNetwork(Interfaces interfaces,
      boolean bypassCache) {
    return interfaces.deleteInAppBillingPurchase(map);
  }
}
