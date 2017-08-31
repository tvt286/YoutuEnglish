package youtuvideos.tranty.vn.youtuvideos.activities;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import youtuvideos.tranty.vn.youtuvideos.MyApplication;
import youtuvideos.tranty.vn.youtuvideos.R;
import youtuvideos.tranty.vn.youtuvideos.abtracts.AbstractResponse;
import youtuvideos.tranty.vn.youtuvideos.activities.parents.BaseActivity;
import youtuvideos.tranty.vn.youtuvideos.dao.knowledges.KnowledgeVO;
import youtuvideos.tranty.vn.youtuvideos.dao.modules.logs.ModuleLogVO;
import youtuvideos.tranty.vn.youtuvideos.dao.parrents.PointModel;
import youtuvideos.tranty.vn.youtuvideos.dao.users.knowledges.KnowledgesUserVO;
import youtuvideos.tranty.vn.youtuvideos.dialog.SettingDialog;
import youtuvideos.tranty.vn.youtuvideos.mics.Constants;
import youtuvideos.tranty.vn.youtuvideos.mics.LoaderImage;
import youtuvideos.tranty.vn.youtuvideos.mics.Util;
import youtuvideos.tranty.vn.youtuvideos.myview.CustomFontTextView;
import youtuvideos.tranty.vn.youtuvideos.myview.DayAxisValueFormatter;
import youtuvideos.tranty.vn.youtuvideos.myview.MyAxisValueFormatter;
import youtuvideos.tranty.vn.youtuvideos.myview.MyMarkerView;
import youtuvideos.tranty.vn.youtuvideos.myview.RevealBackgroundView;
import youtuvideos.tranty.vn.youtuvideos.myview.XYMarkerView;
import youtuvideos.tranty.vn.youtuvideos.requests.ModulesRequest;

public class KnowledgesActivity extends BaseActivity implements RevealBackgroundView.OnStateChangeListener, OnChartValueSelectedListener {
    public static final String ARG_REVEAL_START_LOCATION = "reveal_start_location";
    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator();

    @BindView(R.id.vRevealBackground)
    RevealBackgroundView vRevealBackground;
    @BindView(R.id.vModuleDetails)
    View vModuleDetails;
    @BindView(R.id.vModuleStats)
    View vModuleStats;
    @BindView(R.id.vModuleRoot)
    View vModuleRoot;
    @BindView(R.id.tv_timepicker)
    CustomFontTextView tvTime;
    @BindView(R.id.tv_title)
    CustomFontTextView tvTitle;
    @BindView(R.id.tv_days)
    CustomFontTextView tvDays;
    @BindView(R.id.btn_learn)
    Button btnLearn;
    @BindView(R.id.chart1)
    BarChart mChart;
    @BindView(R.id.view_setting)
    LinearLayout itemSetting;
    @BindView(R.id.im_knowledge)
    ImageView imKnowledge;
    @BindView(R.id.view_content)
    LinearLayout viewContent;
    @BindView(R.id.tv_register)
    CustomFontTextView tvRegister;
    @BindView(R.id.tv_number_modules)
    CustomFontTextView tvNumModules;

    private boolean isUpdate = false;
    private String[] schedule_days;
    protected Typeface mTfLight;
    private KnowledgesUserVO knowledgesUserVO;
    private KnowledgeVO knowledgeVO;
    private ArrayList<ModuleLogVO> arrModulesLogs;


    public static void startKnowledgeFromLocation(int[] startingLocation, Activity startingActivity, KnowledgesUserVO knowledgesUserVO) {
        Intent intent = new Intent(startingActivity, KnowledgesActivity.class);
        intent.putExtra(Constants.KEY_INTENT.KNOWLEDGE_USER, (Serializable) knowledgesUserVO);
        intent.putExtra(Constants.KEY_INTENT.KNOWLEDGE, knowledgesUserVO.knowledge);

        intent.putExtra(ARG_REVEAL_START_LOCATION, startingLocation);
        startingActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledges);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        MyApplication.getAppInstance().setUserChange(false);

