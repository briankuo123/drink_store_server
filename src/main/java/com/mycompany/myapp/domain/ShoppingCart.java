package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ShoppingCart.
 */
@Entity
@Table(name = "shopping_cart")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @NotNull
    @Column(name = "drink_id", nullable = false)
    private UUID drinkId;

    @Column(name = "sugar")
    private String sugar;

    @Column(name = "ice")
    private String ice;

    @Column(name = "toppings")
    private String toppings;

    @Column(name = "drink_size")
    private String drinkSize;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ShoppingCart id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUserId() {
        return this.userId;
    }

    public ShoppingCart userId(UUID userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getDrinkId() {
        return this.drinkId;
    }

    public ShoppingCart drinkId(UUID drinkId) {
        this.setDrinkId(drinkId);
        return this;
    }

    public void setDrinkId(UUID drinkId) {
        this.drinkId = drinkId;
    }

    public String getSugar() {
        return this.sugar;
    }

    public ShoppingCart sugar(String sugar) {
        this.setSugar(sugar);
        return this;
    }

    public void setSugar(String sugar) {
        this.sugar = sugar;
    }

    public String getIce() {
        return this.ice;
    }

    public ShoppingCart ice(String ice) {
        this.setIce(ice);
        return this;
    }

    public void setIce(String ice) {
        this.ice = ice;
    }

    public String getToppings() {
        return this.toppings;
    }

    public ShoppingCart toppings(String toppings) {
        this.setToppings(toppings);
        return this;
    }

    public void setToppings(String toppings) {
        this.toppings = toppings;
    }

    public String getDrinkSize() {
        return this.drinkSize;
    }

    public ShoppingCart drinkSize(String drinkSize) {
        this.setDrinkSize(drinkSize);
        return this;
    }

    public void setDrinkSize(String drinkSize) {
        this.drinkSize = drinkSize;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShoppingCart)) {
            return false;
        }
        return id != null && id.equals(((ShoppingCart) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShoppingCart{" +
            "id=" + getId() +
            ", userId='" + getUserId() + "'" +
            ", drinkId='" + getDrinkId() + "'" +
            ", sugar='" + getSugar() + "'" +
            ", ice='" + getIce() + "'" +
            ", toppings='" + getToppings() + "'" +
            ", drinkSize='" + getDrinkSize() + "'" +
            "}";
    }
}
