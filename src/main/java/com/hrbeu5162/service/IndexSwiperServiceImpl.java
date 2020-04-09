package com.hrbeu5162.service;

import com.hrbeu5162.dao.SwiperPepository;
import com.hrbeu5162.po.IndexSwiper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndexSwiperServiceImpl implements IndexSwiperService {

    @Autowired
    SwiperPepository swiperPepository;

    @Override
    public List<IndexSwiper> listIndexSwiperSrc() {
        return swiperPepository.findAllByShowItTrue();
    }
}
