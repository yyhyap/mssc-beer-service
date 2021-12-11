package com.yyh.msscbeerservice.services.inventory;

import com.yyh.msscbeerservice.services.inventory.InventoryFailoverFeignClient;
import com.yyh.msscbeerservice.services.inventory.model.BeerInventoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class InventoryServiceFeignClientFailover implements InventoryServiceFeignClient{

    @Autowired
    private InventoryFailoverFeignClient failoverFeignClient;

    @Override
    public ResponseEntity<List<BeerInventoryDto>> getOnHandInventory(UUID beerId) {
        return failoverFeignClient.getOnHandInventory();
    }
}
