(function () {
    'use strict';

    angular.module('nfseApp').controller('TarifaBancariaSearchController',
        function ($scope, $state, $modalInstance, TarifaBancaria, ParseLinks) {

            $scope.tarifas = [];
            $scope.filtro = {
                codigo: '',
                descricao: ''
            };
            $scope.page;
            $scope.per_page = 10;

            $scope.ok = function (tarifa) {
                $modalInstance.close(tarifa);
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

            $scope.montarConsulta = function () {
                var campos = [];

                if ($scope.filtro.codigo) {
                    campos.push({
                        campo: "obj.codigo",
                        operador: "LIKE",
                        valorString: '%' + $scope.filtro.codigo + '%'
                    });
                }

                if ($scope.filtro.descricao) {
                    campos.push({
                        campo: "obj.descricao",
                        operador: "LIKE",
                        valorString: '%' + $scope.filtro.descricao + '%'
                    });
                }

                return {
                    offset: $scope.page,
                    limit: $scope.per_page,
                    parametrosQuery: [{
                        juncao: " and ",
                        parametroQueryCampos: campos
                    }]
                };
            };

            $scope.loadAll = function () {
                var consulta = $scope.montarConsulta();
                TarifaBancaria.consultar(consulta,
                    function (result, headers) {
                        if (headers)
                            $scope.links = ParseLinks.parse(headers('link'));
                        $scope.tarifas = result;
                    });
            };

            $scope.loadAll();
        });
})();