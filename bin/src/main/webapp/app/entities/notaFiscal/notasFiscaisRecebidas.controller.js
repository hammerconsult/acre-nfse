(function () {
    'use strict';

angular.module('nfseApp')
    .controller('NotasFiscaisRecebidasController',
        function ($scope, $window, NotaFiscal, NotaFiscalSearch, ParseLinks,
                  localStorageService, SweetAlert, $state, ImpressaoPdf, $modal,
                  DataUtil, $filter, EnumCacheService, Principal, Util, $http, Notificacao) {
            $scope.notaFiscals = [];
            $scope.per_page = 20;
            $scope.mostrarFiltros = false;
            $scope.naturezasOperacao = EnumCacheService.carregarValuesEnum("ExigibilidadeNfseDTO");
            $scope.situacoes = EnumCacheService.carregarValuesEnum("br.com.webpublico.domain.dto.enums.SituacaoDeclaracaoNfseDTO");
            $scope.meses = Util.getMeses();

            Principal.identity().then(function (account) {
                $scope.account = account;
                $scope.prestadorPrincipal = localStorageService.get("prestadorPrincipal");
                $scope.loadAll();
            });

            $scope.isPrestadorPermitido = function () {
                return $scope.prestadorPrincipal && $scope.prestadorPrincipal.permitido;
            };

            $scope.habilitarDesabilitarFiltros = function () {
                $scope.mostrarFiltros = !$scope.mostrarFiltros;
                $scope.iniciarFiltros();
            };

            $scope.iniciarFiltros = function () {
                var fullYear = new Date().getFullYear();
                $scope.emissaoInicial = $filter('date')(new Date(fullYear - 6, 0, 1), 'dd/MM/yyyy');
                $scope.emissaoFinal = $filter('date')(new Date(fullYear, 12, 31), 'dd/MM/yyyy');
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
                var filtros = "";
                var juncao = "";

                var campos = [];
                if ($scope.prestadorPrincipal) {
                    var parametroQueryCampo = {
                        campo: "dptnf.cpfcnpj",
                        operador: "IGUAL",
                        valorString: Util.apenasNumeros($scope.prestadorPrincipal.prestador.pessoa.dadosPessoais.cpfCnpj)
                    };
                    campos.push(parametroQueryCampo);
                } else {
                    var parametroQueryCampo = {
                        campo: "dptnf.cpfcnpj",
                        operador: "IGUAL",
                        valorString: Util.apenasNumeros($scope.account.login)
                    };
                    campos.push(parametroQueryCampo);
                }
                if ($scope.emissaoInicial) {
                    parametroQueryCampo = {
                        campo: "trunc(obj.emissao)",
                        operador: "MAIOR_IGUAL",
                        valorDateString: $scope.emissaoInicial
                    };
                    campos.push(parametroQueryCampo);
                    filtros += juncao + " Período Inicial: " + $scope.emissaoInicial;
                    juncao = ", ";
                }
                if ($scope.emissaoFinal) {
                    parametroQueryCampo = {
                        campo: "trunc(obj.emissao)",
                        operador: "MENOR_IGUAL",
                        valorDateString: $scope.emissaoFinal
                    };
                    campos.push(parametroQueryCampo);
                    filtros += juncao + " Período Final: " + $scope.emissaoFinal;
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
                        campo: "obj.numero",
                        operador: "MAIOR_IGUAL",
                        valorLong: $scope.notaFiscalInicial
                    };
                    campos.push(parametroQueryCampo);
                    filtros += juncao + " N° Nota Fiscal Inicial : " + $scope.notaFiscalInicial;
                    juncao = ", ";
                }
                if ($scope.notaFiscalFinal) {
                    parametroQueryCampo = {
                        campo: "obj.numero",
                        operador: "MENOR_IGUAL",
                        valorLong: $scope.notaFiscalInicial
                    };
                    campos.push(parametroQueryCampo);
                    filtros += juncao + " N° Nota Fiscal Final: " + $scope.notaFiscalFinal;
                    juncao = ", ";
                }
                if ($scope.cpfCnpjPrestador) {
                    parametroQueryCampo = {
                        campo: "dppnf.cpfcnpj",
                        operador: "IGUAL",
                        valorString: Util.apenasNumeros($scope.cpfCnpjPrestador)
                    };
                    campos.push(parametroQueryCampo);
                    filtros += juncao + " CPF/CNPJ Prestador: " + $scope.cpfCnpjPrestador;
                    juncao = ", ";
                }
                if ($scope.nomeRazaoSocialPrestador) {
                    parametroQueryCampo = {
                        campo: "dppnf.nomerazaosocial",
                        operador: "LIKE",
                        valorString: "%" + $scope.nomeRazaoSocialPrestador + "%"
                    };
                    campos.push(parametroQueryCampo);
                    filtros += juncao + " Nome/Razão Social Prestador: " + $scope.nomeRazaoSocialPrestador;
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
                if ($scope.competenciaAnoInicial && $scope.competenciaMesInicial) {
                    var competenciaInicial = DataUtil.dateWithOutHoursMinutesSeconds(new Date($scope.competenciaAnoInicial,
                        $scope.competenciaMesInicial.numeroMes - 1, 1));
                    parametroQueryCampo = {
                        campo: "trunc(dps.competencia)",
                        operador: "MAIOR_IGUAL",
                        valorDateString: $filter('date')(competenciaInicial, "dd/MM/yyyy", "+0000")
                    };
                    campos.push(parametroQueryCampo);
                    filtros += juncao + " Competência Inicial: " + $scope.competenciaAnoInicial + "/" + $scope.competenciaMesInicial;
                }
                if ($scope.competenciaAnoFinal && $scope.competenciaMesFinal) {
                    var competenciaFinal = DataUtil.dateWithOutHoursMinutesSeconds(new Date($scope.competenciaAnoFinal,
                        $scope.competenciaMesFinal.numeroMes, 0));
                    parametroQueryCampo = {
                        campo: "trunc(dps.competencia)",
                        operador: "MENOR_IGUAL",
                        valorDateString: $filter('date')(competenciaFinal, "dd/MM/yyyy", "+0000")
                    };
                    campos.push(parametroQueryCampo);
                    filtros += juncao + " Competência Final: " + $scope.competenciaAnoFinal + "/" + $scope.competenciaMesFinal;
                }
                filtros += ".";

                return {
                    offset: $scope.page,
                    limit: $scope.per_page,
                    parametrosQuery: [{
                        juncao: " and ",
                        parametroQueryCampos: campos
                    }],
                    orderBy: " order by obj.emissao desc "
                };
            };

            $scope.loadAll = function () {
                var consultaNotaFiscal = $scope.montarConsultaNotaFiscal();
                NotaFiscal.buscarNotasFiscais(consultaNotaFiscal,
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

            $scope.imprime = function (id) {
                NotaFiscal.imprime({id: id});
            };

            $scope.refresh = function () {
                $scope.loadAll();
                $scope.clear();
            };

            $scope.clear = function () {
                $scope.notaFiscal = {
                    numero: null, codigoVerificacao: null, id: null,
                    declaracaoPrestacaoServico: {prestador: localStorageService.get("prestadorPrincipal")}
                };
            };


            $scope.imprimirNota = function (id) {
                ImpressaoPdf.imprimirPdfViaUrl('/publico/notaFiscal/imprime/' + id);
            };

            $scope.enviarNota = function (nota) {
                var modalEnviarNota = $modal.open({
                    templateUrl: 'app/entities/notaFiscal/enviar-nota-email.html',
                    controller: 'EnviarNotaEmailController',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return nota
                        }
                    }
                });
                modalEnviarNota.result.then(function (data) {
                    if (data) {
                    }
                });
            };

            $scope.adicionarFiltro = function () {
                $scope.filtrosSelecionados.push({
                    atributo: $scope.filtrosDisponiveis[0],
                    operacao: $scope.filtrosDisponiveis[0].operacoes[0].value
                });
            };

            $scope.removerFiltro = function (index) {
                $scope.filtrosSelecionados.splice(index, 1);
                if ($scope.filtrosSelecionados.length == 0) {
                    $scope.loadAll();
                }
            };

            $scope.selecionouCampo = function (filtro) {
                filtro.valorTexto = null;
                filtro.valorDecimal = null;
                filtro.valorInteiro = null;
                filtro.valorLogico = false;
                filtro.valorData = null;
            };

            $scope.baixarXMlNotaFiscal = function (idNotaFiscal) {
                NotaFiscal.baixarXmlNotaFiscal({
                    idNotaFiscal: idNotaFiscal
                }, function (result) {
                    var a = document.createElement('a');
                    a.href = 'data:application/xml;base64,' + result.conteudo;
                    a.target = '_blank';
                    a.download = 'notaFiscal' + idNotaFiscal + '.xml';
                    document.body.appendChild(a);
                    a.click();
                });
            };

            $scope.baixarPDFResumido = function () {
                $scope.baixarPDF("RESUMIDO");
            };

            $scope.baixarPDFDetalhado = function () {
                $scope.baixarPDF("DETALHADO");
            };

            $scope.ultrapassouLimiteRegistros = function () {
                if (($scope.per_page * $scope.links.last) > 500) {
                    SweetAlert.swal({
                            title: "Atenção",
                            text: "Limite de notas fiscais ultrapassado (500 registros). Por favor melhore os filtros.",
                            type: "warning",
                            showCancelButton: false,
                            confirmButtonColor: "#DD6B55",
                            confirmButtonText: "Ok",
                            closeOnConfirm: true
                        },
                        function (isConfirm) {
                            if (isConfirm) {
                                $scope.mostrarFiltros = true;
                                $scope.iniciarFiltros();
                                $timeout(function () {
                                    var element = $window.document.getElementById("dataInicial");
                                    if (element) {
                                        element.focus();
                                        $window.scrollTo(0, 0);
                                    }
                                });
                            }
                        });
                    return true;
                }
                return false;
            };

            $scope.baixarPDF = function (tipoRelatorio) {
                // if ($scope.ultrapassouLimiteRegistros()) {
                //     return;
                // }
                var emissao = {
                    consultaNotaFiscal: $scope.montarConsultaNotaFiscal(),
                    criteriosUtilizados: $scope.criteriosUtilizados(),
                    tipoNota: "RECEBIDA",
                    tipoRelatorio: tipoRelatorio
                };
                ImpressaoPdf.imprimirPdfViaPost('/api/notaFiscals/relatorio-notas-fiscais', emissao);
            };

            $scope.baixarExcel = function () {
                var consultaNotaFiscal = $scope.montarConsultaNotaFiscal();
                consultaNotaFiscal.orderBy = " order by obj.numero desc ";
                var emissao = {
                    consultaNotaFiscal: consultaNotaFiscal,
                    criteriosUtilizados: $scope.criteriosUtilizados(),
                    tipoNota: "RECEBIDA",
                    tipoRelatorio: "RESUMIDO"
                };
                $http.post('api/notaFiscals/relatorio-notas-fiscais-excel', emissao, {responseType: 'arraybuffer'}).then(function (response) {
                    if (response.data.byteLength == 0) {
                        Notificacao.warn('Atenção', 'Nenhum registro para ser impresso.');
                    } else {
                        var blob = new Blob([response.data], {
                            type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        });
                        saveAs(blob, "NotasFiscaisRecebidas.xlsx");
                    }
                });
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
                if ($scope.cpfCnpjPrestador) {
                    filtros += juncao + " CPF/CNPJ Prestador: " + $scope.cpfCnpjPrestador;
                    juncao = ", ";
                }
                if ($scope.nomeRazaoSocialTomador) {
                    filtros += juncao + " Nome/Razão Social Prestador: " + $scope.nomeRazaoSocialPrestador;
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
        });
})();
