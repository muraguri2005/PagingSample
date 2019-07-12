package org.freelesson.pagingsample;

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
    String stars;
}
