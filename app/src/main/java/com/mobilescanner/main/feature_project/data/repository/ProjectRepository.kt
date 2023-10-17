package com.mobilescanner.main.feature_project.data.repository

import com.mobilescanner.main.feature_project.data.database.ProjectDataBase
import com.mobilescanner.main.feature_project.data.entity.ProjectItem
import com.mobilescanner.main.main.data.repository.BaseRepository
import com.mobilescanner.main.main.ui.MobileScannerApplication
import kotlinx.coroutines.Dispatchers
import kotlin.concurrent.thread

object ProjectRepository : BaseRepository() {
    private val dao = ProjectDataBase.getDataBase(MobileScannerApplication.context).dao
    fun insertProject(item: ProjectItem) = thread{
        dao.insertProject(item)
    }
    fun updateProject(newItem: ProjectItem) =  thread{
        dao.updateProject(newItem)
    }
    fun deleteProject(item: ProjectItem) = thread {
        dao.deleteProject(item)
    }
    fun deleteProjectByTitle(title:String) = thread {
        dao.deleteProjectByTitle(title)
    }
    fun deleteAllProject() = local(Dispatchers.IO) {
        dao.deleteAllProject()
    }
    fun loadAllProject() = local(Dispatchers.IO){
        dao.loadAllProject()
    }
    fun getProjectById(id:Long) = local(Dispatchers.IO){
        dao.getProjectById(id)
    }
    fun getProjectByTitle(title:String) = local(Dispatchers.IO){
        dao.getProjectByTitle(title)
    }
}