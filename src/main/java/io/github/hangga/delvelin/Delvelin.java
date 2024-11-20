package io.github.hangga.delvelin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import io.github.hangga.delvelin.detectors.GeneralScanner;
import io.github.hangga.delvelin.properties.Config;
import io.github.hangga.delvelin.properties.OutputFileFormat;
import io.github.hangga.delvelin.utils.DelvelinLog;
import io.github.hangga.delvelin.utils.FileUtil;
import io.github.hangga.delvelin.utils.Reports;
import io.github.hangga.delvelin.utils.SourceSet;

public class Delvelin {

    SourceSet sourceSets = new SourceSet();
    GeneralScanner generalScanner = new GeneralScanner();
    LogListener logListener;

    public Delvelin setLogListener(LogListener logListener) {
        this.logListener = logListener;
        return this;
    }

    public Delvelin setOutputFormat(OutputFileFormat format) {
        Config.outputFileFormat = format;
        return this;
    }

    public Delvelin() {
    }

    public static void main(String[] args) {
        List<String> slist = Arrays.asList(args);
        new Delvelin().setOutputFormat(
                slist.contains("format_html") ? OutputFileFormat.HTML : (slist.contains("format_json") ? OutputFileFormat.JSON : OutputFileFormat.LOG))
            .setShowSaveDialog(slist.contains("show_save_dialog"))
            .scan();
    }

    public Delvelin setAllowedExtensions(String... extensions) {
        this.sourceSets.setExtensions(extensions);
        Config.isCustomExtensions = true;
        return this;
    }

    public Delvelin setShowDate(boolean showDate) {
        Config.isShowDate = showDate;
        return this;
    }

    public Delvelin setShowSaveDialog(boolean isShow) {
        Config.isShowSaveDialog = isShow;
        return this;
    }

    public Delvelin setIgnoreCommentBlock(boolean ignoreCommentBlock) {
        Config.ignoreCommentBlock = ignoreCommentBlock;
        return this;
    }

    public void scan() {
        try (Stream<Path> stream = FileUtil.getStream()) {
            stream.filter(Files::isRegularFile)
                .filter(path -> {
                    for (String ext : sourceSets.getExtensions()) {
                        if (path.toString()
                            .endsWith(ext)) {
                            return true;
                        }
                    }
                    return false;
                })
                .forEach(generalScanner::scan);
        } catch (IOException ignored) {
        } finally {
            if (Config.outputFileFormat == OutputFileFormat.HTML) {
                Reports.generateHtmlReport();
            } else if (Config.outputFileFormat == OutputFileFormat.JSON) {
                Reports.generateJson();
            } else if (Config.outputFileFormat == OutputFileFormat.LOG) {
                new DelvelinLog().log(Reports.log()
                    .toString());
            } else if (logListener != null) {
                logListener.onGetLog(Reports.log());
                logListener.onGetLog(Reports.log()
                    .toString());
            }
        }
    }
}
