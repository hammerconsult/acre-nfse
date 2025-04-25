(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('LivroFiscalController',
            function ($scope, $state, LivroFiscal, ParseLinks, Notificacao, ImpressaoPdf,
                      DeclaracaoMensalServico, localStorageService, PrestadorServicos, $modal) {

                $scope.prestador;
                $scope.exercicioInicial;
                $scope.exercicioFinal;
                $scope.mesInicial;
                $scope.mesFinal;
                $scope.competenciasServicosPrestados;
                $scope.competenciasServicosTomados;

                $scope.loadAll = loadAll;

                function loadAll() {
                    LivroFiscal.competenciasLivroFiscal({
                        exercicioInicial: $scope.exercicioInicial,
                        exercicioFinal: $scope.exercicioFinal,
                        mesInicial: $scope.mesInicial.numeroMes,
                        mesFinal: $scope.mesFinal.numeroMes,
                        consideraEncerramento: true,
                        tipoMovimento: !$scope.prestador.enquadramentoFiscal.instituicaoFinanceira ? 'NORMAL' : 'INSTITUICAO_FINANCEIRA'
                    }, function (data) {
                        $scope.competenciasServicosPrestados = data;
                    });

                    LivroFiscal.competenciasLivroFiscal({
                        exercicioInicial: $scope.exercicioInicial,
                        exercicioFinal: $scope.exercicioFinal,
                        mesInicial: $scope.mesInicial.numeroMes,
                        mesFinal: $scope.mesFinal.numeroMes,
                        tipoMovimento: 'RETENCAO'
                    }, function (data) {
                        $scope.competenciasServicosTomados = data;
                    });

                    $scope.activeTab = !$scope.prestador.enquadramentoFiscal.instituicaoFinanceira ? 0 : 1;
                }

                DeclaracaoMensalServico.getMeses({}, function (data) {
                    $scope.meses = data;

                    var dataAtual = new Date();
                    $scope.exercicioInicial = dataAtual.getFullYear();
                    $scope.exercicioFinal = dataAtual.getFullYear();
                    angular.forEach(data, function (mes) {
                        if (mes.numeroMes == 1) {
                            $scope.mesInicial = mes;
                        }
                        if (mes.numeroMes == dataAtual.getMonth() + 1) {
                            $scope.mesFinal = mes;
                        }
                    });
                    PrestadorServicos.get({id: localStorageService.get("prestadorPrincipal").prestador.id}, function (data) {
                        $scope.prestador = data;
                        $scope.loadAll();
                    });
                });

                $scope.imprimirLivro = function (competencia) {
                    var modalInstance = $modal.open({
                        templateUrl: 'app/entities/relatorios/relatorioResumidoDetalhado.html',
                        controller: 'RelatorioResumidoDetalhadoController',
                        size: 'md'
                    });

                    modalInstance.result.then(function (detalhado) {
                        competencia.detalhado = detalhado;
                        LivroFiscal.imprimirLivroFiscal(competencia, function (webReport) {
                            $modal.open({
                                templateUrl: 'app/entities/wait-report/wait-report.html',
                                controller: 'WaitReportController',
                                size: 'md',
                                animation: true,
                                keyboard: false,
                                backdrop: 'static',
                                resolve: {
                                    uuid: function () {
                                        return webReport.uuid;
                                    }
                                }
                            });
                        });
                    }, function () {
                    });
                };

                $scope.mudarTab = function (value) {
                    $scope.activeTab = value;
                }
            });
})();