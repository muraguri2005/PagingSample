package org.freelesson.pagingsample.model;

import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "repos")
public class Repo {
   @PrimaryKey
   @SerializedName("id")
   public Long id;
   @SerializedName("name")
    public String name;
   @SerializedName("description")
    public String description;
   @SerializedName("stargazers_count")
    public int stars;
   @SerializedName("full_name")
    public String fullName;
   @SerializedName("forks_count")
   public int forks;

   @SerializedName("language")
   public String language;

    @Override
    public boolean equals(@Nullable Object obj) {
        //TODO: add contents equal to
        assert obj != null;
        return fullName.equals(((Repo)obj).fullName);
    }
}
