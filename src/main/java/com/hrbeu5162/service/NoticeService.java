package com.hrbeu5162.service;

import com.hrbeu5162.po.Notice;
import com.hrbeu5162.po.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NoticeService {

    Notice saveNotice(Notice notice);

    Notice updateNotice(Long id, Notice notice);

    void deleteNotice(Long id);

    Notice getNotice(Long id);

    Page<Notice> listNotice(Pageable pageable);

    List<Notice> listNotice();
}
