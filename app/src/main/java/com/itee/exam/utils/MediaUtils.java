package com.itee.exam.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.util.Log;

import com.itee.exam.core.utils.ExternalStorageUtils;

import java.io.IOException;

/**
 * Created by xin on 2015-07-24.
 */
public class MediaUtils {

    private static final String TAG = MediaUtils.class.getName();

    static final private double EMA_FILTER = 0.6;

    private MediaRecorder mRecorder;
    private double mEMA = 0.0;

    private MediaPlayer mMediaPlayer;

    public static MediaUtils getInstance() {
        return Singleton.INSTANCE;
    }


    public void start(String name) throws IOException {
        if (!ExternalStorageUtils.isExternalStorageWritable()) {
            Log.w(TAG, "当前SD卡不可写");
            return;
        }
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile(name);

            mRecorder.prepare();
            mRecorder.start();

            mEMA = 0.0;
        }
    }

    public void stop() {
        if (mRecorder != null) {
            try {
                mRecorder.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            mRecorder.release();
            mRecorder = null;
        }
    }

    public double getAmplitude() {
        if (mRecorder != null) {
            return (mRecorder.getMaxAmplitude() / 2700.0);
        } else {
            return 0;
        }
    }

    public double getAmplitudeEMA() {
        double amp = getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        return mEMA;
    }

    /**
     * 播放音频文件
     *
     * @param name
     */
    public void playMusic(String name) throws IOException {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
        }
        stopMusic();
        mMediaPlayer.reset();
        mMediaPlayer.setDataSource(name);
        mMediaPlayer.prepare();
        mMediaPlayer.start();
    }

    /**
     * 停止播放音频
     */
    public void stopMusic() {
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
    }

    /**
     * 获取音频时长
     *
     * @param name
     * @return
     * @throws IOException
     */
    public static int getDuration(String name) throws IOException {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.reset();
        mediaPlayer.setDataSource(name);
        mediaPlayer.prepare();
        int duration = mediaPlayer.getDuration();
        mediaPlayer.release();
        return duration;
    }

    private static final class Singleton {
        private static final MediaUtils INSTANCE = new MediaUtils();
    }

    /**
     * 视频播放
     *
     */
    private MediaUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * @param bMute 值为true时为关闭背景音乐。
     */
    @TargetApi(Build.VERSION_CODES.FROYO)
    public static boolean muteAudioFocus(Context context, boolean bMute) {
        boolean bool = false;
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (bMute) {
            int result = am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            bool = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
        } else {
            int result = am.abandonAudioFocus(null);
            bool = result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
        }
        return bool;
    }
}