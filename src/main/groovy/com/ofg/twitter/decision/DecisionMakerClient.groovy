package com.ofg.twitter.decision
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
class DecisionMakerClient {

    @Autowired
    ServiceRestClient serviceRestClient

    @Autowired
    AsyncRetryExecutor executor

    def getDecision = { String loanId ->

        try {
            return doCallGetDecision(loanId)

        } catch (ServiceUnavailableException e) {
            log.info("Cannot connect to collabolator", e)
        }

    }

    private Decision doCallGetDecision(String loanId) {
        return serviceRestClient
                .forService(Collaborators.DECISION_MAKER_SERVICE_DEPENDENCY_NAME)
                .retryUsing(executor.withMaxRetries(3))
                .get()
                .withCircuitBreaker(HystrixCommand.Setter
                .withGroupKey({ 'sendingLoanDetails' })
                .andCommandKey(HystrixCommandKey.Factory.asKey("Command")),
                { log.debug("Breaking the circuit"); return null})
                .onUrl("/api/loanApplication/" + loanId)
                .andExecuteFor()
                .anObject()
                .ofType(Decision.class)
    }
}
