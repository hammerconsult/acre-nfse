(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('SubstitutosTributariosController',
            function ($scope, SubstitutosTributariosService) {

                $scope.limpar = function () {
                    $scope.consultar = {cpfCnpj: "", inscricao: "", nome_razaosocial: ""};
                    $scope.substitutosTributario = [];
                };

                $scope.limpar();

                $scope.buscar = function () {
                    SubstitutosTributariosService.query({
                        cpfCnpj: $scope.consultar.cpfCnpj,
                        inscricao: $scope.consultar.inscricao,
                        nome_razaosocial: $scope.consultar.nome_razaosocial
                    }, function (result, headers) {
                        $scope.substitutosTributario = result;
                        console.log("testando", result);
                    });
                }

            });
})();
