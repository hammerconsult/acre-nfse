(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('PlanoGeralContasComentadoController', function ($state, $scope, $filter, localStorageService,
                                                                     PlanoGeralContasComentado, ParseLinks, Notificacao,
                                                                     PrestadorServicos) {
            $scope.contas = [];
            $scope.filtro = {
                conta: '',
                desdobramento: '',
                nome: ''
            };
            $scope.page;
            $scope.per_page = 20;
            $scope.prestadorPrincipal = localStorageService.get("prestadorPrincipal");

            $scope.init = function () {
                if ($scope.prestadorPrincipal) {
                    PrestadorServicos.get({id: $scope.prestadorPrincipal.prestador.id}, function (data) {
                        $scope.prestadorPrincipal.prestador = data;
                        $scope.loadAll();
                    });
                }
            }

            $scope.init();

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
                        campo: "lower(obj.nome)",
                        operador: "LIKE",
                        valorString: '%' + $scope.filtro.nome.toLowerCase() + '%'
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
                        if (headers) {
                            $scope.links = ParseLinks.parse(headers('link'));
                            $scope.totalItens = headers('x-total-count');
                        }
                        $scope.contas = result;
                    });
            };

            $scope.loadPage = function (page) {
                $scope.page = page;
                $scope.loadAll();
            };

            $scope.remove = function (conta) {
                Notificacao.confirmDelete(function () {
                    PlanoGeralContasComentado.delete({id: conta.id}, function () {
                        $scope.loadAll();
                    })
                })
            }

            $scope.isVersaoDesifAbrasf32 = function () {
                return $scope.prestadorPrincipal &&
                    $scope.prestadorPrincipal.prestador &&
                    $scope.prestadorPrincipal.prestador.enquadramentoFiscal &&
                    $scope.prestadorPrincipal.prestador.enquadramentoFiscal.versaoDesif == 'VERSAO_ABRASF_3_2';
            }
        })
    ;
})();