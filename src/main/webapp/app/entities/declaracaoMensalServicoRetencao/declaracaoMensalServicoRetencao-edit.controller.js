(function () {
    'use strict';

    angular.module('nfseApp').controller('DeclaracaoMensalServicoRetencaoEditController',
        function ($scope, $state, $timeout, $stateParams, entity, DeclaracaoMensalServico, NotaFiscal,
                  Notificacao, localStorageService, SweetAlert, PrestadorServicos, Account, Util) {

            $scope.declaracaoMensalServico = entity;
            $scope.meses = Util.getMeses();
            $scope.todosSelecionado = true;
            $scope.notas = [];
            $scope.page = 1;
            $scope.pageSize = 50;
            $scope.acessarCompetencia = acessarCompetencia;
            $scope.save = save;
            $scope.getTextoBotaoSalvar = getTextoBotaoSalvar;
            $scope.buscarNotasDoPeriodo = buscarNotasDoPeriodo;

            Account.get().$promise
                .then(function (account) {
                    $scope.declaracaoMensalServico.usuarioDeclaracao = account.data.nome;
                });


            var onSaveFinished = function (result) {
                Notificacao.priority("Registro salvo com sucesso", "");
                $scope.$emit('nfseApp:declaracaoMensalServicoUpdate', result);
                $scope.$emit('nfseApp:navbarUpdate');
                $state.go('declaracaoMensalServicoRetencao');
            };

            function save() {
                if ($scope.declaracaoMensalServico.qtdNotas === 0) {
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
                                DeclaracaoMensalServico.save($scope.declaracaoMensalServico, onSaveFinished);
                            }
                        });
                } else {
                    $scope.declaracaoMensalServico.todasNotasDoMes = true;
                    DeclaracaoMensalServico.save($scope.declaracaoMensalServico, onSaveFinished);
                }
            }

            function getTextoBotaoSalvar() {
                if ($scope.declaracaoMensalServico.qtdNotas === 0) {
                    return "Declarar Ausência de Movimento";
                }
                return "Encerrar Declaração";
            }


            function buscarNotasDoPeriodo() {
                NotaFiscal.buscarNotasSemDeclararPorCompetencia({
                        mes: $scope.declaracaoMensalServico.mes,
                        ano: $scope.declaracaoMensalServico.exercicio,
                        tipoMovimento: $scope.declaracaoMensalServico.tipoMovimento,
                        somenteComIssDevido: false,
                        page: $scope.page -1, size: $scope.pageSize
                    },
                    function (result, headers) {
                        $scope.notas = result;
                    }
                );
            }


            function acessarCompetencia() {
                buscarNotasDoPeriodo();
                $scope.agrupador = {};
                NotaFiscal.buscarResumoSemDeclararPorCompetencia({
                        mes: $scope.declaracaoMensalServico.mes,
                        ano: $scope.declaracaoMensalServico.exercicio,
                        tipoMovimento: $scope.declaracaoMensalServico.tipoMovimento,
                        somenteComIssDevido: false
                    },
                    function (result) {
                        $scope.declaracaoMensalServico.qtdNotas = result.qtdNotas;
                        $scope.declaracaoMensalServico.totalServicos = result.totalServicos;
                        $scope.declaracaoMensalServico.totalIss = result.totalIssRetido;
                        for (var i = 0; i < result.naturezas.length; i++) {
                            var nat = result.naturezas[i];
                            if (!$scope.agrupador[nat.natureza]) {
                                $scope.agrupador[nat.natureza] = {
                                    natureza: nat.natureza,
                                    quantidade: 0,
                                    servicos: 0,
                                    iss: 0
                                };
                            }
                            $scope.agrupador[nat.natureza].quantidade = nat.qtdNotas + 1;
                            $scope.agrupador[nat.natureza].servicos = nat.totalServicos;
                            $scope.agrupador[nat.natureza].iss = nat.totalIss;
                        }
                    }
                );
            }

            acessarCompetencia();
        });

})();
