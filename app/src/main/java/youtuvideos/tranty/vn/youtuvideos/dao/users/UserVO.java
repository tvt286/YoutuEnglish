package youtuvideos.tranty.vn.youtuvideos.dao.users;

import java.util.ArrayList;

import youtuvideos.tranty.vn.youtuvideos.dao.knowledges.KnowledgeVO;
import youtuvideos.tranty.vn.youtuvideos.dao.users.knowledges.KnowledgesUserVO;

/**
 * Created by PC on 3/25/2017.
 */
public class UserVO {
    public String id;
    public String reg_id;
    public int num_knowledges;
    public int num_favorites;
    public String name;
    public String image;
    public int language_id;
    public int teacher_id;
    public ArrayList<KnowledgesUserVO> knowledges_user;
}
