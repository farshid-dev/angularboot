package com.avasyspod.angularboot.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Features {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private long id;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "read_option")
    private String readOption;
    @Basic
    @Column(name = "read_write_option")
    private String readWriteOption;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReadOption() {
        return readOption;
    }

    public void setReadOption(String readOption) {
        this.readOption = readOption;
    }

    public String getReadWriteOption() {
        return readWriteOption;
    }

    public void setReadWriteOption(String readWriteOption) {
        this.readWriteOption = readWriteOption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Features features = (Features) o;
        return id == features.id && Objects.equals(name, features.name) && Objects.equals(readOption, features.readOption) && Objects.equals(readWriteOption, features.readWriteOption);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, readOption, readWriteOption);
    }
}
