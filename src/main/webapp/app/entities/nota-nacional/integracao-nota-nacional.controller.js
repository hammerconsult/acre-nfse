(function () {
    'use strict';

    angular.module('nfseApp').controller('IntegracaoNotaNacionalController',
        function ($scope, NotaNacional, ParseLinks, localStorageService, Notificacao) {
            $scope.lista = [];
            $scope.page = 1;
            $scope.per_page = 10;
            $scope.campos = [];
            $scope.links;
            $scope.listaReintegracao = [];
            $scope.filtro = {};
            $scope.activeTab = 0;
            $scope.quantidadePorErro = [];
            $scope.graficoQuantidadePorErro = {};
            $scope.quantidadePorStatus = [];
            $scope.graficoQuantidadePorStatus = {};
            $scope.codigoErro = "";

            $scope.montarCampos = function () {
                $scope.campos = [];
                if ($scope.filtro.inscricaoCadastral) {
                    $scope.campos.push({
                        campo: "dados.inscricao_cadastral",
                        operador: "IGUAL",
                        valorString: $scope.filtro.inscricaoCadastral
                    });
                }
                if ($scope.filtro.numeroNotaFiscal) {
                    $scope.campos.push({
                        campo: "dados.numero_nota_fiscal",
                        operador: "IGUAL",
                        valorString: $scope.filtro.numeroNotaFiscal
                    });
                }
                if ($scope.filtro.mensagem) {
                    $scope.campos.push({
                        campo: "dados.mensagem",
                        operador: "LIKE",
                        valorString: '%' + $scope.filtro.mensagem + '%'
                    });
                }
                localStorageService.set("filtroIntegracaoNotaNacional", $scope.filtro);
            };

            $scope.loadAll = function () {
                var parametrosQuery = [];
                if ($scope.campos.length > 0) {
                    parametrosQuery.push({
                        juncao: " and ",
                        parametroQueryCampos: $scope.campos
                    });
                }
                NotaNacional.buscarIntegracoes({
                        offset: $scope.page,
                        limit: $scope.per_page,
                        parametrosQuery: parametrosQuery
                    },
                    function (result, headers) {
                        if (headers)
                            $scope.links = ParseLinks.parse(headers('link'));
                        $scope.lista = result;
                    });
            };

            $scope.loadPage = function (page) {
                $scope.page = page;
                $scope.loadAll();
            };

            $scope.buscarIntegracoes = function () {
                $scope.page = 1;
                $scope.montarCampos();
                $scope.loadAll();
            }

            $scope.limparFiltros = function () {
                $scope.page = 1;
                $scope.filtro = {};
                $scope.campos = [];
                $scope.loadAll();
            }

            $scope.adicionarTodasParaReintegracao = function () {
                for (var i = 0; i < $scope.lista.length; i++) {
                    if ($scope.lista[i].status == 'PROCESSADO_COM_ERROS') {
                        if (!$scope.paraReintegrar($scope.lista[i].idNotaFiscal)) {
                            $scope.listaReintegracao.push($scope.lista[i].idNotaFiscal);
                        }
                    }
                }
            }

            $scope.removerTodasParaReintegracao = function () {
                for (var i = 0; i < $scope.lista.length; i++) {
                    if ($scope.lista[i].status == 'PROCESSADO_COM_ERROS') {
                        if ($scope.paraReintegrar($scope.lista[i].idNotaFiscal)) {
                            $scope.listaReintegracao.splice($scope.listaReintegracao.indexOf($scope.lista[i].idNotaFiscal), 1);
                        }
                    }
                }
            }

            $scope.adicionarOuRemoverReintegracao = function (idNotaFiscal) {
                if ($scope.paraReintegrar(idNotaFiscal)) {
                    $scope.listaReintegracao.splice($scope.listaReintegracao.indexOf(idNotaFiscal), 1);
                } else {
                    $scope.listaReintegracao.push(idNotaFiscal);
                }
            }

            $scope.paraReintegrar = function (idNotaFiscal) {
                return $scope.listaReintegracao.indexOf(idNotaFiscal) != -1;
            }

            $scope.enviarReintegracao = function () {
                if ($scope.listaReintegracao.length == 0) {
                    Notificacao.warn("Atenção!", "Nenhuma Nfs-e foi marcada para reintegração.");
                    return;
                }
                for (var i = 0; i < $scope.listaReintegracao.length; i++) {
                    NotaNacional.integrar({notaFiscalId: $scope.listaReintegracao[i]});
                }
                $scope.listaReintegracao = [];
                Notificacao.info("Informação!", "Reintegração solicitada com sucesso.");
            }

            $scope.reintegrarNotasPorCodigoErro = function () {
                if (!$scope.codigoErro) {
                    Notificacao.warn("Atenção!", "Informe o código do erro para reintegração.");
                    return;
                }
                NotaNacional.reintegrarNotasPorCodigoErro({codigoErro: $scope.codigoErro});
                $scope.codigoErro = "";
                Notificacao.info("Informação!", "Reintegração solicitada com sucesso.");
            }

            $scope.montarGraficoQuantitativo = function (data) {
                var grafico = {
                    labels: [],
                    data: []
                };
                if (data) {
                    for (var i = 0; i < data.length; i++) {
                        grafico.labels.push(data[i].descricao.length > 60 ? data[i].descricao.substring(0, 60) : data[i].descricao);
                        grafico.data.push(data[i].quantidade);
                    }
                }
                return grafico;
            }

            $scope.buscarInformacoesGraficos = function () {
                NotaNacional.buscarQuantidadePorErro({}, function (data) {
                    $scope.quantidadePorErro = data;
                    $scope.graficoQuantidadePorErro = $scope.montarGraficoQuantitativo(data);
                });
                NotaNacional.buscarQuantidadePorStatus({}, function (data) {
                    $scope.quantidadePorStatus = data;
                    $scope.graficoQuantidadePorStatus = $scope.montarGraficoQuantitativo(data);
                });
            }

            $scope.verIntegracoes = function () {
                $scope.activeTab = 0;
            };

            $scope.verGraficos = function () {
                $scope.activeTab = 1;
                $scope.buscarInformacoesGraficos();
            }

            $scope.init = function () {
                $scope.verIntegracoes();
            }

            $scope.init();

        });
})
();
