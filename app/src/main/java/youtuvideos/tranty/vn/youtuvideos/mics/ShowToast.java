package youtuvideos.tranty.vn.youtuvideos.mics;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import youtuvideos.tranty.vn.youtuvideos.R;

/**
 * Created by PC on 4/22/2017.
 */

public class ShowToast {
    public static void show(String message, Activity context) {
        LayoutInflater inflater = context.getLayoutInflater();
        View layout = inflater.inflate(R.layout.my_toast, (ViewGroup) context.findViewById(R.id.toast_layout_root));
        TextView text = (TextView) layout.findViewById(R.id.tv_toast);
        text.setText(message);
        Toast toast = new Toast(context.getApplicationContext());
        toast.setGravity(Gravity.BOTTOM,0,80);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
