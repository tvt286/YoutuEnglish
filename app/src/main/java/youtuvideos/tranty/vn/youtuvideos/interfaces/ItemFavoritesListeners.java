package youtuvideos.tranty.vn.youtuvideos.interfaces;

import android.view.View;

/**
 * Created by TRUC-SIDA on 2/13/2017.
 */

public interface ItemFavoritesListeners {
    void onCommentsClick(View v, int position);
    void onItemClick(View v, int position);
    void onDeleteClick(View v, int position);
}
