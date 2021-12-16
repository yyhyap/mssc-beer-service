package com.yyh.msscbeerservice.services;

import com.yyh.brewery.model.BeerDto;
import com.yyh.brewery.model.BeerPagedList;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface BeerService {
    CompletableFuture<BeerPagedList> listBeers(String beerName, String beerStyle, PageRequest pageRequest, Boolean showInventoryOnHand);

    BeerDto getById(UUID beerId, Boolean showInventoryOnHand);

    BeerDto saveNewBeer(BeerDto beerDto);

    BeerDto updateBeer(UUID beerId, BeerDto beerDto);

    BeerDto getByUpc(String upc, Boolean showInventoryOnHand);
}
