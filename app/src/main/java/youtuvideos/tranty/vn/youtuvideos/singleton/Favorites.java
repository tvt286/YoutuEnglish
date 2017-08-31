package youtuvideos.tranty.vn.youtuvideos.singleton;

import android.content.Context;

import java.util.ArrayList;

import youtuvideos.tranty.vn.youtuvideos.dao.knowledges.KnowledgeVO;
import youtuvideos.tranty.vn.youtuvideos.dao.knowledges.favorites.KnowledgesFavoritesVO;

/**
 * Created by PC on 5/12/2017.
 */

public class Favorites {
    protected Context context;
    private static Favorites instance = null;
    private ArrayList<KnowledgesFavoritesVO> arrayFavorites;

    protected Favorites(Context context) {
        this.context = context;
        arrayFavorites= new ArrayList<>();
    }

    public static Favorites ins(Context context) {
        if (instance == null) {
            instance = new Favorites(context);
        }
        return instance;
    }

    public void setArrayFavorites(ArrayList<KnowledgesFavoritesVO> array){
        arrayFavorites = array;
    }

    public ArrayList<KnowledgesFavoritesVO> getArrayFavorites(){
        return arrayFavorites;
    }


}
