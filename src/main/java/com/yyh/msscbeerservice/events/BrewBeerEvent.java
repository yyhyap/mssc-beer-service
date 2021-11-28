package com.yyh.msscbeerservice.events;

import com.yyh.msscbeerservice.web.model.BeerDto;

public class BrewBeerEvent extends BeerEvent{
    public BrewBeerEvent(BeerDto beerDto) {
        super(beerDto);
    }
}
