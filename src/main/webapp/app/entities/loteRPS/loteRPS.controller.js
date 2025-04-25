(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('LoteRPSController', function ($scope, LoteRPS, LoteRPSSearch, ParseLinks, localStorageService,
                                                   EnumCacheService) {
            $scope.lotes = [];
            $scope.page = 1;
            $scope.per_page = 50;

            $scope.dataRecebimentoInicial;
            $scope.dataRecebimentoFinal;
            $scope.numeroLoteInicial;
            $scope.numeroLotFinal;
            $scope.situacao;

            $scope.situacoes = EnumCacheService.carregarValuesEnum("br.com.webpublico.domain.dto.enums.SituacaoLoteRps");


            $scope.prestadorPrincipal = localStorageService.get("prestadorPrincipal");

            $scope.montarConsulta = function () {
                var campos = [];

                var parametroQueryCampo = {
                    campo: "l.id",
                    operador: "DIFERENTE",
                    valorLong: 0
                };
                campos.push(parametroQueryCampo);

                if ($scope.dataRecebimentoInicial) {
                    var dataRecebimentoInicial = DataUtil.dateWithOutHoursMinutesSeconds($scope.dataRecebimentoInicial);
                    parametroQueryCampo = {
                        campo: "l.dataRecebimento",
                        operador: "MAIOR_IGUAL",
                        valorDate: dataRecebimentoInicial
                    };
                    campos.push(parametroQueryCampo);
                }
                if ($scope.dataRecebimentoFinal) {
                    var dataRecebimentoFinal = DataUtil.dateWithOutHoursMinutesSeconds($scope.dataRecebimentoFinal);
                    parametroQueryCampo = {
                        campo: "l.dataRecebimento",
                        operador: "MENOR_IGUAL",
                        valorDate: dataRecebimentoFinal
                    };
                    campos.push(parametroQueryCampo);
                }

                if ($scope.numeroLoteInicial) {
                    parametroQueryCampo = {
                        campo: "l.numero",
                        operador: "MAIOR_IGUAL",
                        valorLong: $scope.numeroLoteInicial
                    };
                    campos.push(parametroQueryCampo);
                }
                if ($scope.numeroLoteFinal) {
                    parametroQueryCampo = {
                        campo: "l.numero",
                        operador: "MENOR_IGUAL",
                        valorLong: $scope.numeroLoteFinal
                    };
                    campos.push(parametroQueryCampo);
                }
                if ($scope.situacao && $scope.situacao.name) {
                    parametroQueryCampo = {
                        campo: "l.situacao",
                        operador: "IGUAL",
                        valorString: $scope.situacao.name
                    };
                    campos.push(parametroQueryCampo);
                }

                parametroQueryCampo = {
                    campo: "ce.id",
                    operador: "IGUAL",
                    valorString: $scope.prestadorPrincipal.prestador.id
                };
                campos.push(parametroQueryCampo);


                return {
                    offset: $scope.page,
                    limit: $scope.per_page,
                    parametrosQuery: [{
                        juncao: " and ",
                        parametroQueryCampos: campos
                    }],
                    orderBy: " "
                };
            };

            $scope.loadAll = function () {
                var consultaLoteRps = $scope.montarConsulta();
                LoteRPS.buscarLotesRps(consultaLoteRps,
                    function (result, headers) {
                        if (headers)
                            $scope.links = ParseLinks.parse(headers('link'));
                        $scope.lotes = result;
                    });
            };

            $scope.limparFiltros = function () {
                $scope.dataRecebimentoInicial = null;
                $scope.dataRecebimentoFinal = null;
                $scope.numeroLoteInicial = null;
                $scope.numeroLotFinal = null;
                $scope.inscricaoCadastral = null;
                $scope.situacao = null;
                $scope.versaoSistema = null;
                $scope.versaoAbrasf = null;
                $scope.reprocessar = false;
                $scope.loadAll();
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
                $scope.loteRPS = {codigo: null, id: null};
            };
        });
})();
