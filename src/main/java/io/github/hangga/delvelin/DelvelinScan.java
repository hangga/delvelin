package io.github.hangga.delvelin;

import javax.inject.Inject;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;

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
//                    new Delvelin().setShowSaveDialog(ext.getShowSaveDialog())
//                        .setOutputFormat(ext.outputFileFormat)
//                        .setShowDate(ext.isShowDate)
//                        .scan();
                    OutputFileFormat outputFileFormat;
                    try {
                        outputFileFormat = OutputFileFormat.valueOf(ext.getOutputFileFormat().get().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException("Invalid outputFileFormat: " + ext.getOutputFileFormat().get(), e);
                    }

                    new Delvelin()
                        .setShowSaveDialog(ext.getShowSaveDialog().get())
                        .setOutputFormat(outputFileFormat)
                        .setShowDate(ext.getShowDate().get())
                        .scan();
                });
            });
    }

    public abstract static class DelvelinExtension {

        private final Property<String> outputFileFormat;
        private final Property<Boolean> showDate;
        private final Property<Boolean> showSaveDialog;
        private final Property<Boolean> ignoreCommentBlock;
        private final Property<Boolean> customExtensions;

        @Inject
        public DelvelinExtension(ObjectFactory objectFactory) {
            this.outputFileFormat = objectFactory.property(String.class).convention("LOG");
            this.showDate = objectFactory.property(Boolean.class).convention(true);
            this.showSaveDialog = objectFactory.property(Boolean.class).convention(false);
            this.ignoreCommentBlock = objectFactory.property(Boolean.class).convention(false);
            this.customExtensions = objectFactory.property(Boolean.class).convention(false);
        }

        public Property<String> getOutputFileFormat() {
            return outputFileFormat;
        }

        public Property<Boolean> getShowDate() {
            return showDate;
        }

        public Property<Boolean> getShowSaveDialog() {
            return showSaveDialog;
        }

        public Property<Boolean> getIgnoreCommentBlock() {
            return ignoreCommentBlock;
        }

        public Property<Boolean> getCustomExtensions() {
            return customExtensions;
        }
    }

    // Extension class for task configuration
    //    public static class DelvelinExtension {
    //
    //        public OutputFileFormat outputFileFormat = OutputFileFormat.LOG;
    //        public boolean isShowDate = true;
    //        public boolean isShowSaveDialog = false;
    //        public boolean isIgnoreCommentBlock = false;
    //        public boolean isCustomExtensions = false;
    //
    //        public OutputFileFormat getOutputFormat() {
    //            return outputFileFormat;
    //        }
    //
    //        public void setOutputFormat(OutputFileFormat outputFileFormat) {
    //            this.outputFileFormat = outputFileFormat;
    //        }
    //
    //        public boolean isShowDate() {
    //            return isShowDate;
    //        }
    //
    //        public void setShowDate(boolean showDate) {
    //            isShowDate = showDate;
    //        }
    //
    //        public boolean isShowSaveDialog() {
    //            return isShowSaveDialog;
    //        }
    //
    //        public void setShowSaveDialog(boolean showSaveDialog) {
    //            isShowSaveDialog = showSaveDialog;
    //        }
    //
    //        public boolean isIgnoreCommentBlock() {
    //            return isIgnoreCommentBlock;
    //        }
    //
    //        public void setIgnoreCommentBlock(boolean ignoreCommentBlock) {
    //            isIgnoreCommentBlock = ignoreCommentBlock;
    //        }
    //
    //        public boolean isCustomExtensions() {
    //            return isCustomExtensions;
    //        }
    //
    //        public void setCustomExtensions(boolean customExtensions) {
    //            isCustomExtensions = customExtensions;
    //        }
    //    }
}