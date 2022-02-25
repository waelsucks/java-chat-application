package model.pojo;

import java.util.Date;

public class TrafficPackage {
    
    private PackageType type;
    private Date date;
    private PackageInterface event;

    
    public TrafficPackage(PackageType type, Date date, PackageInterface event) {
        this.type = type;
        this.date = date;
        this.event = event;
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
