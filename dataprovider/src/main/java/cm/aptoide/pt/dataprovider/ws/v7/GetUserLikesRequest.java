package cm.aptoide.pt.dataprovider.ws.v7;

import cm.aptoide.pt.model.v7.GetFollowers;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import rx.Observable;

/**
 * Created by jdandrade on 10/01/2017.
 */

public class GetUserLikesRequest extends V7<GetFollowers, GetUserLikesRequest.Body> {
  protected GetUserLikesRequest(Body body, String baseHost) {
    super(body, baseHost);
  }

  public static GetUserLikesRequest of(String cardUid, BodyInterceptor bodyInterceptor) {

    return new GetUserLikesRequest(((Body) bodyInterceptor.intercept(new Body(cardUid))),
        BASE_HOST);
  }

  @Override protected Observable<GetFollowers> loadDataFromNetwork(Interfaces interfaces,
      boolean bypassCache) {
    return interfaces.getCardUserLikes(body, bypassCache);
  }

  @EqualsAndHashCode(callSuper = true) public static class Body extends BaseBody
      implements Endless {

    private int limit = 25;
    private int offset;
    @Getter private String cardUid;

    public Body(String cardUid) {
      super();
      this.cardUid = cardUid;
    }

    @Override public int getOffset() {
      return offset;
    }

    @Override public void setOffset(int offset) {
      this.offset = offset;
    }

    @Override public Integer getLimit() {
      return limit;
    }
  }
}
