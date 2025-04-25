(function () {
    'use strict';

angular.module('nfseApp').controller('DeclaracaoMensalServicoInstituicaoFinanceiraDetailController',
    function ($scope, entity, PrestadorServicos, localStorageService) {
        $scope.declaracaoMensalServicoInstituicaoFinanceira = entity;

        $scope.init = function () {
            PrestadorServicos.get({id: localStorageService.get("prestadorPrincipal").prestador.id}, function (data) {
                $scope.prestador = data;
            })
        }

        $scope.init();
    });

})();