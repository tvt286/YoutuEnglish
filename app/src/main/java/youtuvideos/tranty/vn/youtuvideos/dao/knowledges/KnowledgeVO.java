package youtuvideos.tranty.vn.youtuvideos.dao.knowledges;

import java.io.Serializable;
import java.util.ArrayList;

import youtuvideos.tranty.vn.youtuvideos.dao.modules.ModuleVO;

/**
 * Created by PC on 3/5/2017.
 */
public class KnowledgeVO implements Serializable {
    public int id;
    public int language_id;
    public int teacher_id;
    public String title;
    public String image;
    public String description;
    public int total_video;
    public int isLike;
    public int likes;
    public int comments;
    public int registers;
    public ArrayList<ModuleVO> modules;

}
