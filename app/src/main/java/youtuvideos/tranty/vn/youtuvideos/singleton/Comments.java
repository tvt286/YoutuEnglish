package youtuvideos.tranty.vn.youtuvideos.singleton;

import android.content.Context;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

import youtuvideos.tranty.vn.youtuvideos.dao.comments.CommentVO;
import youtuvideos.tranty.vn.youtuvideos.dao.knowledges.KnowledgeVO;

/**
 * Created by PC on 5/12/2017.
 */

public class Comments {
    protected Context context;
    private static Comments instance = null;
    private Dictionary<Integer,ArrayList<CommentVO>> arrayComments;

    protected Comments(Context context) {
        this.context = context;
        arrayComments= new Hashtable<>();
    }

    public static Comments ins(Context context) {
        if (instance == null) {
            instance = new Comments(context);
        }
        return instance;
    }

    public void setArrayComments(int id,ArrayList<CommentVO> array){
        arrayComments.put(id,array);
    }

    public ArrayList<CommentVO> getArrayComments(int id){
        return arrayComments.get(id);
    }


}
