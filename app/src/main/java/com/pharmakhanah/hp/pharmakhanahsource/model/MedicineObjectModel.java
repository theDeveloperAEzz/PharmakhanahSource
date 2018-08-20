package com.pharmakhanah.hp.pharmakhanahsource.model;

public class MedicineObjectModel {
    private String id,
            generic_name,
            trade_name,
            dosage_form,
            route_of_administration,
            package_type,
            product_control,
            shelf_life_month,
            storage_conditions;


    public MedicineObjectModel(String id, String generic_name, String trade_name, String dosage_form, String route_of_administration, String package_type, String product_control, String shelf_life_month, String storage_conditions) {
        this.id = id;
        this.generic_name = generic_name;
        this.trade_name = trade_name;
        this.dosage_form = dosage_form;
        this.route_of_administration = route_of_administration;
        this.package_type = package_type;
        this.product_control = product_control;
        this.shelf_life_month = shelf_life_month;
        this.storage_conditions = storage_conditions;
    }

    public MedicineObjectModel() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGeneric_name() {
        return generic_name;
    }

    public void setGeneric_name(String generic_name) {
        this.generic_name = generic_name;
    }

    public String getTrade_name() {
        return trade_name;
    }

    public void setTrade_name(String trade_name) {
        this.trade_name = trade_name;
    }

    public String getDosage_form() {
        return dosage_form;
    }

    public void setDosage_form(String dosage_form) {
        this.dosage_form = dosage_form;
    }

    public String getRoute_of_administration() {
        return route_of_administration;
    }

    public void setRoute_of_administration(String route_of_administration) {
        this.route_of_administration = route_of_administration;
    }

    public String getPackage_type() {
        return package_type;
    }

    public void setPackage_type(String package_type) {
        this.package_type = package_type;
    }

    public String getProduct_control() {
        return product_control;
    }

    public void setProduct_control(String product_control) {
        this.product_control = product_control;
    }

    public String getShelf_life_month() {
        return shelf_life_month;
    }

    public void setShelf_life_month(String shelf_life_month) {
        this.shelf_life_month = shelf_life_month;
    }

    public String getStorage_conditions() {
        return storage_conditions;
    }

    public void setStorage_conditions(String storage_conditions) {
        this.storage_conditions = storage_conditions;
    }
}
