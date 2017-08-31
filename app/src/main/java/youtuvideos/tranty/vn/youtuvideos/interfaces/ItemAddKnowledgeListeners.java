package youtuvideos.tranty.vn.youtuvideos.interfaces;

import android.view.View;

/**
 * Created by TRUC-SIDA on 2/13/2017.
 */

public interface ItemAddKnowledgeListeners {
    void onCommentsClick(View v, int position);

    void onLikesClick(View v, int position, boolean isLike);

    void onImageClick(View v, int position);
}