        setFont();
        getData();
        setData();
        setupRevealBackground(savedInstanceState);
        addEvent();
    }

    private void setFont() {
        mTfLight = Typeface.createFromAsset(getAssets(), "Roboto-Medium.ttf");
    }

    private void setupRevealBackground(Bundle savedInstanceState) {
        vRevealBackground.setOnStateChangeListener(this);
        if (savedInstanceState == null) {
            final int[] startingLocation = getIntent().getIntArrayExtra(ARG_REVEAL_START_LOCATION);
            vRevealBackground.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    vRevealBackground.getViewTreeObserver().removeOnPreDrawListener(this);
                    vRevealBackground.startFromLocation(startingLocation);
                    return true;
                }
            });
        } else {
            vRevealBackground.setToFinishedFrame();

        }
    }

    private void animateKnowledgeHeader() {
        vModuleRoot.setTranslationY(-vModuleRoot.getHeight());
        imKnowledge.setTranslationY(-imKnowledge.getHeight());
        vModuleDetails.setTranslationY(-vModuleDetails.getHeight());
        vModuleStats.setAlpha(0);
        vModuleRoot.animate().translationY(0).setDuration(300).setInterpolator(INTERPOLATOR);
        imKnowledge.animate().translationY(0).setDuration(300).setStartDelay(100).setInterpolator(INTERPOLATOR);
        vModuleDetails.animate().translationY(0).setDuration(300).setStartDelay(200).setInterpolator(INTERPOLATOR);
        vModuleStats.animate().alpha(1).setDuration(200).setStartDelay(400).setInterpolator(INTERPOLATOR).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                viewContent.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();
    }

    @Override
    public void onStateChange(int state) {
        if (RevealBackgroundView.STATE_FINISHED == state) {
            vModuleRoot.setVisibility(View.VISIBLE);
            getSessionsLogs();
            animateKnowledgeHeader();
        } else {
            vModuleRoot.setVisibility(View.INVISIBLE);
        }
    }

    private void setData() {
        tvRegister.setText(String.valueOf(knowledgeVO.registers));
        tvNumModules.setText(String.valueOf(knowledgeVO.total_video));
        tvTitle.setText(knowledgeVO.title);
        LoaderImage.ins(this).show(knowledgeVO.image, imKnowledge);
        schedule_days = Util.splitString(knowledgesUserVO.schedule_days);
        String days = "";
        if (schedule_days[1].equalsIgnoreCase("1"))
            days += "Mon ";
        if (schedule_days[2].equalsIgnoreCase("1"))
            days += "Tue ";
        if (schedule_days[3].equalsIgnoreCase("1"))
            days += "Wed ";
        if (schedule_days[4].equalsIgnoreCase("1"))
            days += "Thu ";
        if (schedule_days[5].equalsIgnoreCase("1"))
            days += "Fri ";
        if (schedule_days[6].equalsIgnoreCase("1"))
            days += "Sat ";
        if (schedule_days[7].equalsIgnoreCase("1"))
            days += "Sun ";

        if (knowledgesUserVO.schedule_hour != -1) {
            tvDays.setText(days);
            tvTime.setText(String.format("%02d:%02d", knowledgesUserVO.schedule_hour, knowledgesUserVO.schedule_min));
        } else {
            tvDays.setText(getString(R.string.null_schedule));
            tvTime.setText("--:--");
        }
    }


    private void getData() {
        knowledgesUserVO = (KnowledgesUserVO) getIntent().getSerializableExtra(Constants.KEY_INTENT.KNOWLEDGE_USER);
        knowledgeVO = (KnowledgeVO) getIntent().getSerializableExtra(Constants.KEY_INTENT.KNOWLEDGE);
    }

    private void getSessionsLogs() {
        ModulesRequest.getModulesLogs(knowledgesUserVO.id, new AbstractResponse() {
            @Override
            public void onSuccess(int error_code, String message, Object obj) {
                super.onSuccess(error_code, message, obj);
                if (error_code == 0) {
                    arrModulesLogs = (ArrayList<ModuleLogVO>) obj;
                    if (arrModulesLogs != null)
                        setChart(arrModulesLogs);
                } else {
                    ArrayList<PointModel> chart = new ArrayList<>(7);
                    long day[] = new long[7];
                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DAY_OF_YEAR, -7);
                    for (int i = 0; i < 7; i++) {
                        cal.add(Calendar.DAY_OF_YEAR, 1);
                        day[i] = cal.getTimeInMillis();
                        chart.add(new PointModel(day[i], 0));
                    }
                    addChart(chart);
                }
            }

            @Override
            public void onFailure() {
                super.onFailure();
            }
        });
    }

    private void setChart(ArrayList<ModuleLogVO> arrModulesLogs) {
        ArrayList<PointModel> arrPoint = getListPoint(arrModulesLogs);
        ArrayList<PointModel> pointChart = getSevenLastDay();
        pointChart = getPointSevenLastDay(pointChart, arrPoint);
        addChart(pointChart);
    }

    // get danh sach diem của tất cả các ngày
    private ArrayList<PointModel> getListPoint(ArrayList<ModuleLogVO> arrModulesLogs) {
        ArrayList<PointModel> arrPoint = new ArrayList<>();
        String date_current = "";
        PointModel pointModel = new PointModel();
        ArrayList<Integer> id = new ArrayList<>();
        for (int i = 0; i < arrModulesLogs.size(); i++) {
            int diem = 0;
            ModuleLogVO moduleLogVO = arrModulesLogs.get(i);
            Date time = new Date((long) moduleLogVO.view_timestamp * 1000);
            SimpleDateFormat dt1 = new SimpleDateFormat("MM/dd");
            String date = dt1.format(time);
            if (date.equalsIgnoreCase(date_current)) {
                if (id.size() != 0) {
                    boolean add = true;
                    for (int m = 0; m < id.size(); m++) {
                        if (id.get(m) == moduleLogVO.module_id) {
                            diem = 1;
                            add = false;
                            break;
                        } else {
                            diem = 10;
                        }
                    }
                    if (add)
                        id.add(moduleLogVO.module_id);
                } else {
                    id.add(moduleLogVO.module_id);
                    diem = 10;
                }
                pointModel.point = pointModel.point + diem;
            } else {
                date_current = date;
                boolean add = true;
                if (id.size() != 0) {
                    for (int m = 0; m < id.size(); m++) {
                        if (id.get(m) == moduleLogVO.module_id) {
                            diem = pointModel.point + 1;
                            add = false;
                            break;
                        } else {
                            diem = pointModel.point + 10;
                        }
                    }
                    if (add)
                        id.add(moduleLogVO.module_id);
                } else {
                    diem = pointModel.point + 10;
                    id.add(moduleLogVO.module_id);
                }

                pointModel = new PointModel(moduleLogVO.view_timestamp, diem);
                arrPoint.add(pointModel);
            }
        }
        return arrPoint;
    }

    // lấy ra bảy ngày gần nhất
    private ArrayList<PointModel> getSevenLastDay() {
        ArrayList<PointModel> sevenLastDay = new ArrayList<>(7);
        long day[] = new long[7];
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -7);
        for (int i = 0; i < 7; i++) {
            cal.add(Calendar.DAY_OF_YEAR, 1);
            day[i] = cal.getTimeInMillis();
            sevenLastDay.add(new PointModel(day[i], 0));
        }
        return sevenLastDay;
    }

    // lấy ra diểm của 7 ngày gần nhất
    private ArrayList<PointModel> getPointSevenLastDay(ArrayList<PointModel> chart, ArrayList<PointModel> arrPoint) {

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
        for (int j = 0; j < arrPoint.size(); j++) {
            for (int i = 0; i < chart.size(); i++) {
                if ((chart.get(i).date / 1000) < arrPoint.get(j).date) {
                    if (sdf.format(new Date(chart.get(i).date)).equalsIgnoreCase(sdf.format(new Date(arrPoint.get(j).date * 1000))))
                        chart.get(i).point = arrPoint.get(j).point;
                    else {
                        if (chart.get(i).point == 0)
                            chart.get(i).point = 0;
                    }
                } else {
                    chart.get(i).point = arrPoint.get(j).point;
                }
            }
        }
        return chart;
    }


    private void addEvent() {
        btnLearn.setOnClickListener(onclick);
        itemSetting.setOnClickListener(onclick);
    }

    private View.OnClickListener onclick = new View.OnClickListener() {
        Intent i = null;

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_learn:
                    i = new Intent(getBaseContext(), LearningActivity.class);
                    i.putExtra(Constants.KEY_INTENT.KNOWLEDGE_USER, (Serializable) knowledgesUserVO);
                    startActivityForResult(i, Constants.ACTIVITY.START_ACTIVITY_PLAYVIDEO);
                    break;
                case R.id.view_setting:
                    i = new Intent(getBaseContext(), SettingDialog.class);
                    i.putExtra(Constants.KEY_INTENT.KNOWLEDGE_USER, (Serializable) knowledgesUserVO);
                    i.putExtra(Constants.KEY_INTENT.TITLE_KNOWLEDGE, knowledgeVO.title);
                    startActivityForResult(i, Constants.ACTIVITY.OPEN_UPDATE_SETTING);
                    break;
            }
        }
    };

    private void addChart(ArrayList<PointModel> array) {
        mChart.setMaxVisibleValueCount(8);
        mChart.setPinchZoom(false);
        mChart.setDragEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.getDescription().setEnabled(false);
        IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(mChart);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTfLight);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f); // only intervals of 1 day
        xAxis.setLabelCount(7);
        xAxis.setValueFormatter(xAxisFormatter);
        IAxisValueFormatter custom = new MyAxisValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(mTfLight);
        leftAxis.setLabelCount(8, false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setTextColor(Color.WHITE);
        rightAxis.setTypeface(mTfLight);
        rightAxis.setLabelCount(8, false);
        rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);

        XYMarkerView mv = new XYMarkerView(this, xAxisFormatter);
        mv.setChartView(mChart); // For bounds control
        mChart.setMarker(mv); // Set the marker to the chart
        setData(array.size(), array);
    }

    private void setData(int count, ArrayList<PointModel> array) {
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        for (int i = 0; i < count; i++) {
            PointModel pointModel = array.get(i);
            Date time = new Date(pointModel.date);
            SimpleDateFormat dt1 = new SimpleDateFormat("dd");
            int date = Integer.valueOf(dt1.format(time));
            yVals1.add(new BarEntry(date, pointModel.point));
        }
        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Days");
            set1.setDrawIcons(false);
            set1.setColors(ColorTemplate.MATERIAL_COLORS);
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);
            mChart.setData(data);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Constants.ACTIVITY.UPDATE_SETTING_RESULT) {
            MyApplication.getAppInstance().setUserChange(true);
            knowledgesUserVO = (KnowledgesUserVO) data.getSerializableExtra(Constants.KEY_INTENT.KNOWLEDGE_USER);
            setData();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
