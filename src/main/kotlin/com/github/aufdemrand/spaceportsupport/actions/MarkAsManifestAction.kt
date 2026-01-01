package com.github.aufdemrand.spaceportsupport.actions

import com.github.aufdemrand.spaceportsupport.services.SpaceportProjectService
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class MarkAsManifestAction : AnAction("Spaceport Configuration Manifest") {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        if (file.isDirectory) return

        val service = SpaceportProjectService.getInstance(project)
        val path = file.path

        if (service.isManifestFile(path)) {
            service.removeManifestFile(path)
        } else {
            service.setManifestFile(path)
        }

        com.intellij.ide.projectView.ProjectView.getInstance(project).refresh()
    }

    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)
        e.presentation.isEnabledAndVisible = file != null && !file.isDirectory

        if (file != null) {
            val project = e.project ?: return
            val service = SpaceportProjectService.getInstance(project)
            if (service.isManifestFile(file.path)) {
                e.presentation.text = "Unmark as Configuration Manifest"
            } else {
                e.presentation.text = "Spaceport Configuration Manifest"
            }
        }
    }
}
