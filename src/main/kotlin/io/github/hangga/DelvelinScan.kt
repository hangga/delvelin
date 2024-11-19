package io.github.hangga

import org.gradle.api.Plugin
import org.gradle.api.Project

class DelvelinScan : Plugin<Project> {
    override fun apply(project: Project) {
        // Create an extension to allow configuration in build scripts
        val ext = project.extensions.create(
            "delvelin",
            Extension::class.java
        )

        // Register the task
        project.tasks.register("delvelinScan") { task ->
            task.doLast {
                // Uncomment and modify the following lines to integrate with Delvelin
//                Delvelin()
//                    .setShowSaveDialog(ext.isShowSaveDialog)
//                    .setOutputFormat(ext.outputFormat)
//                    .setShowDate(ext.isShowDate)
//                    .scan()
                println("Delvelin Scan executed with configuration:")
                println("Show Date: ${ext.isShowDate}")
                println("Show Save Dialog: ${ext.isShowSaveDialog}")
                println("Ignore Comment Block: ${ext.isIgnoreCommentBlock}")
                println("Custom Extensions: ${ext.isCustomExtensions}")
            }
        }
    }

    // Extension class for task configuration
    open class Extension {
        //        var outputFormat: OutputFormat = OutputFormat.LOG
        var isShowDate: Boolean = true
        var isShowSaveDialog: Boolean = false
        var isIgnoreCommentBlock: Boolean = false
        var isCustomExtensions: Boolean = false
    }
}
