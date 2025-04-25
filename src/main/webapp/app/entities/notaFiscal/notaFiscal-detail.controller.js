(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('NotaFiscalDetailController',
            function ($scope, $rootScope, $stateParams, entity, NotaFiscal, $state, PrestadorServicos,
                      localStorageService, DateUtils, Debitos, NotaNacional, $modal, Notificacao) {
                $scope.notaFiscal = entity;
                $scope.prestador = localStorageService.get("prestadorPrincipal").prestador;
                $scope.retornosDfeAdn = [];
                $scope.podeIntegrarNotaNacional = false;

                function existeIntegracaoNotaNacionalComSucesso() {
                    if ($scope.retornosDfeAdn && $scope.retornosDfeAdn.length > 0) {
                        for (var i = 0; i < $scope.retornosDfeAdn.length; i++) {
                            if ($scope.retornosDfeAdn[i].statusProcessamento == 'PROCESSADO_COM_SUCESSO') {
                                return true;
                            }
                        }
                    }
                    return false;
                }

                $scope.buscarRetornosDfeAdn = function () {
                    NotaNacional.buscarRetornosDfeAdn({notaFiscalId: $scope.notaFiscal.id}, function (data) {
                        $scope.retornosDfeAdn = data;
                        $scope.podeIntegrarNotaNacional = !existeIntegracaoNotaNacionalComSucesso();
                    });
                }

                $scope.buscarRetornosDfeAdn();

                function getTotalRetencoesFederais() {
                    var total = 0;
                    if ($scope.notaFiscal
                        && $scope.notaFiscal.declaracaoPrestacaoServico
                        && $scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais) {
                        if ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.pis) {
                            total = total + ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.pis);
                        }
                        if ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.cofins) {
                            total = total + ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.cofins);
                        }
                        if ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.inss) {
                            total = total + ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.inss);
                        }
                        if ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.irrf) {
                            total = total + ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.irrf);
                        }
                        if ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.csll) {
                            total = total + ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.csll);
                        }
                    }
                    return total;
                }

                $scope.atualizarRentencaoFederal = function () {
                    $scope.notaFiscal.retencoesFederais = getTotalRetencoesFederais();
                };

                $scope.atualizarRentencaoFederal();

                NotaFiscal.getParcelasDaNota({id: $stateParams.id}, function (res) {
                    $scope.parcelas = res;
                    for (var i = 0; i < $scope.parcelas.length; i++) {
                        var parcela = $scope.parcelas[i];
                        Debitos.buscarUltimoDamParcela({idParcela: parcela.id}, function (dam) {
                            parcela.dam = dam;
                        });
                    }
                });

                NotaFiscal.getCancelamentos({id: entity.id}, function (res) {
                    $scope.cancelamentos = res;
                    for (var i = 0; i < res.length; i++) {
                        var cancelamento = res[i];
                        if (cancelamento.situacaoFiscal == 'EM_ANALISE') {
                            $scope.cancelamentoEmAnalise = true;
                        }
                    }
                });

                $rootScope.$on('nfseApp:notaFiscalUpdate', function (event, result) {
                    $scope.notaFiscal = result;
                });


                $scope.baixarXMlNotaFiscal = function (idNotaFiscal) {
                    NotaFiscal.downloadXmlNotaFiscal(idNotaFiscal);
                };

                $scope.copiarNota = function (nota) {
                    NotaFiscal.copiarNota(nota);
                };

                $scope.imprimirNota = function (nota) {
                    NotaFiscal.imprimirNota(nota);
                };

                $scope.enviarNota = function (nota) {
                    NotaFiscal.enviarNota(nota);
                };

                $scope.cancelarNota = function (nota) {
                    NotaFiscal.cancelarNota(nota, function () {
                        $state.go('notaFiscal.detail', {id: nota.id}, {reload: true});
                    });
                };

                $scope.cartaCorrecaoNota = function (nota) {
                    NotaFiscal.cartaCorrecaoNota(nota, function () {
                        $state.go('notaFiscal.detail', {id: nota.id}, {reload: true});
                    });
                };

                $scope.removerDoMongo = function (nota) {
                    NotaFiscal.removerDoMongo({id: nota.id});
                    $state.go('notaFiscal.detail', {id: nota.id}, {reload: true});
                };

                $scope.tributadoEmOutroMunicipio = function () {
                    return $scope.notaFiscal &&
                        $scope.notaFiscal.declaracaoPrestacaoServico &&
                        $scope.notaFiscal.declaracaoPrestacaoServico.naturezaOperacao === "TRIBUTACAO_FORA_MUNICIPIO";

                };

                $scope.temHora = function (data) {
                    return DateUtils.temHora(data);
                }

                $scope.integrarNotaNacional = function () {
                    NotaNacional.integrar({notaFiscalId: $scope.notaFiscal.id}, function () {
                        Notificacao.info("Informação", "Nfs-e enviada para integração com a Nfs-e Nacional.");
                    });
                }

                $scope.exibirMensagensNotaNacional = function (retorno) {
                    NotaNacional.buscarMensagensDfeAd({retornoId: retorno.id}, function (data) {
                        console.log(data);
                        $modal.open({
                            templateUrl: 'app/entities/nota-nacional/mensagem-dfe-adn.html',
                            controller: 'MensagemDfeAdnController',
                            size: 'lg',
                            resolve: {
                                mensagens: function () {
                                    return data;
                                }
                            }
                        });
                    });
                }
            });
})();
