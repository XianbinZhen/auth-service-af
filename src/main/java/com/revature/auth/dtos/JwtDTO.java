package com.revature.auth.dtos;

public class JwtDTO {
    private String data;

    public JwtDTO(String data) {
        this.data = data;
    }

    public JwtDTO(){
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "JwtDTO{" +
                "data='" + data + '\'' +
                '}';
    }
}
