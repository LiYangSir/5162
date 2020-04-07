package com.hrbeu5162.service;

import com.hrbeu5162.po.Comment;

import java.util.ArrayList;
import java.util.List;

public interface CommentService {
    List<Comment> listCommentByBlogId(Long blogId);

    Comment saveComment(Comment comment);

    Long countComment();
}
