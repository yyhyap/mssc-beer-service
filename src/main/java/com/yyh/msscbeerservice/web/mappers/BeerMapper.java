package com.yyh.msscbeerservice.web.mappers;

import com.yyh.msscbeerservice.domain.Beer;
import com.yyh.msscbeerservice.web.model.BeerDto;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class})
public interface BeerMapper {

    BeerDto beerToBeerDto(Beer beer);

    Beer beerDtoToBeer(BeerDto beerDto);

}
