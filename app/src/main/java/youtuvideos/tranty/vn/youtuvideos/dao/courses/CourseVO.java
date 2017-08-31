package youtuvideos.tranty.vn.youtuvideos.dao.courses;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.Serializable;


public class CourseVO implements Serializable {
    public int id;
    public String title;
    public String channel;
    public String image;
    public int language_id;
    public String discription;
    public int likes;
    public boolean isLike = false;
}
