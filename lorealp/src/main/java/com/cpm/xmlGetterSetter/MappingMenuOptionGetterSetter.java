package com.cpm.xmlGetterSetter;

import java.util.ArrayList;

public class MappingMenuOptionGetterSetter {
    String table;
    ArrayList<String>region_Id=new ArrayList<>();
    ArrayList<String>menu_name=new ArrayList<>();
    ArrayList<String>menu_icon=new ArrayList<>();
    ArrayList<String>menu_icon_done=new ArrayList<>();

    public ArrayList<String> getMenu_icon_gray() {
        return menu_icon_gray;
    }

    public void setMenu_icon_gray(String menu_icon_gray) {
        this.menu_icon_gray.add(menu_icon_gray);
    }

    ArrayList<String>menu_icon_gray=new ArrayList<>();

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public ArrayList<String> getRegion_Id() {
        return region_Id;
    }

    public void setRegion_Id(String region_Id) {
        this.region_Id.add(region_Id);
    }

    public ArrayList<String> getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name.add(menu_name);
    }

    public ArrayList<String> getMenu_icon() {
        return menu_icon;
    }

    public void setMenu_icon(String menu_icon) {
        this.menu_icon.add(menu_icon);
    }

    public ArrayList<String> getMenu_icon_done() {
        return menu_icon_done;
    }

    public void setMenu_icon_done(String menu_icon_done) {
        this.menu_icon_done.add(menu_icon_done);
    }

    public ArrayList<String> getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path.add(image_path);
    }

    ArrayList<String>image_path=new ArrayList<>();

    public ArrayList<String> getSEQUENCE() {
        return SEQUENCE;
    }

    public void setSEQUENCE(String SEQUENCE) {
        this.SEQUENCE.add(SEQUENCE);
    }

    ArrayList<String>SEQUENCE=new ArrayList<>();

    public String getJudge_imagename() {
        return judge_imagename;
    }

    public void setJudge_imagename(String judge_imagename) {
        this.judge_imagename = judge_imagename;
    }

    String judge_imagename="";
}
