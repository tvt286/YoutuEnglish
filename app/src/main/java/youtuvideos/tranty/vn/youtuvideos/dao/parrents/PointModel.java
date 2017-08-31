package youtuvideos.tranty.vn.youtuvideos.dao.parrents;

/**
 * Created by TRUC-SIDA on 4/5/2017.
 */

public class PointModel {
    public long date;
    public int point = 0;

    public PointModel(long date, int point) {
        this.date = date;
        this.point = point;
    }

    public PointModel() {
    }
}