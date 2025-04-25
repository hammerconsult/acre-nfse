(function () {
    'use strict';

angular.module('nfseApp')
    .controller('ServicoDeclaradoController',
        function ($state, $scope, $window, $http, $filter, ServicoDeclarado, NotaFiscalSearch, ImpressaoPdf,
                  $modal, ParseLinks, ExportarXls, localStorageService, DataUtil, Util, Notificacao, SweetAlert,
                  EnumCacheService) {
            $scope.per_page = 10;
            $scope.searchQuery = "";
            $scope.servicoDeclaradoSelecionado;
            $scope.prestadorPrincipal = localStorageService.get("prestadorPrincipal");
            $scope.emissaoInicial;
            $scope.emissaoFinal;
            $scope.numeroInicial;
            $scope.numeroFinal;
            $scope.cpfCnpjPrestador;
            $scope.nomeRazaoSocialPrestador;
            $scope.mostrarFiltros = false;
            $scope.meses = Util.getMeses();
            $scope.situacoes = EnumCacheService.carregarValuesEnum("br.com.webpublico.domain.dto.enums.SituacaoDeclaracaoNfseDTO");


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
                $scope.numeroInicial = null;
                $scope.numeroFinal = null;
                $scope.cpfCnpjPrestador = null;
                $scope.nomeRazaoSocialPrestador = null;
                $scope.competenciaAnoInicial = null;
                $scope.competenciaMesInicial = null;
                $scope.competenciaAnoFinal = null;
                $scope.competenciaMesFinal = null;
            };

            $scope.iniciarFiltros();

            $scope.montarConsultaServicoDeclarado = function (orderBy) {
                var campos = [];
                var parametroQueryCampo = {
                    campo: "sd.cadastroeconomico_id",
                    operador: "IGUAL",
                    valorLong: $scope.prestadorPrincipal.prestador.id
                };
                campos.push(parametroQueryCampo);

                if ($scope.emissaoInicial) {
                    parametroQueryCampo = {
                        campo: "trunc(sd.emissao)",
                        operador: "MAIOR_IGUAL",
                        valorDateString: $scope.emissaoInicial
                    };
                    campos.push(parametroQueryCampo);
                }
                if ($scope.emissaoFinal) {
                    parametroQueryCampo = {
                        campo: "trunc(sd.emissao)",
                        operador: "MENOR_IGUAL",
                        valorDateString: $scope.emissaoFinal
                    };
                    campos.push(parametroQueryCampo);
                }
                if ($scope.numeroInicial) {
                    parametroQueryCampo = {
                        campo: "sd.numero",
                        operador: "MAIOR_IGUAL",
                        valorLong: $scope.numeroInicial
                    };
                    campos.push(parametroQueryCampo);
                }
                if ($scope.numeroFinal) {
                    parametroQueryCampo = {
                        campo: "sd.numero",
                        operador: "MENOR_IGUAL",
                        valorLong: $scope.numeroFinal
                    };
                    campos.push(parametroQueryCampo);
                }
                if ($scope.cpfCnpjPrestador) {
                    parametroQueryCampo = {
                        campo: "dpp.cpfcnpj",
                        operador: "LIKE",
                        valorString: "%" + Util.apenasNumeros($scope.cpfCnpjPrestador) + "%"
                    };
                    campos.push(parametroQueryCampo);
                }
                if ($scope.nomeRazaoSocialPrestador) {
                    parametroQueryCampo = {
                        campo: "lower(dpp.nomerazaosocial)",
                        operador: "LIKE",
                        valorString: "%" + $scope.nomeRazaoSocialPrestador.toLowerCase() + "%"
                    };
                    campos.push(parametroQueryCampo);
                }
                if ($scope.competenciaAnoInicial && $scope.competenciaMesInicial) {
                    var competenciaInicial = $filter('date')(new Date($scope.competenciaAnoInicial,
                        $scope.competenciaMesInicial.numeroMes - 1, 1), 'dd/MM/yyyy');
                    parametroQueryCampo = {
                        campo: "trunc(dec.competencia)",
                        operador: "MAIOR_IGUAL",
                        valorDateString: competenciaInicial
                    };
                    campos.push(parametroQueryCampo);
                }
                if ($scope.competenciaAnoFinal && $scope.competenciaMesFinal) {
                    var competenciaFinal = $filter('date')(new Date($scope.competenciaAnoFinal,
                        $scope.competenciaMesFinal.numeroMes, 0), "dd/MM/yyyy");
                    parametroQueryCampo = {
                        campo: "trunc(dec.competencia)",
                        operador: "MENOR_IGUAL",
                        valorDateString: competenciaFinal
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

                return {
                    offset: $scope.page,
                    limit: $scope.per_page,
                    parametrosQuery: [{
                        juncao: " and ",
                        parametroQueryCampos: campos
                    }],
                    orderBy: orderBy
                };
            };

            $scope.criteriosUtilizados = function () {
                var filtros = "";
                var juncao = "";

                if ($scope.emissaoInicial) {
                    filtros += juncao + " Período Inicial: " + $scope.emissaoInicial;
                    juncao = ", ";
                }
                if ($scope.emissaoFinal) {
                    filtros += juncao + " Período Final: " + $scope.emissaoFinal;
                    juncao = ", ";
                }
                if ($scope.numeroInicial) {
                    filtros += juncao + " N° Inicial : " + $scope.numeroInicial;
                    juncao = ", ";
                }
                if ($scope.numeroFinal) {
                    filtros += juncao + " N° Final: " + $scope.numeroFinal;
                    juncao = ", ";
                }
                if ($scope.cpfCnpjPrestador) {
                    filtros += juncao + " CPF/CNPJ Prestador: " + $scope.cpfCnpjPrestador;
                    juncao = ", ";
                }
                if ($scope.nomeRazaoSocialPrestador) {
                    filtros += juncao + " Nome/Razão Social Prestador: " + $scope.nomeRazaoSocialPrestador;
                    juncao = ", ";
                }
                return filtros += ".";
            };

            $scope.baixarExcel = function () {
                var emissao = {
                    consultaServicoDeclarado: $scope.montarConsultaServicoDeclarado(" order by dpp.nomerazaosocial asc "),
                    criteriosUtilizados: $scope.criteriosUtilizados(),
                    tipoRelatorio: "RESUMIDO"
                };
                $http.post('/api/servico-declarado/relatorio-excel', emissao, {responseType: 'arraybuffer'}).then(function (response) {
                    if (response.data.byteLength == 0) {
                        Notificacao.warn('Atenção', 'Nenhum registro para ser impresso.');
                    } else {
                        var blob = new Blob([response.data], {
                            type: "application/excel"
                        });
                        saveAs(blob, "ServicosDeclarados.xlsx");
                    }
                });
            };

            $scope.baixarPDF = function () {
                var emissao = {
                    consultaServicoDeclarado: $scope.montarConsultaServicoDeclarado(" order by dpp.nomerazaosocial asc "),
                    criteriosUtilizados: $scope.criteriosUtilizados(),
                    tipoRelatorio: "RESUMIDO"
                };
                ImpressaoPdf.imprimirPdfViaPost('/api/servico-declarado/relatorio-servicos-declarados', emissao);
            };

            $scope.loadAll = function () {
                var consultaServicoDeclarado = $scope.montarConsultaServicoDeclarado(" order by sd.emissao desc ");
                ServicoDeclarado.buscarServicosDeclarados(consultaServicoDeclarado,
                    function (result, headers) {
                        if (headers)
                            $scope.links = ParseLinks.parse(headers('link'));
                        $scope.servicosDeclarados = result;
                    });
            };

            $scope.loadPage = function (page) {
                $scope.page = page;
                $scope.loadAll();
            };

            $scope.loadAll();

            $scope.imprimir = function (id) {
                ImpressaoPdf.imprimirPdfViaUrl('/api/servico-declarado/imprime/' + id);
            };

            $scope.excluir = function (id) {
                SweetAlert.swal({
                        title: "Confirme a exclusão",
                        text: "Você tem certeza que quer excluir o registro selecionado?",
                        type: "warning",
                        showCancelButton: true,
                        confirmButtonColor: "#DD6B55", confirmButtonText: "Sim, Excluir",
                        cancelButtonText: "Não, Cancelar",
                        closeOnConfirm: true,
                        closeOnCancel: true
                    },
                    function (isConfirm) {
                        if (isConfirm) {
                            ServicoDeclarado.remove({id:id}, function () {
                                $scope.loadAll();
                                Notificacao.info('Informação!', 'Registro excluído com sucesso!');
                            });
                        }
                    });
            };

            $scope.cancelar = function (id) {
                SweetAlert.swal({
                        title: "Confirme o cancelamento",
                        text: "Você tem certeza que quer cancelar o registro selecionado?",
                        type: "warning",
                        showCancelButton: true,
                        confirmButtonColor: "#DD6B55", confirmButtonText: "Sim, Cancelar",
                        cancelButtonText: "Não",
                        closeOnConfirm: true,
                        closeOnCancel: true
                    },
                    function (isConfirm) {
                        $scope.cancelamento = {idNota: id};
                        Account.get().$promise
                            .then(function (account) {
                                $scope.cancelamento.tomador = account.data;
                            });
                        if (isConfirm) {
                            ServicoDeclarado.remove({id:id}, function () {
                                $scope.loadAll();
                            });
                        }
                    });
            };

            $scope.refresh = function () {
                $scope.loadAll();
                $scope.clear();
            };

            $scope.cancelarServicoDeclarado = function (servicoDeclarado) {
                var modalInstance = $modal.open({
                    templateUrl: 'app/entities/servico-declarado/cancelamento-servico-declarado.html',
                    controller: 'CancelamentoServicoDeclaradoController',
                    size: 'lg',
                    resolve: {
                        entity: function (ServicoDeclarado) {
                            return ServicoDeclarado.get({id: servicoDeclarado.id}).$promise;
                        }
                    }
                });
                modalInstance.result.then(function () {
                    $scope.loadAll();
                }, function () {
                });
            };

            $scope.selecionarServicoDeclarado = function (servicoDeclarado) {
                $scope.servicoDeclaradoSelecionado = servicoDeclarado;
            };

            $scope.classLinhaTabela = function (servicoDeclaradoSelecionado) {
                if ($scope.servicoDeclaradoSelecionado && $scope.servicoDeclaradoSelecionado.id == servicoDeclaradoSelecionado.id) {
                    return "success";
                }
                return "";
            };

            $scope.imprimirDam = function (servicoDeclarado) {
                if (servicoDeclarado.idDms) {
                    ImpressaoPdf.imprimirPdfViaUrl('/api/imprimir-dam-declaracao/' + servicoDeclarado.idDms);
                } else {
                    var emissao = new Date(servicoDeclarado.emissao);
                    $state.go("guiaUnica",
                        {
                            mes: emissao.getMonth() + 1,
                            ano: emissao.getFullYear(),
                            idDeclaracao: servicoDeclarado.idDeclaracao,
                            tipoMovimento: servicoDeclarado.tipo == 'SERVICO_PRESTADO' ? 'NORMAL' : 'RETENCAO'
                        });
                }
            };

            $scope.cancelar = function () {
                $scope.habilitarDesabilitarFiltros();
                $scope.loadAll();
            }
        });
})();
