package model.pojo;

import java.io.Serializable;
import java.util.Date;

public class TrafficPackage implements Serializable {

    private PackageType type;
    private Date date;
    private PackageInterface event;
    private User user;

    public TrafficPackage(PackageType type, Date date, PackageInterface event, User user) {
        this.type = type;
        this.date = date;
        this.event = event;
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PackageType getType() {
        return this.type;
    }

    public void setType(PackageType type) {
        this.type = type;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public PackageInterface getEvent() {
        return this.event;
    }

    public void setEvent(PackageInterface event) {
        this.event = event;
    }

}
