package com.mobilescanner.main.feature_project.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.mobilescanner.main.feature_project.data.entity.ProjectItem

@Dao
interface ProjectDao {
    @Insert
    fun insertProject(item: ProjectItem)

    @Update
    fun updateProject(newItem: ProjectItem)

    @Query("SELECT * FROM Project")
    fun loadAllProject():List<ProjectItem>
    @Query("SELECT * FROM Project WHERE id = :id")
    fun getProjectById(id:Long): ProjectItem
    @Query("SELECT * FROM Project WHERE title = :title")
    fun getProjectByTitle(title:String): ProjectItem

    @Delete
    fun deleteProject(item: ProjectItem)

    @Query("DELETE FROM Project WHERE title = :title")
    fun deleteProjectByTitle(title:String)

    @Query("DELETE FROM Project")
    fun deleteAllProject()
}