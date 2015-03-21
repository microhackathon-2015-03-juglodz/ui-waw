package com.ofg.twitter.loan

import org.springframework.stereotype.Component

import java.util.concurrent.atomic.AtomicInteger

/**
 * Created by mkucharek.
 */
@Component
class LoanIdGenerator {

    private AtomicInteger currentId = new AtomicInteger(0)

    String getNextLoanId() {
        return currentId.incrementAndGet().toString()
    }

}
