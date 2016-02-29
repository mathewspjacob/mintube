package com.shapps.ytube;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shyam on 18/2/16.
 */
class GetHtmlInterface {
    Context context;
    static String PlayerId = "";
    static boolean foundPlayerId;
    static Handler handlerForJavascriptInterface = new Handler();
    public GetHtmlInterface(PlayerService playerService) {
        this.context = playerService;
    }

    @JavascriptInterface
    public void showHTML(final String html, WebView player) {
        Pattern pattern = Pattern.compile(
                ".*\\n.*(player_uid_\\d+_1).*\\n.*",Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(html);
        if (matcher.matches()) {
            PlayerId = matcher.group(1);
            Log.e("Player Id ", PlayerId);
            foundPlayerId = true;
            Session.setPlayerId(PlayerId);
            handlerForJavascriptInterface.post(new Runnable() {
                @Override
                public void run() {
                    PlayerService.InitializePlayer();
                }
            });
        }
        else {
            Log.e("Fuck it ", "Man!!!");
            handlerForJavascriptInterface.post(new Runnable() {
                @Override
                public void run() {
                    PlayerService.tryAgainForPID();
                }
            });
        }

    }
    @JavascriptInterface
    public void showPlayerState (final int status) {
        Log.e("Player Status ", String.valueOf(status));
        handlerForJavascriptInterface.post(new Runnable() {
            @Override
            public void run() {
                PlayerService.setPlayingStatus(status);
            }
        });
    }
    @JavascriptInterface
    public void showVID (final String vId) {
        Log.e("New Video Id ", vId);
        handlerForJavascriptInterface.post(new Runnable() {
            @Override
            public void run() {
                PlayerService.setImageTitleAuthor(vId);
            }
        });
    }

}