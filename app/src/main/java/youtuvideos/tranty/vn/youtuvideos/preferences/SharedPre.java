package youtuvideos.tranty.vn.youtuvideos.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by TranTy on 5/16/2016.
 */
public class SharedPre {
    protected Context context;
    protected SharedPreferences.Editor editor;
    protected SharedPreferences pre;

    protected SharedPre(Context context, String preference_key) {
        this.context = context;
        pre = context.getSharedPreferences(preference_key, context.MODE_PRIVATE);
    }

    public void clear()
    {
        editor = pre.edit();
        editor.clear();
        editor.commit();
    }

}
