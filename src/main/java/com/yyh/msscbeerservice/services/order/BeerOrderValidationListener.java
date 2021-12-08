package com.yyh.msscbeerservice.services.order;

import com.yyh.brewery.model.events.ValidateOrderRequest;
import com.yyh.brewery.model.events.ValidateOrderResult;
import com.yyh.msscbeerservice.config.JmsConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BeerOrderValidationListener {

    @Autowired
    private BeerOrderValidator validator;

    @Autowired
    private JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.VALIDATE_ORDER_QUEUE)
    public void listen(ValidateOrderRequest validateOrderRequest) {
        Boolean isValid = validator.validateOrder(validateOrderRequest.getBeerOrderDto());

        log.debug("Beer Validation is valid: " + isValid);

        ValidateOrderResult validateOrderResult = ValidateOrderResult.builder()
                .isValid(isValid)
                .orderId(validateOrderRequest.getBeerOrderDto().getId())
                .build();

        jmsTemplate.convertAndSend(JmsConfig.VALIDATE_ORDER_RESPONSE_QUEUE, validateOrderResult);
    }
}
