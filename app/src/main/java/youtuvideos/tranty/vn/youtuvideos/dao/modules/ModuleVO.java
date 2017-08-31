package youtuvideos.tranty.vn.youtuvideos.dao.modules;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by PC on 3/29/2017.
 */
public class ModuleVO implements Serializable, Parcelable{
    public int id;
    public int knowledge_id;
    public String title;
    public String image;
    public String youtube_id;
    public int length;
    public int completed;

    protected ModuleVO(Parcel in) {
        id = in.readInt();
        knowledge_id = in.readInt();
        title = in.readString();
        image = in.readString();
        youtube_id = in.readString();
        length = in.readInt();
        completed = in.readInt();
    }

    public static final Creator<ModuleVO> CREATOR = new Creator<ModuleVO>() {
        @Override
        public ModuleVO createFromParcel(Parcel in) {
            return new ModuleVO(in);
        }

        @Override
        public ModuleVO[] newArray(int size) {
            return new ModuleVO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(knowledge_id);
        dest.writeString(title);
        dest.writeString(image);
        dest.writeString(youtube_id);
        dest.writeInt(length);
        dest.writeInt(completed);
    }
}
