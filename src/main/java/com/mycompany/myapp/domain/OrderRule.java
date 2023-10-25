package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OrderRule.
 */
@Entity
@Table(name = "order_rule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrderRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "order_status", nullable = false)
    private Boolean orderStatus;

    @NotNull
    @Column(name = "order_frequency", nullable = false)
    private Integer orderFrequency;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrderRule id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getOrderStatus() {
        return this.orderStatus;
    }

    public OrderRule orderStatus(Boolean orderStatus) {
        this.setOrderStatus(orderStatus);
        return this;
    }

    public void setOrderStatus(Boolean orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getOrderFrequency() {
        return this.orderFrequency;
    }

    public OrderRule orderFrequency(Integer orderFrequency) {
        this.setOrderFrequency(orderFrequency);
        return this;
    }

    public void setOrderFrequency(Integer orderFrequency) {
        this.orderFrequency = orderFrequency;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderRule)) {
            return false;
        }
        return id != null && id.equals(((OrderRule) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderRule{" +
            "id=" + getId() +
            ", orderStatus='" + getOrderStatus() + "'" +
            ", orderFrequency=" + getOrderFrequency() +
            "}";
    }
}
