package com.wz.entity;

import lombok.Data;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class ImgDTO {
    private String name;
    private InputStream file;
    private byte[] bytes;
    private List<ImgDTO> childs = new ArrayList<>();

    public ImgDTO(String name, byte[] bytes, InputStream file) {
        this.name = name;
        this.bytes = bytes;
        this.file = file;
    }

    public ImgDTO() {

    }

    @Override
    public String toString() {
        return "ImgDTO{" +
                "name='" + name + '\'' +
                ", file=" + file +
                ", bytes=" + Arrays.toString(bytes) +
                ", childs=" + childs +
                '}';
    }
}
