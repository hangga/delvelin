package io.github.hangga.delvelin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import io.github.hangga.delvelin.properties.OutputFileFormat;

public class DelvelinScan implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        // Menambahkan ekstensi "delvelin" ke dalam proyek
        DelvelinExtension ext = project.getExtensions()
            .create("delvelin", DelvelinExtension.class);

        project.getTasks()
            .register("delvelinScan", task -> {
                task.doLast(action -> {
                    new Delvelin().setShowSaveDialog(ext.isShowSaveDialog())
                        .setOutputFormat(ext.getOutputFormat())
                        .setShowDate(ext.isShowDate())
                        .scan();
                });
            });
    }

    // Extension class for task configuration
    public static class DelvelinExtension {

        private OutputFileFormat outputFileFormat = OutputFileFormat.LOG;
        private boolean isShowDate = true;
        private boolean isShowSaveDialog = false;
        private boolean isIgnoreCommentBlock = false;
        private boolean isCustomExtensions = false;

        public OutputFileFormat getOutputFormat() {
            return outputFileFormat;
        }

        public void setOutputFormat(OutputFileFormat outputFileFormat) {
            this.outputFileFormat = outputFileFormat;
        }

        public boolean isShowDate() {
            return isShowDate;
        }

        public void setShowDate(boolean showDate) {
            isShowDate = showDate;
        }

        public boolean isShowSaveDialog() {
            return isShowSaveDialog;
        }

        public void setShowSaveDialog(boolean showSaveDialog) {
            isShowSaveDialog = showSaveDialog;
        }

        public boolean isIgnoreCommentBlock() {
            return isIgnoreCommentBlock;
        }

        public void setIgnoreCommentBlock(boolean ignoreCommentBlock) {
            isIgnoreCommentBlock = ignoreCommentBlock;
        }

        public boolean isCustomExtensions() {
            return isCustomExtensions;
        }

        public void setCustomExtensions(boolean customExtensions) {
            isCustomExtensions = customExtensions;
        }
    }
}