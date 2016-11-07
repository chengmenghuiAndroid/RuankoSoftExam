package com.itee.exam.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itee.exam.app.entity.AnswerSheet;
import com.itee.exam.app.entity.ExamPaperVO;
import com.itee.exam.app.entity.User;
import com.itee.exam.core.utils.CryptoUtils;
import com.itee.exam.core.utils.DateTypeAdapter;
import com.itee.exam.vo.UserAppToken;

import java.util.Date;

/**
 * 配置文件管理
 * Created by xin on 2014/9/24 0024.
 */
public class PreferenceUtil {

    private static final String SECRET_KEY = "0jksfh8o2mfu02mfs";

    private static final String USER_NAME = "userName";
    private static final String USER_ID = "userId";
    private static final String PASSWORD = "password";
    private static final String AUTO_LOGIN = "autoLogin";
    private static final String LOGIN_METHOD = "loginMethod";
    private static final String THIRD_OPEN_ID = "thirdOpenId";
    private static final String IS_LOGIN = "isLogin";
    private static final String PUSH_SERVICE_ID = "pushServiceId";
    private static final String APP_TOKEN_CONTENT = "appTokenContent";

    private static final String USER = "user";
    private static final String APP_TOKEN = "appToken";

    private static final String EXAM_PAPER = "examPaper";
    private static final String ANSWER_SHEET = "answerSheet";
    private static final String DO_EXAM = "doExam";
    private static final String EXAM_TYPE = "examType";

    public static PreferenceUtil getInstance() {
        return SingleInstance.INSTANCE;
    }

    private SharedPreferences sharedPreferences;

    private PreferenceUtil() {
    }

    public void init(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setUserName(String userName) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(USER_NAME, userName);
        edit.apply();
    }

    public void setAutoLogin(boolean need){
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(AUTO_LOGIN, need);
        edit.apply();
    }

    public void setIsLogin(Boolean isLogin){
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(IS_LOGIN, isLogin);
        edit.apply();
    }

    public boolean getIsLogin(){
        return sharedPreferences.getBoolean(IS_LOGIN, false);
    }

    public void setExamType(int type){
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(EXAM_TYPE, type);
        edit.apply();
    }

    public int getExamType(){
        return sharedPreferences.getInt(EXAM_TYPE, -1);
    }

    public void setDoExam(boolean doExam){
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(DO_EXAM, doExam);
        edit.apply();
    }

    public boolean getIsDoExam(){
        return sharedPreferences.getBoolean(DO_EXAM, false);
    }

    public void setPushServiceId(String pushServiceId){
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(PUSH_SERVICE_ID, pushServiceId);
        edit.apply();
    }

    public String getPushServiceId(){
        return sharedPreferences.getString(PUSH_SERVICE_ID, null);
    }

    public boolean isAutoLogin(){
        return sharedPreferences.getBoolean(AUTO_LOGIN,false);
    }

    public String getUserName() {
        return sharedPreferences.getString(USER_NAME, null);
    }

    public void setPassword(String password) {
        try {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString(PASSWORD, CryptoUtils.encrypt(SECRET_KEY, password));
            edit.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getPassword() {
        String password = sharedPreferences.getString(PASSWORD, null);
        if (password == null) {
            return null;
        }
        try {
            return CryptoUtils.decrypt(SECRET_KEY, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setLoginMethod(String loginMethod) {
        try {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString(LOGIN_METHOD, loginMethod);
            edit.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public String getLoginMethod() {
//        return sharedPreferences.getString(LOGIN_METHOD, LoginManager.DEFAUL_LOGIN_METHOD);
//    }

    public void setThirdOpenId(String openId) {
        try {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString(THIRD_OPEN_ID, openId);
            edit.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getThirdOpenId() {
        return sharedPreferences.getString(THIRD_OPEN_ID, null);
    }

    public int getUserId() {
        String customerId = sharedPreferences.getString(USER_ID, null);
        if (customerId != null) {
            return Integer.parseInt(customerId);
        } else {
            return 0;
        }
    }

    /**
     * 保存登录数据
     *
     * @param userName
     * @param password
     * @param user
     */
    public void saveLoginData(String userName, String password, User user) {
        setUserName(userName);
        setPassword(password);
        // 清除用户密码
        user.setPassword(null);
        saveUser(user);
    }

    public void saveUser(User user) {
        user.setPassword(null);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();
        String json = gson.toJson(user);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(USER, json);
        edit.putString(USER_ID, String.valueOf(user.getUserId()));
        edit.putString(USER_NAME, String.valueOf(user.getUserName()));
        edit.apply();
    }

    public User getUser() {
        String json = sharedPreferences.getString(USER, null);
        if (json == null) {
            return null;
        }
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();
        return gson.fromJson(json, User.class);
    }

    public void saveExamPaper(ExamPaperVO examPaperVO) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();
        String json = gson.toJson(examPaperVO);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(EXAM_PAPER, json);
        edit.apply();
    }

    public ExamPaperVO getExamPaper() {
        String json = sharedPreferences.getString(EXAM_PAPER, null);
        if (json == null) {
            return null;
        }
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();
        return gson.fromJson(json, ExamPaperVO.class);
    }

    public void saveAnswerSheet(AnswerSheet answerSheet) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();
        String json = gson.toJson(answerSheet);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(ANSWER_SHEET, json);
        edit.apply();
    }

    public AnswerSheet getAnswerSheet() {
        String json = sharedPreferences.getString(ANSWER_SHEET, null);
        if (json == null) {
            return null;
        }
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();
        return gson.fromJson(json, AnswerSheet.class);
    }

    public void saveAppToken(UserAppToken token) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();
        String json = gson.toJson(token);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(APP_TOKEN, json);
        edit.putString(APP_TOKEN_CONTENT, token.getAppToken());
        edit.apply();
    }

    public UserAppToken getAppToken() {
        String json = sharedPreferences.getString(APP_TOKEN, null);
        if (json == null) {
            return null;
        }
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create();
        return gson.fromJson(json, UserAppToken.class);
    }

    public void setTokenContent(String tokenContent) {
        try {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString(APP_TOKEN_CONTENT, tokenContent);
            edit.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTokenContent() {
        return sharedPreferences.getString(APP_TOKEN_CONTENT, null);
    }

    private static class SingleInstance {
        private static final PreferenceUtil INSTANCE = new PreferenceUtil();
    }
}
