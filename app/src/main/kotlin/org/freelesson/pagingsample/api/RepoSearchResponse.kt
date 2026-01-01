package org.freelesson.pagingsample.api

import org.freelesson.pagingsample.model.Repo
import com.google.gson.annotations.SerializedName

class RepoSearchResponse(
    @SerializedName("total_count") var total: Int = 0,
    @SerializedName("items") var items: List<Repo> = emptyList<Repo>(),
    var nextPage: Int? = null,
)