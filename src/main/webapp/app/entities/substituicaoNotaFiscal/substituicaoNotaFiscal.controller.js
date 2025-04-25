(function () {
    'use strict';

angular.module('nfseApp')
    .controller('SubstituicaoNotaFiscalController', function ($scope, SubstituicaoNotaFiscal, SubstituicaoNotaFiscalSearch, ParseLinks) {
        $scope.substituicaoNotaFiscals = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            SubstituicaoNotaFiscal.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.substituicaoNotaFiscals = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            SubstituicaoNotaFiscal.get({id: id}, function(result) {
                $scope.substituicaoNotaFiscal = result;
                $('#deleteSubstituicaoNotaFiscalConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            SubstituicaoNotaFiscal.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteSubstituicaoNotaFiscalConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            SubstituicaoNotaFiscalSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.substituicaoNotaFiscals = result;
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
            $scope.substituicaoNotaFiscal = {id: null};
        };
    });
})();
