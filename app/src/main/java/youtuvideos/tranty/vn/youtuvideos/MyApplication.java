package youtuvideos.tranty.vn.youtuvideos;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

import youtuvideos.tranty.vn.youtuvideos.dao.comments.CommentVO;
import youtuvideos.tranty.vn.youtuvideos.dao.modules.ModuleVO;

public class MyApplication extends Application {

    private static MyApplication appInstance;
    private boolean favoritesChange; // nếu như favorite có thay doi thi load lai
    private boolean userChange;
    private Dictionary<Integer,ArrayList<ModuleVO>> arrModules;

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        arrModules = new Hashtable<>();
        favoritesChange= false;
        userChange = false;
     }


    public static MyApplication getAppInstance() {
        return appInstance;
    }


    public  void setFavoritesChange(boolean change){
        this.favoritesChange = change;
    }

    public boolean getFavoritesChange(){
        return favoritesChange;
    }

    public void setArrModules(int id,ArrayList<ModuleVO> arr){
        arrModules.put(id,arr);
    }

    public ArrayList<ModuleVO> getArrModules(int id){
        return arrModules.get(id);
    }

    public void setUserChange(boolean isChange){
        userChange = isChange;
    }

    public boolean getUserChange(){
        return userChange;
    }




}
