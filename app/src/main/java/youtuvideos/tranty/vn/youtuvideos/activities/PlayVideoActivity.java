package youtuvideos.tranty.vn.youtuvideos.activities;

import android.content.Intent;
import android.os.Bundle;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import youtuvideos.tranty.vn.youtuvideos.R;
import youtuvideos.tranty.vn.youtuvideos.abtracts.AbstractResponse;
import youtuvideos.tranty.vn.youtuvideos.activities.parents.BaseActivity;
import youtuvideos.tranty.vn.youtuvideos.adapters.VideoModulesAdapter;
import youtuvideos.tranty.vn.youtuvideos.dao.modules.ModuleVO;
import youtuvideos.tranty.vn.youtuvideos.dao.modules.logs.ModuleLogVO;
import youtuvideos.tranty.vn.youtuvideos.dao.users.knowledges.KnowledgesUserVO;
import youtuvideos.tranty.vn.youtuvideos.interfaces.ItemModulesListeners;
import youtuvideos.tranty.vn.youtuvideos.mics.Constants;
import youtuvideos.tranty.vn.youtuvideos.requests.UsersRequest;


public class PlayVideoActivity extends BaseActivity implements YouTubePlayer.OnInitializedListener {

    @BindView(R.id.rc_lessons)
    RecyclerView recyclerView;
    @BindView(R.id.tv_author)
    TextView tvAuthor;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.view_network)
    RelativeLayout viewNetwork;
    @BindView(R.id.layout_loading)
    LinearLayout layout_loading;

    private YouTubePlayer youTubePlayer;
    private ArrayList<ModuleVO> arrModules = new ArrayList<>();
    private KnowledgesUserVO knowledgeUser;
    private ArrayList<ModuleLogVO> modulesLogs;
    private int videoSelected = 0;
    private boolean isPaused = false, isPlay = false;
    private boolean isAds = false;
    private long timeCount = 0;
    private ModuleVO moduleSelected;
    private Timer timer;
    private VideoModulesAdapter adapter;
    private YouTubePlayerSupportFragment youTubePlayerFragment;
    private int course_user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        ButterKnife.bind(this);
        addYoutubeFragment();
        addRecycler();
    }

    /* add youtube fragment */
    private void addYoutubeFragment() {
        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.video_fragment, youTubePlayerFragment).commit();
    }

    /* add recycler */
    private void addRecycler() {
        recyclerView = (RecyclerView) findViewById(R.id.rc_lessons);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new VideoModulesAdapter(this, new ItemModulesListeners() {
            @Override
            public void onItemClicked(View view, int position) {
                isCommpleted();
                if (timer != null)
                    timer.cancel();
                timeCount = 0;
                moduleSelected = arrModules.get(position); // bài học được chọn
                videoSelected = position;
                tvTitle.setText(moduleSelected.title);
                youTubePlayer.loadVideo(moduleSelected.youtube_id);
                youTubePlayer.play();
            }
        });
        recyclerView.setAdapter(adapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("video: ", "Activity resume" + timeCount);
        getData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("video: ", "Activity pause" + timeCount);
    }

    // stop activity thì check xem da completed chưa
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("video: ", "Activity stop" + timeCount);
        isCommpleted();
    }

    // khi nhân vào full screens thì lưu time lại
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        if (timer != null)
            timer.cancel();
        Log.d("video: ", "SaveInstance" + timeCount);
        outState.putLong("time", timeCount);

    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        timeCount = savedInstanceState.getLong("time");
        Log.d("video: ", "RestoreInstance" + timeCount);

    }


    private void getData() {
        Intent i = getIntent();
        course_user_id = i.getIntExtra("knowledge_user_id", 0);
        if (course_user_id == 0) {
            knowledgeUser = (KnowledgesUserVO) getIntent().getSerializableExtra("KnowledgeUser");
            course_user_id = knowledgeUser.id;
            setData(course_user_id);
        } else
            setData(course_user_id);
    }

    @OnClick(R.id.view_network)
    public void onClickNetwork(){
        getData();
    }

    private void setData(int id) {
        layout_loading.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        viewNetwork.setVisibility(View.GONE);
        UsersRequest.getKnowledges(id, new AbstractResponse() {
            @Override
            public void onFailure() {
                super.onFailure();
                layout_loading.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                viewNetwork.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(int error_code, String message, Object obj) {
                super.onSuccess(error_code, message, obj);
                layout_loading.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                viewNetwork.setVisibility(View.GONE);
                knowledgeUser = (KnowledgesUserVO) obj;
                if (knowledgeUser != null) {
                    modulesLogs = knowledgeUser.logs; // array session completed
                    arrModules = knowledgeUser.modules; // array session
                    // set completed session
                    if (modulesLogs != null) {
                        for (int i = 0; i < arrModules.size(); i++) {
                            for (int j = 0; j < modulesLogs.size(); j++) {
                                if (arrModules.get(i).id == modulesLogs.get(j).module_id) {
                                    arrModules.get(i).completed = 1;
                                    break;
                                }
                            }
                        }
                    }
                    // lay ra video chua completed gan nhat de play
                    moduleSelected = arrModules.get(videoSelected);
                    if (arrModules != null) {
                        for (int i = 0; i < arrModules.size(); i++) {
                            if (arrModules.get(i).completed == 0) {
                                videoSelected = i;
                                moduleSelected = arrModules.get(i);
                                break;
                            }
                        }
                    }
                    // set data
                    tvTitle.setText(moduleSelected.title);
             //       tvAuthor.setText(knowledgeUser.knowledge.title);
                    adapter.setArrModules(arrModules);
                    youTubePlayerFragment.initialize(Constants.KEY.YOUTUB_API_KEY, PlayVideoActivity.this);
                }
            }
        });
    }



    /* Initialization Success */
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
        if (!b) {
            this.youTubePlayer = youTubePlayer;
            this.youTubePlayer.setFullscreen(false);
            this.youTubePlayer.setPlaybackEventListener(playbackEventListener);
            this.youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
            this.youTubePlayer.loadVideo(moduleSelected.youtube_id);
            this.youTubePlayer.play();
        }
    }

    /* Initialization Failure */
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, Constants.KEY.RECOVERY_REQUEST).show();
        } else {
            String error = String.format(getString(R.string.player_error), youTubeInitializationResult.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }


    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onPlaying() {
            Log.d("video: ", "onPlay");
            if (isPaused) {
                countTime();
            }
        }

        @Override
        public void onPaused() {
            if (timer != null)
                timer.cancel();
            isPaused = true;
            Log.d("video", "curent time: " + youTubePlayer.getCurrentTimeMillis());
            Log.d("video", "duration time: " + youTubePlayer.getDurationMillis());
        }

        @Override
        public void onStopped() {
            if (isPlay) {
                if (!isAds) {
                    Log.d("video: ", "Stop Video");
                    isCommpleted();
                } else {
                    Log.d("video: ", "Stop Ads");
                }
            }

        }

        @Override
        public void onBuffering(boolean b) {

        }

        @Override
        public void onSeekTo(int i) {
            Log.d("video: ", "seekto " + i);
        }
    };

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onLoading() {
            Log.d("video: ", "onLoading");
        }

        @Override
        public void onLoaded(String s) {
            Log.d("video: ", "onLoad");
        }

        // start ads
        @Override
        public void onAdStarted() {
            Log.d("video: ", "ad start");
            isAds = true;
        }

        // start video
        @Override
        public void onVideoStarted() {
            isAds = false;
            isPlay = true;
            Log.d("video: ", "video start");
            countTime();

        }

        @Override
        public void onVideoEnded() {
            Log.d("video: ", "end");
            isCommpleted();
            doNextVideo();
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {
            Log.d("video: ", "error");
        }
    };

    /* Tự động next video khi kết thúc video  trước */
    private void doNextVideo() {
        if (timer != null)
            timer.cancel();
        timeCount = 0;
        videoSelected += 1;
        moduleSelected = arrModules.get(videoSelected);
        youTubePlayer.loadVideo(moduleSelected.youtube_id);
        youTubePlayer.play();
    }

    private void isCommpleted() {
        if (timer != null)
            timer.cancel();
        if (timeCount > 10) {
            Log.d("video", "video completed");
            arrModules.get(videoSelected).completed = 1;
//            SessionsRequest.setCompleted(courseUser.id, lessonSelected.id, new AbstractResponse() {
//                @Override
//                public void onSuccess(int error_code, String message, Object obj) {
//                    super.onSuccess(error_code, message, obj);
//                    adapter.notifyItemChanged(videoSelected,arrLessons.get(videoSelected));
//                }
//                @Override
//                public void onFailure() {
//                    super.onFailure();
//                }
//            });
        } else
            Log.d("video", "video not completed");
    }


    private void countTime() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("video: ", "" + timeCount);
                        timeCount += 1;
                    }
                });
            }
        }, 1000, 1000);
    }

    @OnClick(R.id.view_network)
    public void onclickNetWork() {
        Log.d("ty", "click network");
        setData(course_user_id);
    }


}
