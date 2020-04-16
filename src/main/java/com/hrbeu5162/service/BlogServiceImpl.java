package com.hrbeu5162.service;

import com.hrbeu5162.NotFoundException;
import com.hrbeu5162.dao.BlogRepository;
import com.hrbeu5162.dao.UserRepository;
import com.hrbeu5162.dao.WebBasicRepository;
import com.hrbeu5162.po.Blog;
import com.hrbeu5162.po.Type;
import com.hrbeu5162.po.User;
import com.hrbeu5162.po.WebBasic;
import com.hrbeu5162.util.MarkDownUtils;
import com.hrbeu5162.util.MyBeanUtils;
import com.hrbeu5162.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebBasicRepository webBasicRepository;

    @Transactional
    @Override
    public Blog getBlog(Long id) {
        return blogRepository.findById(id).get();
    }

    //引入缓存技术
    @Transactional
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    predicates.add(criteriaBuilder.like(root.<String>get("title"), "%"+blog.getTitle() + "%"));
                }
                if (blog.getTypeId() != null) {
                    predicates.add(criteriaBuilder.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                if (blog.isPublished()) {
                    predicates.add(criteriaBuilder.equal(root.<Boolean>get("published"), blog.isPublished()));
                }
                if (blog.getUserId()!=null)
                    predicates.add(criteriaBuilder.equal(root.<User>get("user").get("id"), blog.getUserId()));
                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null) {
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        }else {
            blog.setUpdateTime(new Date());
        }
        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog blog1 = blogRepository.findById(id).get();
        if (blog1 == null) {
            throw new NotFoundException("该博客不存在");
        }
        BeanUtils.copyProperties(blog, blog1, MyBeanUtils.getNullPropertyNames(blog));
        blog1.setUpdateTime(new Date());
        return blogRepository.save(blog1);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(Long id, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>(){
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Join join = root.join("tags");
                return criteriaBuilder.equal(join.get("id"), id);
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listUserBlog(Long id, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>(){
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Join join = root.join("user");
                return criteriaBuilder.equal(join.get("id"), id);
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {

        return blogRepository.findByQuery(query, pageable);
    }

    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.findById(id).get();
        if (blog == null)
            throw new NotFoundException("博客不存在");
        Blog b = new Blog();
        BeanUtils.copyProperties(blog, b);
        String content = b.getContent();
        b.setContent(MarkDownUtils.markdownToHtmlExtensions(content));
        blogRepository.updateViews(id);
        return b;
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        return blogRepository.findTop(pageable);
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = blogRepository.findGroupYears();
        Map<String, List<Blog>> map = new HashMap<>();
        for (String year : years) {
            map.put(year, blogRepository.findByYear(year));
        }
        return map;
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public boolean getQingmingFlag() {
        WebBasic webBasic = webBasicRepository.findById(1L).get();
        return webBasic.isTomb();
    }
}
