package org.freelesson.pagingsample.model

import androidx.lifecycle.LiveData
import androidx.paging.PagingData

class RepoSearchResult(
    var data: LiveData<PagingData<Repo>>,
    var networkErrors: LiveData<String?>?
)