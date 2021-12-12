package com.yyh.msscbeerservice.services.inventory;

import com.yyh.msscbeerservice.services.inventory.model.BeerInventoryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Profile("!local-discovery")
@Slf4j
// @ConfigurationProperties(prefix = "com.yyh", ignoreUnknownFields = true)
@Component
public class BeerInventoryServiceRestTemplateImpl implements BeerInventoryService {
    public static final String INVENTORY_PATH = "/api/v1/beer/{beerId}/inventory";
    private final RestTemplate restTemplate;

    @Value("${com.yyh.beer-inventory-service-host}")
    private String beerInventoryServiceHost;

    @Autowired
    public BeerInventoryServiceRestTemplateImpl(RestTemplateBuilder restTemplateBuilder,
                                                @Value("${com.yyh.inventory-username}") String inventoryUsername,
                                                @Value("${com.yyh.inventory-password}") String inventoryPassword) {
        this.restTemplate = restTemplateBuilder
                .basicAuthentication(inventoryUsername, inventoryPassword)
                .build();
    }

    @Override
    public Integer getOnHandInventory(UUID beerId) {

        log.debug("Calling Inventory Service");

        ResponseEntity<List<BeerInventoryDto>> responseEntity = restTemplate
                .exchange(beerInventoryServiceHost + INVENTORY_PATH, HttpMethod.GET, null,
                        // Response Type
                        new ParameterizedTypeReference<List<BeerInventoryDto>>() {},
                        // uriVariables: {beerId}
                        (Object) beerId);

        Objects.requireNonNull(responseEntity.getBody()).forEach(beerInventoryDto -> {
            System.out.println(beerInventoryDto.getBeerId() + " has quantity " + beerInventoryDto.getQuantityOnHand());
        });

        //sum from inventory list
        Integer onHand = Objects.requireNonNull(responseEntity.getBody())
                .stream()
                // beerInventoryDto -> beerInventoryDto.getQuantityOnHand()
                .mapToInt(BeerInventoryDto::getQuantityOnHand)
                .sum();

        return onHand;
    }
}
