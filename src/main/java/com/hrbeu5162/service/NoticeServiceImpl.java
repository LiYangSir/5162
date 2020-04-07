package com.hrbeu5162.service;

import com.hrbeu5162.dao.NoticeRepository;
import com.hrbeu5162.po.Notice;
import com.hrbeu5162.po.User;
import com.hrbeu5162.util.MyBeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    NoticeRepository noticeRepository;

    @Transactional
    @Override
    public Notice saveNotice(Notice notice) {
        notice.setCreateTime(new Date());
        notice.setUpdateTime(new Date());
        return noticeRepository.save(notice);
    }

    @Transactional
    @Override
    public Notice updateNotice(Long id, Notice notice) {
        Notice notice1 = noticeRepository.findById(id).get();
        BeanUtils.copyProperties(notice, notice1, MyBeanUtils.getNullPropertyNames(notice));
        notice1.setUpdateTime(new Date());
        return noticeRepository.save(notice1);
    }

    @Override
    public Page<Notice> listNotice(Pageable pageable) {
        return noticeRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public void deleteNotice(Long id) {
        noticeRepository.deleteById(id);
    }

    @Override
    public Notice getNotice(Long id) {
        return noticeRepository.findById(id).get();
    }

    @Override
    public List<Notice> listNotice() {
        return noticeRepository.findAllByPublishedTrue();
    }
}
