package cm.aptoide.pt.view.search.result;

import android.view.View;
import android.widget.ProgressBar;
import cm.aptoide.pt.R;
import cm.aptoide.pt.view.ItemView;

public class SearchLoadingViewHolder extends ItemView<Void> {

  public static final int LAYOUT = R.layout.search_ad_loading_list_item;

  private ProgressBar progressBar;

  public SearchLoadingViewHolder(View itemView) {
    super(itemView);
    this.progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);
  }

  @Override public void setup(Void item) {

  }
}
