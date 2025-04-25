(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('RPSController', function ($scope, $state, RPS, ParseLinks, SweetAlert, ExportarXls,
                                               localStorageService, DataUtil, ImpressaoPdf, Util, EnumCacheService,
                                               $filter) {
            $scope.listaRps = [];
            $scope.page = 0;
            $scope.per_page = 20;
            $scope.orderBy = "";
            $scope.sortReverse = false;
            $scope.mostrarFiltros = false;
            $scope.prestadorPrincipal = localStorageService.get("prestadorPrincipal");
            $scope.meses = Util.getMeses();
            $scope.exercicios = Util.getExercicios();

            $scope.naturezasOperacao = EnumCacheService.carregarValuesEnum("ExigibilidadeNfseDTO");
            $scope.situacoes = EnumCacheService.carregarValuesEnum("br.com.webpublico.domain.dto.enums.SituacaoDeclaracaoNfseDTO");

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
                $scope.numeroRpsInicial = null;
                $scope.numeroRpsFinal = null;
                $scope.serieRps = null;
                $scope.situacao = {name: null, value: ""};
                $scope.competenciaMesInicial = null;
                $scope.competenciaAnoInicial = null;
                $scope.competenciaMesFinal = null;
                $scope.competenciaAnoFinal = null;
            };

            $scope.iniciarFiltros();

            $scope.montarConsultaRps = function () {
                var campos = [];
                var parametroQueryCampo = {
                    campo: "n.prestador_id",
                    operador: "IGUAL",
                    valorLong: $scope.prestadorPrincipal.prestador.id
                };
                campos.push(parametroQueryCampo);

                if ($scope.emissaoInicial) {
                    var emissaoInicial = DataUtil.dateWithOutHoursMinutesSeconds($scope.emissaoInicial);
                    parametroQueryCampo = {
                        campo: "trunc(r.DATAEMISSAO)",
                        operador: "MAIOR_IGUAL",
                        valorDate: emissaoInicial
                    };
                    campos.push(parametroQueryCampo);
                }
                if ($scope.emissaoFinal) {
                    var emissaoFinal = DataUtil.dateWithOutHoursMinutesSeconds($scope.emissaoFinal);
                    parametroQueryCampo = {
                        campo: "trunc(r.DATAEMISSAO)",
                        operador: "MENOR_IGUAL",
                        valorDate: emissaoFinal
                    };
                    campos.push(parametroQueryCampo);
                }
                if ($scope.naturezaOperacao && $scope.naturezaOperacao.value) {
                    parametroQueryCampo = {
                        campo: "dec.naturezaOperacao",
                        operador: "IGUAL",
                        valorString: $scope.naturezaOperacao.name
                    };
                    campos.push(parametroQueryCampo);
                }
                if ($scope.notaFiscalInicial) {
                    parametroQueryCampo = {
                        campo: "n.numero",
                        operador: "MAIOR_IGUAL",
                        valorLong: $scope.notaFiscalInicial
                    };
                    campos.push(parametroQueryCampo);
                }
                if ($scope.notaFiscalFinal) {
                    parametroQueryCampo = {
                        campo: "n.numero",
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
                if ($scope.numeroRpsInicial) {
                    parametroQueryCampo = {
                        campo: "to_number(r.numero)",
                        operador: "MAIOR_IGUAL",
                        valorLong: $scope.numeroRpsInicial
                    };
                    campos.push(parametroQueryCampo);
                }
                if ($scope.numeroRpsFinal) {
                    parametroQueryCampo = {
                        campo: "to_number(r.numero)",
                        operador: "MENOR_IGUAL",
                        valorLong: $scope.numeroRpsFinal
                    };
                    campos.push(parametroQueryCampo);
                }
                if ($scope.serieRps) {
                    parametroQueryCampo = {
                        campo: "lower(r.serie)",
                        operador: "LIKE",
                        valorString: "%" + $scope.serieRps.toLowerCase() + "%"
                    };
                    campos.push(parametroQueryCampo);
                }
                if ($scope.situacao && $scope.situacao.value) {
                    parametroQueryCampo = {
                        campo: "dec.situacao",
                        operador: "IGUAL",
                        valorString: $scope.situacao.name
                    };
                    campos.push(parametroQueryCampo);
                }
                if ($scope.competenciaAnoInicial && $scope.competenciaMesInicial) {
                    var competenciaInicial = DataUtil.dateWithOutHoursMinutesSeconds(new Date($scope.competenciaAnoInicial,
                        $scope.competenciaMesInicial.numeroMes - 1, 1));
                    parametroQueryCampo = {
                        campo: "trunc(dec.competencia)",
                        operador: "MAIOR_IGUAL",
                        valorDate: competenciaInicial
                    };
                    campos.push(parametroQueryCampo);
                }
                if ($scope.competenciaAnoFinal && $scope.competenciaMesFinal) {
                    var competenciaFinal = DataUtil.dateWithOutHoursMinutesSeconds(new Date($scope.competenciaAnoFinal,
                        $scope.competenciaMesFinal.numeroMes, 0));
                    parametroQueryCampo = {
                        campo: "trunc(dec.competencia)",
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

            $scope.loadAll = function () {
                if (!$scope.orderBy) {
                    $scope.orderBy = "to_number(r.numero)";
                    $scope.sortReverse = true;
                }
                var consultaRps = $scope.montarConsultaRps();
                RPS.buscarRps(consultaRps,
                    function (result, headers) {
                        if (headers)
                            $scope.links = ParseLinks.parse(headers('link'));
                        $scope.listaRps = result;
                    });
            };

            $scope.loadPage = function (page) {
                $scope.page = page;
                $scope.loadAll();
            };

            $scope.loadAll();

            $scope.refresh = function () {
                $scope.loadAll();
            };

            $scope.validarFiltros = function () {
                for (var i = 0; i < $scope.filtrosSelecionados.length; i++) {
                    if ($scope.filtrosSelecionados[i].atributo == null) {
                        SweetAlert.warning("Filtro Personalizado inválido!", "Selecione uma opção de filtro.");
                        return false;
                    }
                    if (!$scope.filtrosSelecionados[i].operacao) {
                        SweetAlert.warning("Filtro Personalizado inválido!", "Selecione um operador de filtro.");
                        return false;
                    }
                    if ($scope.filtrosSelecionados[i].atributo.tipo != 'logico' &&
                        !$scope.filtrosSelecionados[i].valorTexto &&
                        !$scope.filtrosSelecionados[i].valorData &&
                        !$scope.filtrosSelecionados[i].valorDecimal &&
                        !$scope.filtrosSelecionados[i].valorInteiro) {
                        SweetAlert.warning("Filtro Personalizado inválido!", "Defina um valor para o filtro.");
                        return false;
                    }
                }
                return true;
            };

            $scope.gerarNota = function (rps) {
                SweetAlert.swal({
                        title: "Confirme para gerar a Nota Fiscal",
                        text: "Tem certeza que quer gerar a nota fiscal  do RPS de N° " + rps.numero + " para a competência atual? ",
                        type: "warning",
                        showCancelButton: true,
                        confirmButtonColor: "#F7A54A", confirmButtonText: "Sim, Copiar Nota Fiscal",
                        cancelButtonText: "Não, Cancelar",
                        closeOnConfirm: false,
                        closeOnCancel: false
                    },
                    function (isConfirm) {
                        if (isConfirm) {
                            $state.go('notaFiscal.byRps', ({numeroRps: rps.numero}));
                            SweetAlert.swal("Nota Fiscal Copiada!", "Prossiga para a emissão da Nota Fiscal.", "success");
                        } else {
                            SweetAlert.swal("Cancelado", "A Nota Fiscal não foi copiada.", "error");
                        }
                    }
                )
            };

            $scope.adicionarFiltro = function () {
                $scope.filtrosSelecionados.push({
                    atributo: $scope.filtrosDisponiveis[0],
                    operacao: $scope.filtrosDisponiveis[0].operacoes[0].value
                });
            };

            $scope.removerFiltro = function (index) {
                $scope.filtrosSelecionados.splice(index, 1);
            };


            $scope.montarFiltros = function () {
                var lista = [];

                angular.forEach($scope.filtrosSelecionados, function (filtro, index) {

                    lista.push(
                        {
                            operacao: filtro.operacao,
                            campo: filtro.atributo.nomeAtributo,
                            valorTexto: (filtro.operacao !== 'LIKE' ? filtro.valorTexto : '%' + filtro.valorTexto.trim().toLowerCase() + '%'),
                            valorData: filtro.valorData,
                            valorLogico: filtro.valorLogico,
                            valorDecimal: filtro.valorDecimal,
                            valorInteiro: filtro.valorInteiro
                        }
                    );
                });
                return lista;
            };

            var colunas = [
                {descricao: 'Número', valor: 'numero'},
                {descricao: 'Série', valor: 'serie'},
                {descricao: 'Emissão', valor: 'dataEmissao'},
                {descricao: 'Valor Serviços', valor: 'totalServicos'},
                {descricao: 'Valor ISS', valor: 'iss'},
                {descricao: 'Núemro da Nfse', valor: 'numeroNotaFiscal'},
                {descricao: 'Núemro do Lote', valor: 'numeroLote'},
            ];

            $scope.baixarXls = function () {
                var xls = {
                    colunas: colunas,
                    linhas: $scope.listaRps
                };
                ExportarXls.getXls(xls);
            };

            $scope.baixarPDFResumido = function () {
                $scope.baixarPDF("RESUMIDO");
            };

            $scope.baixarPDFDetalhado = function () {
                $scope.baixarPDF("DETALHADO");
            };


            $scope.criteriosUtilizados = function () {
                var filtros = "";
                var juncao = "";

                if ($scope.emissaoInicial) {
                    filtros += juncao + " Emissão Inicial: " + $filter('date')($scope.emissaoInicial, "dd/MM/yyyy");
                    juncao = ", ";
                }
                if ($scope.emissaoFinal) {
                    filtros += juncao + " Emissão Final: " + $filter('date')($scope.emissaoFinal, "dd/MM/yyyy");
                    juncao = ", ";
                }
                if ($scope.naturezaOperacao && $scope.naturezaOperacao.value) {
                    filtros += juncao + " Operação: " + $filter('translate')('nfseApp.NaturezaOperacao.' + $scope.naturezaOperacao.name);
                    juncao = ", ";
                }
                if ($scope.situacao && $scope.situacao.value) {
                    filtros += juncao + " Situação: " + $filter('translate')('nfseApp.SituacaoNota.' + $scope.situacao.name);
                }
                if ($scope.notaFiscalInicial) {
                    filtros += juncao + " N° Nota Fiscal Inicial : " + $scope.notaFiscalInicial;
                    juncao = ", ";
                }
                if ($scope.notaFiscalFinal) {
                    filtros += juncao + " N° Nota Fiscal Final: " + $scope.notaFiscalFinal;
                    juncao = ", ";
                }
                if ($scope.numeroRpsInicial) {
                    filtros += juncao + " N° RPS Inicial : " + $scope.numeroRpsInicial;
                    juncao = ", ";
                }
                if ($scope.numeroRpsFinal) {
                    filtros += juncao + " N° RPS Final: " + $scope.numeroRpsFinal;
                    juncao = ", ";
                }
                if ($scope.serieRps) {
                    filtros += juncao + " Serie RPS: " + $scope.serieRps;
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
                if ($scope.competenciaAnoInicial && $scope.competenciaMesInicial) {
                    filtros += juncao + " Competência Inicial: " + $scope.competenciaMesInicial.numeroMes + "/" +
                        $scope.competenciaAnoInicial;
                    juncao = ", ";
                }
                if ($scope.competenciaAnoFinal && $scope.competenciaMesFinal) {
                    filtros += juncao + " Competência Final: " + $scope.competenciaMesFinal.numeroMes + "/" +
                        $scope.competenciaAnoFinal;
                    juncao = ", ";
                }
                return filtros += ".";
            };

            $scope.baixarPDF = function (tipoRelatorio) {

                if (!$scope.orderBy) {
                    $scope.orderBy = "to_number(r.numero)";
                    $scope.sortReverse = true;
                }
                var emissao = {
                    consultaGenerica: $scope.montarConsultaRps(),
                    criteriosUtilizados: $scope.criteriosUtilizados(),
                    tipoRelatorio: tipoRelatorio
                };
                ImpressaoPdf.imprimirPdfViaPost('/api/imprimir-relatorio-rps', emissao);
            };

        });
})();
