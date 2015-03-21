package com.ofg.twitter.offers
import com.netflix.hystrix.HystrixCommand
import com.netflix.hystrix.HystrixCommandKey
import com.nurkiewicz.asyncretry.AsyncRetryExecutor
import com.ofg.infrastructure.discovery.ServiceUnavailableException
import com.ofg.infrastructure.web.resttemplate.fluent.ServiceRestClient
import com.ofg.twitter.config.Collaborators
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
/**
 * Created by mkucharek.
 */
@Slf4j
@Component
class MarketingOfferClient {

    @Autowired
    ServiceRestClient serviceRestClient

    @Autowired
    AsyncRetryExecutor executor

    def getOffer = { String firstName, String lastName ->

        try {
            def offer = doCallGetOffer(firstName, lastName)

            offer.name = firstName + " " + lastName

            return offer

        } catch (ServiceUnavailableException e) {
            log.info("Cannot connect to collabolator", e)
        }

    }

    private MarketingOffer doCallGetOffer(String firstName, String lastName) {
        return serviceRestClient
                .forService(Collaborators.DECISION_MAKER_SERVICE_DEPENDENCY_NAME)
                .retryUsing(executor.withMaxRetries(3))
                .get()
                .withCircuitBreaker(HystrixCommand.Setter
                .withGroupKey({ 'gettingMarketingOffers' })
                .andCommandKey(HystrixCommandKey.Factory.asKey("Command")),
                { log.debug("Breaking the circuit"); return null})
                .onUrl("/api/marketing/" + firstName + "_" + lastName)
                .andExecuteFor()
                .anObject()
                .ofType(MarketingOffer.class)
    }
}
