package youtuvideos.tranty.vn.youtuvideos.dao.modules.logs;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by PC on 4/5/2017.
 */
public class ModuleLogVO implements Serializable,Parcelable {
    public int id;
    public int knowledge_user_id;
    public int module_id;
    public int view_timestamp;

    protected ModuleLogVO(Parcel in) {
        id = in.readInt();
        knowledge_user_id = in.readInt();
        module_id = in.readInt();
        view_timestamp = in.readInt();
    }

    public static final Creator<ModuleLogVO> CREATOR = new Creator<ModuleLogVO>() {
        @Override
        public ModuleLogVO createFromParcel(Parcel in) {
            return new ModuleLogVO(in);
        }

        @Override
        public ModuleLogVO[] newArray(int size) {
            return new ModuleLogVO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(knowledge_user_id);
        dest.writeInt(module_id);
        dest.writeInt(view_timestamp);
    }
}
