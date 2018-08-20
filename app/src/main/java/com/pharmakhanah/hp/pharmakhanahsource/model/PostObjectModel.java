package com.pharmakhanah.hp.pharmakhanahsource.model;

public class PostObjectModel {
    private String m_id;
    private String m_uid;
    private String m_medicine_name;
    private String m_dosage_form;
    private String m_package_type;
    private String m_production_date;
    private String m_expiration_date;
    private String m_shelf_life;
    private String author_avatar;
    private String author_name;
    private String author_city;
    private Long author_post_time;
    private String author_phone_number;


    public PostObjectModel(String m_uid, String m_medicine_name, String m_dosage_form, String m_package_type,
                           String m_production_date, String m_expiration_date, String m_shelf_life, String author_avatar,
                           String author_name, String author_city, Long author_post_time, String author_phone_number) {
        this.m_uid = m_uid;
        this.m_medicine_name = m_medicine_name;
        this.m_dosage_form = m_dosage_form;
        this.m_package_type = m_package_type;
        this.m_production_date = m_production_date;
        this.m_expiration_date = m_expiration_date;
        this.m_shelf_life = m_shelf_life;
        this.author_avatar = author_avatar;
        this.author_name = author_name;
        this.author_city = author_city;
        this.author_post_time = author_post_time;
        this.author_phone_number = author_phone_number;
    }

    public String getM_uid() {
        return m_uid;
    }

    public void setM_uid(String m_uid) {
        this.m_uid = m_uid;
    }

    public String getM_id() {
        return m_id;
    }

    public void setM_id(String m_id) {
        this.m_id = m_id;
    }

    public String getM_medicine_name() {
        return m_medicine_name;
    }

    public void setM_medicine_name(String m_medicine_name) {
        this.m_medicine_name = m_medicine_name;
    }

    public String getM_dosage_form() {
        return m_dosage_form;
    }

    public void setM_dosage_form(String m_dosage_form) {
        this.m_dosage_form = m_dosage_form;
    }

    public String getM_package_type() {
        return m_package_type;
    }

    public void setM_package_type(String m_package_type) {
        this.m_package_type = m_package_type;
    }

    public String getM_production_date() {
        return m_production_date;
    }

    public void setM_production_date(String m_production_date) {
        this.m_production_date = m_production_date;
    }

    public String getM_expiration_date() {
        return m_expiration_date;
    }

    public void setM_expiration_date(String m_expiration_date) {
        this.m_expiration_date = m_expiration_date;
    }

    public String getM_shelf_life() {
        return m_shelf_life;
    }

    public void setM_shelf_life(String m_shelf_life) {
        this.m_shelf_life = m_shelf_life;
    }

    public String getAuthor_avatar() {
        return author_avatar;
    }

    public void setAuthor_avatar(String author_avatar) {
        this.author_avatar = author_avatar;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getAuthor_city() {
        return author_city;
    }

    public void setAuthor_city(String author_city) {
        this.author_city = author_city;
    }

    public Long getAuthor_post_time() {
        return author_post_time;
    }

    public void setAuthor_post_time(Long author_post_time) {
        this.author_post_time = author_post_time;
    }



    public String getAuthor_phone_number() {
        return author_phone_number;
    }

    public void setAuthor_phone_number(String author_phone_number) {
        this.author_phone_number = author_phone_number;
    }
}
