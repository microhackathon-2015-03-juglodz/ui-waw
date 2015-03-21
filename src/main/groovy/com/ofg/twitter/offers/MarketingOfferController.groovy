package com.ofg.twitter.offers

import com.ofg.twitter.loan.ApplicantsHolder
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
@RequestMapping('/offers')
class MarketingOfferController {

    @Autowired
    MarketingOfferClient marketingOfferClient

    @Autowired
    ApplicantsHolder applicantsHolder;

    @Autowired
    LoanIdGenerator loanIdGenerator;

    @RequestMapping(produces = "application/json")
    List<MarketingOffer> getOffers() {

        def offers = new ArrayList()

        def loanIds = loanIdGenerator.getAssignedIds();

        for (String loanId : loanIds) {

            def applicant = applicantsHolder.getApplicant(loanId)

            log.debug("Applicant = {}", applicant)

            def offer = marketingOfferClient.getOffer(applicant.getfName(), applicant.getlName())
            log.debug("Offer = {}", offer)

            if (null != offer) {
                offers.add(offer)
            } else {
                offers.add(new MarketingOffer(name: applicant.fName + " " + applicant.lName, marketingOffer: null ))
            }
        }

        return offers
    }

}
