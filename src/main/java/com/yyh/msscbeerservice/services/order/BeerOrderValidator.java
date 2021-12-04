package com.yyh.msscbeerservice.services.order;

import com.yyh.brewery.model.BeerOrderDto;
import com.yyh.msscbeerservice.repositories.BeerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class BeerOrderValidator {

    @Autowired
    private BeerRepository beerRepository;

    public Boolean validateOrder(BeerOrderDto beerOrderDto) {

        AtomicInteger beersNotFound = new AtomicInteger();

        beerOrderDto.getBeerOrderLines().forEach(beerOrderLineDto -> {
            if(beerRepository.findByUpc(beerOrderLineDto.getUpc()) == null) {
                beersNotFound.incrementAndGet();
            }
        });

        return beersNotFound.get() == 0;
    }
}
