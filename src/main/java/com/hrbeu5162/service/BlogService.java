package com.hrbeu5162.service;

import com.hrbeu5162.po.Blog;
import com.hrbeu5162.po.WebBasic;
import com.hrbeu5162.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;


public interface BlogService {
    Blog getBlog(Long id);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    Page<Blog> listBlog(Pageable pageable);

    Page<Blog> listBlog(Long id, Pageable pageable);

    Page<Blog> listBlog(String query, Pageable pageable);

    Page<Blog> listUserBlog(Long id, Pageable pageable);

    Blog getAndConvert(Long id);

    List<Blog> listRecommendBlogTop(Integer size);

    Map<String, List<Blog>> archiveBlog();

    Long countBlog();

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id, Blog blog);

    void deleteBlog(Long id);

    boolean getQingmingFlag();
}
