package com.ofg.twitter.loan

import org.springframework.stereotype.Component

import java.util.concurrent.ConcurrentHashMap

/**
 * Created by mkucharek.
 */
@Component
class ApplicantsHolder {

    private ConcurrentHashMap<String, Applicant> applicants = new ConcurrentHashMap<>();

    void addApplicant(String loanId, Applicant applicant) {
        applicants.put(loanId, applicant)

    }

    Applicant getApplicant(String loanId) {
        return applicants.get(loanId);
    }

    List<Applicant> getAllApplicants
}
