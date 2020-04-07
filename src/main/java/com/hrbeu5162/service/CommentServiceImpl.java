package com.hrbeu5162.service;

import com.hrbeu5162.dao.CommentRepository;
import com.hrbeu5162.po.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(blogId, Sort.by(Sort.Direction.ASC, "createTime"));
        return eachComment(comments);
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        if (parentCommentId != -1) {
            comment.setParentComment(commentRepository.findById(parentCommentId).get());
        }else
            comment.setParentComment(null);
        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }

    @Override
    public Long countComment() {
        return commentRepository.count();
    }

    private List<Comment> eachComment(List<Comment> comments) {
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment, c);
            commentsView.add(c);
        }

        combineChildren(commentsView);
        return commentsView;
    }

    private void combineChildren(List<Comment> comments) {
        for (Comment comment : comments) {
            List<Comment> replys = comment.getReplyComments();
            for (Comment reply : replys) {
                recursively(reply);
            }
            comment.setReplyComments(tempReplys);
            tempReplys = new ArrayList<>();
        }
    }

    private void recursively(Comment comment) {
//        tempReplys.add(comment);
//        if (comment.getReplyComments().size() > 0) {
//            List<Comment> replys = comment.getReplyComments();
//            for (Comment reply : replys) {
//                tempReplys.add(reply);
//                if (reply.getReplyComments().size()>0)
//                    recursively(reply);
//            }
//        }
        if (comment == null)
            return;
        tempReplys.add(comment);
        List<Comment> replys = comment.getReplyComments();
        for (Comment reply : replys) {
            recursively(reply);
        }
    }

    private List<Comment> tempReplys = new ArrayList<>();
}
