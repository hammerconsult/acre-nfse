(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('NotaFiscalController',
            function ($scope, $window, NotaFiscal, NotaFiscalSearch, ParseLinks,
                      localStorageService, SweetAlert, $state, ImpressaoPdf,
                      $modal, ExportarXls, moment, DataUtil, $filter, EnumCacheService,
                      Notificacao, $timeout, Util, $http, DateUtils) {
                $scope.notaFiscals = [];
                $scope.per_page = 20;
                $scope.notaFiscalSelecionada;
                $scope.prestadorPrincipal = localStorageService.get("prestadorPrincipal");
                $scope.mostrarFiltros = false;
                $scope.naturezasOperacao = EnumCacheService.carregarValuesEnum("ExigibilidadeNfseDTO");
                $scope.situacoes = EnumCacheService.carregarValuesEnum("br.com.webpublico.domain.dto.enums.SituacaoDeclaracaoNfseDTO");
                $scope.ordenacao = [];
                $scope.meses = Util.getMeses();

                $scope.isPrestadorPermitido = function () {
                    return $scope.prestadorPrincipal && $scope.prestadorPrincipal.permitido;
                };

                $scope.irParaNotasRecebidas = function () {
                    if (!$scope.isPrestadorPermitido()) {
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
                    $scope.ordenacao.push({coluna: "obj.numero", sort: "desc"});
                };

                $scope.iniciarFiltros();

                $scope.montarConsultaNotaFiscal = function () {
                    var campos = [];
                    var parametroQueryCampo = {
                        campo: "obj.prestador_id",
                        operador: "IGUAL",
                        valorLong: $scope.prestadorPrincipal.prestador.id
                    };
                    campos.push(parametroQueryCampo);
                    parametroQueryCampo = {
                        campo: "coalesce(obj.homologacao, 0)",
                        operador: "IGUAL",
                        valorLong: 0
                    };
                    campos.push(parametroQueryCampo);
                    if ($scope.emissaoInicial) {
                        parametroQueryCampo = {
                            campo: "trunc(obj.emissao)",
                            operador: "MAIOR_IGUAL",
                            valorDateString: $scope.emissaoInicial
                        };
                        campos.push(parametroQueryCampo);
                    }
                    if ($scope.emissaoFinal) {
                        parametroQueryCampo = {
                            campo: "trunc(obj.emissao)",
                            operador: "MENOR_IGUAL",
                            valorDateString: $scope.emissaoFinal
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
                            campo: "obj.numero",
                            operador: "MAIOR_IGUAL",
                            valorLong: $scope.notaFiscalInicial
                        };
                        campos.push(parametroQueryCampo);
                    }
                    if ($scope.notaFiscalFinal) {
                        parametroQueryCampo = {
                            campo: "obj.numero",
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
                        var competenciaInicial = $filter('date')(new Date($scope.competenciaAnoInicial,
                            $scope.competenciaMesInicial.numeroMes - 1, 1), 'dd/MM/yyyy');
                        parametroQueryCampo = {
                            campo: "trunc(dps.competencia)",
                            operador: "MAIOR_IGUAL",
                            valorDateString: competenciaInicial
                        };
                        campos.push(parametroQueryCampo);
                    }
                    if ($scope.competenciaAnoFinal && $scope.competenciaMesFinal) {
                        var competenciaFinal = $filter('date')(new Date($scope.competenciaAnoFinal,
                            $scope.competenciaMesFinal.numeroMes, 0), "dd/MM/yyyy");
                        parametroQueryCampo = {
                            campo: "trunc(dps.competencia)",
                            operador: "MENOR_IGUAL",
                            valorDateString: competenciaFinal
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
                        orderBy: $scope.montarOrderBy()
                    };
                };

                $scope.criteriosUtilizados = function () {
                    var filtros = "";
                    var juncao = "";

                    if ($scope.emissaoInicial) {
                        filtros += juncao + " Emissão Inicial: " + $scope.emissaoInicial;
                        juncao = ", ";
                    }
                    if ($scope.emissaoFinal) {
                        filtros += juncao + " Emissão Final: " +  $scope.emissaoFinal;
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

                $scope.montarOrderBy = function () {
                    var orderBy = "";
                    if ($scope.ordenacao) {
                        var juncao = " order by ";
                        for (var i = 0; i < $scope.ordenacao.length; i++) {
                            orderBy += juncao + " " + $scope.ordenacao[i].coluna + " " +
                                $scope.ordenacao[i].sort + " ";
                            juncao = ", ";
                        }
                    }
                    return orderBy;
                };

                $scope.loadAll = function () {
                    var consultaNotaFiscal = $scope.montarConsultaNotaFiscal();
                    NotaFiscal.buscarNotasFiscais(consultaNotaFiscal,
                        function (result, headers) {
                            if (headers) {
                                $scope.links = ParseLinks.parse(headers('link'));
                                $scope.totalItens = headers('x-total-count');
                            }
                            $scope.notaFiscals = result;
                            $scope.getTotalIss();
                        });
                };

                $scope.loadPage = function (page) {
                    $scope.page = page;
                    $scope.loadAll();
                };

                $scope.loadAll();

                $scope.copiarNota = function (nota) {
                    NotaFiscal.copiarNota(nota);
                };

                $scope.delete = function (id) {
                    NotaFiscal.get({id: id}, function (result) {
                        $scope.notaFiscal = result;
                        SweetAlert.swal({
                                title: "Confirme a exclusão",
                                text: "Você tem certeza que quer excluir a Nota Fiscal?",
                                type: "warning",
                                showCancelButton: true,
                                confirmButtonColor: "#DD6B55", confirmButtonText: "Sim, Excluir",
                                cancelButtonText: "Não, Cancelar",
                                closeOnConfirm: false,
                                closeOnCancel: false
                            },
                            function (isConfirm) {
                                if (isConfirm) {
                                    $scope.confirmDelete(id);
                                } else {
                                    SweetAlert.swal("Cancelado", "A Nota Fiscal não foi removida :)", "error");
                                }
                            });

                    });
                };

                $scope.imprime = function (id) {
                    NotaFiscal.imprime({id: id});
                };

                $scope.confirmDelete = function (id) {
                    NotaFiscal.delete({id: id},
                        function () {
                            $scope.loadAll();
                            SweetAlert.swal("Pronto!", "A Nota Fiscal foi removida com sucesso.", "success");
                        });
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
                    NotaFiscal.imprimirNota(id);
                };

                $scope.baixarXMlNotaFiscal = function (idNotaFiscal) {
                    NotaFiscal.downloadXmlNotaFiscal(idNotaFiscal);
                };

                $scope.baixarXMl = function () {
                    // if ($scope.ultrapassouLimiteRegistros()) {
                    //     return;
                    // }
                    $scope.orderBy = "obj.numero";
                    $scope.sortReverse = true;
                    var baixarXml = {
                        tipo: $scope.tipo,
                        consultaNotaFiscalDTO: $scope.montarConsultaNotaFiscal()
                    };
                    NotaFiscal.baixarXml(baixarXml, function (result) {
                        var a = document.createElement('a');
                        a.href = 'data:application/xml;base64,' + result.conteudo;
                        a.target = '_blank';
                        a.download = 'lote-notas-fiscais.xml';
                        document.body.appendChild(a);
                        a.click();
                    });
                };

                $scope.baixarExcel = function () {
                    var consultaNotaFiscal = $scope.montarConsultaNotaFiscal();
                    consultaNotaFiscal.orderBy = " order by dptnf.nomerazaosocial, obj.numero desc ";
                    var emissao = {
                        consultaNotaFiscal: consultaNotaFiscal,
                        criteriosUtilizados: $scope.criteriosUtilizados(),
                        tipoNota: "EMITIDA",
                        tipoRelatorio: "RESUMIDO"
                    };
                    $http.post('api/notaFiscals/relatorio-notas-fiscais-excel', emissao, {responseType: 'arraybuffer'}).then(function (response) {
                        if (response.data.byteLength == 0) {
                            Notificacao.warn('Atenção', 'Nenhum registro para ser impresso.');
                        } else {
                            var blob = new Blob([response.data], {
                                type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                            });
                            saveAs(blob, "NotasFiscaisEmitidas.xlsx");
                        }
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
                    var consultaNotaFiscal = $scope.montarConsultaNotaFiscal();
                    consultaNotaFiscal.orderBy = " order by dptnf.nomerazaosocial, obj.numero desc ";
                    var emissao = {
                        consultaNotaFiscal: consultaNotaFiscal,
                        criteriosUtilizados: $scope.criteriosUtilizados(),
                        tipoNota: "EMITIDA",
                        tipoRelatorio: tipoRelatorio
                    };
                    ImpressaoPdf.imprimirPdfViaPost('/api/notaFiscals/relatorio-notas-fiscais', emissao);
                };

                $scope.enviarNota = function (nota) {
                    NotaFiscal.enviarNota(nota);
                };

                $scope.cartaCorrecaoNota = function (nota) {
                    NotaFiscal.get({id: nota.id}, function (data) {
                        NotaFiscal.cartaCorrecaoNota(data, function () {
                            $scope.loadAll();
                        });
                    });
                };

                $scope.cancelarNota = function (nota) {
                    NotaFiscal.cancelarNota(nota, function () {
                        $scope.loadAll();
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

                $scope.getTotalIss = function () {
                    var totalIss = 0;
                    angular.forEach($scope.notaFiscals, function (value, key) {
                        totalIss += value.iss;
                    });
                    return totalIss;
                };

                $scope.getTotalServicos = function () {
                    var totalIss = 0;
                    angular.forEach($scope.notaFiscals, function (value, key) {
                        totalIss += value.totalServicos;
                    });
                    return totalIss;
                };

                $scope.getTotalBaseCalculo = function () {
                    var totalIss = 0;
                    angular.forEach($scope.notaFiscals, function (value, key) {
                        totalIss += value.baseCalculo;
                    });
                    return totalIss;
                };

                $scope.selecionarNota = function (notaFiscal) {
                    $scope.notaFiscalSelecionada = notaFiscal;
                };

                $scope.classLinhaTabela = function (notaFiscal) {
                    if ($scope.notaFiscalSelecionada && $scope.notaFiscalSelecionada.id == notaFiscal.id) {
                        return "success";
                    }
                    return "";
                };

                $scope.getItemOrdenacaoByColuna = function (coluna) {
                    var item;
                    for (var i = 0; i < $scope.ordenacao.length; i++) {
                        if ($scope.ordenacao[i].coluna === coluna) {
                            item = $scope.ordenacao[i];
                        }
                    }
                    return item;
                };

                $scope.ordenarPor = function (coluna) {
                    var item = $scope.getItemOrdenacaoByColuna(coluna);
                    if (item) {
                        if (item.sort === "asc") {
                            item.sort = "desc";
                        } else if (item.sort === "desc") {
                            $scope.ordenacao.splice($scope.ordenacao.indexOf(item), 1);
                        }
                    } else {
                        $scope.ordenacao.push({coluna: coluna, sort: "asc"});
                    }
                    $scope.loadAll();
                };

                $scope.iconOrdenacao = function (coluna) {
                    var item = $scope.getItemOrdenacaoByColuna(coluna);
                    if (item) {
                        return item.sort === "asc" ? "fa fa-sort-amount-asc" : "fa fa-sort-amount-desc";
                    } else {
                        return "fa fa-sort";
                    }
                };

                $scope.temHora = function (data) {
                    return DateUtils.temHora(data);
                }
            });
})();
