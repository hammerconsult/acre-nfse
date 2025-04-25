(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('PlanoGeralContasComentadoEditController', function ($state, $scope, entity,
                                                                         PlanoGeralContasComentado, PrestadorServicos,
                                                                         localStorageService, Cosif, Notificacao, $modal,
                                                                         CodigoTributacao, TarifaBancaria, ProdutoServico,
                                                                         Servico) {
            $scope.entity = entity;
            $scope.cosif;
            $scope.codigoServico;
            $scope.codigoTributacao;
            $scope.tarifaBancaria;
            $scope.produtoServico;
            $scope.tipoVinculo;
            $scope.prestadorPrincipal = localStorageService.get("prestadorPrincipal");

            if (!$scope.entity.id) {
                PrestadorServicos.get({id: localStorageService.get("prestadorPrincipal").prestador.id}, function (data) {
                    $scope.entity.prestador = data;
                    if ($scope.entity.prestador.enquadramentoFiscal.versaoDesif == 'VERSAO_ABRASF_3_2' && !$scope.tipoVinculo) {
                        $scope.tipoVinculo = 'TARIFA_BANCARIA';
                    }
                });
            } else {
                console.log($scope.entity);
                if ($scope.entity.cosif) {
                    $scope.cosif = $scope.entity.cosif.conta;
                }
                if ($scope.entity.servico) {
                    $scope.codigoServico = $scope.entity.servico.codigo;
                }
                if ($scope.entity.codigoTributacao) {
                    $scope.codigoTributacao = $scope.entity.codigoTributacao.codigo;
                }
                if ($scope.entity.tarifaBancaria) {
                    $scope.tipoVinculo = 'TARIFA_BANCARIA';
                    $scope.tarifaBancaria = $scope.entity.tarifaBancaria.tarifaBancaria.codigo;
                }
                if ($scope.entity.produtoServico) {
                    $scope.tipoVinculo = 'PRODUTO_SERVICO';
                    $scope.produtoServico = $scope.entity.produtoServico.produtoServico.codigo;
                }
                if ($scope.entity.prestador.enquadramentoFiscal.versaoDesif == 'VERSAO_ABRASF_3_2' && !$scope.tipoVinculo) {
                    $scope.tipoVinculo = 'TARIFA_BANCARIA';
                }
            }

            $scope.preSave = function () {
                if ($scope.entity.prestador.enquadramentoFiscal.versaoDesif == 'VERSAO_ABRASF_3_2') {
                    $scope.entity.servico = null;
                    if ($scope.tipoVinculo == 'TARIFA_BANCARIA') {
                        $scope.entity.produtoServico = null;
                    } else {
                        $scope.entity.tarifaBancaria = null;
                    }
                } else {
                    $scope.entity.codigoTributacao = null;
                    $scope.entity.tarifaBancaria = null;
                    $scope.entity.produtoServico = null;
                }
            }

            $scope.save = function (editForm) {
                if (editForm.$invalid) {
                    return;
                }
                $scope.preSave();
                PlanoGeralContasComentado.save($scope.entity, function () {
                    $state.go('planoGeralContasComentado');
                });
            }

            $scope.buscarCosif = function () {
                $scope.entity.cosif = null;
                if ($scope.cosif) {
                    Cosif.buscarPorConta({conta: $scope.cosif}, function (data) {
                        if (data && data.id) {
                            $scope.entity.cosif = data;
                        } else {
                            $scope.cosif = "";
                            Notificacao.warn('Atenção', 'Conta Cosif não encontrada.');
                        }
                    });
                }
            }

            $scope.pesquisarCosif = function () {
                var modal = $modal.open({
                    templateUrl: 'app/entities/cosif/cosif-search.html',
                    controller: 'CosifSearchController',
                    size: 'lg'
                });
                modal.result.then(function (data) {
                    $scope.cosif = data.conta;
                    if (data) {
                        $scope.entity.cosif = data;
                    }
                });
            }

            $scope.buscarCodigoTributacao = function () {
                console.log("Código tributação: " + $scope.codigoTributacao);
                $scope.entity.codigoTributacao = null;
                if ($scope.codigoTributacao) {
                    CodigoTributacao.buscarPorCodigo({codigo: $scope.codigoTributacao}, function (data) {
                        if (data && data.id) {
                            $scope.entity.codigoTributacao = data;
                        } else {
                            $scope.codigoTributacao = "";
                            Notificacao.warn('Atenção', 'Código de Tributação não encontrado.');
                        }
                    });
                }
            }

            $scope.pesquisarCodigoTributacao = function () {
                var modal = $modal.open({
                    templateUrl: 'app/entities/codigo-tributacao/codigo-tributacao-search.html',
                    controller: 'CodigoTributacaoSearchController',
                    size: 'lg'
                });
                modal.result.then(function (data) {
                    $scope.codigoTributacao = data.codigo;
                    if (data) {
                        $scope.entity.codigoTributacao = data;
                    }
                });
            }

            $scope.buscarTarifaBancaria = function () {
                $scope.entity.tarifaBancaria = null;
                if ($scope.tarifaBancaria) {
                    TarifaBancaria.buscarPorCodigo({codigo: $scope.tarifaBancaria}, function (data) {
                        if (data && data.id) {
                            $scope.entity.tarifaBancaria = {
                                tarifaBancaria: data,
                                valorUnitario: 0,
                                valorPercentual: 0
                            };
                        } else {
                            $scope.tarifaBancaria = "";
                            Notificacao.warn('Atenção', 'Tarifa Bancária não encontrada.');
                        }
                    });
                }
            }

            $scope.pesquisarTarifaBancaria = function () {
                var modal = $modal.open({
                    templateUrl: 'app/entities/tarifa-bancaria/tarifa-bancaria-search.html',
                    controller: 'TarifaBancariaSearchController',
                    size: 'lg'
                });
                modal.result.then(function (data) {
                    if (data) {
                        $scope.tarifaBancaria = data.codigo;
                        $scope.entity.tarifaBancaria = {
                            tarifaBancaria: data,
                            valorUnitario: 0,
                            valorPercentual: 0
                        };
                    }
                });
            }

            $scope.buscarProdutoServico = function () {
                $scope.entity.produtoServico = null;
                if ($scope.produtoServico) {
                    ProdutoServico.buscarPorCodigo({codigo: $scope.produtoServico}, function (data) {
                        if (data && data.id) {
                            $scope.entity.produtoServico = {
                                produtoServico: data
                            };
                        } else {
                            $scope.produtoServico = "";
                            Notificacao.warn('Atenção', 'Produto/Serviço não encontrado.');
                        }
                    });
                }
            }

            $scope.pesquisarProdutoServico = function () {
                var modal = $modal.open({
                    templateUrl: 'app/entities/produto-servico/produto-servico-search.html',
                    controller: 'ProdutoServicoSearchController',
                    size: 'lg'
                });
                modal.result.then(function (data) {
                    if (data) {
                        $scope.produtoServico = data.codigo;
                        $scope.entity.produtoServico = {
                            produtoServico: data
                        };
                    }
                });
            }

            $scope.changeTipoVinculo = function (tipoVinculo) {
                if (tipoVinculo == $scope.tipoVinculo) {
                    return;
                }
                $scope.tipoVinculo = tipoVinculo;
                if ($scope.tipoVinculo == 'TARIFA_BANCARIA') {
                    $scope.produtoServico = null;
                    $scope.entity.produtoServico = null;
                } else {
                    $scope.tarifaBancaria = null;
                    $scope.entity.tarifaBancaria = null;
                }
            }

            $scope.isVersaoDesif10 = function () {
                return $scope.prestadorPrincipal &&
                    $scope.prestadorPrincipal.prestador &&
                    $scope.prestadorPrincipal.prestador.enquadramentoFiscal &&
                    $scope.prestadorPrincipal.prestador.enquadramentoFiscal.versaoDesif == 'VERSAO_1_0';
            }

            $scope.isVersaoDesifAbrasf32 = function () {
                return $scope.prestadorPrincipal &&
                    $scope.prestadorPrincipal.prestador &&
                    $scope.prestadorPrincipal.prestador.enquadramentoFiscal &&
                    $scope.prestadorPrincipal.prestador.enquadramentoFiscal.versaoDesif == 'VERSAO_ABRASF_3_2';
            }

            $scope.buscarServico = function () {
                $scope.entity.servico = null;
                if ($scope.codigoServico) {
                    Servico.getPorCodigo({codigo: $scope.codigoServico}, function (data) {
                        if (data && data.id) {
                            $scope.entity.servico = data;
                        } else {
                            $scope.codigoServico = "";
                            Notificacao.warn('Atenção', 'Serviço não encontrado.');
                        }
                    });
                }
            }

            $scope.pesquisarServico = function () {
                var modal = $modal.open({
                    templateUrl: 'app/entities/servico/servico-search.html',
                    controller: 'ServicoSearchController',
                    size: 'lg'
                });
                modal.result.then(function (data) {
                    if (data) {
                        $scope.codigoServico = data.codigo;
                        $scope.entity.servico = data;
                    }
                });
            }
        });
})();