package io.learning.graphql.repository;

import io.learning.graphql.domain.Author;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "authors", path = "authors")
public interface AuthorRepository extends CrudRepository<Author, Long> {
}
