package cn.juhe.zjsb.entity;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;

public class APIResult {
    private SimpleStringProperty reason = new SimpleStringProperty();
    private SimpleIntegerProperty error_code = new SimpleIntegerProperty();
    private SimpleStringProperty birthday = new SimpleStringProperty();
    private Image headImg;
    private SimpleStringProperty sex = new SimpleStringProperty();
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty filename = new SimpleStringProperty();
    private SimpleStringProperty minzu = new SimpleStringProperty();
    private SimpleStringProperty address = new SimpleStringProperty();
    private SimpleStringProperty idno = new SimpleStringProperty();
    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getFilename() {
        return filename.get();
    }

    public SimpleStringProperty filenameProperty() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename.set(filename);
    }

    public APIResult(String filename, String reason, int error_code, String birthday, Image headImg, String sex, String minzu, String address, String idno, String name) {
        this.reason.set(reason);
        this.filename.set(filename);
        this.error_code.set(error_code);
        this.birthday.set(birthday);
        this.headImg = headImg;
        this.sex.set(sex);
        this.minzu.set(minzu);
        this.address.set(address);
        this.idno.set(idno);
        this.name.set(name);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public APIResult() {
    }

    public String getIdno() {
        return idno.get();
    }

    public SimpleStringProperty idnoProperty() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno.set(idno);
    }

    public String getReason() {
        return reason.get();
    }

    public SimpleStringProperty reasonProperty() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason.set(reason);
    }

    public int getError_code() {
        return error_code.get();
    }

    public SimpleIntegerProperty error_codeProperty() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code.set(error_code);
    }

    public String getBirthday() {
        return birthday.get();
    }

    public SimpleStringProperty birthdayProperty() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday.set(birthday);
    }

    public Image getHeadImg() {
        return headImg;
    }


    public void setHeadImg(Image headImg) {
        this.headImg = headImg;
    }

    public String getSex() {
        return sex.get();
    }

    public SimpleStringProperty sexProperty() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex.set(sex);
    }

    public String getMinzu() {
        return minzu.get();
    }

    public SimpleStringProperty minzuProperty() {
        return minzu;
    }

    public void setMinzu(String minzu) {
        this.minzu.set(minzu);
    }

    public String getAddress() {
        return address.get();
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }

    @Override
    public String toString() {
        return "识别结果：【" +
                "reason=" + reason.getValue() +
                ", error_code=" + error_code.getValue() +
                ", birthday=" + birthday.getValue() +
                ", headImg=" + headImg +
                ", sex=" + sex.getValue() +
                ", name=" + name.getValue() +
                ", minzu=" + minzu.getValue() +
                ", address=" + address.getValue() +
                ", idno=" + idno.getValue() +
                "】\n";
    }

    public void setAddress(String address) {
        this.address.set(address);
    }
}
