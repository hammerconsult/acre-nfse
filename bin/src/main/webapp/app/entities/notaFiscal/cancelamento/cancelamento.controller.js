(function () {
    'use strict';

angular.module('nfseApp')
    .controller('CancelamentoController', function ($scope, NotaFiscal, ParseLinks, Principal, localStorageService, EnumCacheService, Util, DataUtil) {
        $scope.notaFiscals = [];
        $scope.per_page = 20;
        $scope.notaFiscalSelecionada;
        $scope.prestadorPrincipal = localStorageService.get("prestadorPrincipal");
        $scope.mostrarFiltros = false;
        $scope.naturezasOperacao = EnumCacheService.carregarValuesEnum("ExigibilidadeNfseDTO");
        $scope.situacoes = EnumCacheService.carregarValuesEnum("br.com.webpublico.domain.dto.enums.SituacaoDeclaracaoNfseDTO");
        $scope.orderBy = "";
        $scope.sortReverse = false;
        $scope.meses = Util.getMeses();

        $scope.isPrestadorPermitido = function () {
            return $scope.prestadorPrincipal && $scope.prestadorPrincipal.permitido;
        };

        $scope.irParaNotasRecebidas = function () {
            if (!$scope.isPrestadorPermitido() || !$scope.prestadorPrincipal.prestador.emiteNfs) {
                $state.go('notas-recebidas');
            }
        };

        $scope.irParaNotasRecebidas();

        $scope.habilitarDesabilitarFiltros = function () {
            $scope.mostrarFiltros = !$scope.mostrarFiltros;
            $scope.iniciarFiltros();
        };

        $scope.iniciarFiltros = function () {
            var fullYear = new Date().getFullYear();
            $scope.emissaoInicial = new Date(fullYear - 6, 0, 1);
            $scope.emissaoFinal = new Date(fullYear, 12, 31);
            $scope.naturezaOperacao = {name: null, value: ""};
            $scope.notaFiscalInicial = null;
            $scope.notaFiscalFinal = null;
            $scope.cpfCnpjTomador = null;
            $scope.nomeRazaoSocialTomador = null;
            $scope.numeroRps = null;
            $scope.serieRps = null;
            $scope.situacao = {name: null, value: ""};
            $scope.competenciaAnoInicial = null;
            $scope.competenciaMesInicial = null;
            $scope.competenciaAnoFinal = null;
            $scope.competenciaMesFinal = null;
        };

        $scope.iniciarFiltros();

        $scope.montarConsultaNotaFiscal = function () {
            var campos = [];
            var parametroQueryCampo = {
                campo: "nf.prestador_id",
                operador: "IGUAL",
                valorLong: $scope.prestadorPrincipal.prestador.id
            };
            campos.push(parametroQueryCampo);

            if ($scope.emissaoInicial) {
                var emissaoInicial = DataUtil.dateWithOutHoursMinutesSeconds($scope.emissaoInicial);
                parametroQueryCampo = {
                    campo: "trunc(nf.emissao)",
                    operador: "MAIOR_IGUAL",
                    valorDate: emissaoInicial
                };
                campos.push(parametroQueryCampo);
            }
            if ($scope.emissaoFinal) {
                var emissaoFinal = DataUtil.dateWithOutHoursMinutesSeconds($scope.emissaoFinal);
                parametroQueryCampo = {
                    campo: "trunc(nf.emissao)",
                    operador: "MENOR_IGUAL",
                    valorDate: emissaoFinal
                };
                campos.push(parametroQueryCampo);
            }
            if ($scope.naturezaOperacao && $scope.naturezaOperacao.value) {
                parametroQueryCampo = {
                    campo: "dps.naturezaOperacao",
                    operador: "IGUAL",
                    valorString: $scope.naturezaOperacao.name
                };
                campos.push(parametroQueryCampo);
            }
            if ($scope.notaFiscalInicial) {
                parametroQueryCampo = {
                    campo: "nf.numero",
                    operador: "MAIOR_IGUAL",
                    valorLong: $scope.notaFiscalInicial
                };
                campos.push(parametroQueryCampo);
            }
            if ($scope.notaFiscalFinal) {
                parametroQueryCampo = {
                    campo: "nf.numero",
                    operador: "MENOR_IGUAL",
                    valorLong: $scope.notaFiscalFinal
                };
                campos.push(parametroQueryCampo);
            }
            if ($scope.cpfCnpjTomador) {
                parametroQueryCampo = {
                    campo: "dptnf.cpfcnpj",
                    operador: "LIKE",
                    valorString: "%" + Util.apenasNumeros($scope.cpfCnpjTomador) + "%"
                };
                campos.push(parametroQueryCampo);
            }
            if ($scope.nomeRazaoSocialTomador) {
                parametroQueryCampo = {
                    campo: "lower(dptnf.nomerazaosocial)",
                    operador: "LIKE",
                    valorString: "%" + $scope.nomeRazaoSocialTomador.toLowerCase() + "%"
                };
                campos.push(parametroQueryCampo);
            }
            if ($scope.numeroRps) {
                parametroQueryCampo = {
                    campo: "rps.numero",
                    operador: "IGUAL",
                    valorString: $scope.numeroRps
                };
                campos.push(parametroQueryCampo);
            }
            if ($scope.serieRps) {
                parametroQueryCampo = {
                    campo: "rps.serie",
                    operador: "IGUAL",
                    valorString: $scope.serieRps
                };
                campos.push(parametroQueryCampo);
            }
            if ($scope.situacao && $scope.situacao.value) {
                parametroQueryCampo = {
                    campo: "dps.situacao",
                    operador: "IGUAL",
                    valorString: $scope.situacao.name
                };
                campos.push(parametroQueryCampo);
            }
            if ($scope.competenciaAnoInicial && $scope.competenciaMesInicial) {
                var competenciaInicial = DataUtil.dateWithOutHoursMinutesSeconds(new Date($scope.competenciaAnoInicial,
                    $scope.competenciaMesInicial.numeroMes - 1, 1));
                parametroQueryCampo = {
                    campo: "trunc(dps.competencia)",
                    operador: "MAIOR_IGUAL",
                    valorDate: competenciaInicial
                };
                campos.push(parametroQueryCampo);
            }
            if ($scope.competenciaAnoFinal && $scope.competenciaMesFinal) {
                var competenciaFinal = DataUtil.dateWithOutHoursMinutesSeconds(new Date($scope.competenciaAnoFinal,
                    $scope.competenciaMesFinal.numeroMes, 0));
                parametroQueryCampo = {
                    campo: "trunc(dps.competencia)",
                    operador: "MENOR_IGUAL",
                    valorDate: competenciaFinal
                };
                campos.push(parametroQueryCampo);
            }

            return {
                offset: $scope.page,
                limit: $scope.per_page,
                parametrosQuery: [{
                    juncao: " and ",
                    parametroQueryCampos: campos
                }],
                orderBy: "order by " + $scope.orderBy + " " + ($scope.sortReverse ? 'desc' : 'asc')
            };
        };

        $scope.criteriosUtilizados = function () {
            var filtros = "";
            var juncao = "";

            if ($scope.emissaoInicial) {
                var emissaoInicial = DataUtil.dateWithOutHoursMinutesSeconds($scope.emissaoInicial);
                filtros += juncao + " Período Inicial: " + $filter('date')(emissaoInicial, "dd/MM/yyyy");
                juncao = ", ";
            }
            if ($scope.emissaoFinal) {
                var emissaoFinal = DataUtil.dateWithOutHoursMinutesSeconds($scope.emissaoFinal);
                filtros += juncao + " Período Final: " + $filter('date')(emissaoFinal, "dd/MM/yyyy");
                juncao = ", ";
            }
            if ($scope.naturezaOperacao && $scope.naturezaOperacao.value) {
                filtros += juncao + " Operação: " + $filter('translate')('nfseApp.NaturezaOperacao.' + $scope.naturezaOperacao.name);
                juncao = ", ";
            }
            if ($scope.notaFiscalInicial) {
                filtros += juncao + " N° Nota Fiscal Inicial : " + $scope.notaFiscalInicial;
                juncao = ", ";
            }
            if ($scope.notaFiscalFinal) {
                filtros += juncao + " N° Nota Fiscal Final: " + $scope.notaFiscalFinal;
                juncao = ", ";
            }
            if ($scope.cpfCnpjTomador) {
                filtros += juncao + " CPF/CNPJ Tomador: " + $scope.cpfCnpjTomador;
                juncao = ", ";
            }
            if ($scope.nomeRazaoSocialTomador) {
                filtros += juncao + " Nome/Razão Social Tomador: " + $scope.nomeRazaoSocialTomador;
                juncao = ", ";
            }
            if ($scope.numeroRps) {
                filtros += juncao + " N° RPS: " + $scope.numeroRps;
                juncao = ", ";
            }
            if ($scope.serieRps) {
                filtros += juncao + " Série RPS: " + $scope.serieRps;
                juncao = ", ";
            }
            if ($scope.situacao && $scope.situacao.value) {
                filtros += juncao + " Situação: " + $filter('translate')('nfseApp.SituacaoNota.' + $scope.situacao.name);
            }
            return filtros += ".";
        };

        $scope.loadAll = function () {
            if (!$scope.orderBy) {
                $scope.orderBy = "nf.numero";
                $scope.sortReverse = true;
            }
            var consultaNotaFiscal = $scope.montarConsultaNotaFiscal();
            NotaFiscal.buscarCancelamentos(consultaNotaFiscal,
                function (result, headers) {
                    if (headers)
                        $scope.links = ParseLinks.parse(headers('link'));
                    $scope.notaFiscals = result;
                });
        };

        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };

        $scope.loadAll();

        $scope.clear = function () {
            $scope.notaFiscal = {
                numero: null, codigoVerificacao: null, id: null,
                declaracaoPrestacaoServico: {prestador: localStorageService.get("prestadorPrincipal")}
            };
        };

    });
})();
