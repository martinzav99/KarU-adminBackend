package com.ungspp1.gadminbackend.model.entity;

import java.math.BigInteger;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "g08_vehicle", schema = "public")
public class VehicleDE {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "g08_id", length = 8, nullable = false)
    private BigInteger id;
    @Column(name = "g08_plate", length = 10, nullable = false)
    private String plate;
    @Column(name = "g08_purchase_price", nullable = true)
    private Float purchasePrice;
    @Column(name = "g08_sell_price", nullable = true) 
    private Float sellPrice;
    @Column(name = "g08_status", length = 40, nullable = false)
    private String status;
    @Column(name = "g08_technical_score", nullable = true)
    private Float score;
    @Column(name = "g08_branch", length = 10, nullable = true)
    private String branch;
    @Column(name = "g08_kilometers", length = 10, nullable = false)
    private String kilometers;
    @Column(name = "g08_message", nullable = true)
    private String message;
    @Column(name = "g08_dni", length = 15, nullable = true)
    private String dni;
    @Column(name = "g08_gnc", nullable = true)
    private Boolean gnc;
    @Column(name = "g08_origin", length = 10, nullable = true)
    private String origin;
    @Column(name = "g08_repair_cost", nullable = true)
    private Float repairCost;
    @Column(name = "g08_picture_1", nullable = true)
    private String picture1;
    @Column(name = "g08_picture_2", nullable = true)
    private String picture2;
    @Column(name = "g08_picture_3", nullable = true)
    private String picture3;
    
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "g08_model_id", nullable = false)
    private ModelDE modelData;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "g08_paperwork_id", nullable = false)
    private PaperworkDE paperworkData;
    
}
