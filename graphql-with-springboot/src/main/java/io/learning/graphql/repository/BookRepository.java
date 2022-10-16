package io.learning.graphql.repository;

import io.learning.graphql.domain.Book;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "books_v1", path = "books_v1")
public interface BookRepository extends CrudRepository<Book, Long> {
}
