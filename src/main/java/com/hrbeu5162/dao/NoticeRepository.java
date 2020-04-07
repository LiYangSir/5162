package com.hrbeu5162.dao;

import com.hrbeu5162.po.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    List<Notice> findAllByPublishedTrue();
}
