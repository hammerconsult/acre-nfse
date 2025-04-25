(function () {
    'use strict';

angular.module('nfseApp')
    .controller('PaisSearchController', function ($scope, Pais, PaisSearch, ParseLinks, SweetAlert, $modalInstance) {
        $scope.paises = [];
        $scope.page = 1;
        $scope.loadAll = function () {
            Pais.query({page: $scope.page, per_page: 5}, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.paises = result;
            });
        };
        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.search = function () {
            PaisSearch.query({query: $scope.searchQuery}, function (result) {
                $scope.paises = result;
            }, function (response) {
                if (response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.pais = { id: null, codigo: null, nome: null, sigla: null};
        };

        $scope.ok = function (pais) {
            $modalInstance.close(pais);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    });
})();