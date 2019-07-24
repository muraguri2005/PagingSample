package org.freelesson.pagingsample;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "repos")
public class Repo {
   @PrimaryKey
   @SerializedName("id")
   Long id;
   @SerializedName("name")
    String name;
   @SerializedName("description")
    String description;
   @SerializedName("stargazers_count")
    int stars;
   @SerializedName("full-name")
    String fullName;
   @SerializedName("forks_count")
   int forks;

   @SerializedName("language")
   String language;

    @Override
    public boolean equals(@Nullable Object obj) {
        //TODO: add contents equal to
        return fullName.equals(((Repo)obj).fullName);
    }
}
