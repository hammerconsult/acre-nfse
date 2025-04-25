(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('PlanoGeralContasComentadoSearchController', function ($scope, $modalInstance, PlanoGeralContasComentado,
                                                                           ParseLinks, localStorageService, $filter) {

            $scope.contas = [];
            $scope.filtro = {
                conta: '',
                desdobramento: '',
                nome: ''
            };
            $scope.page;
            $scope.per_page = 10;
            $scope.prestadorPrincipal = localStorageService.get("prestadorPrincipal");

            $scope.ok = function (conta) {
                $modalInstance.close(conta);
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

            $scope.montarConsulta = function () {
                var campos = [];
                campos.push({
                    campo: "obj.cadastroeconomico_id",
                    operador: "IGUAL",
                    valorLong: $scope.prestadorPrincipal.prestador.id
                });

                campos.push({
                    campo: "trunc(obj.iniciovigencia)",
                    operador: "MENOR_IGUAL",
                    valorDateString: $filter('date')(new Date(), 'dd/MM/yyyy')
                });

                campos.push({
                    campo: "trunc(coalesce(obj.fimvigencia, current_date))",
                    operador: "MAIOR_IGUAL",
                    valorDateString: $filter('date')(new Date(), 'dd/MM/yyyy')
                });

                if ($scope.filtro.conta) {
                    campos.push({
                        campo: "obj.conta",
                        operador: "LIKE",
                        valorString: '%' + $scope.filtro.conta + '%'
                    });
                }

                if ($scope.filtro.desdobramento) {
                    campos.push({
                        campo: "obj.desdobramento",
                        operador: "LIKE",
                        valorString: '%' + $scope.filtro.desdobramento + '%'
                    });
                }

                if ($scope.filtro.nome) {
                    campos.push({
                        campo: "obj.nome",
                        operador: "LIKE",
                        valorString: '%' + $scope.filtro.nome + '%'
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
                PlanoGeralContasComentado.consultar(consulta,
                    function (result, headers) {
                        if (headers)
                            $scope.links = ParseLinks.parse(headers('link'));
                        $scope.contas = result;
                    });
            };

            $scope.loadAll();
        });
})();