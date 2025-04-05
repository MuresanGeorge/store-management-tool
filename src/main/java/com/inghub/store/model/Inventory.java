package com.inghub.store.model;

import jakarta.persistence.*;

public class Inventory {

    @Id
    private Long id;
    @Column
    private int stock;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Product product;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
