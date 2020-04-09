package com.hrbeu5162.po;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "t_swiper")
public class IndexSwiper {

    @Id
    @GeneratedValue
    private  Long id;

    private String swiperSrc;

    private boolean showIt;

    public boolean isShowIt() {
        return showIt;
    }

    public void setShowIt(boolean showIt) {
        this.showIt = showIt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSwiperSrc() {
        return swiperSrc;
    }

    public void setSwiperSrc(String swiperSrc) {
        this.swiperSrc = swiperSrc;
    }

    @Override
    public String toString() {
        return "IndexSwiper{" +
                "id=" + id +
                ", swiperSrc='" + swiperSrc + '\'' +
                ", showIt=" + showIt +
                '}';
    }
}
