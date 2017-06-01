package com.adriankubala.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.adriankubala.domain.Root;

import com.adriankubala.repository.RootRepository;
import com.adriankubala.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Root.
 */
@RestController
@RequestMapping("/api")
public class RootResource {

    private final Logger log = LoggerFactory.getLogger(RootResource.class);

    private static final String ENTITY_NAME = "root";
        
    private final RootRepository rootRepository;

    public RootResource(RootRepository rootRepository) {
        this.rootRepository = rootRepository;
    }

    /**
     * POST  /roots : Create a new root.
     *
     * @param root the root to create
     * @return the ResponseEntity with status 201 (Created) and with body the new root, or with status 400 (Bad Request) if the root has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/roots")
    @Timed
    public ResponseEntity<Root> createRoot(@RequestBody Root root) throws URISyntaxException {
        log.debug("REST request to save Root : {}", root);
        if (root.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new root cannot already have an ID")).body(null);
        }
        Root result = rootRepository.save(root);
        return ResponseEntity.created(new URI("/api/roots/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /roots : Updates an existing root.
     *
     * @param root the root to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated root,
     * or with status 400 (Bad Request) if the root is not valid,
     * or with status 500 (Internal Server Error) if the root couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/roots")
    @Timed
    public ResponseEntity<Root> updateRoot(@RequestBody Root root) throws URISyntaxException {
        log.debug("REST request to update Root : {}", root);
        if (root.getId() == null) {
            return createRoot(root);
        }
        Root result = rootRepository.save(root);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, root.getId().toString()))
            .body(result);
    }

    /**
     * GET  /roots : get all the roots.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of roots in body
     */
    @GetMapping("/roots")
    @Timed
    public List<Root> getAllRoots() {
        log.debug("REST request to get all Roots");
        List<Root> roots = rootRepository.findAll();
        return roots;
    }

    /**
     * GET  /roots/:id : get the "id" root.
     *
     * @param id the id of the root to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the root, or with status 404 (Not Found)
     */
    @GetMapping("/roots/{id}")
    @Timed
    public ResponseEntity<Root> getRoot(@PathVariable Long id) {
        log.debug("REST request to get Root : {}", id);
        Root root = rootRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(root));
    }

    /**
     * DELETE  /roots/:id : delete the "id" root.
     *
     * @param id the id of the root to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/roots/{id}")
    @Timed
    public ResponseEntity<Void> deleteRoot(@PathVariable Long id) {
        log.debug("REST request to delete Root : {}", id);
        rootRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}
