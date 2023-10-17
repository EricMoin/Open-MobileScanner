package com.mobilescanner.main.feature_project.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.mobilescanner.main.feature_project.data.repository.ImageRepository
import com.mobilescanner.main.feature_project.data.entity.ProjectItem
import com.mobilescanner.main.feature_project.data.repository.ProjectRepository
import com.mobilescanner.main.feature_project.data.entity.ImageItem
import com.mobilescanner.main.main.data.utils.FileUtils
import com.mobilescanner.main.main.data.utils.FileUtils.logD

class ProjectViewModel:ViewModel() {
    val historyList = mutableListOf<ImageItem>()
    private val _historyLiveData = MutableLiveData<Unit>()
    val historyLiveData = _historyLiveData.switchMap {
        ImageRepository.loadAllHistory()
    }
    fun loadAllHistory(){
        _historyLiveData.value = _historyLiveData.value
    }
    val projectList = mutableListOf<ProjectItem>()
    private val _projectLiveData = MutableLiveData<Unit>()
    val projectLiveData = _projectLiveData.switchMap {
        ProjectRepository.loadAllProject()
    }
    fun loadAllProject(){
        _projectLiveData.value = _projectLiveData.value
    }
    fun insertProject(item:ProjectItem) = ProjectRepository.insertProject(item)
    fun saveProject() = ProjectRepository.insertProject(project)
    fun updateProject() = ProjectRepository.updateProject(project)
    fun deleteProject(item:ProjectItem) = ProjectRepository.deleteProject(item)

    val pdfImageList = mutableListOf<ImageItem>(ImageItem("",""))
    var project:ProjectItem = ProjectItem()
    fun newProject(){
        val newId = (1..10000).random().toLong()
        project = ProjectItem( id = newId, title = "无标题", body = "创建时间${FileUtils.getCurrentTime()}" )
    }
    private val _imageLiveData = MutableLiveData<String>()
    val imageLiveData get() = _imageLiveData
    fun insertImage(filePath: String) {
        _imageLiveData.value = filePath
    }
}