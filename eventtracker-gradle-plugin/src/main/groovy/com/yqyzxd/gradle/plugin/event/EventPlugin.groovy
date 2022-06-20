package com.yqyzxd.gradle.plugin.event
import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

class EventPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        AppExtension android= project.extensions.findByType(AppExtension)
        android.registerTransform(new EventTrackerTransformation())
    }
}