package com.hrbeu5162.dao;

import com.hrbeu5162.po.IndexSwiper;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface SwiperPepository extends JpaRepository<IndexSwiper, Long> {

    List<IndexSwiper> findAllByShowItTrue();
}
