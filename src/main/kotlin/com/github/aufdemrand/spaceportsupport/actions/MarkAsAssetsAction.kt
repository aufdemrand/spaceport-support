package com.github.aufdemrand.spaceportsupport.actions

import com.github.aufdemrand.spaceportsupport.services.SpaceportProjectService
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager

class MarkAsAssetsAction : AnAction("Assets Root") {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        if (!file.isDirectory) return

        val service = SpaceportProjectService.getInstance(project)
        val path = file.path

        if (service.isAssetsRoot(path)) {
            service.removeAssetsRoot(path)
        } else {
            service.addAssetsRoot(path)
        }

        com.intellij.ide.projectView.ProjectView.getInstance(project).refresh()
    }

    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)
        e.presentation.isEnabledAndVisible = file != null && file.isDirectory

        if (file != null) {
            val project = e.project ?: return
            val service = SpaceportProjectService.getInstance(project)
            if (service.isAssetsRoot(file.path)) {
                e.presentation.text = "Unmark as Assets Root"
            } else {
                e.presentation.text = "Assets Root"
            }
        }
    }
}
