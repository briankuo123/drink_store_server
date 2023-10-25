package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Order.
 */
@Entity
@Table(name = "jhi_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "order_id", nullable = false, unique = true)
    private UUID orderId;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "pay_method")
    private String payMethod;

    @Column(name = "delivery_location")
    private String deliveryLocation;

    @Column(name = "total_price")
    private Integer totalPrice;

    @Column(name = "delivery_time")
    private Instant deliveryTime;

    @Column(name = "order_status")
    private String orderStatus;

    @Column(name = "coupon")
    private String coupon;

    @Size(max = 50)
    @Column(name = "create_by", length = 50)
    private String createBy;

    @Column(name = "create_datetime")
    private Instant createDatetime;

    @Size(max = 50)
    @Column(name = "last_modified_by", length = 50)
    private String lastModifiedBy;

    @Column(name = "last_modified_datetime")
    private Instant lastModifiedDatetime;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Order id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getOrderId() {
        return this.orderId;
    }

    public Order orderId(UUID orderId) {
        this.setOrderId(orderId);
        return this;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public UUID getUserId() {
        return this.userId;
    }

    public Order userId(UUID userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getPayMethod() {
        return this.payMethod;
    }

    public Order payMethod(String payMethod) {
        this.setPayMethod(payMethod);
        return this;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getDeliveryLocation() {
        return this.deliveryLocation;
    }

    public Order deliveryLocation(String deliveryLocation) {
        this.setDeliveryLocation(deliveryLocation);
        return this;
    }

    public void setDeliveryLocation(String deliveryLocation) {
        this.deliveryLocation = deliveryLocation;
    }

    public Integer getTotalPrice() {
        return this.totalPrice;
    }

    public Order totalPrice(Integer totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Instant getDeliveryTime() {
        return this.deliveryTime;
    }

    public Order deliveryTime(Instant deliveryTime) {
        this.setDeliveryTime(deliveryTime);
        return this;
    }

    public void setDeliveryTime(Instant deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getOrderStatus() {
        return this.orderStatus;
    }

    public Order orderStatus(String orderStatus) {
        this.setOrderStatus(orderStatus);
        return this;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCoupon() {
        return this.coupon;
    }

    public Order coupon(String coupon) {
        this.setCoupon(coupon);
        return this;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public Order createBy(String createBy) {
        this.setCreateBy(createBy);
        return this;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Instant getCreateDatetime() {
        return this.createDatetime;
    }

    public Order createDatetime(Instant createDatetime) {
        this.setCreateDatetime(createDatetime);
        return this;
    }

    public void setCreateDatetime(Instant createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Order lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDatetime() {
        return this.lastModifiedDatetime;
    }

    public Order lastModifiedDatetime(Instant lastModifiedDatetime) {
        this.setLastModifiedDatetime(lastModifiedDatetime);
        return this;
    }

    public void setLastModifiedDatetime(Instant lastModifiedDatetime) {
        this.lastModifiedDatetime = lastModifiedDatetime;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return id != null && id.equals(((Order) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", orderId='" + getOrderId() + "'" +
            ", userId='" + getUserId() + "'" +
            ", payMethod='" + getPayMethod() + "'" +
            ", deliveryLocation='" + getDeliveryLocation() + "'" +
            ", totalPrice=" + getTotalPrice() +
            ", deliveryTime='" + getDeliveryTime() + "'" +
            ", orderStatus='" + getOrderStatus() + "'" +
            ", coupon='" + getCoupon() + "'" +
            ", createBy='" + getCreateBy() + "'" +
            ", createDatetime='" + getCreateDatetime() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDatetime='" + getLastModifiedDatetime() + "'" +
            "}";
    }
}
