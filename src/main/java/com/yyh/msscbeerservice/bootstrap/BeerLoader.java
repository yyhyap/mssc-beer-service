package com.yyh.msscbeerservice.bootstrap;

import com.yyh.msscbeerservice.domain.Beer;
import com.yyh.msscbeerservice.repositories.BeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

// @Component
// Add in some seed data for Beer objects
public class BeerLoader implements CommandLineRunner {

    public static final String BEER_1_UPC = "0631234200036";
    public static final String BEER_2_UPC = "0631234300019";
    public static final String BEER_3_UPC = "0083783375213";

    @Autowired
    private BeerRepository beerRepository;

    @Override
    public void run(String... args) throws Exception {
        loadBeerObjects();
    }

    private void loadBeerObjects() {
        if(beerRepository.count() == 0) {
            beerRepository.save(Beer.builder()
                            .beerName("Heineken")
                            .beerStyle("IPA")
                            .quantityToBrew(200)
                            .minOnHand(12)
                            .upc(BEER_1_UPC)
                            .price(new BigDecimal("12.95"))
                            .build());

            beerRepository.save(Beer.builder()
                    .beerName("Tiger")
                    .beerStyle("PALE_ALE")
                    .quantityToBrew(200)
                    .minOnHand(12)
                    .upc(BEER_2_UPC)
                    .price(new BigDecimal("11.95"))
                    .build());

            beerRepository.save(Beer.builder()
                    .beerName("Kronenbourg")
                    .beerStyle("PALE_ALE")
                    .quantityToBrew(200)
                    .minOnHand(12)
                    .upc(BEER_3_UPC)
                    .price(new BigDecimal("11.95"))
                    .build());
        }
        System.out.println("Loaded Beers: " + beerRepository.count());
    }
}
