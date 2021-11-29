package com.yyh.msscbeerservice.events;

import com.yyh.msscbeerservice.web.model.BeerDto;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerEvent implements Serializable {

    static final long serialVersionUID = 1362973740494499357L;

    private BeerDto beerDto;
}
