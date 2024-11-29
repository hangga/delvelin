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
                    OutputFileFormat outputFileFormat;
                    try {
                        outputFileFormat = OutputFileFormat.valueOf(ext.getOutputFileFormat()
                            .get()
                            .toUpperCase());
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException("Invalid outputFileFormat: " + ext.getOutputFileFormat()
                            .get(), e);
                    }

                    new Delvelin().setShowSaveDialog(ext.getShowSaveDialog().get())
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
        private final Property<Boolean> autoLaunchBrowser;
        private final Property<Boolean> ignoreCommentBlock;
        private final Property<Boolean> customExtensions;

        @Inject
        public DelvelinExtension(ObjectFactory objectFactory) {
            this.outputFileFormat = objectFactory.property(String.class)
                .convention("LOG");
            this.showDate = objectFactory.property(Boolean.class)
                .convention(true);
            this.showSaveDialog = objectFactory.property(Boolean.class)
                .convention(false);
            this.ignoreCommentBlock = objectFactory.property(Boolean.class)
                .convention(false);
            this.customExtensions = objectFactory.property(Boolean.class)
                .convention(false);
            this.autoLaunchBrowser = objectFactory.property(Boolean.class)
                .convention(false);
        }

        public Property<Boolean> getIsAutoLaunchBrowser(){
            return autoLaunchBrowser;
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
}