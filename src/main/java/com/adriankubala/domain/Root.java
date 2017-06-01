package com.adriankubala.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Root.
 */
@Entity
@Table(name = "root")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Root implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "root")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Directory> directories = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Root name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Directory> getDirectories() {
        return directories;
    }

    public Root directories(Set<Directory> directories) {
        this.directories = directories;
        return this;
    }

    public Root addDirectory(Directory directory) {
        this.directories.add(directory);
        directory.setRoot(this);
        return this;
    }

    public Root removeDirectory(Directory directory) {
        this.directories.remove(directory);
        directory.setRoot(null);
        return this;
    }

    public void setDirectories(Set<Directory> directories) {
        this.directories = directories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Root root = (Root) o;
        if (root.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, root.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Root{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
