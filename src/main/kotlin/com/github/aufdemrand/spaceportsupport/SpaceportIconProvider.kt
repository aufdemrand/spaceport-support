package com.github.aufdemrand.spaceportsupport

import com.github.aufdemrand.spaceportsupport.services.SpaceportProjectService
import com.intellij.ide.IconProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import javax.swing.Icon

class SpaceportIconProvider : IconProvider(), DumbAware {
    override fun getIcon(element: PsiElement, flags: Int): Icon? {
        val project = element.project
        val service = SpaceportProjectService.getInstance(project)
        val virtualFile = when (element) {
            is PsiDirectory -> element.virtualFile
            is PsiFile -> element.virtualFile
            else -> return null
        }
        val path = virtualFile.path

        if (element is PsiDirectory) {
            // Check manual marks first
            if (service.isLaunchpadRoot(path)) {
                return SpaceportIcons.LaunchpadRoot
            }
            if (service.isAssetsRoot(path)) {
                return SpaceportIcons.AssetsRoot
            }

            // Check structure rules (inside a Launchpad Root)
            val parent = virtualFile.parent
            if (parent != null && service.isLaunchpadRoot(parent.path)) {
                when (virtualFile.name) {
                    "parts" -> return SpaceportIcons.PartsRoot
                    "elements" -> return SpaceportIcons.ElementsRoot
                }
            }
        } else if (element is PsiFile) {
            // Check manual manifest mark
            if (service.isManifestFile(path)) {
                return SpaceportIcons.ManifestFile
            }

            if (virtualFile.name == "spaceport.config") {
                return SpaceportIcons.ManifestFile
            }

            // Check .groovy files inside 'elements' folder of a Launchpad Root
            if (virtualFile.extension == "groovy") {
                // Traverse up to find if we are inside 'elements' which is inside a Launchpad Root
                var current = virtualFile.parent
                var insideElements = false

                while (current != null) {
                    // Stop if we hit project root or filesystem root to avoid infinite loops (though unlikely with VFS)
                    if (current.path == project.basePath) break

                    if (current.name == "elements") {
                        val parent = current.parent
                        if (parent != null && service.isLaunchpadRoot(parent.path)) {
                            insideElements = true
                            break
                        }
                    }
                    current = current.parent
                }

                if (insideElements) {
                    return SpaceportIcons.ServerElement
                }
            }
        }

        return null
    }
}
