package com.example.moviecatalogue5.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.ArrayList;

public class Credit implements Serializable {

    @SerializedName("id")
    private int id;
    @SerializedName("cast")
    private ArrayList<cast> casts;
    @SerializedName("crew")
    private ArrayList<crew> crews;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<cast> getCasts() {
        return casts;
    }

    public void setCasts(ArrayList<cast> casts) {
        this.casts = casts;
    }

    public ArrayList<crew> getCrews() {
        return crews;
    }

    public void setCrews(ArrayList<crew> crews) {
        this.crews = crews;
    }

    public class cast implements Serializable {
        @SerializedName("character")
        private String character;
        @SerializedName("name")
        private String name;
        @SerializedName("profile_path")
        private String profilePath;

        public String getCharacter() {
            return character;
        }

        public void setCharacter(String character) {
            this.character = character;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProfilePath() {
            return profilePath;
        }

        public void setProfilePath(String profilePath) {
            this.profilePath = profilePath;
        }
    }
    public class crew implements Serializable {
        @SerializedName("department")
        private String department;
        @SerializedName("job")
        private String job;
        @SerializedName("name")
        private String name;
        @SerializedName("profile_path")
        private String profilePath;

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getProfilePath() {
            return profilePath;
        }

        public void setProfilePath(String profilePath) {
            this.profilePath = profilePath;
        }
    }
}
