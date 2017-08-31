package youtuvideos.tranty.vn.youtuvideos.dao.users.knowledges;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

import youtuvideos.tranty.vn.youtuvideos.dao.knowledges.KnowledgeVO;
import youtuvideos.tranty.vn.youtuvideos.dao.modules.ModuleVO;
import youtuvideos.tranty.vn.youtuvideos.dao.modules.logs.ModuleLogVO;

/**
 * Created by PC on 3/25/2017.
 */
public class KnowledgesUserVO implements Serializable,Parcelable {
    public  int id;
    public String user_id;
    public int knowledge_id;
    public int schedule_hour;
    public int schedule_min;
    public String schedule_days;
    public String schedule_timezone;
    public int point;
    public int completed;
    public int schedule_actived;
    public KnowledgeVO knowledge;
    public ArrayList<ModuleLogVO> logs;
    public ArrayList<ModuleVO> modules;

    protected KnowledgesUserVO(Parcel in) {
        id = in.readInt();
        user_id = in.readString();
        knowledge_id = in.readInt();
        schedule_hour = in.readInt();
        schedule_min = in.readInt();
        schedule_days = in.readString();
        schedule_timezone = in.readString();
        point = in.readInt();
        completed = in.readInt();
        schedule_actived = in.readInt();
    }

    public static final Creator<KnowledgesUserVO> CREATOR = new Creator<KnowledgesUserVO>() {
        @Override
        public KnowledgesUserVO createFromParcel(Parcel in) {
            return new KnowledgesUserVO(in);
        }

        @Override
        public KnowledgesUserVO[] newArray(int size) {
            return new KnowledgesUserVO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(user_id);
        dest.writeInt(knowledge_id);
        dest.writeInt(schedule_hour);
        dest.writeInt(schedule_min);
        dest.writeString(schedule_days);
        dest.writeString(schedule_timezone);
        dest.writeInt(point);
        dest.writeInt(completed);
        dest.writeInt(schedule_actived);
    }
}
