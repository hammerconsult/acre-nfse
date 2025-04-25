(function () {
    'use strict';

angular.module('nfseApp')
    .controller('RelatorioNotasEmitidasController', function ($scope,
                                                              localStorageService,
                                                              ImpressaoPdf,
                                                              EnumCacheService,
                                                              $filter,
                                                              DataUtil,
                                                              Notificacao) {

        $scope.prestadorPrincipal = localStorageService.get("prestadorPrincipal");
        $scope.naturezasOperacao = EnumCacheService.carregarValuesEnum("ExigibilidadeNfseDTO");
        $scope.situacoes = EnumCacheService.carregarValuesEnum("br.com.webpublico.domain.dto.enums.SituacaoDeclaracaoNfseDTO");
        $scope.tiposRelatorio = EnumCacheService.carregarValuesEnum("br.com.webpublico.domain.dto.EmissaoRelatorioNotaFiscalDTO$TipoRelatorio");

        $scope.iniciarFiltros = function() {
            $scope.emissaoInicial = new Date();
            $scope.emissaoFinal = new Date();
            $scope.naturezaOperacao = {name: null, value: ""};
            $scope.notaFiscalInicial = null;
            $scope.notaFiscalFinal = null;
            $scope.cpfCnpjTomador = null;
            $scope.nomeRazaoSocialTomador = null;
            $scope.numeroRps = null;
            $scope.serieRps = null;
            $scope.situacao = {name: null, value: ""};
            $scope.tipoRelatorio = {name: "RESUMIDO", value: "RESUMIDO"};
        };

        $scope.iniciarFiltros();

        $scope.montarDadosEmissao = function () {
            var filtros = "";
            var juncao = "";

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
                filtros += juncao + " Período Inicial: " + $filter('date')(emissaoInicial, "dd/MM/yyyy");
                juncao = ", ";
            }
            if ($scope.emissaoFinal) {
                var emissaoFinal = DataUtil.dateWithOutHoursMinutesSeconds($scope.emissaoFinal);
                parametroQueryCampo = {
                    campo: "trunc(nf.emissao)",
                    operador: "MENOR_IGUAL",
                    valorDate: emissaoFinal
                };
                campos.push(parametroQueryCampo);
                filtros += juncao + " Período Final: " + $filter('date')(emissaoFinal, "dd/MM/yyyy");
                juncao = ", ";
            }
            if ($scope.naturezaOperacao && $scope.naturezaOperacao.value) {
                parametroQueryCampo = {
                    campo: "dps.naturezaOperacao",
                    operador: "IGUAL",
                    valorString: $scope.naturezaOperacao.name
                };
                campos.push(parametroQueryCampo);
                filtros += juncao + " Operação: " + $filter('translate')('nfseApp.NaturezaOperacao.' + $scope.naturezaOperacao.name);
                juncao = ", ";
            }
            if ($scope.notaFiscalInicial) {
                parametroQueryCampo = {
                    campo: "nf.numero",
                    operador: "MAIOR_IGUAL",
                    valorLong: $scope.notaFiscalInicial
                };
                campos.push(parametroQueryCampo);
                filtros += juncao + " N° Nota Fiscal Inicial : " + $scope.notaFiscalInicial;
                juncao = ", ";
            }
            if ($scope.notaFiscalFinal) {
                parametroQueryCampo = {
                    campo: "nf.numero",
                    operador: "MENOR_IGUAL",
                    valorLong: $scope.notaFiscalInicial
                };
                campos.push(parametroQueryCampo);
                filtros += juncao + " N° Nota Fiscal Final: " + $scope.notaFiscalFinal;
                juncao = ", ";
            }
            if ($scope.cpfCnpjTomador) {
                parametroQueryCampo = {
                    campo: "dptnf.cpfcnpj",
                    operador: "IGUAL",
                    valorString: $scope.cpfCnpjTomador
                };
                campos.push(parametroQueryCampo);
                filtros += juncao + " CPF/CNPJ Tomador: " + $scope.cpfCnpjTomador;
                juncao = ", ";
            }
            if ($scope.nomeRazaoSocialTomador) {
                parametroQueryCampo = {
                    campo: "dptnf.nomerazaosocial",
                    operador: "LIKE",
                    valorString: "%" + $scope.nomeRazaoSocialTomador + "%"
                };
                campos.push(parametroQueryCampo);
                filtros += juncao + " Nome/Razão Social Tomador: " + $scope.nomeRazaoSocialTomador;
                juncao = ", ";
            }
            if ($scope.numeroRps) {
                parametroQueryCampo = {
                    campo: "rps.numero",
                    operador: "IGUAL",
                    valorString: $scope.numeroRps
                };
                campos.push(parametroQueryCampo);
                filtros += juncao + " N° RPS: " + $scope.numeroRps;
                juncao = ", ";
            }
            if ($scope.serieRps) {
                parametroQueryCampo = {
                    campo: "rps.serie",
                    operador: "IGUAL",
                    valorString: $scope.serieRps
                };
                campos.push(parametroQueryCampo);
                filtros += juncao + " Série RPS: " + $scope.serieRps;
                juncao = ", ";
            }
            if ($scope.situacao && $scope.situacao.value) {
                parametroQueryCampo = {
                    campo: "dps.situacao",
                    operador: "IGUAL",
                    valorString: $scope.situacao.name
                };
                campos.push(parametroQueryCampo);
                filtros += juncao + " Situação: " + $filter('translate')('nfseApp.SituacaoNota.' + $scope.situacao.name);
            }
            filtros += ".";

            return {
                parametrosQuery: [{
                    juncao: " and ",
                    parametroQueryCampos: campos
                }],
                filtros: filtros,
                orderBy: " order by dptnf.nomerazaosocial asc ",
                tipoNota: "EMITIDA",
                tipoRelatorio: $scope.tipoRelatorio.name
            }
        };

        function validarCampos() {
            var camposNaoInformados = [];
            if (!$scope.emissaoInicial) {
                camposNaoInformados.push("O campo Período Inicial deve ser informado");
            }
            if (!$scope.emissaoFinal) {
                camposNaoInformados.push("O campo Período Final deve ser informado");
            }
            if (!$scope.tipoRelatorio.name) {
                camposNaoInformados.push("O campo Tipo de Relatório deve ser informado");
            }
            if (camposNaoInformados.length > 0) {
                Notificacao.camposObrigatorios(camposNaoInformados, "error");
                return false;
            }
            return true;
        }

        $scope.imprimirRelatorioNotasFiscais = function () {
            if (validarCampos()) {
                var dadosEmissao = $scope.montarDadosEmissao();
                ImpressaoPdf.imprimirPdfViaPost('/api/notaFiscals/relatorio-notas-fiscais', dadosEmissao);
            }
        };

    })
;
})();
