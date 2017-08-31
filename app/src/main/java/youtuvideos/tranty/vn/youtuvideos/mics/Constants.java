package youtuvideos.tranty.vn.youtuvideos.mics;

/**
 * Created by PC on 3/4/2017.
 */
public class Constants {
    public class  KEY {
        public static final String YOUTUB_API_KEY = "AIzaSyBvVVSlw1jayjHIDXAk9aioyG2xrR9mPqg";
        public final static int RECOVERY_REQUEST = 1;
    }

    public class  API {
      //public static final String URL = "http://www.quanlyhocvien.esy.es/youtu_english/index.php/";
        public static final String URL = "http://192.168.137.1:8080/youtu_english/index.php/";
    }

    public class ADS{
        public static final String NATIVE_ADS="ca-app-pub-4464139281471789/2299117753";
    }

    public class ACTIVITY {
        public final static int OPEN_LOGIN = 1 ;
        public final static int LOGIN_RESULT = 2;

        public final static int OPEN_LANGUAGE = 3;
        public final static int LANGUAGE_RESULT = 4;

        public final static int OPEN_UPDATE_SETTING = 5;
        public final static int UPDATE_SETTING_RESULT = 6;

        public final static int START_ACTIVITY_PLAYVIDEO = 7;
        public final static int ACTIVITY_PLAYVIDEO_SUCCESS = 8;

        public final static int OPEN_SETTING = 9;
        public final static int SETTING_RESULT = 10;

    }

    public class LOGIN_DEFAULT {
        public final static String NAME = "USER";
        public final static String EMAIL = "You are not login.";
        public final static String BTN_LOGIN = "Login";
        public final static String NUM_KNOWLEDGES = "0";
    }

    public class REGISTER_USER {
        public final static String IMAGE = "image";
        public final static String NAME = "name";
        public final static String EMAIL = "email";
        public final static String ID = "id";
    }

    public class MESSAGE {
        public final static String KEY = "message";
        public final static String REGISTER_SUCCESS = "Login success.";
        public final static String DELETE_SUCCESS = "Delete success.";
        public final static String WELLCOME = "Wellcome to comeback.";
    }

    public class SERVICES {
        public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    }

    public class KEY_INTENT{
        public final static String KNOWLEDGE = "Knowledge";
        public final static String KNOWLEDGE_ID = "KnowledgeId";
        public final static String KNOWLEDGE_USER = "KnowledgeUser";
        public final static String KNOWLEDGE_USER_ID = "KnowledgeUserId";
        public final static String NOTIFICATION_ID = "NotificationId";
        public final static String USER_ID = "UserId";
        public final static String TEACHER_ID = "TeacherId";
        public final static String LANGUAGE_ID = "LanguageId";
        public final static String FROM = "From";
        public final static String FROM_MAIN = "FromMain";
        public final static String FROM_PLAY_MODULES = "fromPlayModules";
        public final static String TITLE_KNOWLEDGE = "Title";
    }

    public  class KEY_DATA{
        public final static String USER_ID = "user_id";
        public final static String KNOWLEDGE_ID = "knowledge_id";
        public final static String KNOWLEDGE_USER_ID = "knowledge_user_id";
        public final static String SCHEDULE_MIN = "schedule_min";
        public final static String SCHEDULE_HOUR = "schedule_hour";
        public final static String SCHEDULE_DAYS = "schedule_days";
        public final static String SCHEDULE_TIMEZONE = "schedule_timezone";
        public final static String SCHEDULE_ACTIVED = "schedule_actived";
    }

}
