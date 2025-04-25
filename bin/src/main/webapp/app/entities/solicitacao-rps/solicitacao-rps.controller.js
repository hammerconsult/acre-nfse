(function () {
    'use strict';

angular.module('nfseApp')
    .controller('SolicitacaoRPSController', function ($scope, $state, Solicitacaorps, $filter, ParseLinks, SweetAlert, localStorageService) {

        $scope.aidfes = [];
        $scope.per_page = 10;
        $scope.searchQuery = "";

        $scope.loadAll = function () {
            var prestador = localStorageService.get("prestadorPrincipal");
            Solicitacaorps.buscarTodasDoPrestador({
                prestador: prestador.id,
                page: $scope.page,
                per_page: $scope.per_page
            }, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.solicitacoes = result;
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

        $scope.clear = function () {
            $scope.solicitacaorps = {id: null};
        };
    });
})();
