(function () {
    'use strict';

    angular.module('nfseApp').controller('DeclaracaoMensalServicoInstituicaoFinanceiraEditController',
        function ($scope, $state, $timeout, $stateParams, entity, DeclaracaoMensalServico, NotaFiscal, Notificacao,
                  localStorageService, SweetAlert, PrestadorServicos, PlanoGeralContasComentado, $modal, Util, Account,
                  CodigoTributacaoMunicipio) {

            $scope.declaracaoMensalServico = entity;
            $scope.meses = Util.getMeses();
            $scope.valorTotalDeISS = 0;
            $scope.conta;
            $scope.declaracaoContaBancaria;

            $scope.init = function () {
                Account.get().$promise
                    .then(function (account) {
                        $scope.declaracaoMensalServico.usuarioDeclaracao = account.data.nome;
                    });
                PrestadorServicos.get({id: localStorageService.get("prestadorPrincipal").prestador.id}, function (data) {
                    $scope.prestador = data;
                })
            }

            $scope.init();

            $scope.onSaveFinished = function (result) {
                Notificacao.priority("Registro salvo com sucesso", "");
                $scope.$emit('nfseApp:declaracaoMensalServicoUpdate', result);
                $scope.$emit('nfseApp:navbarUpdate');
                $state.go('declaracaoMensalServicoInstituicaoFinanceira');
            };

            $scope.save = function () {
                if (!$scope.declaracaoMensalServico.contasBancarias || $scope.declaracaoMensalServico.contasBancarias.length == 0) {
                    SweetAlert.swal({
                            title: "Declaração de Ausência de Movimento",
                            text: "Você tem certeza que deseja declarar ausência de movimento para o periodo " +
                                $scope.declaracaoMensalServico.mes + "/" + $scope.declaracaoMensalServico.exercicio + "?",
                            type: "warning",
                            showCancelButton: true,
                            confirmButtonColor: "#DD6B55", confirmButtonText: "Sim, Declarar",
                            cancelButtonText: "Não, Cancelar",
                            closeOnConfirm: true,
                            closeOnCancel: true
                        },
                        function (isConfirm) {
                            if (isConfirm) {
                                DeclaracaoMensalServico.save($scope.declaracaoMensalServico, $scope.onSaveFinished);
                            }
                        });
                } else {
                    DeclaracaoMensalServico.save($scope.declaracaoMensalServico, $scope.onSaveFinished);
                }
            }

            $scope.limparContas = function () {
                $scope.declaracaoMensalServico.contasBancarias = [];
            }

            $scope.atualizarValores = function () {
                $scope.declaracaoMensalServico.qtdNotas = 0;
                $scope.declaracaoMensalServico.totalServicos = 0;
                $scope.declaracaoMensalServico.totalIss = 0;
                for (var i = 0; i < $scope.declaracaoMensalServico.contasBancarias.length; i++) {
                    if ($scope.declaracaoMensalServico.contasBancarias[i].baseCalculo > 0) {
                        $scope.declaracaoMensalServico.qtdNotas = $scope.declaracaoMensalServico.qtdNotas + 1;
                        $scope.declaracaoMensalServico.totalServicos = $scope.declaracaoMensalServico.totalServicos +
                            $scope.declaracaoMensalServico.contasBancarias[i].baseCalculo;
                        $scope.declaracaoMensalServico.totalIss = $scope.declaracaoMensalServico.totalIss +
                            ($scope.declaracaoMensalServico.contasBancarias[i].baseCalculo *
                                ($scope.declaracaoMensalServico.contasBancarias[i].aliquotaIssqn / 100));
                    }
                }
            }

            $scope.atualizarValores();

            $scope.iniciarDeclaracaoContaBancaria = function () {
                $scope.declaracaoContaBancaria = {};
                $scope.declaracaoContaBancaria.saldoInicial = 0;
                $scope.declaracaoContaBancaria.valorDebito = 0;
                $scope.declaracaoContaBancaria.valorCredito = 0;
                $scope.declaracaoContaBancaria.baseCalculo = 0;
                $scope.declaracaoContaBancaria.aliquotaIssqn = 0;
            }

            $scope.buscarPgcc = function () {
                $scope.iniciarDeclaracaoContaBancaria();
                if ($scope.conta) {
                    PlanoGeralContasComentado.buscarPorConta({conta: $scope.conta}, function (data) {
                        if (!data.id) {
                            Notificacao.warn('Atenção!', 'Nenhuma conta encontrada para o valor digitado: ' + $scope.conta);
                        } else {
                            $scope.declaracaoContaBancaria.conta = data;
                            $scope.foco("declaracaoContaBancaria.baseCalculo");
                            $scope.buscarAliquotaDeclaracaoContaBancaria();
                        }
                    })
                }
            }

            $scope.foco = function (campo) {
                $timeout(function () {
                    angular.element('[ng-model="' + campo + '"]').focus();
                }, 1500);
            }

            $scope.pesquisarPgcc = function () {
                var modal = $modal.open({
                    templateUrl: 'app/entities/plano-geral-contas-comentado/plano-geral-contas-comentado-search.html',
                    controller: 'PlanoGeralContasComentadoSearchController',
                    size: 'lg'
                });
                modal.result.then(function (data) {
                    if (data) {
                        $scope.iniciarDeclaracaoContaBancaria();
                        $scope.conta = data.conta;
                        $scope.declaracaoContaBancaria.conta = data;
                        $scope.buscarAliquotaDeclaracaoContaBancaria();
                        $scope.foco("declaracaoContaBancaria.baseCalculo");
                    }
                });
            }

            $scope.validarDeclaracaoContaBancaria = function () {
                var validado = true;
                if (!$scope.declaracaoContaBancaria.conta) {
                    validado = false;
                    Notificacao.error('Erro', 'O campo Conta é obrigatório!');
                } else {
                    if (!$scope.declaracaoContaBancaria.baseCalculo || $scope.declaracaoContaBancaria.baseCalculo < 0) {
                        validado = false;
                        Notificacao.error('Erro', 'O campo Base de Cálculo é obrigatório e deve ser maior ou igual a zero(0)!');
                    }
                }
                return validado;
            }

            $scope.adicionarDeclaracaoContaBancaria = function () {
                if ($scope.validarDeclaracaoContaBancaria()) {
                    $scope.declaracaoMensalServico.contasBancarias.push($scope.declaracaoContaBancaria);
                    $scope.conta = "";
                    $scope.iniciarDeclaracaoContaBancaria();
                    $scope.atualizarValores();
                }
            }

            $scope.removerDeclaracaoContaBancaria = function (declaracaoContaBancaria) {
                var index = $scope.declaracaoMensalServico.contasBancarias.indexOf(declaracaoContaBancaria);
                $scope.declaracaoMensalServico.contasBancarias.splice(index, 1);
                $scope.atualizarValores();
            }

            $scope.calcularIssPagar = function (declaracaoContaBancaria) {
                if (!declaracaoContaBancaria) {
                    return 0;
                }
                return declaracaoContaBancaria.baseCalculo * (declaracaoContaBancaria.aliquotaIssqn / 100);
            }

            $scope.calcularTotalIssPagar = function () {
                var totalIssPagar = 0;
                for (var i = 0; i < $scope.declaracaoMensalServico.contasBancarias.length; i++) {
                    totalIssPagar += $scope.calcularIssPagar($scope.declaracaoMensalServico.contasBancarias[i]);
                }
                return totalIssPagar;
            }

            $scope.calcularTotalBaseCalculo = function () {
                var totalBaseCalculo = 0;
                for (var i = 0; i < $scope.declaracaoMensalServico.contasBancarias.length; i++) {
                    totalBaseCalculo += $scope.declaracaoMensalServico.contasBancarias[i].baseCalculo;
                }
                return totalBaseCalculo;
            }

            $scope.getTextoBotaoSalvar = function () {
                if ($scope.declaracaoMensalServico.contasBancarias.length == 0) {
                    return "Declarar Ausência de Movimento";
                }
                return "Encerrar Declaração";
            }

            $scope.buscarAliquotaDeclaracaoContaBancaria = function () {
                if ($scope.declaracaoContaBancaria && $scope.declaracaoContaBancaria.conta) {
                    if ($scope.prestador.enquadramentoFiscal.versaoDesif == 'VERSAO_ABRASF_3_2') {
                        $scope.buscarAliquotaCodigoTributacao();
                    } else {
                        $scope.buscarAliquotaServico();
                    }
                }
                return 0;
            }

            $scope.buscarAliquotaCodigoTributacao = function () {
                if (!$scope.declaracaoContaBancaria.conta.codigoTributacao ||
                    !$scope.declaracaoContaBancaria.conta.codigoTributacao.id) {
                    Notificacao.error('Erro', 'Conta não possui um código de tributação ' + $scope.declaracaoContaBancaria.conta.conta);
                    $scope.iniciarDeclaracaoContaBancaria();
                } else {
                    CodigoTributacaoMunicipio.buscarPorIdCodigoTributacao({
                        idCodigoTributacao: $scope.declaracaoContaBancaria.conta.codigoTributacao.id
                    }, function (data) {
                        if (!data.id) {
                            Notificacao.error('Erro', 'Não existe alíquota configurada para o código de tributação ' +
                                $scope.declaracaoContaBancaria.conta.codigoTributacao.codigo + " - " +
                                $scope.declaracaoContaBancaria.conta.codigoTributacao.descricao);
                            $scope.iniciarDeclaracaoContaBancaria();
                        } else {
                            $scope.declaracaoContaBancaria.aliquotaIssqn = data.aliquota;
                        }
                    })
                }
            }

            $scope.buscarAliquotaServico = function () {
                if (!$scope.declaracaoContaBancaria.conta.servico ||
                    !$scope.declaracaoContaBancaria.conta.servico.id) {
                    Notificacao.error('Erro', 'A conta não possui um serviço vinculado. ');
                    $scope.iniciarDeclaracaoContaBancaria();
                } else {
                    if (!$scope.declaracaoContaBancaria.conta.servico.aliquota || $scope.declaracaoContaBancaria.conta.servico.aliquota <= 0) {
                        Notificacao.error('Erro', 'Não existe alíquota configurada para o serviço ' +
                            $scope.declaracaoContaBancaria.conta.servico.codigo + " - " +
                            $scope.declaracaoContaBancaria.conta.servico.descricao);
                        $scope.iniciarDeclaracaoContaBancaria();
                    } else {
                        $scope.declaracaoContaBancaria.aliquotaIssqn = $scope.declaracaoContaBancaria.conta.servico.aliquota;
                    }
                }
            }
        });


})();
