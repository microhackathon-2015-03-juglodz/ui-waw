'use strict';

/**
 * @ngdoc function
 * # MainCtrl
 */
angular.module('BootstrapApplication.controllers')
        .controller('MainCtrl', ['$scope', 'LoanService', function ($scope, LoanService) {

            $scope.user={
                fName: "Test",
                lName: "Test2",
                age: 36,
                position: "IT",
                amount: 100
            };

            $scope.applyForLoan = function() {
                console.log($scope.user);
                LoanService.apply($scope.user).then(function(data) {

                })


            }

        }]);
