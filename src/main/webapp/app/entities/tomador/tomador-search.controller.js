(function () {
    'use strict';

angular.module('nfseApp')
    .controller('TomadorSearchController', function ($scope, $timeout, Tomador, TomadorSearch,
                                                     ParseLinks, SweetAlert, $modalInstance, localStorageService) {
        $scope.tomadors = [];
        $scope.page = 1;
        $scope.per_page = 5;
        $scope.prestadorPrincipal = localStorageService.get("prestadorPrincipal");

        $timeout(function () {
            angular.element('[name="searchQuery"]').focus();
        });


        $scope.montarConsultaGenerica = function () {
            var parametrosQuery = [];
            var parametroQueryPrestador =
                {
                    juncao: " and ",
                    parametroQueryCampos: [{
                        campo: "t.prestador_id",
                        operador: "IGUAL",
                        valorLong: $scope.prestadorPrincipal.prestador.id
                    }]
                };
            parametrosQuery.push(parametroQueryPrestador);

            if ($scope.searchQuery) {
                var parametroQueryFiltro =
                    {
                        juncao: " or ",
                        parametroQueryCampos: [
                            {
                                campo: "dados.cpfcnpj",
                                operador: "LIKE",
                                valorString: "%" + $scope.searchQuery + "%"
                            },
                            {
                                campo: "dados.nomerazaosocial",
                                operador: "LIKE",
                                valorString: "%" + $scope.searchQuery + "%"
                            },
                            {
                                campo: "dados.nomefantasia",
                                operador: "LIKE",
                                valorString: "%" + $scope.searchQuery + "%"
                            },
                            {
                                campo: "dados.apelido",
                                operador: "LIKE",
                                valorString: "%" + $scope.searchQuery + "%"
                            },
                            {
                                campo: "dados.email",
                                operador: "LIKE",
                                valorString: "%" + $scope.searchQuery + "%"
                            }
                        ]
                    };
                parametrosQuery.push(parametroQueryFiltro);
            }

            return {
                offset: $scope.page,
                limit: $scope.per_page,
                parametrosQuery: parametrosQuery,
                orderBy: ""
            };
        };

        $scope.loadAll = function () {
            var consultaGenerica = $scope.montarConsultaGenerica();
            Tomador.query(consultaGenerica,
                function (result, headers) {
                    if (headers)
                        $scope.links = ParseLinks.parse(headers('link'));
                    $scope.tomadors = result;
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
            $scope.tomador = {codigo: null, descricao: null, ativo: null, id: null};
        };

        $scope.ok = function (tomador) {
            $modalInstance.close(tomador);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    });
})();
