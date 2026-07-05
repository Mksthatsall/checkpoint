package com.mks.projects.checkpointapp.strategy;

import com.mks.projects.checkpointapp.entity.Inventory;

import java.math.BigDecimal;

public interface PricingStrategy {
    BigDecimal calculatePrice(Inventory inventory);


}
