package com.yyh.msscbeerservice.services;

import com.yyh.msscbeerservice.web.model.BeerDto;
import com.yyh.msscbeerservice.web.model.BeerPagedList;
import com.yyh.msscbeerservice.web.model.BeerStyleEnum;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface BeerService {
    BeerPagedList listBeers(String beerName, String beerStyle, PageRequest pageRequest);

    BeerDto getById(UUID beerId);

    BeerDto saveNewBeer(BeerDto beerDto);

    BeerDto updateBeer(UUID beerId, BeerDto beerDto);
}
