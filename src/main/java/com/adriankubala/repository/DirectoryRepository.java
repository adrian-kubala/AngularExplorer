package com.adriankubala.repository;

import com.adriankubala.domain.Directory;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Directory entity.
 */
@SuppressWarnings("unused")
public interface DirectoryRepository extends JpaRepository<Directory,Long> {

}
