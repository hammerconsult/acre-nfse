(function () {
    'use strict';

angular.module('nfseApp')
    .controller('LoteServicoTomadoController', function ($scope, LoteServicoTomado, ParseLinks) {
        $scope.lotes = [];
        $scope.page = 1;
        $scope.searchQuery = "";
        $scope.per_page = 10;

        $scope.loadAll = function () {
            LoteServicoTomado.query({
                page: $scope.page,
                per_page: $scope.per_page,
                filtro: $scope.searchQuery
            }, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.lotes = result;
            });
        };
        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.loteServicoTomado = {codigo: null, id: null};
        };
    });
})();