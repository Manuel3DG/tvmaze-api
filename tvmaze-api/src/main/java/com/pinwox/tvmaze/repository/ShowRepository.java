package com.pinwox.tvmaze.repository;

import com.pinwox.tvmaze.model.ShowDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRepository extends MongoRepository<ShowDocument, Long> {
}
