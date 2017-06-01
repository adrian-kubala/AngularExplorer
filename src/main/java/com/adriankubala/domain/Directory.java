package com.adriankubala.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Directory.
 */
@Entity
@Table(name = "directory")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Directory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(optional = false)
    @NotNull
    private Root root;

    @OneToMany(mappedBy = "directory")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<File> files = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Directory name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Root getRoot() {
        return root;
    }

    public Directory root(Root root) {
        this.root = root;
        return this;
    }

    public void setRoot(Root root) {
        this.root = root;
    }

    public Set<File> getFiles() {
        return files;
    }

    public Directory files(Set<File> files) {
        this.files = files;
        return this;
    }

    public Directory addFile(File file) {
        this.files.add(file);
        file.setDirectory(this);
        return this;
    }

    public Directory removeFile(File file) {
        this.files.remove(file);
        file.setDirectory(null);
        return this;
    }

    public void setFiles(Set<File> files) {
        this.files = files;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Directory directory = (Directory) o;
        if (directory.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, directory.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Directory{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
