package org.freelesson.pagingsample.api;

import com.google.gson.annotations.SerializedName;

import org.freelesson.pagingsample.model.Repo;

import java.util.Collections;
import java.util.List;

public class RepoSearchResponse {
    @SerializedName("total_count") Integer total = 0;
    @SerializedName("items")
    public List<Repo> items= Collections.emptyList();
    Integer nextPage;
}
