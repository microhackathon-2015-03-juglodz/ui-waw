package com.ofg.twitter.loan

import org.springframework.stereotype.Component

import java.util.concurrent.atomic.AtomicInteger
import java.util.function.Function
import java.util.stream.Collectors

/**
 * Created by mkucharek.
 */
@Component
class LoanIdGenerator {

    private AtomicInteger currentId = new AtomicInteger(0)

    String getNextLoanId() {
        return currentId.incrementAndGet().toString()
    }

    List<String> getAssignedIds() {
        def curId = currentId.get()

        if (curId > 0) {
            return (1..curId).stream().map(new Function<Integer, String>() {
                @Override
                String apply(Integer o) {
                    return o.toString()
                }

            }).collect(Collectors.toList())

        } else {
            return Collections.emptyList()

        }
    }

}
