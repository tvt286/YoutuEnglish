package youtuvideos.tranty.vn.youtuvideos.mics;

import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class LoaderImage {
    private static LoaderImage ins = null;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    protected LoaderImage(Context context) {
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();
    }
    public static LoaderImage ins(Context context) {
        if(ins == null) {
            ins = new LoaderImage(context);
        }
        return ins;
    }

    public void show(String url, ImageView iv){
        if (url != null) {
            try {
                imageLoader.displayImage(url, iv, options);
            } catch (Exception e) {
            }
        }
    }
}
