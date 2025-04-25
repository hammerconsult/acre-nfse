(function () {
    'use strict';

angular.module('nfseApp')
    .controller('FaleConoscoController',
        function ($scope, $state, $http, $translate, $timeout, FaleConoscoService, Notificacao) {

            $scope.faleConosco = {
            };

            $scope.loadAll = function () {
            };

            $scope.loadAll();


            $scope.save = function () {
                FaleConoscoService.save($scope.faleConosco, function (data) {
                    if (data.id) {
                        $state.go("home", {}, {reload: true});
                        Notificacao.info("Operação Realizada", "Sua solicitação foi encaminhada para o departamento responsável. Entraremos em contato o mais breve possível.");
                    } else {
                        Notificacao.error("Algo inesperado aconteceu!", "Entre em contato com o suporte da prefeitura.");
                    }
                });
            };

            $scope.atribuirTipo = function (tipo) {
                $scope.faleConosco.tipo = tipo;
            }

        });
})();
