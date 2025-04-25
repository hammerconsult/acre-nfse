(function () {
    'use strict';

angular.module('nfseApp')
    .controller('EventoSimplesNacionalController', function ($scope, $state, EventoSimplesNacional, ParseLinks, SweetAlert, localStorageService) {

        $scope.eventos = [];
        $scope.per_page = 10;
        $scope.searchQuery = "";

        $scope.loadAll = function () {
            console.log('loadAll');
            var prestador = localStorageService.get("prestadorPrincipal");
            EventoSimplesNacional.buscarEventosPorEmpresa({
                prestador: prestador.id,
                filtro: $scope.searchQuery
            }, function (result) {
                $scope.eventos = result;
            });
        };

        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };

        $scope.loadAll();

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clean();
        };
    });
})();