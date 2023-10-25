package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Coupon.
 */
@Entity
@Table(name = "coupon")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Coupon implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "coupon_id", nullable = false, unique = true)
    private UUID couponId;

    @NotNull
    @Column(name = "coupon_code", nullable = false, unique = true)
    private String couponCode;

    @NotNull
    @Column(name = "coupon_value", nullable = false)
    private Integer couponValue;

    @Column(name = "coupon_use_times")
    private Integer couponUseTimes;

    @Column(name = "coupon_expire_datetime")
    private Instant couponExpireDatetime;

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

    public Coupon id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getCouponId() {
        return this.couponId;
    }

    public Coupon couponId(UUID couponId) {
        this.setCouponId(couponId);
        return this;
    }

    public void setCouponId(UUID couponId) {
        this.couponId = couponId;
    }

    public String getCouponCode() {
        return this.couponCode;
    }

    public Coupon couponCode(String couponCode) {
        this.setCouponCode(couponCode);
        return this;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public Integer getCouponValue() {
        return this.couponValue;
    }

    public Coupon couponValue(Integer couponValue) {
        this.setCouponValue(couponValue);
        return this;
    }

    public void setCouponValue(Integer couponValue) {
        this.couponValue = couponValue;
    }

    public Integer getCouponUseTimes() {
        return this.couponUseTimes;
    }

    public Coupon couponUseTimes(Integer couponUseTimes) {
        this.setCouponUseTimes(couponUseTimes);
        return this;
    }

    public void setCouponUseTimes(Integer couponUseTimes) {
        this.couponUseTimes = couponUseTimes;
    }

    public Instant getCouponExpireDatetime() {
        return this.couponExpireDatetime;
    }

    public Coupon couponExpireDatetime(Instant couponExpireDatetime) {
        this.setCouponExpireDatetime(couponExpireDatetime);
        return this;
    }

    public void setCouponExpireDatetime(Instant couponExpireDatetime) {
        this.couponExpireDatetime = couponExpireDatetime;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public Coupon createBy(String createBy) {
        this.setCreateBy(createBy);
        return this;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Instant getCreateDatetime() {
        return this.createDatetime;
    }

    public Coupon createDatetime(Instant createDatetime) {
        this.setCreateDatetime(createDatetime);
        return this;
    }

    public void setCreateDatetime(Instant createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Coupon lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDatetime() {
        return this.lastModifiedDatetime;
    }

    public Coupon lastModifiedDatetime(Instant lastModifiedDatetime) {
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
        if (!(o instanceof Coupon)) {
            return false;
        }
        return id != null && id.equals(((Coupon) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Coupon{" +
            "id=" + getId() +
            ", couponId='" + getCouponId() + "'" +
            ", couponCode='" + getCouponCode() + "'" +
            ", couponValue=" + getCouponValue() +
            ", couponUseTimes=" + getCouponUseTimes() +
            ", couponExpireDatetime='" + getCouponExpireDatetime() + "'" +
            ", createBy='" + getCreateBy() + "'" +
            ", createDatetime='" + getCreateDatetime() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDatetime='" + getLastModifiedDatetime() + "'" +
            "}";
    }
}
