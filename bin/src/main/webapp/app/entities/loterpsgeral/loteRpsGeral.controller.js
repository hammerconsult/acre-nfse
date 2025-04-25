(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('LoteRpsGeralController', function ($scope, LoteRPS, ParseLinks, EnumCacheService, DataUtil, SweetAlert,
                                                        localStorageService) {
            $scope.lotes = [];
            $scope.links;
            $scope.filtro = {
                inscricaoCadastral: null,
                cnpj: null,
                razaoSocial: null,
                dataRecebimentoInicial: null,
                dataRecebimentoFinal: null,
                numeroLoteInicial: null,
                numeroLoteFinal: null,
                numeroRps: null,
                numeroNotaFiscal: null,
                situacao: null,
                versaoSistema: null,
                versaoAbrasf: null,
                reprocessar: null
            };
            $scope.page;
            $scope.per_page = 20;
            $scope.situacoes = EnumCacheService.carregarValuesEnum("br.com.webpublico.domain.dto.enums.SituacaoLoteRps");

            $scope.init = function () {
                console.log(localStorageService.get("filtroLoteRps"));
                if (localStorageService.get("filtroLoteRps") != null) {
                    $scope.filtro = localStorageService.get("filtroLoteRps");
                }
            }

            $scope.init();

            $scope.montarConsulta = function () {
                var campos = [];
                if ($scope.filtro.inscricaoCadastral) {
                    campos.push({
                        campo: "ce.inscricaocadastral",
                        operador: "IGUAL",
                        valorString: $scope.filtro.inscricaoCadastral
                    });
                }
                if ($scope.filtro.cnpj) {
                    campos.push({
                        campo: "replace(replace(replace(pj.cnpj, '.', ''), '-', ''), '/', '')",
                        operador: "IGUAL",
                        valorString: $scope.filtro.cnpj.replaceAll("[^a-zA-Z0-9]+", "")
                    });
                }
                if ($scope.filtro.razaoSocial) {
                    campos.push({
                        campo: "lower(pj.razaosocial)",
                        operador: "LIKE",
                        valorString: "%" + $scope.filtro.razaoSocial.toLowerCase() + "%"
                    });
                }
                if ($scope.filtro.dataRecebimentoInicial) {
                    var dataRecebimentoInicial = DataUtil.dateWithOutHoursMinutesSeconds($scope.filtro.dataRecebimentoInicial);
                    campos.push({
                        campo: "l.dataRecebimento",
                        operador: "MAIOR_IGUAL",
                        valorDate: dataRecebimentoInicial
                    });
                }
                if ($scope.filtro.dataRecebimentoFinal) {
                    var dataRecebimentoFinal = DataUtil.dateWithOutHoursMinutesSeconds($scope.filtro.dataRecebimentoFinal);
                    campos.push({
                        campo: "l.dataRecebimento",
                        operador: "MENOR_IGUAL",
                        valorDate: dataRecebimentoFinal
                    });
                }
                if ($scope.filtro.numeroLoteInicial) {
                    campos.push({
                        campo: "l.numero",
                        operador: "MAIOR_IGUAL",
                        valorLong: $scope.filtro.numeroLoteInicial
                    });
                }
                if ($scope.filtro.numeroLoteFinal) {
                    campos.push({
                        campo: "l.numero",
                        operador: "MENOR_IGUAL",
                        valorLong: $scope.filtro.numeroLoteFinal
                    });
                }
                if ($scope.filtro.numeroRps) {
                    campos.push({
                        campo: " exists (select 1 from rps " +
                            " where rps.loterps_id = l.id and rps.numero = " + $scope.filtro.numeroRps + ") "
                    });
                }
                if ($scope.filtro.numeroNotaFiscal) {
                    campos.push({
                        campo: " exists (select 1 from rps " +
                            "  inner join notafiscal nf on nf.rps_id = rps.id " +
                            " where rps.loterps_id = l.id and nf.numero = " + $scope.filtro.numeroNotaFiscal + ") "
                    });
                }
                if ($scope.filtro.situacao && $scope.filtro.situacao.name) {
                    campos.push({
                        campo: "l.situacao",
                        operador: "IGUAL",
                        valorString: $scope.filtro.situacao.name
                    });
                }
                if ($scope.filtro.versaoSistema) {
                    campos.push({
                        campo: "l.versaosistema",
                        operador: "IGUAL",
                        valorString: $scope.filtro.versaoSistema
                    });
                }
                if ($scope.filtro.versaoAbrasf) {
                    campos.push({
                        campo: "l.versaoabrasf",
                        operador: "IGUAL",
                        valorString: $scope.filtro.versaoAbrasf
                    });
                }
                if ($scope.filtro.reprocessar) {
                    campos.push({
                        campo: "l.reprocessar",
                        operador: "IGUAL",
                        valorBoolean: $scope.filtro.reprocessar
                    });
                }
                var parametrosQuery = [];
                if (campos.length > 0) {
                    parametrosQuery.push({
                        juncao: " and ",
                        parametroQueryCampos: campos
                    });
                }
                localStorageService.set("filtroLoteRps", $scope.filtro);
                return {
                    offset: $scope.page,
                    limit: $scope.per_page,
                    parametrosQuery: parametrosQuery,
                    orderBy: " order by l.id desc "
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
                $scope.filtro = {};
                $scope.loadAll();
            };

            $scope.loadPage = function (page) {
                $scope.page = page;
                $scope.loadAll();
            };

            $scope.loadAll();

            $scope.inverterReprocessar = function (lote) {
                LoteRPS.inverterReprocessar(lote, function () {
                    lote.reprocessar = !lote.reprocessar;
                });
            };

            $scope.dispararReprocessamento = function () {
                SweetAlert.swal({
                        title: 'Atenção',
                        text: 'Tem certeza que deseja disparar o reprocessamento de lotes de rps nesse momento?',
                        type: 'warning',
                        showCancelButton: true,
                        confirmButtonColor: "#DD6B55",
                        confirmButtonText: "Sim",
                        cancelButtonText: "Não",
                        closeOnConfirm: false,
                        closeOnCancel: true
                    },
                    function (isConfirm) {
                        if (isConfirm) {
                            LoteRPS.dispararReprocessamento(null, function () {
                                SweetAlert.swal("Informação", "Reprocessamento disparado com sucesso!");
                            });
                        }
                    });
            }
        });
})();
