package youtuvideos.tranty.vn.youtuvideos.dialog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import youtuvideos.tranty.vn.youtuvideos.R;
import youtuvideos.tranty.vn.youtuvideos.activities.LoginActivity;
import youtuvideos.tranty.vn.youtuvideos.mics.Constants;

import static youtuvideos.tranty.vn.youtuvideos.activities.LoginActivity.ARG_DRAWING_START_LOCATION;

public class InformationLoginDialog extends AppCompatActivity {
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_login)
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_information_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_cancel)
    public void onClickCancel(){
        finish();
    }

    @OnClick(R.id.btn_login)
    public void onClickLogin(){

    }
}
