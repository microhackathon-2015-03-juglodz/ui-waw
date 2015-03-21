package com.ofg.twitter.loan

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import static org.springframework.web.bind.annotation.RequestMethod.POST

/**
 * Created by mkucharek.
 */
@Slf4j
@RestController
@RequestMapping('/loan')
class LoanApplicationController {

    @Autowired
    LoanApplicationService loanApplicationService;

    @Autowired
    ApplicantsHolder applicantsHolder

    @Autowired
    LoanIdGenerator loanIdGenerator

    @RequestMapping(method = POST)
    void applyForLoan(@RequestBody Applicant applicant) {

        log.debug("Applicant = {}", applicant)

        String loanId = loanIdGenerator.nextLoanId

        applicantsHolder.addApplicant(loanId, applicant)

        loanApplicationService.sendClientDetails(
                new Client(firstName: applicant.fName, lastName: applicant.lName, age: applicant.age, loanId: loanId))

        loanApplicationService.applyForLoan(
                new Loan(loanId: loanId, amount: applicant.amount))



    }


}
