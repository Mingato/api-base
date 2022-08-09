package com.netagentciadigital.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @param <T>
 * @param <ID>
 * Refenrence: https://docs.spring.io/spring-data/jpa/docs/current/reference/html/
 */

@Repository
public interface GenericRepository<T, ID> extends JpaRepository<T, ID> {


}
