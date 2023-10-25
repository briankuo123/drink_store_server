package com.mycompany.myapp.domain;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Menu.
 */
@Entity
@Table(name = "menu")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "drink_id", nullable = false, unique = true)
    private UUID drinkId;

    @NotNull
    @Size(max = 50)
    @Column(name = "drink_name", length = 50, nullable = false, unique = true)
    private String drinkName;

    @NotNull
    @Column(name = "sugar", nullable = false)
    private Boolean sugar;

    @NotNull
    @Column(name = "ice", nullable = false)
    private Boolean ice;

    @NotNull
    @Column(name = "hot", nullable = false)
    private Boolean hot;

    @Column(name = "toppings")
    private String toppings;

    @NotNull
    @Column(name = "drink_size", nullable = false)
    private Boolean drinkSize;

    @NotNull
    @Column(name = "drink_price", nullable = false)
    private Integer drinkPrice;

    @Column(name = "drink_picture_url")
    private String drinkPictureURL;

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

    public Menu id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getDrinkId() {
        return this.drinkId;
    }

    public Menu drinkId(UUID drinkId) {
        this.setDrinkId(drinkId);
        return this;
    }

    public void setDrinkId(UUID drinkId) {
        this.drinkId = drinkId;
    }

    public String getDrinkName() {
        return this.drinkName;
    }

    public Menu drinkName(String drinkName) {
        this.setDrinkName(drinkName);
        return this;
    }

    public void setDrinkName(String drinkName) {
        this.drinkName = drinkName;
    }

    public Boolean getSugar() {
        return this.sugar;
    }

    public Menu sugar(Boolean sugar) {
        this.setSugar(sugar);
        return this;
    }

    public void setSugar(Boolean sugar) {
        this.sugar = sugar;
    }

    public Boolean getIce() {
        return this.ice;
    }

    public Menu ice(Boolean ice) {
        this.setIce(ice);
        return this;
    }

    public void setIce(Boolean ice) {
        this.ice = ice;
    }

    public Boolean getHot() {
        return this.hot;
    }

    public Menu hot(Boolean hot) {
        this.setHot(hot);
        return this;
    }

    public void setHot(Boolean hot) {
        this.hot = hot;
    }

    public String getToppings() {
        return this.toppings;
    }

    public Menu toppings(String toppings) {
        this.setToppings(toppings);
        return this;
    }

    public void setToppings(String toppings) {
        this.toppings = toppings;
    }

    public Boolean getDrinkSize() {
        return this.drinkSize;
    }

    public Menu drinkSize(Boolean drinkSize) {
        this.setDrinkSize(drinkSize);
        return this;
    }

    public void setDrinkSize(Boolean drinkSize) {
        this.drinkSize = drinkSize;
    }

    public Integer getDrinkPrice() {
        return this.drinkPrice;
    }

    public Menu drinkPrice(Integer drinkPrice) {
        this.setDrinkPrice(drinkPrice);
        return this;
    }

    public void setDrinkPrice(Integer drinkPrice) {
        this.drinkPrice = drinkPrice;
    }

    public String getDrinkPictureURL() {
        return this.drinkPictureURL;
    }

    public Menu drinkPictureURL(String drinkPictureURL) {
        this.setDrinkPictureURL(drinkPictureURL);
        return this;
    }

    public void setDrinkPictureURL(String drinkPictureURL) {
        this.drinkPictureURL = drinkPictureURL;
    }

    public String getCreateBy() {
        return this.createBy;
    }

    public Menu createBy(String createBy) {
        this.setCreateBy(createBy);
        return this;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Instant getCreateDatetime() {
        return this.createDatetime;
    }

    public Menu createDatetime(Instant createDatetime) {
        this.setCreateDatetime(createDatetime);
        return this;
    }

    public void setCreateDatetime(Instant createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public Menu lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDatetime() {
        return this.lastModifiedDatetime;
    }

    public Menu lastModifiedDatetime(Instant lastModifiedDatetime) {
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
        if (!(o instanceof Menu)) {
            return false;
        }
        return id != null && id.equals(((Menu) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Menu{" +
            "id=" + getId() +
            ", drinkId='" + getDrinkId() + "'" +
            ", drinkName='" + getDrinkName() + "'" +
            ", sugar='" + getSugar() + "'" +
            ", ice='" + getIce() + "'" +
            ", hot='" + getHot() + "'" +
            ", toppings='" + getToppings() + "'" +
            ", drinkSize='" + getDrinkSize() + "'" +
            ", drinkPrice=" + getDrinkPrice() +
            ", drinkPictureURL='" + getDrinkPictureURL() + "'" +
            ", createBy='" + getCreateBy() + "'" +
            ", createDatetime='" + getCreateDatetime() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDatetime='" + getLastModifiedDatetime() + "'" +
            "}";
    }
}
