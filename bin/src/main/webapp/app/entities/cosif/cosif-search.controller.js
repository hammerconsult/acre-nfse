(function () {
    'use strict';

    angular.module('nfseApp').controller('CosifSearchController',
        function ($scope, $state, $modalInstance, Cosif, ParseLinks) {

            $scope.contas = [];
            $scope.filtro = {
                conta: '',
                descricao: ''
            };
            $scope.page;
            $scope.per_page = 10;

            $scope.ok = function (conta) {
                $modalInstance.close(conta);
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

            $scope.montarConsulta = function () {
                var campos = [];

                if ($scope.filtro.conta) {
                    campos.push({
                        campo: "obj.conta",
                        operador: "LIKE",
                        valorString: '%' + $scope.filtro.conta + '%'
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
                Cosif.consultar(consulta,
                    function (result, headers) {
                        if (headers)
                            $scope.links = ParseLinks.parse(headers('link'));
                        $scope.contas = result;
                    });
            };

            $scope.loadAll();
        });
})();