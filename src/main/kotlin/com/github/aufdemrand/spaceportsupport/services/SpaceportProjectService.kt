package com.github.aufdemrand.spaceportsupport.services

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.FileUtil
import java.io.File

@Service(Service.Level.PROJECT)
@State(name = "SpaceportProjectService", storages = [Storage("spaceport.xml")])
class SpaceportProjectService(private val project: Project) : PersistentStateComponent<SpaceportProjectService.State> {

    data class State(
        var launchpadRoots: MutableSet<String> = mutableSetOf(),
        var assetsRoots: MutableSet<String> = mutableSetOf(),
        var manifestFiles: MutableSet<String> = mutableSetOf()
    )

    private var myState = State()

    override fun getState(): State = myState

    override fun loadState(state: State) {
        myState = state
    }

    private fun getRelativePath(path: String): String? {
        val projectBasePath = project.basePath ?: return null
        val systemIndependentPath = FileUtil.toSystemIndependentName(path)
        val systemIndependentBase = FileUtil.toSystemIndependentName(projectBasePath)

        return FileUtil.getRelativePath(systemIndependentBase, systemIndependentPath, '/')
    }

    fun isLaunchpadRoot(path: String): Boolean {
        val relPath = getRelativePath(path) ?: return false
        return myState.launchpadRoots.contains(relPath)
    }

    fun addLaunchpadRoot(path: String) {
        val relPath = getRelativePath(path)
        if (relPath != null) {
            myState.launchpadRoots.add(relPath)
        }
    }

    fun removeLaunchpadRoot(path: String) {
        val relPath = getRelativePath(path)
        if (relPath != null) {
            myState.launchpadRoots.remove(relPath)
        }
    }

    fun isAssetsRoot(path: String): Boolean {
        val relPath = getRelativePath(path) ?: return false
        return myState.assetsRoots.contains(relPath)
    }

    fun addAssetsRoot(path: String) {
        val relPath = getRelativePath(path)
        if (relPath != null) {
            myState.assetsRoots.add(relPath)
        }
    }

    fun removeAssetsRoot(path: String) {
        val relPath = getRelativePath(path)
        if (relPath != null) {
            myState.assetsRoots.remove(relPath)
        }
    }

    fun isManifestFile(path: String): Boolean {
        val relPath = getRelativePath(path) ?: return false
        return myState.manifestFiles.contains(relPath)
    }

    fun setManifestFile(path: String) {
        val relPath = getRelativePath(path)
        if (relPath != null) {
            myState.manifestFiles.clear()
            myState.manifestFiles.add(relPath)
        }
    }

    fun removeManifestFile(path: String) {
        val relPath = getRelativePath(path)
        if (relPath != null) {
            myState.manifestFiles.remove(relPath)
        }
    }

    companion object {
        fun getInstance(project: Project): SpaceportProjectService = project.service()
    }
}
