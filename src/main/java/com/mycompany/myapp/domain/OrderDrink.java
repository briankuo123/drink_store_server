package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OrderDrink.
 */
@Entity
@Table(name = "order_drink")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderDrink implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "order_id", nullable = false)
    private UUID orderId;

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

    @Column(name = "drink_price")
    private Integer drinkPrice;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderDrink id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getOrderId() {
        return this.orderId;
    }

    public OrderDrink orderId(UUID orderId) {
        this.setOrderId(orderId);
        return this;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getDrinkId() {
        return this.drinkId;
    }

    public OrderDrink drinkId(UUID drinkId) {
        this.setDrinkId(drinkId);
        return this;
    }

    public void setDrinkId(UUID drinkId) {
        this.drinkId = drinkId;
    }

    public String getSugar() {
        return this.sugar;
    }

    public OrderDrink sugar(String sugar) {
        this.setSugar(sugar);
        return this;
    }

    public void setSugar(String sugar) {
        this.sugar = sugar;
    }

    public String getIce() {
        return this.ice;
    }

    public OrderDrink ice(String ice) {
        this.setIce(ice);
        return this;
    }

    public void setIce(String ice) {
        this.ice = ice;
    }

    public String getToppings() {
        return this.toppings;
    }

    public OrderDrink toppings(String toppings) {
        this.setToppings(toppings);
        return this;
    }

    public void setToppings(String toppings) {
        this.toppings = toppings;
    }

    public String getDrinkSize() {
        return this.drinkSize;
    }

    public OrderDrink drinkSize(String drinkSize) {
        this.setDrinkSize(drinkSize);
        return this;
    }

    public void setDrinkSize(String drinkSize) {
        this.drinkSize = drinkSize;
    }

    public Integer getDrinkPrice() {
        return this.drinkPrice;
    }

    public OrderDrink drinkPrice(Integer drinkPrice) {
        this.setDrinkPrice(drinkPrice);
        return this;
    }

    public void setDrinkPrice(Integer drinkPrice) {
        this.drinkPrice = drinkPrice;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderDrink)) {
            return false;
        }
        return id != null && id.equals(((OrderDrink) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderDrink{" +
            "id=" + getId() +
            ", orderId='" + getOrderId() + "'" +
            ", drinkId='" + getDrinkId() + "'" +
            ", sugar='" + getSugar() + "'" +
            ", ice='" + getIce() + "'" +
            ", toppings='" + getToppings() + "'" +
            ", drinkSize='" + getDrinkSize() + "'" +
            ", drinkPrice=" + getDrinkPrice() +
            "}";
    }
}
