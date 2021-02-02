package com.module.project.ui.fragment.project

import androidx.lifecycle.MutableLiveData
import com.base.base.entity.remote.ArticleList
import com.base.base.entity.remote.ArticleTree
import com.base.base.ui.mvvm.BaseViewModel
import com.module.project.data.ProjectRepository

/**
 * title:
 * describe:
 *
 * @author memo
 * @date 2021-01-31 11:23 PM
 * @email zhou_android@163.com
 *
 * Talk is cheap, Show me the code.
 */
class ProjectViewModel : BaseViewModel() {

    val treeLiveData = MutableLiveData<ArrayList<ArticleTree>>()
    val articleLiveData = MutableLiveData<ArticleList>()

    fun getProjectTree() {
        request(
            request = { ProjectRepository.getProjectTree() },
            onSuccess = { treeLiveData.postValue(it) }
        )
    }

    fun getProjectArticles(page: Int, cid: Int) {
        request(
            request = { ProjectRepository.getProjectArticles(page, cid) },
            onSuccess = { articleLiveData.postValue(it) }
        )
    }
}