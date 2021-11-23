package com.yyh.msscbeerservice.services;

import com.yyh.msscbeerservice.domain.Beer;
import com.yyh.msscbeerservice.repositories.BeerRepository;
import com.yyh.msscbeerservice.web.controller.NotFoundException;
import com.yyh.msscbeerservice.web.mappers.BeerMapper;
import com.yyh.msscbeerservice.web.model.BeerDto;
import com.yyh.msscbeerservice.web.model.BeerPagedList;
import com.yyh.msscbeerservice.web.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BeerServiceImpl implements BeerService {

    @Autowired
    private BeerMapper beerMapper;

    @Autowired
    private BeerRepository beerRepository;

    // conditional caching, cache only when it is not going to get quantityOnHand from Beer Inventory Service
    @Cacheable(cacheNames = "beerListCache", condition = "#showInventoryOnHand == false")
    @Override
    public BeerPagedList listBeers(String beerName, String beerStyle, PageRequest pageRequest, Boolean showInventoryOnHand) {
        BeerPagedList beerPagedList;
        Page<Beer> beerPage;

//        // for testing
//        String methodName = new Object() {}
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        log.debug(methodName + " is called");

        if (!StringUtils.isEmpty(beerName) && !StringUtils.isEmpty(beerStyle)) {
            //search both
            beerPage = beerRepository.findAllByBeerNameAndBeerStyle(beerName, beerStyle, pageRequest);
        } else if (!StringUtils.isEmpty(beerName) && StringUtils.isEmpty(beerStyle)) {
            //search beer name
            beerPage = beerRepository.findAllByBeerName(beerName, pageRequest);
        } else if (StringUtils.isEmpty(beerName) && !StringUtils.isEmpty(beerStyle)) {
            //search beer style
            beerPage = beerRepository.findAllByBeerStyle(beerStyle, pageRequest);
        } else {
            beerPage = beerRepository.findAll(pageRequest);
        }

        if(showInventoryOnHand) {
            beerPagedList = new BeerPagedList(beerPage
                    .getContent()
                    .stream()
                    .map(beerMapper::beerToBeerDtoWithInventory)
                    .collect(Collectors.toList()),
                    PageRequest
                            .of(beerPage.getPageable().getPageNumber(),
                                    beerPage.getPageable().getPageSize()),
                    beerPage.getTotalElements());
        } else {
            beerPagedList = new BeerPagedList(beerPage
                    .getContent()
                    .stream()
                    .map(beerMapper::beerToBeerDto)
                    .collect(Collectors.toList()),
                    PageRequest
                            .of(beerPage.getPageable().getPageNumber(),
                                    beerPage.getPageable().getPageSize()),
                    beerPage.getTotalElements());
        }

        return beerPagedList;
    }

    // conditional caching, cache only when it is not going to get quantityOnHand from Beer Inventory Service
    @Cacheable(cacheNames = "beerCache", key = "#beerId",condition = "#showInventoryOnHand == false")
    @Override
    public BeerDto getById(UUID beerId, Boolean showInventoryOnHand) {


//        // for testing
//        String methodName = new Object() {}
//                .getClass()
//                .getEnclosingMethod()
//                .getName();
//        log.debug(methodName + " is called");

        if(showInventoryOnHand) {
            return beerMapper.beerToBeerDtoWithInventory(beerRepository.findById(beerId).orElseThrow(() -> new NotFoundException()));
        } else {
            return beerMapper.beerToBeerDto(beerRepository.findById(beerId).orElseThrow(() -> new NotFoundException()));
        }
    }

    @Override
    @Transactional
    public BeerDto saveNewBeer(BeerDto beerDto) {
        return beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beerDto)));
    }

    @Override
    @Transactional
    public BeerDto updateBeer(UUID beerId, BeerDto beerDto) {
        Beer beer = beerRepository.findById(beerId).orElseThrow(() -> new NotFoundException());

        beer.setBeerName(beerDto.getBeerName());
        beer.setBeerStyle(beerDto.getBeerStyle().name());
        beer.setPrice(beerDto.getPrice());
        beer.setUpc(beerDto.getUpc());

        return beerMapper.beerToBeerDto(beerRepository.save(beer));
    }

    @Cacheable(cacheNames = "beerUpcCache", condition = "#showInventoryOnHand == false")
    @Override
    public BeerDto getByUpc(String upc, Boolean showInventoryOnHand) {
        return (showInventoryOnHand
                ? beerMapper.beerToBeerDtoWithInventory(beerRepository.findByUpc(upc))
                : beerMapper.beerToBeerDto(beerRepository.findByUpc(upc)));
    }
}
