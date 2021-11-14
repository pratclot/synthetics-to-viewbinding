package com.github.pratclot.syntheticstoviewbinding.services

import com.intellij.openapi.project.Project
import com.github.pratclot.syntheticstoviewbinding.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
