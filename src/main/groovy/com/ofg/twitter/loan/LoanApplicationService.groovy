package com.ofg.twitter.loan

import com.ofg.infrastructure.web.resttemplate.fluent.ServiceRestClient
import groovy.json.JsonBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by mkucharek.
 */
@Service
class LoanApplicationService {

    @Autowired
    ServiceRestClient serviceRestClient

    void applyForLoan(Loan loan) {
        serviceRestClient
                .forService("loan-application-service")
                .post()
                .onUrl("/api/loanApplication")
                .body(new JsonBuilder(loan).toString()).ignoringResponse()

        // TODO

    }

    void sendClientDetails(Client client) {
        serviceRestClient.forService("client-service")
                .post().onUrl("/api/client")
                .body(new JsonBuilder(client).toString())
                .ignoringResponse()

    }

}
