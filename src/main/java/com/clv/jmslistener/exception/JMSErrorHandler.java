package com.clv.jmslistener.exception;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;

/**
 * Created by Usuario on 16/03/2016.
 */
@Component("jmsErrorHandler")
public class JMSErrorHandler implements ErrorHandler {

    private final Logger logger = Logger.getLogger(getClass());

    @Override
    public void handleError(Throwable throwable) {
        logger.error("An exception ocurred :: " + throwable.getMessage(), throwable);
    }
}
