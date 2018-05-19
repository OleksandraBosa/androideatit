package com.example.sasha.androideatit.Model;

/**
 * Created by sasha on 14.03.18.
 */

public class Category {
    private String Image;
    private String Name;


    public Category(){
    }

    public Category(String image,String name){
        Image=image;
        Name=name;
    }

    public String getName(){
        return  Name;
    }

    public void setName(String name){
        Name=name;
    }

    public String getImage(){
        return Image;
    }

    public void setImage(String image){
        Image=image;
    }
}
