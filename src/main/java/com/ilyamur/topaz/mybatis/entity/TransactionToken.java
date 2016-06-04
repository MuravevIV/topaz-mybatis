package com.ilyamur.topaz.mybatis.entity;

import com.google.common.base.MoreObjects;

public class TransactionToken {

    private long id = -1;
    private String transaction = "";
    private String token = "";

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("transaction", transaction)
                .add("token", token)
                .toString();
    }
}
