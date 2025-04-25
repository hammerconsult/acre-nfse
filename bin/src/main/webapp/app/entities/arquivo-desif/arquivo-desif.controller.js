(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('ArquivoDesifController',
            function ($scope, $state, localStorageService, ArquivoDesif, ParseLinks, EnumCacheService) {
                $scope.arquivos = [];
                $scope.per_page = 20;
                $scope.prestadorPrincipal = localStorageService.get("prestadorPrincipal");
                $scope.modulos = EnumCacheService.carregarValuesEnum("br.com.webpublico.domain.dto.importacaodesif.ModuloDesifNfseDTO");
                $scope.situacoes = EnumCacheService.carregarValuesEnum("br.com.webpublico.domain.dto.importacaodesif.SituacaoArquivoDesifNfseDTO");
                $scope.filtro = {
                    modulo: null,
                    situacao: null
                }

                $scope.montarConsulta = function () {
                    var campos = [];
                    campos.push({
                        campo: "obj.cadastroeconomico_id",
                        operador: "IGUAL",
                        valorLong: $scope.prestadorPrincipal.prestador.id
                    });
                    if ($scope.filtro.modulo != null && $scope.filtro.modulo.name != null) {
                        campos.push({
                            campo: "obj.modulo",
                            operador: "IGUAL",
                            valorString: $scope.filtro.modulo.name
                        });
                    }
                    if ($scope.filtro.situacao != null && $scope.filtro.situacao.name != null) {
                        campos.push({
                            campo: "obj.situacao",
                            operador: "IGUAL",
                            valorString: $scope.filtro.situacao.name
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
                    ArquivoDesif.consultar(consulta,
                        function (result, headers) {
                            if (headers) {
                                $scope.links = ParseLinks.parse(headers('link'));
                                $scope.totalItens = headers('x-total-count')
                            }
                            $scope.arquivos = result;
                        });
                };

                $scope.loadPage = function (page) {
                    $scope.page = page;
                    $scope.loadAll();
                };

                $scope.loadAll();
            });
})();
