package com.ofg.twitter.decision

import com.ofg.twitter.loan.LoanIdGenerator
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
/**
 * Created by mkucharek.
 */
@Slf4j
@RestController
@RequestMapping('/decisions')
class DecisionController {

    @Autowired
    DecisionMakerClient decisionMakerClient

    @Autowired
    LoanIdGenerator loanIdGenerator;

    @RequestMapping(produces = "application/json")
    List<Decision> getDecisions() {

        def decisions = new ArrayList()

        def loanIds = loanIdGenerator.getAssignedIds();

        for (String loanId : loanIds) {
            def decision = decisionMakerClient.getDecision(loanId)
            if (null != decision) {
                decisions.add(decision)
            } else {
                decisions.add(new Decision(loanId: loanId, decision: null))
            }
        }

        return decisions
    }

}
