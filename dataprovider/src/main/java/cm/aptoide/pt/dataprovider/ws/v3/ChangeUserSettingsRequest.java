package cm.aptoide.pt.dataprovider.ws.v3;

import android.text.TextUtils;
import cm.aptoide.pt.model.v3.BaseV3Response;
import cm.aptoide.pt.networkclient.okhttp.OkHttpClientFactory;
import cm.aptoide.pt.preferences.secure.SecurePreferences;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;

public class ChangeUserSettingsRequest extends V3<BaseV3Response> {

  public static final String ACCESS_TOKEN = "access_token";
  public static final String ACTIVE = "active";
  public static final String INACTIVE = "inactive";

  public ChangeUserSettingsRequest(String baseHost, BaseBody baseBody) {
    super(baseHost, baseBody,
        OkHttpClientFactory.getSingletonClient(() -> SecurePreferences.getUserAgent(), isDebug()));
  }

  public static ChangeUserSettingsRequest of(boolean matureSwitchStatus, String accessToken) {
    BaseBody body = new BaseBody();
    body.put("mode", "json");
    List<String> list = new ArrayList<>();
    list.add("matureswitch=" + (matureSwitchStatus ? ACTIVE : INACTIVE));
    body.put("settings", TextUtils.join(",", list));

    if (TextUtils.isEmpty(accessToken)) {
      body.put(ACCESS_TOKEN, accessToken);
    }
    return new ChangeUserSettingsRequest(BASE_HOST, body);
  }

  @Override protected Observable<BaseV3Response> loadDataFromNetwork(Interfaces interfaces,
      boolean bypassCache) {
    return interfaces.changeUserSettings(map, bypassCache);
  }
}
