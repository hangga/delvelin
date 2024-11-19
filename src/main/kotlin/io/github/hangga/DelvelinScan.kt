package io.github.hangga

import io.delvelin.Delvelin
import io.delvelin.properties.OutputFormat
import org.gradle.api.Plugin
import org.gradle.api.Project

class DelvelinScan : Plugin<Project> {

    override fun apply(project: Project) {
        val ext = project.extensions.create(
            "delvelin", Extension::class.java
        )

        project.tasks.register("delvelinScan") { task ->
            task.doLast {
                Delvelin()
                    .setShowSaveDialog(ext.isShowSaveDialog)
                    .setOutputFormat(ext.outputFormat)
                    .setShowDate(ext.isShowDate)
                    .scan()
            }
        }
    }

    // Extension class for task configuration
    open class Extension {
        var outputFormat: OutputFormat = OutputFormat.LOG
        var isShowDate: Boolean = true
        var isShowSaveDialog: Boolean = false
        var isIgnoreCommentBlock: Boolean = false
        var isCustomExtensions: Boolean = false
    }
}
