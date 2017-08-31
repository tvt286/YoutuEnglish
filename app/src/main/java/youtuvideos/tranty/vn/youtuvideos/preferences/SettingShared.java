package youtuvideos.tranty.vn.youtuvideos.preferences;

import android.content.Context;

/**
 * Created by PC on 4/1/2017.
 */
public class SettingShared extends SharedPre{
    private static SettingShared instance = null;
    private boolean isFirst;
    private int language_id;
    private int teacher_id;
    private SettingShared(Context context) {
        super(context, "setting");
        restoringPreferences();
    }

    public static SettingShared ins(Context context) {
        if (instance == null) {
            instance = new SettingShared(context);
        }
        return instance;
    }

    public void restoringPreferences() {
        editor = pre.edit();
        isFirst = getIsFirst();
        language_id = getLanguageId();
        teacher_id = getTeacherId();
    }



    public void setIsFirst(boolean isFirst)
    {
        editor = pre.edit();
        editor.putBoolean("isFirst", isFirst);
        editor.commit();
        this.isFirst = isFirst;
    }

    public void setSetting(int languageId, int teacherId)
    {
        editor = pre.edit();
        editor.putInt("language_id", languageId);
        editor.putInt("teacher_id", teacherId);
        editor.commit();
        this.language_id = languageId;
        this.teacher_id = teacherId;
    }


    public int getLanguageId() {
        return pre.getInt("language_id", 1);
    }
    public int getTeacherId() {
        return pre.getInt("teacher_id", 1);
    }

    public boolean getIsFirst() {
        return pre.getBoolean("isFirst", false);
    }
}
