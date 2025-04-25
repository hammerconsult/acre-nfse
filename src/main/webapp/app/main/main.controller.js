(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('MainController', function ($scope, $rootScope, $filter, Principal, localStorageService,
                                                $state, NotaFiscal, DeclaracaoMensalServico,
                                                TrocaEmpresa, ServicoDeclarado, Util, PrestadorServicos,
                                                MensagemContribuinteUsuarioService, $modal, Notificacao) {

            $scope.competencias = [];
            $scope.competenciaAtual = {};
            $scope.compenteciaAnterior = {};
            $scope.hoje = new Date();
            $scope.activeTab = 1;
            $scope.abrirTrocarEmpresa = abrirTrocarEmpresa;
            $scope.isPrestadorPermitido = isPrestadorPermitido;

            function isPrestadorPermitido() {
                return $scope.prestadorPrincipal && $scope.prestadorPrincipal.permitido;
            }

            function definirActiveTab() {
                if ($scope.prestadorPrincipal) {
                    if (isPrestadorPermitido() && $scope.prestadorPrincipal.prestador.emiteNfs) {
                        $scope.activeTab = 1;
                        $scope.loadNotas();
                    } else {
                        $scope.activeTab = 2;
                        $scope.loadRecebidas();
                    }
                } else {
                    $scope.activeTab = 0;
                }
            }

            $scope.exibirMensagensAoUsuario = function () {
                if (Principal.isInRole('ROLE_ADMIN') ||
                    !$scope.prestadorPrincipal ||
                    !$scope.prestadorPrincipal.prestador ||
                    !$scope.prestadorPrincipal.prestador.id ||
                    $rootScope.exibiuMensagem) {
                    return;
                }
                $rootScope.exibiuMensagem = true;
                MensagemContribuinteUsuarioService.proximaMensagemNaoLida({}, function (data) {
                    if (data && data.id) {
                        $modal.open({
                            templateUrl: 'app/entities/mensagem-contribuinte-usuario/mensagem-contribuinte-usuario-dialog.html',
                            controller: 'MensagemContribuinteUsuarioDialogController',
                            size: 'sm',
                            animation: true,
                            keyboard: false,
                            backdrop: 'static',
                            resolve: {
                                mensagemContribuinteUsuario: function () {
                                    return data;
                                }
                            }
                        });
                    }
                });
            }

            function carregaGeral() {
                Principal.identity().then(function (account) {
                    $scope.account = account;
                    $scope.isAuthenticated = Principal.isAuthenticated;
                    $scope.prestadorPrincipal = localStorageService.get("prestadorPrincipal");
                    if ($scope.prestadorPrincipal &&
                        $scope.prestadorPrincipal.prestador &&
                        $scope.prestadorPrincipal.prestador.id) {
                        $scope.exibirMensagensAoUsuario();
                        $scope.carregarUsuarios();
                        definirActiveTab();
                    } else if (account.pessoaFisica) {
                        $state.go("notas-recebidas");
                    }

                });
            }

            $rootScope.$on('nfseApp:navbarUpdate', function (event, account) {
                carregaGeral();
            });

            $scope.carregarUsuarios = function () {
                $scope.usuariosInativos = [];
                PrestadorServicos.getUsuariosInativosPrestador({}, function (data) {
                    $scope.usuariosInativos = data;
                })
            };

            $scope.$on("nfseApp:verificarDebitos", function (event, account) {
                PrestadorServicos.getDebitos(
                    {
                        vencimentoFim: new Date(),
                        situacoes: ["EM_ABERTO"]
                    }, function (result) {
                        if (result && result.length > 0) {
                            $modal.open({
                                templateUrl: 'app/entities/debitos/aviso-debito-vencido.html',
                                controller: 'AvisoDebitoVencidoController',
                                size: 'md',
                                animation: true,
                                keyboard: false,
                                backdrop: 'static'
                            });
                        }
                    }
                );
            });

            $scope.carregarCancelamentosNaoVisualizados = function () {
                $scope.cancelamentosNaoVisualizados = [];
                NotaFiscal.buscarCancelamentosNaoVisualizados({}, function (data) {
                    $scope.cancelamentosNaoVisualizados = data;
                })
            };

            $scope.visualizarCancelamento = function (cancelamento) {
                NotaFiscal.marcarCancelamentoVisualizado(cancelamento, function (data) {
                    $state.go("notaFiscal.detail", {id: cancelamento.notaFiscal.id});
                })
            }

            $scope.deferirAcessoUsuario = function (user) {
                Notificacao.confirm('Atenção!',
                    'Tem certeza que deseja deferir o acesso ao usuário ' + user.nome + '?',
                    'warning', function () {
                        PrestadorServicos.ativarOuDesativarUsuarioEmpresa({login: user.login}, function () {
                            $scope.carregarUsuarios();
                            Notificacao.info('Informação!', "Deferimento realizado com sucesso!");
                        });
                    });

            };

            $scope.indeferirAcessoUsuario = function (user) {
                Notificacao.confirm('Atenção!',
                    'Tem certeza que deseja indeferir o acesso ao usuário ' + user.nome + '?',
                    'warning', function () {
                        PrestadorServicos.removerUsuarioEmpresa({id: user.id}, function () {
                            $scope.carregarUsuarios();
                            Notificacao.info('Informação!', "Indeferimento realizado com sucesso!");
                        });
                });
            };


            $scope.verNotasEmitidas = function () {
                $scope.activeTab = 1;
                $scope.loadNotas();
            };

            $scope.verCredenciamento = function () {
                $scope.activeTab = 0;
            };

            $scope.loadNotas = function () {
                var campos = [];

                var parametroQueryCampo = {
                    campo: "obj.prestador_id",
                    operador: "IGUAL",
                    valorLong: $scope.prestadorPrincipal.prestador.id
                };
                campos.push(parametroQueryCampo);

                NotaFiscal.buscarUltimasDezNotasFiscais({
                    offset: 0,
                    limit: 10,
                    parametrosQuery: [{
                        juncao: " and ",
                        parametroQueryCampos: campos
                    }],
                    orderBy: " order by obj.emissao desc "
                }, function (result) {
                    $scope.notas = result;
                });
            };

            $scope.verServicos = function () {
                $scope.activeTab = 3;
                $scope.loadServicos();
            };

            $scope.loadServicos = function () {
                var campos = [];

                var parametroQueryCampo = {
                    campo: "sd.cadastroeconomico_id",
                    operador: "IGUAL",
                    valorLong: $scope.prestadorPrincipal.prestador.id
                };
                campos.push(parametroQueryCampo);

                ServicoDeclarado.buscarServicosDeclarados({
                    offset: 0,
                    limit: 10,
                    parametrosQuery: [{
                        juncao: " and ",
                        parametroQueryCampos: campos
                    }],
                    orderBy: " order by sd.emissao desc "
                }, function (result) {
                    $scope.servicos = result;
                });
            };

            $scope.verNotasRecebidas = function () {
                $scope.activeTab = 2;
                $scope.loadRecebidas();
            };

            $scope.loadRecebidas = function () {
                var campos = [];
                if ($scope.prestadorPrincipal && $scope.prestadorPrincipal.prestador) {
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


                NotaFiscal.buscarNotasFiscais({
                    offset: 0,
                    limit: 10,
                    parametrosQuery: [{
                        juncao: " and ",
                        parametroQueryCampos: campos
                    }],
                    orderBy: " order by obj.emissao desc "
                }, function (result) {
                    $scope.recebidas = result;
                });
            };


            $scope.role = function (role) {
                return Principal.isInRole(role);
            };


            $scope.calculaCompetencia = function () {
                if ($scope.competenciaAtual && $scope.compenteciaAnterior) {
                    $scope.competenciaAtual.porcentagemEmissao =
                        $scope.calculaCrescimento($scope.compenteciaAnterior.emitidas, $scope.competenciaAtual.emitidas);
                    $scope.competenciaAtual.porcentagemRetencao =
                        $scope.calculaCrescimento($scope.compenteciaAnterior.retidas, $scope.competenciaAtual.retidas);
                    $scope.competenciaAtual.porcentagemCancelamento =
                        $scope.calculaCrescimento($scope.compenteciaAnterior.canceladas, $scope.competenciaAtual.canceladas);
                    $scope.competenciaAtual.porcentagemValor =
                        $scope.calculaCrescimento($scope.compenteciaAnterior.totalServicos, $scope.competenciaAtual.totalServicos);
                    $scope.competenciaAtual.porcentagemIss =
                        $scope.calculaCrescimento($scope.compenteciaAnterior.iss, $scope.competenciaAtual.iss);
                } else {
                    $scope.competenciaAtual.porcentagemEmissao = 0;
                    $scope.competenciaAtual.porcentagemCancelamento = 0;
                    $scope.competenciaAtual.porcentagemRetencao = 0;
                    $scope.competenciaAtual.porcentagemValor = 0;
                    $scope.competenciaAtual.porcentagemIss = 0;
                }
            };

            $scope.calculaCrescimento = function (anterior, atual) {
                var diferenca = atual - anterior;
                if (diferenca !== 0) {
                    diferenca = diferenca / anterior;
                    return diferenca * 100;
                } else {
                    return 0;
                }
            }

            $scope.valorMedioMensal = 0;
            $scope.quantidadeMediaMensal = 0;

            $scope.montarGraficoFaturamento = function () {
                var meses = [], valorServicos = [], valorIss = [];

                var valorTotal = 0;
                var quantidadeTotal = 0;
                var mesesQueTemNota = 0;
                angular.forEach($scope.competencias, function (nota) {
                    meses.push(nota.ano + '/' + $filter('translate')('nfseApp.Mes.' + nota.mes));

                    valorServicos.push(nota.totalServicos);
                    valorIss.push(nota.iss);
                    if (nota.totalServicos && nota.totalServicos > 0) {
                        valorTotal = valorTotal + nota.totalServicos;
                        quantidadeTotal = quantidadeTotal + nota.emitidas;
                        mesesQueTemNota = mesesQueTemNota + 1;
                    }
                });
                $scope.valorMedioMensal = valorTotal / mesesQueTemNota;
                $scope.quantidadeMediaMensal = quantidadeTotal / mesesQueTemNota;

                $scope.lineData = {
                    labels: meses,
                    datasets: [
                        {
                            label: "Faturamento",
                            fillColor: "rgba(26,179,148,0.5)",
                            strokeColor: "rgba(26,179,148,0.7)",
                            pointColor: "rgba(26,179,148,1)",
                            pointStrokeColor: "#fff",
                            pointHighlightFill: "#fff",
                            pointHighlightStroke: "rgba(26,179,148,1)",
                            data: valorServicos
                        },
                        {
                            label: "ISS",
                            fillColor: "rgba(26, 123, 185, 1)",
                            strokeColor: "rgba(26,123,148,0.7)",
                            pointColor: "rgba(26,123,148,1)",
                            pointStrokeColor: "#fff",
                            pointHighlightFill: "#fff",
                            pointHighlightStroke: "rgba(26,123,148,1)",
                            data: valorIss
                        }
                    ]
                };

                $scope.calculaCompetencia();
            };

            $scope.lineOptions = {
                scaleShowGridLines: true,
                scaleGridLineColor: "rgba(0,0,0,.05)",
                scaleGridLineWidth: 1,
                bezierCurve: true,
                bezierCurveTension: 0.4,
                pointDot: true,
                pointDotRadius: 4,
                pointDotStrokeWidth: 1,
                pointHitDetectionRadius: 20,
                datasetStroke: true,
                datasetStrokeWidth: 2,
                datasetFill: true
            };

            function abrirTrocarEmpresa() {
                TrocaEmpresa.open();
            }

            carregaGeral();
        });
})();
