package youtuvideos.tranty.vn.youtuvideos.dialog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import youtuvideos.tranty.vn.youtuvideos.R;
import youtuvideos.tranty.vn.youtuvideos.activities.AddKnowledgesActivity;
import youtuvideos.tranty.vn.youtuvideos.mics.Constants;

public class LanguageDialog extends AppCompatActivity {
    public static final String ARG_DRAWING_START_LOCATION = "arg_drawing_start_location";
    public static final int FROM_MAIN = 1;
    public static final int FROM_ADD_COURSES = 2;

    @BindView(R.id.contentRoot)
    LinearLayout contentRoot;
    @BindView(R.id.btnOk)
    Button btnOk;
    @BindView(R.id.btnCancel)
    Button btnCancel;
    @BindView(R.id.spin_teachers)
    AppCompatSpinner spinTeachers;
    @BindView(R.id.spin_languages)
    AppCompatSpinner spinLanguages;

    private int drawingStartLocation;
    private String arrLanguages[], arrTeachers[], user_id;
    private int language_id = 1, teacher_id = 1, iFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_dialog);
        this.setFinishOnTouchOutside(false);
        ButterKnife.bind(this);
        getData();
        addSpinner();
        drawingStartLocation = getIntent().getIntExtra(ARG_DRAWING_START_LOCATION, 0);
        if (savedInstanceState == null) {
            contentRoot.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    contentRoot.getViewTreeObserver().removeOnPreDrawListener(this);
                    startIntroAnimation();
                    return true;
                }
            });
        }
    }

    private void getData() {
        user_id = getIntent().getStringExtra("user_id");
        language_id = getIntent().getIntExtra("language_id", 1);
        teacher_id = getIntent().getIntExtra("teacher_id", 1);
        iFrom = getIntent().getIntExtra("from", 1);
    }

    private void startIntroAnimation() {
        contentRoot.setScaleY(0.1f);
        contentRoot.setPivotY(drawingStartLocation);
        contentRoot.animate()
                .scaleY(1)
                .setDuration(200)
                .setInterpolator(new AccelerateInterpolator())
                .start();

    }


    void addSpinner() {
        // setup spinner languages
        arrLanguages = getResources().getStringArray(R.array.spinner_languages);
        spinLanguages = (AppCompatSpinner) findViewById(R.id.spin_languages);
        ArrayAdapter<String> adapterLanguage = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrLanguages);
        adapterLanguage.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinLanguages.setAdapter(adapterLanguage);
        spinLanguages.setOnItemSelectedListener(new MySpinnerLanguageEvent());

        // setup spinner teachers
        arrTeachers = getResources().getStringArray(R.array.spinner_teachers);
        spinTeachers = (AppCompatSpinner) findViewById(R.id.spin_teachers);
        ArrayAdapter<String> adapterTeacher = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrTeachers);
        adapterTeacher.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinTeachers.setAdapter(adapterTeacher);
        spinTeachers.setOnItemSelectedListener(new MySpinnerTeacherEvent());

    }

    @OnClick(R.id.btnCancel)
    public void clickCancel() {
        finish();
    }

    @OnClick(R.id.btnOk)
    public void clickOk() {
        if (iFrom == FROM_MAIN) {
            int[] startingLocation = new int[2];
            btnOk.getLocationOnScreen(startingLocation);
            startingLocation[0] += btnOk.getWidth() / 2;
         //   AddKnowledgesActivity.startAddKnowledgeFromLocation(startingLocation, LanguageDialog.this, user_id, language_id, teacher_id);
            overridePendingTransition(0, 0);
            finish();

        } else {
            Intent intent = getIntent();
            intent.putExtra("language_id", language_id);
            intent.putExtra("teacher_id", teacher_id);
            setResult(Constants.ACTIVITY.LANGUAGE_RESULT, intent);
            finish();
        }
    }



    private class MySpinnerLanguageEvent implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            if (arg2 == 0)
                language_id = 1;
            else if (arg2 == 1)
                language_id = 2;
            else if (arg2 == 2)
                language_id = 3;
            else if (arg2 == 3)
                language_id = 4;
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }

    private class MySpinnerTeacherEvent implements AdapterView.OnItemSelectedListener {
        public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            if (arg2 == 0)
                teacher_id = 1;
            else if (arg2 == 1)
                teacher_id = 2;
            else if (arg2 == 2)
                teacher_id = 3;
            else if (arg2 == 3)
                teacher_id = 4;
        }

        public void onNothingSelected(AdapterView<?> arg0) {
        }
    }


}
