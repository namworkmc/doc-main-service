package edu.hcmus.doc.mainservice.repository;

import edu.hcmus.doc.mainservice.model.entity.Comment;
import edu.hcmus.doc.mainservice.repository.custom.CustomCommentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends
    JpaRepository<Comment, Long>,
    QuerydslPredicateExecutor<Comment>,
    CustomCommentRepository {

}
