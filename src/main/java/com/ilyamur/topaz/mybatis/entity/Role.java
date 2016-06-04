package com.ilyamur.topaz.mybatis.entity;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class Role {

    public final static Role REGISTERED_USER;
    public final static Role ADMIN;

    static {

        REGISTERED_USER = new Role();
        REGISTERED_USER.setIdRole(0);
        REGISTERED_USER.setName("REGISTERED_USER");

        ADMIN = new Role();
        ADMIN.setIdRole(1);
        ADMIN.setName("ADMIN");
    }

    private int idRole;
    private String name;

    public int getIdRole() {
        return idRole;
    }

    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("idRole", idRole)
                .add("name", name)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return idRole == role.idRole &&
                Objects.equal(name, role.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idRole, name);
    }
}
