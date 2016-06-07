package com.ilyamur.topaz.mybatis.entity;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.time.LocalDate;
import java.util.Set;

public class User {

    private Long idUser;
    private String login;
    private String email;
    private LocalDate birthday;
    private Set<Role> roles;

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("idUser", idUser)
                .add("login", login)
                .add("email", email)
                .add("birthday", birthday)
                .add("roles", roles)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equal(idUser, user.idUser) &&
                Objects.equal(login, user.login) &&
                Objects.equal(email, user.email) &&
                Objects.equal(birthday, user.birthday) &&
                Objects.equal(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idUser, login, email, birthday, roles);
    }
}
