package com.uminho.uce15.cityroots.data;

import java.io.Serializable;

public class Photo implements Serializable {
    private String name;
    private String path;

    public Photo(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
