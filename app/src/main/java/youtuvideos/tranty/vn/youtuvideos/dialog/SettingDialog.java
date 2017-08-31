package youtuvideos.tranty.vn.youtuvideos.dialog;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import butterknife.BindView;
import youtuvideos.tranty.vn.youtuvideos.R;
import youtuvideos.tranty.vn.youtuvideos.abtracts.AbstractResponse;
import youtuvideos.tranty.vn.youtuvideos.activities.MainActivity;
import youtuvideos.tranty.vn.youtuvideos.activities.parents.BaseActivity;
import youtuvideos.tranty.vn.youtuvideos.dao.knowledges.KnowledgeVO;
import youtuvideos.tranty.vn.youtuvideos.dao.users.UserLoginVO;
import youtuvideos.tranty.vn.youtuvideos.dao.users.knowledges.KnowledgesUserVO;
import youtuvideos.tranty.vn.youtuvideos.mics.Constants;
import youtuvideos.tranty.vn.youtuvideos.mics.Util;
import youtuvideos.tranty.vn.youtuvideos.myview.CustomFontTextView;
import youtuvideos.tranty.vn.youtuvideos.requests.UsersRequest;

import static youtuvideos.tranty.vn.youtuvideos.services.MyFirebaseMessagingService.NOTIFICATION_ID;

public class SettingDialog extends BaseActivity implements TimePickerDialog.OnTimeSetListener {
    @BindView(R.id.btnOk)
    Button btnOk;
    @BindView(R.id.btnCancel)
    Button btnCancel;
    @BindView(R.id.tv_time)
    CustomFontTextView tvTime;
    @BindView(R.id.cb_mon)
    CheckBox cbMon;
    @BindView(R.id.cb_tue)
    CheckBox cbTue;
    @BindView(R.id.cb_wed)
    CheckBox cbWed;
    @BindView(R.id.cb_thu)
    CheckBox cbThu;
    @BindView(R.id.cb_fri)
    CheckBox cbFri;
    @BindView(R.id.cb_sat)
    CheckBox cbSat;
    @BindView(R.id.cb_sun)
    CheckBox cbSun;
    @BindView(R.id.tv_title)
    CustomFontTextView tvTitle;
    @BindView(R.id.sw_active)
    SwitchCompat swActived;
    @BindView(R.id.view_days_time)
    LinearLayout viewDays;
    @BindView(R.id.im_warning_days)
    ImageView imWarningDays;
    @BindView(R.id.im_warning_time)
    ImageView imWarningTime;


