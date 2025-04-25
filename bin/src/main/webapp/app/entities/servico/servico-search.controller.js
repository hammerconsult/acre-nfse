(function () {
    'use strict';

angular.module('nfseApp')
    .controller('ServicoSearchController', function ($scope, $modalInstance, Servico, ParseLinks) {
        $scope.servicos = [];
        $scope.filtro = {
            codigo: '',
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
            if ($scope.filtro.codigo) {
                campos.push({
                    campo: "obj.codigo",
                    operador: "LIKE",
                    valorString: '%' + $scope.filtro.codigo + '%'
                });
            }

            if ($scope.filtro.descricao) {
                campos.push({
                    campo: "lower(obj.nome)",
                    operador: "LIKE",
                    valorString: '%' + $scope.filtro.descricao.toLowerCase() + '%'
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
            Servico.consultar(consulta,
                function (result, headers) {
                    if (headers)
                        $scope.links = ParseLinks.parse(headers('link'));
                    $scope.servicos = result;
                });
        };

        $scope.loadAll();
    });
})();