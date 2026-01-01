package com.github.aufdemrand.spaceportsupport

import com.intellij.openapi.fileTypes.LanguageFileType
import javax.swing.Icon

object LaunchpadFileType : LanguageFileType(LaunchpadLanguage) {
    override fun getName(): String = "Launchpad Template"
    override fun getDescription(): String = "Launchpad template file"
    override fun getDefaultExtension(): String = "ghtml"
    override fun getIcon(): Icon = SpaceportIcons.GhtmlFile
}