    private int[] days = new int[7];
    private KnowledgeVO knowledgeVO;
    private int hour = -1, min = -1;
    private Map<String, String> data;
    private String schedule_days = "", timeZone;
   private KnowledgesUserVO knowledgesUserVO;
    private String[] arr_days;
    private int iActived = 1;
    private int knowledgeUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_settings);
        this.setFinishOnTouchOutside(false);
        cancelNoti();
        getData();
        addEvent();
    }

    private void cancelNoti() {
        int notification_id = getIntent().getIntExtra(Constants.KEY_INTENT.NOTIFICATION_ID, 0);
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notification_id);
    }

    private void getData() {
        knowledgeVO = (KnowledgeVO) getIntent().getSerializableExtra(Constants.KEY_INTENT.KNOWLEDGE);
        if (knowledgeVO != null) // neu tu man hinh register
            tvTitle.setText(knowledgeVO.title);
        else { // nếu từ activity knowledge
            knowledgesUserVO = (KnowledgesUserVO) getIntent().getSerializableExtra(Constants.KEY_INTENT.KNOWLEDGE_USER);
            if (knowledgesUserVO != null) {
                tvTitle.setText(getIntent().getStringExtra(Constants.KEY_INTENT.TITLE_KNOWLEDGE));
                setData();
            } else { // nếu như từ notification
                knowledgeUserId = getIntent().getIntExtra(Constants.KEY_INTENT.KNOWLEDGE_USER_ID, 0);
                UsersRequest.getKnowledges(knowledgeUserId, new AbstractResponse() {
                    @Override
                    public void onSuccess(int error_code, String message, Object obj) {
                        super.onSuccess(error_code, message, obj);
                        knowledgesUserVO = (KnowledgesUserVO) obj;
                        setData();
                    }

                    @Override
                    public void onFailure() {
                        super.onFailure();
                    }
                });
            }

        }
    }

    private void setData() {
        iActived = knowledgesUserVO.schedule_actived;
        if (iActived == 1) {
            swActived.setChecked(true);
            setEnableSetting(1f, true);
        } else {
            swActived.setChecked(false);
            setEnableSetting(0.3f, false);
        }

        min = knowledgesUserVO.schedule_min;
        hour = knowledgesUserVO.schedule_hour;
        arr_days = Util.splitString(knowledgesUserVO.schedule_days);
        if (arr_days[1].equalsIgnoreCase("1")) {
            days[0] = 1;
            cbMon.setChecked(true);
        } else {
            cbMon.setChecked(false);
        }
        if (arr_days[2].equalsIgnoreCase("1")) {
            days[1] = 1;
            cbTue.setChecked(true);
        } else {
            cbTue.setChecked(false);
        }
        if (arr_days[3].equalsIgnoreCase("1")) {
            days[2] = 1;
            cbWed.setChecked(true);
        } else {
            cbWed.setChecked(false);
        }
        if (arr_days[4].equalsIgnoreCase("1")) {
            days[3] = 1;
            cbThu.setChecked(true);
        } else {
            cbThu.setChecked(false);
        }
        if (arr_days[5].equalsIgnoreCase("1")) {
            days[4] = 1;
            cbFri.setChecked(true);
        } else {
            cbFri.setChecked(false);
        }
        if (arr_days[6].equalsIgnoreCase("1")) {
            days[5] = 1;
            cbSat.setChecked(true);
        } else {
            cbSat.setChecked(false);
        }
        if (arr_days[7].equalsIgnoreCase("1")) {
            days[6] = 1;
            cbSun.setChecked(true);
        } else {
            cbSun.setChecked(false);
        }
        if (hour != -1)
            tvTime.setText(String.format("%02d:%02d", knowledgesUserVO.schedule_hour, knowledgesUserVO.schedule_min));
        else
            tvTime.setText(String.valueOf("--:--"));
    }


    private void addEvent() {
        tvTime.setOnClickListener(onclick);
        cbMon.setOnClickListener(onclick);
        cbTue.setOnClickListener(onclick);
        cbWed.setOnClickListener(onclick);
        cbThu.setOnClickListener(onclick);
        cbFri.setOnClickListener(onclick);
        cbSat.setOnClickListener(onclick);
        cbSun.setOnClickListener(onclick);
        btnOk.setOnClickListener(onclick);
        btnCancel.setOnClickListener(onclick);
        swActived.setOnClickListener(onclick);
    }

    private View.OnClickListener onclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_time:
                    showPickTime();
                    break;
                case R.id.cb_mon:
                    days[0] = (days[0] == 0) ? 1 : 0;
                    break;
                case R.id.cb_tue:
                    days[1] = (days[1] == 0) ? 1 : 0;
                    break;
                case R.id.cb_wed:
                    days[2] = (days[2] == 0) ? 1 : 0;
                    break;
                case R.id.cb_thu:
                    days[3] = (days[3] == 0) ? 1 : 0;
                    break;
                case R.id.cb_fri:
                    days[4] = (days[4] == 0) ? 1 : 0;
                    break;
                case R.id.cb_sat:
                    days[5] = (days[5] == 0) ? 1 : 0;
                    break;
                case R.id.cb_sun:
                    days[6] = (days[6] == 0) ? 1 : 0;
                    break;
                case R.id.btnOk:
                    doOk();
                    break;
                case R.id.btnCancel:
                    finish();
                case R.id.sw_active:
                    doSwichActived();
                    break;

            }
        }
    };

    private void doOk() {
        schedule_days = "";
        setRepeatDay();
        if (swActived.isChecked()) { // nếu như active thì fai check null time và day
            if (schedule_days.matches("0000000") && hour == -1) { // chưa pick time va day
                Toast.makeText(this, getString(R.string.schedule_days_time_null), Toast.LENGTH_LONG).show();
                imWarningDays.setVisibility(View.VISIBLE);
                imWarningTime.setVisibility(View.VISIBLE);
            } else if (!schedule_days.matches("0000000") && hour == -1) { // neu da pick day nhung chua time
                Toast.makeText(getBaseContext(), getString(R.string.schedule_days_time_null), Toast.LENGTH_LONG).show();
                imWarningDays.setVisibility(View.GONE);
                imWarningTime.setVisibility(View.VISIBLE);
            } else if (schedule_days.matches("0000000") && hour != -1) {
                Toast.makeText(getBaseContext(), getString(R.string.schedule_days_null), Toast.LENGTH_LONG).show();
                imWarningDays.setVisibility(View.VISIBLE);
                imWarningTime.setVisibility(View.GONE);
            } else {
                doUpdateOrAdd();
            }
        } else  // nếu như chưa active thì cho update hoac add luôn
            doUpdateOrAdd();

    }

    private void doUpdateOrAdd() {
        setMapData();
        if (knowledgeVO != null)
            addKnowledge();
        else
            updateKnowledge();
    }

    private void doSwichActived() {
        imWarningDays.setVisibility(View.GONE);
        imWarningTime.setVisibility(View.GONE);
        if (swActived.isChecked()) {
            iActived = 1;
            setEnableSetting(1f, true);
        } else {
            iActived = 0;
            setEnableSetting(0.3f, false);
        }
    }

    private void setEnableSetting(float alpha, boolean clickAble) {
        viewDays.setAlpha(alpha);
        tvTime.setClickable(clickAble);
        cbMon.setClickable(clickAble);
        cbTue.setClickable(clickAble);
        cbWed.setClickable(clickAble);
        cbThu.setClickable(clickAble);
        cbFri.setClickable(clickAble);
        cbSat.setClickable(clickAble);
        cbSun.setClickable(clickAble);
    }

    private void updateKnowledge() {
        UsersRequest.updateKnowledge(data, new AbstractResponse() {
            @Override
            public void onSuccess(int error_code, String message, Object obj) {
                super.onSuccess(error_code, message, obj);
                if (error_code == 0) {
                    knowledgesUserVO.schedule_hour = hour;
                    knowledgesUserVO.schedule_min = min;
                    knowledgesUserVO.schedule_days = schedule_days;
                    Intent i = new Intent();
                    i.putExtra(Constants.KEY_INTENT.KNOWLEDGE_USER, (Serializable) knowledgesUserVO);
                    setResult(Constants.ACTIVITY.UPDATE_SETTING_RESULT, i);
                    finish();
                }
            }

            @Override
            public void onFailure() {
                super.onFailure();
                Toast.makeText(getBaseContext(), getResources().getString(R.string.network_fail), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setMapData() {
        timeZone = TimeZone.getDefault().getID();
        data = new HashMap<>();
        data.put(Constants.KEY_DATA.USER_ID, String.valueOf(userLoginVO.getGoogleID()));
        if (knowledgeVO == null) // nếu như null thì là add
            data.put(Constants.KEY_DATA.KNOWLEDGE_USER_ID, String.valueOf(knowledgesUserVO.id));
        else
            data.put(Constants.KEY_DATA.KNOWLEDGE_ID, String.valueOf(knowledgeVO.id));
        data.put(Constants.KEY_DATA.SCHEDULE_MIN, String.valueOf(min));
        data.put(Constants.KEY_DATA.SCHEDULE_HOUR, String.valueOf(hour));
        data.put(Constants.KEY_DATA.SCHEDULE_DAYS, schedule_days);
        data.put(Constants.KEY_DATA.SCHEDULE_TIMEZONE, timeZone);
        data.put(Constants.KEY_DATA.SCHEDULE_ACTIVED, String.valueOf(iActived));
    }

    private void addKnowledge() {
        UsersRequest.registerKnowledge(data, new AbstractResponse() {
            @Override
            public void onSuccess(int error_code, String message, Object obj) {
                super.onSuccess(error_code, message, obj);
                if (error_code == 0) {
                    if (getIntent().getBooleanExtra(Constants.KEY_INTENT.FROM_PLAY_MODULES,false)) {
                        setResult(Constants.ACTIVITY.SETTING_RESULT);
                        finish();
                    }else {
                        MainActivity.startMainFromLocation(SettingDialog.this);
                        overridePendingTransition(0, 0);
                    }
                }
            }

            @Override
            public void onFailure() {
                super.onFailure();
                Toast.makeText(getBaseContext(), getResources().getString(R.string.network_fail), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setRepeatDay() {
        for (int i = 0; i < days.length; i++) {
            if (days[i] == 0)
                schedule_days += "0";
            else
                schedule_days += "1";
        }
    }

    private void showPickTime() {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
        );
        tpd.setAccentColor(Color.parseColor("#F44336"));

        tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
            }
        });
        tpd.show(getFragmentManager(), "Timepickerdialog");
    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        hour = hourOfDay;
        min = minute;
        tvTime.setText(String.format("%02d:%02d", hour, min));
    }
}
