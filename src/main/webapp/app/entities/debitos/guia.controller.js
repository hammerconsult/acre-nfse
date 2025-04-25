(function () {
    'use strict';

    angular.module('nfseApp').controller('GuiaController',
        function ($scope, $state, $timeout, $stateParams, entity, DeclaracaoMensalServico, NotaFiscal,
                  Notificacao, localStorageService, SweetAlert, PrestadorServicos, Account, ImpressaoPdf, idDeclaracao) {

            $scope.declaracaoMensalServico = entity;
            $scope.idDeclaracao = idDeclaracao;
            $scope.meses = [];
            $scope.quantidadeNotasFiscaisNaDeclaracao = 0;
            $scope.valorTotalServicos = 0;
            $scope.valorTotalIss = 0;
            $scope.valorTotalIssRetido = 0;
            $scope.valorTotalIssDevido = 0;
            $scope.tiposMovimento = ['NORMAL', 'RETENCAO', 'ISS_RETIDO'];
            $scope.page = 1;
            $scope.pageSize = 50;
            $scope.notas = [];
            $scope.meses = DeclaracaoMensalServico.getMeses();
            $scope.usuarioDeclaracao;

            $scope.buscarNotas = buscarNotas;
            $scope.limparNotas = limparNotas;
            $scope.acessarCompetencia = acessarCompetencia;
            $scope.atualizarContadores = atualizarContadores;
            $scope.save = save;
            $scope.mudouFiltros = mudouFiltros;
            $scope.getIndexNota = getIndexNota;
            $scope.inverterSelecaoNota = inverterSelecaoNota;
            $scope.todasNotasSelecionadas = todasNotasSelecionadas;
            $scope.inverterSelecaoTodasNotas = inverterSelecaoTodasNotas;

            function mudouFiltros() {
                limparNotas();
                atualizarContadores();
            }

            function getIndexNota(id) {
                for (var i = 0; i < $scope.declaracaoMensalServico.notas.length; i++) {
                    if ($scope.declaracaoMensalServico.notas[i].id == id) {
                        return i;
                    }
                }
                return -1;
            }

            function inverterSelecaoNota(nota) {
                var indexNota = $scope.getIndexNota(nota.id);
                if (indexNota >= 0) {
                    $scope.declaracaoMensalServico.notas.splice(indexNota, 1);
                } else {
                    $scope.declaracaoMensalServico.notas.push(nota);
                }
                atualizarContadores();
            }

            function todasNotasSelecionadas() {
                if (!$scope.notas || $scope.notas.length == 0) {
                    return false;
                }
                for (var i = 0; i < $scope.notas.length; i++) {
                    if (getIndexNota($scope.notas[i].id) < 0) {
                        return false;
                    }
                }
                return true;
            }

            function inverterSelecaoTodasNotas() {
                var selecionarTodas = !todasNotasSelecionadas();
                for (var i = 0; i < $scope.notas.length; i++) {
                    var indexNota = getIndexNota($scope.notas[i].id);
                    if (selecionarTodas && indexNota == -1) {
                        $scope.declaracaoMensalServico.notas.push($scope.notas[i]);
                    } else if (!selecionarTodas && indexNota >= 0) {
                        $scope.declaracaoMensalServico.notas.splice(indexNota, 1);
                    }
                }
                atualizarContadores();
            }

            Account.get().$promise
                .then(function (account) {
                    $scope.declaracaoMensalServico.usuarioDeclaracao = account.data.nome;
                });


            function carregar() {
                PrestadorServicos.get({id: localStorageService.get("prestadorPrincipal").prestador.id}, function (prestador) {
                    $scope.prestador = prestador;

                    DeclaracaoMensalServico.getMeses({},
                        function (data) {
                            $scope.meses = data;
                            angular.forEach(data, function (mes) {
                                if (mes.numeroMes == $scope.declaracaoMensalServico.mes) {
                                    $scope.declaracaoMensalServico.mes = mes;
                                    acessarCompetencia();
                                }
                            });
                        });
                });
            }

            carregar();

            var onSaveFinished = function (result) {
                $scope.$emit('nfseApp:declaracaoMensalServicoUpdate', result);
                $scope.$emit('nfseApp:navbarUpdate');
                $state.go("guia", {reload: true});
                ImpressaoPdf.imprimirPdfViaUrl('/api/imprimir-dam-declaracao/' + result.id);
                acessarCompetencia();
            };

            function save() {
                var editDTO = {
                    id: $scope.declaracaoMensalServico.id,
                    mes: $scope.declaracaoMensalServico.mes.numeroMes,
                    exercicio: $scope.declaracaoMensalServico.exercicio,
                    tipo: 'PRINCIPAL',
                    notas: $scope.declaracaoMensalServico.notas,
                    prestador: $scope.prestador,
                    qtdNotas: $scope.quantidadeNotasFiscaisNaDeclaracao,
                    totalServicos: $scope.valorTotalServicos,
                    totalIss: $scope.valorTotalIssDevido,
                    abertura: new Date(),
                    tipoMovimento: $scope.declaracaoMensalServico.tipoMovimento,
                    encerramento: new Date(),
                    usuarioDeclaracao: $scope.declaracaoMensalServico.usuarioDeclaracao,
                    lancadoPor: 'CLIENTE'
                };

                if (!$scope.declaracaoMensalServico.notas || $scope.declaracaoMensalServico.notas.length == 0) {
                    DeclaracaoMensalServico.query({
                        page: 1,
                        per_page: 9999,
                        mes: editDTO.mes,
                        ano: editDTO.exercicio,
                        tipoMovimento: $scope.declaracaoMensalServico.tipoMovimento
                    }, function (result) {
                        if (result && result.length > 0) {
                            Notificacao.error("Você não pode declarar ausência de movimentos para "
                                + $scope.declaracaoMensalServico.mes.descricao + "/" + $scope.declaracaoMensalServico.exercicio
                                + ". Já existem declarações no periodo", "");
                        } else {
                            SweetAlert.swal({
                                    title: "Declaração de Ausência de Movimento",
                                    text: "Você tem certeza que deseja declarar ausência de movimento para o periodo " + $scope.declaracaoMensalServico.mes.descricao + "/" + $scope.declaracaoMensalServico.exercicio + "?",
                                    type: "warning",
                                    showCancelButton: true,
                                    confirmButtonColor: "#DD6B55", confirmButtonText: "Sim, Declarar",
                                    cancelButtonText: "Não, Cancelar",
                                    closeOnConfirm: true,
                                    closeOnCancel: true
                                },
                                function (isConfirm) {
                                    if (isConfirm) {
                                        SweetAlert.close();
                                        $timeout(DeclaracaoMensalServico.save(editDTO, onSaveFinished), 1000);
                                    }
                                });
                        }
                    });
                } else {
                    DeclaracaoMensalServico.save(editDTO, onSaveFinished);
                }
            }

            function limparNotas() {
                $scope.declaracaoMensalServico.notas = [];
                $scope.notas = [];
            }

            function buscarNotas() {
                NotaFiscal.buscarResumoSemDeclararPorCompetencia({
                        mes: $scope.declaracaoMensalServico.mes.numeroMes,
                        ano: $scope.declaracaoMensalServico.exercicio,
                        tipoMovimento: $scope.declaracaoMensalServico.tipoMovimento,
                        somenteComIssDevido: true
                    },
                    function (result) {
                        $scope.totalNotasNoPeriodo = result.qtdNotas;
                    });
                NotaFiscal.buscarNotasSemDeclararPorCompetencia({
                        mes: $scope.declaracaoMensalServico.mes.numeroMes,
                        ano: $scope.declaracaoMensalServico.exercicio,
                        tipoMovimento: $scope.declaracaoMensalServico.tipoMovimento,
                        somenteComIssDevido: true,
                        page: $scope.page - 1,
                        size: $scope.pageSize
                    },
                    function (result) {
                        $scope.notas = result;
                        for (var i = 0; i < $scope.notas.length; i++) {
                            if ($scope.notas[i].situacao == 'EMITIDA') {
                                if ($scope.notas[i].id == $scope.idDeclaracao) {
                                    $scope.declaracaoMensalServico.notas.push($scope.notas[i]);
                                }
                            }
                        }
                        atualizarContadores();
                    }
                );
            }

            function acessarCompetencia() {
                limparNotas();
                buscarNotas();
            }

            function atualizarContadores() {
                $scope.quantidadeNotasFiscaisNaDeclaracao = 0;
                $scope.valorTotalServicos = 0;
                $scope.valorTotalIss = 0;
                $scope.valorTotalIssRetido = 0;
                $scope.valorTotalIssDevido = 0;
                for (var i = 0; i < $scope.declaracaoMensalServico.notas.length; i++) {
                    $scope.quantidadeNotasFiscaisNaDeclaracao = $scope.quantidadeNotasFiscaisNaDeclaracao + 1;
                    $scope.valorTotalServicos = $scope.valorTotalServicos + $scope.declaracaoMensalServico.notas[i].totalServicos;
                    $scope.valorTotalIss = $scope.valorTotalIss + $scope.declaracaoMensalServico.notas[i].issCalculado;
                    if ($scope.declaracaoMensalServico.notas[i].issRetido) {
                        $scope.valorTotalIssRetido = $scope.valorTotalIssRetido + $scope.declaracaoMensalServico.notas[i].issCalculado;
                    }
                    $scope.valorTotalIssDevido = $scope.valorTotalIssDevido + ($scope.declaracaoMensalServico.tipoMovimento == 'NORMAL' ? $scope.declaracaoMensalServico.notas[i].iss : $scope.declaracaoMensalServico.notas[i].issCalculado);
                }
            }
        });

})();
