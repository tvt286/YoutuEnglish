package youtuvideos.tranty.vn.youtuvideos.singleton;

import android.content.Context;

import java.util.ArrayList;

import youtuvideos.tranty.vn.youtuvideos.dao.knowledges.KnowledgeVO;
import youtuvideos.tranty.vn.youtuvideos.dao.knowledges.favorites.KnowledgesFavoritesVO;
import youtuvideos.tranty.vn.youtuvideos.preferences.UserShared;

/**
 * Created by PC on 5/12/2017.
 */

public class Knowledges {
    protected Context context;
    private static Knowledges instance = null;
    private ArrayList<KnowledgeVO> arrayKnowledges;

    protected Knowledges(Context context) {
        this.context = context;
        arrayKnowledges= new ArrayList<>();
    }

    public static Knowledges ins(Context context) {
        if (instance == null) {
            instance = new Knowledges(context);
        }
        return instance;
    }

    public void setArrayKnowledges(ArrayList<KnowledgeVO> array){
        arrayKnowledges = array;
    }

    public ArrayList<KnowledgeVO> getArrayKnowledges(){
        return arrayKnowledges;
    }


}
