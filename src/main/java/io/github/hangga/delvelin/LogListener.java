package io.github.hangga.delvelin;

public interface LogListener {
    void onGetLog(String log);
    void onGetLog(StringBuffer log);
}
