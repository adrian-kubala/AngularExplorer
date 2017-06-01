package com.adriankubala.repository;

import com.adriankubala.domain.Root;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Root entity.
 */
@SuppressWarnings("unused")
public interface RootRepository extends JpaRepository<Root,Long> {

}
