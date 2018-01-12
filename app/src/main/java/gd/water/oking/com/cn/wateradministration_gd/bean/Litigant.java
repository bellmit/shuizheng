package gd.water.oking.com.cn.wateradministration_gd.bean;

/**
 * Created by zhao on 2017-5-19.
 */

public class Litigant {

    private String name;
    private String sex;
    private String birthday;
    private int age;
    private String photo;
    private String idCard;
    private String work;
    private String address;

    public Litigant() {
    }

    public Litigant(String name, String sex, String birthday, int age, String photo, String idCard, String work, String address) {
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
        this.age = age;
        this.photo = photo;
        this.idCard = idCard;
        this.work = work;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
