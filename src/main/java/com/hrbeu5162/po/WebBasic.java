package com.hrbeu5162.po;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_basic")
public class WebBasic {
    @Id
    @GeneratedValue
    private  Long id;

    private boolean tomb;

    private String labIntroduction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getLabIntroduction() {
        return labIntroduction;
    }

    public void setLabIntroduction(String labIntroduction) {
        this.labIntroduction = labIntroduction;
    }

    public boolean isTomb() {
        return tomb;
    }

    public void setTomb(boolean tomb) {
        this.tomb = tomb;
    }

    @Override
    public String toString() {
        return "WebBasic{" +
                "id=" + id +
                ", tomb=" + tomb +
                ", labIntroduction='" + labIntroduction + '\'' +
                '}';
    }
}
