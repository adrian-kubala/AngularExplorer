package com.adriankubala.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.adriankubala.domain.Directory;

import com.adriankubala.repository.DirectoryRepository;
import com.adriankubala.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Directory.
 */
@RestController
@RequestMapping("/api")
public class DirectoryResource {

    private final Logger log = LoggerFactory.getLogger(DirectoryResource.class);

    private static final String ENTITY_NAME = "directory";
        
    private final DirectoryRepository directoryRepository;

    public DirectoryResource(DirectoryRepository directoryRepository) {
        this.directoryRepository = directoryRepository;
    }

    /**
     * POST  /directories : Create a new directory.
     *
     * @param directory the directory to create
     * @return the ResponseEntity with status 201 (Created) and with body the new directory, or with status 400 (Bad Request) if the directory has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/directories")
    @Timed
    public ResponseEntity<Directory> createDirectory(@Valid @RequestBody Directory directory) throws URISyntaxException {
        log.debug("REST request to save Directory : {}", directory);
        if (directory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new directory cannot already have an ID")).body(null);
        }
        Directory result = directoryRepository.save(directory);
        return ResponseEntity.created(new URI("/api/directories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /directories : Updates an existing directory.
     *
     * @param directory the directory to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated directory,
     * or with status 400 (Bad Request) if the directory is not valid,
     * or with status 500 (Internal Server Error) if the directory couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/directories")
    @Timed
    public ResponseEntity<Directory> updateDirectory(@Valid @RequestBody Directory directory) throws URISyntaxException {
        log.debug("REST request to update Directory : {}", directory);
        if (directory.getId() == null) {
            return createDirectory(directory);
        }
        Directory result = directoryRepository.save(directory);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, directory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /directories : get all the directories.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of directories in body
     */
    @GetMapping("/directories")
    @Timed
    public List<Directory> getAllDirectories() {
        log.debug("REST request to get all Directories");
        List<Directory> directories = directoryRepository.findAll();
        return directories;
    }

    /**
     * GET  /directories/:id : get the "id" directory.
     *
     * @param id the id of the directory to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the directory, or with status 404 (Not Found)
     */
    @GetMapping("/directories/{id}")
    @Timed
    public ResponseEntity<Directory> getDirectory(@PathVariable Long id) {
        log.debug("REST request to get Directory : {}", id);
        Directory directory = directoryRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(directory));
    }

    /**
     * DELETE  /directories/:id : delete the "id" directory.
     *
     * @param id the id of the directory to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/directories/{id}")
    @Timed
    public ResponseEntity<Void> deleteDirectory(@PathVariable Long id) {
        log.debug("REST request to delete Directory : {}", id);
        directoryRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
