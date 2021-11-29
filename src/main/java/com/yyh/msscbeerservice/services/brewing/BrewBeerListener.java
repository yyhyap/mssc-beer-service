package com.yyh.msscbeerservice.services.brewing;

import com.yyh.msscbeerservice.config.JmsConfig;
import com.yyh.msscbeerservice.domain.Beer;
import com.yyh.msscbeerservice.events.BrewBeerEvent;
import com.yyh.msscbeerservice.events.NewInventoryEvent;
import com.yyh.msscbeerservice.repositories.BeerRepository;
import com.yyh.msscbeerservice.web.model.BeerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BrewBeerListener {

    @Autowired
    private BeerRepository beerRepository;

    @Autowired
    private JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.BREWING_REQUEST_QUEUE)
    public void listen(BrewBeerEvent event) {
        BeerDto beerDto = event.getBeerDto();

        Beer beer = beerRepository.getById(beerDto.getId());

        beerDto.setQuantityOnHand(beer.getQuantityToBrew());

        NewInventoryEvent newInventoryEvent = new NewInventoryEvent(beerDto);

        log.debug("Brewed beer " + beer.getBeerName() + " for quantity: " + beer.getQuantityToBrew());
        log.debug("Now " + beerDto.getBeerName() + " has quantity: " + beerDto.getQuantityOnHand());

        jmsTemplate.convertAndSend(JmsConfig.NEW_INVENTORY_QUEUE, newInventoryEvent);
    }

}
