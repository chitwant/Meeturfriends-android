package com.meetfriend.app.ui.activities

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.PowerManager
import android.view.Window
import android.view.WindowManager
import com.alphamovie.lib.AlphaMovieView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.meetfriend.app.R
import com.meetfriend.app.base.BaseActivity
import com.meetfriend.app.utilclasses.CustomVideoPlayer
import kotlinx.android.synthetic.main.activity_video_play.*


class VideoPlayActivity : BaseActivity() {
    var player: SimpleExoPlayer? = null
    var path = ""
    lateinit var screenLock: PowerManager.WakeLock
    override fun initializeLayout(): Int {
        return R.layout.activity_video_play
    }

    override fun inflateLayout() {
    }

    @SuppressLint("InvalidWakeLockTag")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        screenLock = (getSystemService(POWER_SERVICE) as PowerManager).newWakeLock(
//            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG"
//        )
        val win: Window = window
        win.addFlags(
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
        )
        setContentView(R.layout.activity_video_play)
        path = intent.extras!!.getString("path", "")
        player = ExoPlayerFactory.newSimpleInstance(this)
        var uri = Uri.parse(
            path
        )
        exoPlayer.setPlayer(player)
// Produces DataSource instances through which media data is loaded.
        // Produces DataSource instances through which media data is loaded.
        val dataSourceFactory: DataSource.Factory =
            DefaultDataSourceFactory(
                this,
                Util.getUserAgent(this, "Meet Friend")
            )
// This is the MediaSource representing the media to be played.
        // This is the MediaSource representing the media to be played.
        val videoSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)
// Prepare the player with the source.
        // Prepare the player with the source.
        player!!.prepare(videoSource)
        player!!.playWhenReady = true

        exoPlayer.player!!.addListener(object : Player.EventListener {
            override fun onLoadingChanged(isLoading: Boolean) {}

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playbackState == ExoPlayer.STATE_ENDED) {

                }

            }


        })
        ivBackIcon.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        playPlayer()
    }
    override fun onPause() {
        super.onPause()
        if (player != null) {
            pausePlayer()
            //  player!!.stop()
            //player!!.release()
            // player = null;
        }
    }
    private fun playPlayer() {
        if (player != null) {
            player!!.playWhenReady = true
        }
    }
    private fun pausePlayer(){
        if (player != null) {
            player!!.playWhenReady = false
        }
    }
}