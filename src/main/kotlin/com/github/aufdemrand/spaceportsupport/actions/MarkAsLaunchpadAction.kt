package com.github.aufdemrand.spaceportsupport.actions

import com.github.aufdemrand.spaceportsupport.services.SpaceportProjectService
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager

class MarkAsLaunchpadAction : AnAction("Launchpad Root") {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        if (!file.isDirectory) return

        val service = SpaceportProjectService.getInstance(project)
        val path = file.path

        if (service.isLaunchpadRoot(path)) {
            service.removeLaunchpadRoot(path)
        } else {
            service.addLaunchpadRoot(path)
        }

        // Refresh project view to show new icon
        refreshProjectView(project, file)
    }

    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)
        e.presentation.isEnabledAndVisible = file != null && file.isDirectory

        if (file != null) {
            val project = e.project ?: return
            val service = SpaceportProjectService.getInstance(project)
            if (service.isLaunchpadRoot(file.path)) {
                e.presentation.text = "Unmark as Launchpad Root"
            } else {
                e.presentation.text = "Launchpad Root"
            }
        }
    }

    private fun refreshProjectView(project: Project, file: VirtualFile) {
        // Force refresh of the file in the project view
        com.intellij.ide.projectView.ProjectView.getInstance(project).refresh()
    }
}
