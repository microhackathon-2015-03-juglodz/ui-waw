package com.ofg.twitter.loan

import com.netflix.hystrix.HystrixCommand
import com.nurkiewicz.asyncretry.AsyncRetryExecutor
import com.ofg.infrastructure.discovery.ServiceUnavailableException
import com.ofg.infrastructure.web.resttemplate.fluent.ServiceRestClient
import groovy.json.JsonBuilder
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by mkucharek.
 */
@Slf4j
@Service
class LoanApplicationService {

    @Autowired
    ServiceRestClient serviceRestClient

    @Autowired
    AsyncRetryExecutor executor

    //TODO: how do we handle ServiceUnavailable

    void applyForLoan(Loan loan) {

        try {
            serviceRestClient
                    .forService("loan-application-service")
                    .retryUsing(executor.withMaxRetries(3))
                    .post()
                    .withCircuitBreaker(HystrixCommand.Setter.withGroupKey({ 'sendingLoanDetails' }))
                    .onUrl("/api/loanApplication")
                    .body(new JsonBuilder(loan).toString())
                    .andExecuteFor()
                    .ignoringResponse()

        } catch (ServiceUnavailableException e) {
            log.info("Cannot connect to collabolator", e)
        }

    }

    void sendClientDetails(Client client) {

        try {
            serviceRestClient.forService("client-service")
                    .retryUsing(executor.withMaxRetries(3))
                    .post()
                    .withCircuitBreaker(HystrixCommand.Setter.withGroupKey({ 'sendingClientDetails' }))
                    .onUrl("/api/client")
                    .body(new JsonBuilder(client).toString())
                    .ignoringResponse()

        } catch (ServiceUnavailableException e) {
            log.info("Cannot connect to collabolator", e)
        }


    }

}
