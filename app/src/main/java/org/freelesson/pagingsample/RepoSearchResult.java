package org.freelesson.pagingsample;

import androidx.lifecycle.LiveData;

import java.util.List;

public class RepoSearchResult {
    LiveData<List<Repo>> data;
    LiveData<String> networkErrors;
}
