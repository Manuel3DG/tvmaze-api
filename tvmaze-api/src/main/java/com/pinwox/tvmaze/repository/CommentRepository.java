package com.pinwox.tvmaze.repository;

import com.pinwox.tvmaze.model.CommentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<CommentDocument, String> {
    List<CommentDocument> findByShowId(Long showId);
    List<CommentDocument> findByShowIdIn(Collection<Long> showIds);
}
