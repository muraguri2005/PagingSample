package org.freelesson.pagingsample;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

public class RepoSearchResponse {
    @SerializedName("total_count") Integer total = 0;
    @SerializedName("items")
    List<Repo> items= Collections.emptyList();
    Integer nextPage;
}
