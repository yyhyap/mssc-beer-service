package com.yyh.msscbeerservice.services.brewing;

import com.yyh.msscbeerservice.config.JmsConfig;
import com.yyh.msscbeerservice.domain.Beer;
import com.yyh.brewery.model.events.BrewBeerEvent;
import com.yyh.msscbeerservice.repositories.BeerRepository;
import com.yyh.msscbeerservice.services.inventory.BeerInventoryService;
import com.yyh.msscbeerservice.web.mappers.BeerMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BrewingServiceImpl implements BrewingService{

    @Autowired
    private BeerRepository beerRepository;

    @Autowired
    private BeerInventoryService beerInventoryService;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private BeerMapper beerMapper;

    @Scheduled(fixedRate = 5000)
    public void checkForLowInventory() {
        List<Beer> beers = beerRepository.findAll();

        beers.forEach(beer -> {
            Integer inventoryQuantityOnHand = beerInventoryService.getOnHandInventory(beer.getId());

            log.debug("Minimum quantity on hand for beer " + beer.getBeerName() + " is "+ beer.getMinOnHand());
            log.debug("Inventory quantity on hand is: " + inventoryQuantityOnHand);

            if(beer.getMinOnHand() >= inventoryQuantityOnHand) {
                jmsTemplate.convertAndSend(JmsConfig.BREWING_REQUEST_QUEUE, new BrewBeerEvent(beerMapper.beerToBeerDto(beer)));
            }
        });
    }
}
