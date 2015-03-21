'use strict';

angular.module('BootstrapApplication.services')
    .factory('LoanService', ['$http', function($http) {
        return {
            apply: function(user) {
                return $http.post('/loan', user);
            },

            getDecisions: function() {
                return $http.get('/decisions')
            }


        };
    }
]);
