(function () {
    'use strict';

angular.module('nfseApp')
    .controller('SubstituicaoRPSController', function ($scope, SubstituicaoRPS, SubstituicaoRPSSearch, ParseLinks) {
        $scope.substituicaoRPSs = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            SubstituicaoRPS.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.substituicaoRPSs = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            SubstituicaoRPS.get({id: id}, function(result) {
                $scope.substituicaoRPS = result;
                $('#deleteSubstituicaoRPSConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            SubstituicaoRPS.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteSubstituicaoRPSConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            SubstituicaoRPSSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.substituicaoRPSs = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.substituicaoRPS = {id: null};
        };
    });
})();
