(function () {
    'use strict';

    angular.module('nfseApp').controller('NotaFiscalEditController',
        ['$rootScope', '$scope', '$modal', '$state', '$filter', '$stateParams', 'entity', 'NotaFiscal',
            'localStorageService', 'Notificacao', 'Tomador', 'Servico', 'Municipio', 'CEP', 'Pais',
            'Configuracao', 'PrestadorServicos', 'ImpressaoPdf', 'Cnae', 'SweetAlert', 'ConstrucaoCivil', 'Principal',
            'moment',

            function ($rootScope, $scope, $modal, $state, $filter, $stateParams, entity, NotaFiscal,
                      localStorageService, Notificacao, Tomador, Servico, Municipio, CEP, Pais,
                      Configuracao, PrestadorServicos, ImpressaoPdf, Cnae, SweetAlert, ConstrucaoCivil, Principal,
                      moment) {

                $scope.mensagemCamposObrigatorios = [];
                $scope.itensDeclaracaoServico = [];
                $scope.aliquotas = [0.00, 2.00, 2.79, 3.00, 3.50, 3.84, 3.87, 4.23, 4.26, 4.31, 4.61, 4.65, 5.00];
                $scope.mostrarDadosNota = false;
                $scope.permiteExigibilidade = true;
                $scope.bloqueiaAliquota = false;
                $scope.passos = {tipoTomador: 1, servico: 2, resumo: 3};
                $scope.passoAtual = $scope.passos.tipoTomador;
                $scope.cpfCnpjTomadorAnterior = "";
                $scope.bloqueiaAliquota = true;
                $scope.emissaoRetroativa = false;
                $scope.emissaoRetroativaUltimaEmissao = false;
                $scope.ultimaDataEmissao = null;
                $scope.naturezasOperacao = [];
                $scope.art;
                $scope.aliquotaServicoExcedente;
                $scope.emissaoPopUp = false;
                $scope.date = null;
                $scope.opened = false;

                $scope.open = function($event) {
                    $event.preventDefault();
                    $event.stopPropagation();

                    $scope.opened = true;
                };

                Configuracao.get({}, function (data) {

                    $scope.municipio = data.municipio;
                    $scope.configuracao = data;
                    $scope.emissaoRetroativa = data.emissaoRetroativa;
                    $scope.emissaoRetroativaUltimaEmissao = data.emissaoRetroativaUltimaEmitida;

                    Principal.identity().then(function (account) {
                        if (account) {
                            $scope.account = account;
                        }
                    });

                    PrestadorServicos.get({id: localStorageService.get("prestadorPrincipal").prestador.id}, function (data) {
                        $scope.prestador = data;
                        $scope.cpfCnpjDoPrestador = $scope.prestador.pessoa.dadosPessoais.cpfCnpj;
                        if (!$scope.prestador.enquadramentoFiscal || !$scope.prestador.enquadramentoFiscal.tipoIssqn) {
                            SweetAlert.swal("Atenção", "Não foi possível encontrar o enquadramento fiscal do prestador de serviços, " +
                                "não é possível emitir uma Nota Fiscal de Serviços.", "error");
                            $state.go("notaFiscal");

                        }
                        if ($scope.prestador.situacao && $scope.prestador.situacao !== 'ATIVA_REGULAR' && $scope.prestador.situacao !== 'ATIVA_NAO_REGULAR') {
                            SweetAlert.swal("Atenção", "O cadastro do prestador de serviços não se encontra ATIVO.", "error");
                            $state.go("notaFiscal");
                        }
                        if (!localStorageService.get("prestadorPrincipal").permitido) {
                            SweetAlert.swal("Atenção", "Você não tem permissão para emissão de Nota Fiscal de Serviços.", "error");
                            $state.go("notaFiscal");
                        }

                        if (entity.$promise) {
                            entity.$promise.then(function (nota) {
                                $scope.notaFiscal = nota;
                                $scope.notaFiscal.emails = "";
                                if (!$scope.notaFiscal.id) {
                                    $scope.itemDeclaracaoServico().municipio = $scope.municipio;
                                    $scope.itemDeclaracaoServico().prestadoNoPais = true;
                                }
                                if ($scope.temCpfOuCnpj()) {
                                    $scope.cpfCnpjTomadorAnterior = $scope.notaFiscal.tomador.dadosPessoais.cpfCnpj;
                                }
                                if ($scope.notaFiscal.rps) {
                                    $scope.notaFiscal.rps.dataEmissao = new Date();
                                    $scope.notaFiscal.declaracaoPrestacaoServico.competencia = new Date();
                                }
                                $scope.segueParaFinalNotaCopiada();
                            });
                        } else {
                            $scope.notaFiscal = entity;
                            $scope.notaFiscal.modalidade = "IDENTIFICADO";
                        }

                        $scope.servicos = Servico.queryPorPrestador()

                    });
                });

                $scope.clearServico = function () {
                    $scope.notaFiscal.declaracaoPrestacaoServico.itens[0].valorServico = 0.0;
                    $scope.notaFiscal.declaracaoPrestacaoServico.itens[0].quantidade = 1;
                    $scope.notaFiscal.declaracaoPrestacaoServico.itens[0].descontosIncondicionados = 0.0;
                    $scope.notaFiscal.declaracaoPrestacaoServico.itens[0].deducoes = 0.0;
                    $scope.notaFiscal.totalNota = 0.0;
                    $scope.notaFiscal.baseCalculo = 0.0;
                    $scope.notaFiscal.declaracaoPrestacaoServico.itens[0].aliquotaServico = 0.0;
                    $scope.notaFiscal.issCalculado = 0.0;
                    $scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.pis = 0.0;
                    $scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.cofins = 0.0;
                    $scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.inss = 0.0;
                    $scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.irrf = 0.0;
                    $scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.csll = 0.0;
                    $scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.outrasRetencoes = 0.0;
                    $scope.notaFiscal.declaracaoPrestacaoServico.itens[0].detalhes = [];
                };

                $scope.itemDeclaracaoServico = function () {
                    return $scope.notaFiscal ? $scope.notaFiscal.declaracaoPrestacaoServico.itens[0] : null;
                };

                $scope.openDateDialog = function ($event) {
                    $event.preventDefault();
                    $event.stopPropagation();
                    $scope.emissaoPopUp = true;
                };

                $scope.isPrestadorSimplesNacional = function () {
                    return $scope.notaFiscal && $scope.notaFiscal.prestador.enquadramentoFiscal &&
                        $scope.notaFiscal.prestador.enquadramentoFiscal.simplesNacional;
                };

                $scope.segueParaFinalNotaCopiada = function () {
                    $scope.calculaValoresDaNota(true);

                    if ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais) {
                        $scope.mostrarTributosFederais = true;
                    }
                    if ($scope.notaFiscal.declaracaoPrestacaoServico.intermediario) {
                        $scope.mostrarIntermediario = true;
                    }
                    if ($scope.notaFiscal.declaracaoPrestacaoServico.construcaoCivil) {
                        $scope.mostrarConstrucaoCivil = true;
                        ConstrucaoCivil.buscarConstrucaoCivilFromPrestador({
                            prestador: $scope.prestador.id,
                            page: 0,
                            per_page: 999,
                            filtro: ""
                        }, function (result) {
                            $scope.obras = result;
                        });
                    }
                    if ($scope.notaFiscal.rps && $scope.notaFiscal.rps.id) {
                        $scope.passoAtual = $scope.passos.resumo;
                    }
                };

                $scope.validarDadosTomador = function () {
                    $scope.mensagemCamposObrigatorios = [];
                    if ($scope.notaFiscal.modalidade == "IDENTIFICADO") {
                        if (!$scope.notaFiscal.tomador.dadosPessoais.cpfCnpj) {
                            $scope.mensagemCamposObrigatorios.push("O campo " +
                                ($scope.tipoPessoa == "FISICA" ? "CPF" : "CNPJ") + " deve ser informado.");
                        } else {
                            if (!$scope.notaFiscal.tomador.dadosPessoais.nomeRazaoSocial) {
                                $scope.mensagemCamposObrigatorios.push("O campo " +
                                    ($scope.tipoPessoa == "FISICA" ? "Nome" : "Razão Social") + " deve ser informado.");
                            }
                            if (!$scope.notaFiscal.tomador.dadosPessoais.cep) {
                                $scope.mensagemCamposObrigatorios.push("O campo CEP deve ser informado.");
                            }
                            if (!$scope.notaFiscal.tomador.dadosPessoais.municipio) {
                                $scope.mensagemCamposObrigatorios.push("O campo Município deve ser informado.");
                            }
                        }
                    } else if ($scope.notaFiscal.modalidade == "SEM_CPF") {
                        if (!$scope.notaFiscal.tomador.dadosPessoais.nomeRazaoSocial) {
                            $scope.mensagemCamposObrigatorios.push("O campo Nome ou Razão Social deve ser informado.");
                        }
                    }
                    if ($scope.mensagemCamposObrigatorios.length > 0) {
                        Notificacao.camposObrigatorios($scope.mensagemCamposObrigatorios, "error");
                        return false;
                    }
                    return true;
                }

                $scope.preparadoProximoPasso = function () {
                    var preparado = true;
                    if ($scope.passoAtual == 1) {
                        if (!$scope.validarDadosTomador()) {
                            preparado = false;
                        } else {
                            if (!$scope.notaFiscal.declaracaoPrestacaoServico.itens) {
                                $scope.notaFiscal.declaracaoPrestacaoServico.itens = [];
                            }
                            $scope.verificarExigibilidadeIss();
                        }
                    } else if ($scope.passoAtual == 2) {
                        if (!$scope.validarServico()) {
                            preparado = false;
                        }
                    }
                    return preparado;
                };

                $scope.proximoPasso = function () {
                    if ($scope.preparadoProximoPasso()) {
                        $scope.passoAtual = $scope.passoAtual + 1;
                        document.body.scrollTop = document.documentElement.scrollTop = 0;
                    }
                };

                $scope.passoAnterior = function () {
                    $scope.passoAtual = $scope.passoAtual - 1;
                    document.body.scrollTop = document.documentElement.scrollTop = 0;
                };

                $scope.searchItemDeclaracaoServico = function () {
                    var modalDescriminacaoServico = $modal.open({
                        templateUrl: 'app/entities/notaFiscal/discriminacao-servico-search.html',
                        controller: 'DiscriminacaoServicoController',
                        size: 'lg'
                    });
                    modalDescriminacaoServico.result.then(function (data) {
                        if (data) {
                            $scope.notaFiscal.descriminacaoServico = data;
                        }
                    });
                };

                $scope.preLoadIntermediario = function () {
                    if ($scope.notaFiscal && $scope.notaFiscal.declaracaoPrestacaoServico && $scope.notaFiscal.declaracaoPrestacaoServico.intermediario) {
                        var cpfCnpj = $scope.notaFiscal.declaracaoPrestacaoServico.intermediario.cpfCnpj;
                        $scope.notaFiscal.declaracaoPrestacaoServico.intermediario = {};
                        $scope.notaFiscal.declaracaoPrestacaoServico.intermediario.cpfCnpj = cpfCnpj;
                        Tomador.getTomadorPorCpfCnpj({cpfCnpj: cpfCnpj}, function (result) {
                            if (!result.dadosPessoais) {
                                Tomador.getInPessoRepoByCpfCnpj({cpfCnpj: cpfCnpj}, function (result) {
                                    if (result.dadosPessoais && result.dadosPessoais.cpfCnpj) {
                                        $scope.notaFiscal.declaracaoPrestacaoServico.intermediario.nomeRazaoSocial = result.dadosPessoais.nomeRazaoSocial;
                                    }
                                });
                            } else {
                                $scope.notaFiscal.declaracaoPrestacaoServico.intermediario.nomeRazaoSocial = result.dadosPessoais.nomeRazaoSocial;
                            }
                        });
                    }
                };

                $scope.novoDetalhamentoServico = function () {

                    var modalInstance = $modal.open({
                        templateUrl: 'app/entities/notaFiscal/item-detalhamento-servico.html',
                        controller: 'ItemDetalhamentoServicoController',
                        size: 'lg',
                        backdrop: 'static',
                        keyboard: false,
                        resolve: {
                            servico: function () {
                                return $scope.itemDeclaracaoServico();
                            },
                            notaFiscal: function () {
                                return $scope.notaFiscal;
                            }
                        }
                    });
                    modalInstance.result.then(function (servico) {
                        $scope.itemDeclaracaoServico().detalhes = servico.detalhes;
                    });

                };

                $scope.removerDetalhe = function (index) {
                    $scope.itemDeclaracaoServico().detalhes.splice(index, 1);
                };

                $scope.$watch('editForm.cpfCnpjIntermediario.$valid', function (newVal) {
                    $scope.preLoadIntermediario();
                });

                function removerTracoDoCEP() {
                    if ($scope.notaFiscal.tomador.dadosPessoais
                        && $scope.notaFiscal.tomador.dadosPessoais.cep
                        && $scope.notaFiscal.tomador.dadosPessoais.cep.includes("-")) {
                        $scope.notaFiscal.tomador.dadosPessoais.cep = $scope.notaFiscal.tomador.dadosPessoais.cep.replace("-", "");
                    }
                }

                $scope.preLoadTomador = function (valid) {
                    $scope.notaFiscal.substitutoTributario = false;
                    if (!valid) {
                        $scope.notaFiscal.tomador = {dadosPessoais: {}};
                        return;
                    }
                    if ($scope.temCpfOuCnpj()) {
                        var cpfCnpj = $scope.notaFiscal.tomador.dadosPessoais.cpfCnpj;
                        $scope.cpfCnpjTomadorAnterior = cpfCnpj;
                        $scope.notaFiscal.tomador.dadosPessoais.cpfCnpj = cpfCnpj;
                        if ($scope.cpfDigitadoIgualPrestador()) {
                            Notificacao.error("Atenção", "Não é permitido que o Tomador de Serviços seja o próprio Prestador de Serviços.");
                            $scope.cpfCnpjTomadorAnterior = "";
                            $scope.notaFiscal.tomador.dadosPessoais.cpfCnpj = "";
                        } else {
                            Tomador.getTomadorPorCpfCnpj({cpfCnpj: cpfCnpj}, function (result) {
                                if (!result.dadosPessoais) {
                                    Tomador.getInPessoRepoByCpfCnpj({cpfCnpj: cpfCnpj}, function (data) {
                                        if (data.dadosPessoais && data.dadosPessoais.cpfCnpj) {
                                            data.prestador = localStorageService.get("prestadorPrincipal").prestador;
                                            buscarCadastroEconomicoDoTomador(data);
                                        }
                                    });
                                } else {
                                    buscarCadastroEconomicoDoTomador(result);
                                }
                            });
                        }
                    }
                };

                function quantidadeSubstitutoTributario(cadastrosEconomicos) {
                    var quantidadeSubstituto = 0;
                    cadastrosEconomicos.forEach(function (cadastroEconomico) {
                        if (cadastroEconomico.enquadramentoFiscal && cadastroEconomico.enquadramentoFiscal.substitutoTributario)
                            quantidadeSubstituto += 1;
                    });
                    return quantidadeSubstituto;
                }

                function buscarSubstitutoTributario(cadastrosEconomicos) {
                    var cadastroEconomico = null;
                    cadastrosEconomicos.forEach(function (cadastro) {
                        if (cadastro.enquadramentoFiscal && cadastro.enquadramentoFiscal.substitutoTributario) {
                            cadastroEconomico = cadastro;
                        }
                    });
                    return cadastroEconomico;
                }

                function buscarCadastroEconomicoDoTomador(tomador) {
                    PrestadorServicos.buscarPorCpfCnpj({cpfCnpj: tomador.dadosPessoais.cpfCnpj.replace(/[^\w\s]/gi, '')}, function (result) {
                        if (result && result.length > 1) {
                            if (quantidadeSubstitutoTributario(result) == 1) {
                                atribuirTomadorAndCadastroEconomico(tomador, buscarSubstitutoTributario(result));
                            } else {
                                $scope.selecionarCadastroEconomico(tomador, result);
                            }
                        } else {
                            atribuirTomadorAndCadastroEconomico(tomador, result[0]);
                        }
                    })
                }

                function atribuirTomadorAndCadastroEconomico(tomador, cadastroEconomico) {
                    $scope.notaFiscal.tomador = tomador;
                    if (cadastroEconomico) {
                        $scope.notaFiscal.tomador.cadastroEconomico = cadastroEconomico;
                        $scope.notaFiscal.tomador.dadosPessoais.inscricaoMunicipal = $scope.notaFiscal.tomador.cadastroEconomico.inscricaoMunicipal;
                        if ($scope.notaFiscal.tomador.cadastroEconomico.pessoa) {
                            $scope.notaFiscal.tomador.dadosPessoais.pessoaId = $scope.notaFiscal.tomador.cadastroEconomico.pessoa.id;
                        }
                        $scope.notaFiscal.substitutoTributario = $scope.notaFiscal.tomador.cadastroEconomico.enquadramentoFiscal.substitutoTributario;
                    }
                    removerTracoDoCEP();
                }

                $scope.selecionouObra = function () {
                    if ($scope.itemDeclaracaoServico().servico.construcaoCivil &&
                        $scope.notaFiscal.declaracaoPrestacaoServico.construcaoCivil &&
                        $scope.notaFiscal.declaracaoPrestacaoServico.construcaoCivil.incorporacao &&
                        $scope.notaFiscal.modalidade === 'NAO_IDENTIFICADO') {
                        SweetAlert.swal("Atenção", "É necessário identificar o tomador para selecionar uma obra de incorporação", "error");
                        $scope.notaFiscal.declaracaoPrestacaoServico.construcaoCivil = null;
                        return;
                    } else {
                        $scope.verificarExigibilidadeIss();
                    }
                };

                $scope.verificarExigibilidadeIss = function () {

                    $scope.permiteRetencao = false;
                    $scope.notaFiscal.declaracaoPrestacaoServico.issRetido = false;

                    $scope.naturezasOperacao = [];
                    $scope.permiteExigibilidade = true;
                    $scope.permiteMudarPais = true;
                    $scope.permiteMudarCidade = true;

                    if (!$scope.itemDeclaracaoServico()) {
                        return;
                    }

                    // if ($scope.itemDeclaracaoServico().servico &&
                    //     !$scope.itemDeclaracaoServico().servico.permiteRecolhimentoFora) {
                    //     $scope.itemDeclaracaoServico().municipio = $scope.municipio;
                    //     $scope.permiteMudarCidade = false;
                    // }

                    var tipoIssqn = $scope.prestador.enquadramentoFiscal.tipoIssqn;

                    if ($scope.notaFiscal.substitutoTributario) {
                        $scope.notaFiscal.declaracaoPrestacaoServico.issRetido = true;
                    }

                    if (tipoIssqn === "MEI"
                        || tipoIssqn === "FIXO"
                        || tipoIssqn === "ESTIMADO"
                        || tipoIssqn === "NAO_INCIDENCIA") {
                        $scope.naturezasOperacao.push("NAO_INCIDENCIA");
                        $scope.notaFiscal.declaracaoPrestacaoServico.naturezaOperacao = "NAO_INCIDENCIA";
                        $scope.notaFiscal.declaracaoPrestacaoServico.issRetido = false;
                        return;
                    }

                    if (tipoIssqn === 'ISENTO') {
                        $scope.permiteExigibilidade = false;
                        $scope.naturezasOperacao.push("ISENCAO");
                        $scope.notaFiscal.declaracaoPrestacaoServico.naturezaOperacao = "ISENCAO";
                        $scope.notaFiscal.declaracaoPrestacaoServico.issRetido = false;
                        return;
                    }

                    if ($scope.itemDeclaracaoServico() &&
                        $scope.itemDeclaracaoServico().servico &&
                        $scope.itemDeclaracaoServico().servico.vetadoLC1162003 || tipoIssqn == "IMUNE") {
                        $scope.permiteExigibilidade = false;
                        $scope.naturezasOperacao.push("IMUNIDADE");
                        $scope.notaFiscal.declaracaoPrestacaoServico.naturezaOperacao = "IMUNIDADE";
                        $scope.notaFiscal.declaracaoPrestacaoServico.issRetido = false;
                        return;
                    }


                    if ($scope.itemDeclaracaoServico() &&
                        $scope.itemDeclaracaoServico().servico &&
                        $scope.itemDeclaracaoServico().servico.permiteRecolhimentoFora &&
                        !$scope.itemDeclaracaoServico().prestadoNoPais) {
                        $scope.permiteExigibilidade = false;
                        $scope.naturezasOperacao.push("EXPORTACAO");
                        $scope.notaFiscal.declaracaoPrestacaoServico.naturezaOperacao = "EXPORTACAO";
                        $scope.notaFiscal.declaracaoPrestacaoServico.issRetido = false;
                        return;
                    }

                    if ($scope.itemDeclaracaoServico()
                        && $scope.itemDeclaracaoServico().servico
                        && $scope.itemDeclaracaoServico().servico.permiteRecolhimentoFora
                        && ($scope.itemDeclaracaoServico().municipio && $scope.itemDeclaracaoServico().municipio.id != $scope.municipio.id)) {
                        $scope.naturezasOperacao.push("TRIBUTACAO_FORA_MUNICIPIO");
                        $scope.notaFiscal.declaracaoPrestacaoServico.naturezaOperacao = "TRIBUTACAO_FORA_MUNICIPIO";
                        $scope.notaFiscal.declaracaoPrestacaoServico.issRetido = false;
                        return;
                    }

                    if ($scope.itemDeclaracaoServico() &&
                        $scope.itemDeclaracaoServico().servico &&
                        $scope.itemDeclaracaoServico().servico.construcaoCivil &&
                        $scope.notaFiscal.declaracaoPrestacaoServico.construcaoCivil &&
                        $scope.notaFiscal.declaracaoPrestacaoServico.construcaoCivil.incorporacao &&
                        $scope.configuracao.isentaServicoIncorporacao) {
                        $scope.permiteExigibilidade = false;
                        $scope.naturezasOperacao.push("ISENCAO");
                        $scope.notaFiscal.declaracaoPrestacaoServico.naturezaOperacao = "ISENCAO";
                        $scope.notaFiscal.declaracaoPrestacaoServico.issRetido = false;
                        return
                    }

                    if ($scope.isPrestadorSimplesNacional() && $scope.notaFiscal.declaracaoPrestacaoServico.issRetido) {
                        $scope.naturezasOperacao.push("RETENCAO_SIMPLES_NACIONAL");
                        $scope.notaFiscal.declaracaoPrestacaoServico.naturezaOperacao = "RETENCAO_SIMPLES_NACIONAL";
                        return
                    }

                    if ($scope.isPrestadorSimplesNacional() && !$scope.notaFiscal.declaracaoPrestacaoServico.issRetido) {
                        $scope.naturezasOperacao.push("SIMPLES_NACIONAL");
                        $scope.notaFiscal.declaracaoPrestacaoServico.naturezaOperacao = "SIMPLES_NACIONAL";
                        return
                    }


                    if ($scope.notaFiscal.declaracaoPrestacaoServico.issRetido) {
                        $scope.naturezasOperacao.push("RETENCAO");
                        $scope.notaFiscal.declaracaoPrestacaoServico.naturezaOperacao = "RETENCAO";
                        return
                    }

                    if ($scope.naturezasOperacao.length === 0) {
                        $scope.naturezasOperacao.push("TRIBUTACAO_MUNICIPAL");
                        $scope.notaFiscal.declaracaoPrestacaoServico.naturezaOperacao = "TRIBUTACAO_MUNICIPAL";
                    }

                    if ($scope.naturezasOperacao.length === 1) {
                        $scope.permiteExigibilidade = false;
                    }
                };


                $scope.selecionarCadastroEconomico = function (tomador, cadastrosEconomico) {
                    var modalInstance = $modal.open({
                        templateUrl: 'app/entities/cadastro-economico-dlg/cadastro-economico-dlg.html',
                        controller: 'CadastroEconomicoDialogController',
                        size: 'lg',
                        backdrop: 'static',
                        keyboard: false,
                        resolve: {
                            cadastrosEconomico: function () {
                                return cadastrosEconomico;
                            }
                        }
                    });
                    modalInstance.result.then(function (selecionado) {
                        atribuirTomadorAndCadastroEconomico(tomador, selecionado);
                    });
                };

                $scope.searchTomadorServico = function () {
                    var modalInstance = $modal.open({
                        templateUrl: 'app/entities/tomador/tomador-search.html',
                        controller: 'TomadorSearchController',
                        size: 'lg'
                    });
                    modalInstance.result.then(function (tomador) {

                        Tomador.get({id: tomador.id}, function (data) {
                            if (data && data.dadosPessoais) {
                                $scope.notaFiscal.tomador = {};
                                $scope.notaFiscal.tomador.dadosPessoais = data.dadosPessoais;
                                $scope.notaFiscal.tomador.dadosPessoais.id = null;
                                if ($scope.notaFiscal.tomador.dadosPessoais.endereco) {
                                    $scope.notaFiscal.tomador.dadosPessoais.endereco.id = null;
                                }
                                $scope.notaFiscal.tomadorHabitual = true;
                                $scope.preLoadTomador(true);
                            }
                        });


                    }, function () {
                        //$log.info('Modal dismissed at: ' + new Date());
                    });
                };

                $scope.searchIntermediarioServico = function () {
                    var modalInstance = $modal.open({
                        templateUrl: 'app/entities/tomador/tomador-search.html',
                        controller: 'TomadorSearchController',
                        size: 'lg'
                    });
                    modalInstance.result.then(function (tomador) {
                        if (tomador && tomador.dadosPessoais) {
                            $scope.notaFiscal.declaracaoPrestacaoServico.intermediario = {};
                            $scope.notaFiscal.declaracaoPrestacaoServico.intermediario.dadosPessoais = tomador.dadosPessoais;
                            $scope.notaFiscal.declaracaoPrestacaoServico.intermediario.dadosPessoais.id = null;
                            if ($scope.notaFiscal.declaracaoPrestacaoServico.intermediario.dadosPessoais.endereco) {
                                $scope.notaFiscal.declaracaoPrestacaoServico.intermediario.dadosPessoais.endereco.id = null;
                            }
                            $scope.notaFiscal.declaracaoPrestacaoServico.intermediario.tomador = tomador;
                        }
                    }, function () {
                        //$log.info('Modal dismissed at: ' + new Date());
                    });
                };

                var onSaveFinished = function (result) {
                    if (result.situacao == 'EMITIDA') {
                        $state.go("notaFiscal.detail", {id: result.id});
                        ImpressaoPdf.imprimirPdfViaUrl('/publico/notaFiscal/imprime/' + result.id);
                    }
                };

                var onSaveError = function (result) {
                    $state.go("notaFiscal");
                };

                $scope.save = function () {
                    if ($scope.validarCamposObrigatorios()) {
                        $scope.notaFiscal.situacao = 'EMITIDA';
                        NotaFiscal.save($scope.notaFiscal, onSaveFinished, onSaveError);
                    }
                };

                $scope.openCompetencia = function ($event) {
                    $scope.status.openedCompetencia = true;
                };

                $scope.status = {openedCompetencia: false};

                $scope.clear = function () {
                };

                $scope.getSomaDescontosServico = function () {
                    if ($scope.itemDeclaracaoServico()) {
                        return $scope.itemDeclaracaoServico().descontosCondicionados + $scope.itemDeclaracaoServico().descontosIncondicionados +
                            $scope.itemDeclaracaoServico().deducoes;
                    }
                };

                $scope.validarServico = function () {
                    $scope.mensagemCamposObrigatorios = [];
                    if (!$scope.itemDeclaracaoServico().nomeServico) {
                        $scope.mensagemCamposObrigatorios.push("O campo serviço deve ser informado.");
                    }
                    if (!$scope.itemDeclaracaoServico().descricao) {
                        $scope.mensagemCamposObrigatorios.push("O campo descrição do serviço deve ser informado.");
                    }
                    if (!$scope.itemDeclaracaoServico().quantidade) {
                        $scope.mensagemCamposObrigatorios.push("O campo quantidade deve ser maior que zero.");
                    }
                    if ($scope.getSomaDescontosServico() > ($scope.itemDeclaracaoServico().valorServico * $scope.itemDeclaracaoServico().quantidade)) {
                        $scope.mensagemCamposObrigatorios.push("Os valores de desconto não podem ser maiores que o valor total dos serviços.");
                    }
                    if (!$scope.itemDeclaracaoServico().valorServico || $scope.itemDeclaracaoServico().valorServico === 0) {
                        $scope.mensagemCamposObrigatorios.push("O campo valor do serviço deve ser maior que zero.");
                    }
                    if ($scope.itemDeclaracaoServico().deducoes > 0 && $scope.configuracao.validarDadosDeducaoNfse) {
                        if (!$scope.itemDeclaracaoServico().tipoDeducao) {
                            $scope.mensagemCamposObrigatorios.push("O campo tipo de dedução deve ser informado");
                        }
                        if (!$scope.itemDeclaracaoServico().tipoOperacao) {
                            $scope.mensagemCamposObrigatorios.push("O campo tipo de operação deve ser informado");
                        }
                        if (!$scope.itemDeclaracaoServico().numeroDocumentoFiscal) {
                            $scope.mensagemCamposObrigatorios.push("O campo número do documento fiscal deve ser informado");
                        }
                        if (!$scope.itemDeclaracaoServico().cpfCnpjDeducao) {
                            $scope.mensagemCamposObrigatorios.push("O campo CPF/CNPJ deve ser informado");
                        }
                        if (!$scope.itemDeclaracaoServico().cpfCnpjDeducao) {
                            $scope.mensagemCamposObrigatorios.push("O campo CPF/CNPJ deve ser informado");
                        }
                    }
                    if (!$scope.itemDeclaracaoServico().prestadoNoPais && !$scope.itemDeclaracaoServico().pais) {
                        $scope.mensagemCamposObrigatorios.push("O campo Qual Pais? deve ser infoirmado");
                    }
                    if ($scope.itemDeclaracaoServico().detalhes && $scope.itemDeclaracaoServico().detalhes.length > 0) {
                        var totalDetalhes = 0;
                        for (var i = 0; i < $scope.itemDeclaracaoServico().detalhes.length; i++) {
                            totalDetalhes += $scope.itemDeclaracaoServico().detalhes[i].valorServico;
                        }
                        var diferenca = totalDetalhes - $scope.itemDeclaracaoServico().valorServico;
                        if (parseFloat(diferenca > 0 ? diferenca : diferenca * -1).toFixed(2) > 0) {
                            $scope.mensagemCamposObrigatorios.push("O valor total dos itens do serviço deve ser igual ao valor total do serviço");
                        }
                    }
                    if ($scope.mensagemCamposObrigatorios.length > 0) {
                        Notificacao.camposObrigatorios($scope.mensagemCamposObrigatorios, "error");
                        return false;
                    }
                    if (!$scope.validarAliquotaNota()) {
                        return false;
                    }
                    return true;
                };

                $scope.validarAliquotaNota = function () {
                    if ($scope.isPrestadorSimplesNacional()
                        && $scope.notaFiscal.declaracaoPrestacaoServico.issRetido) {
                        if ($scope.rbt12 > 0) {
                            if (!$scope.anexoLei1232006) {
                                Notificacao.warn("Atenção", "Por favor selecione o Anexo da Lei 123/2006.");
                                return false;
                            }
                            if ($scope.itemDeclaracaoServico().aliquotaServico < $scope.aliquotaMinima) {
                                $scope.itemDeclaracaoServico().aliquotaServico = 0.00;
                                Notificacao.warn("Atenção", "A Alíquota do ISS deve ser superior ou igual a " +
                                    $scope.aliquotaMinima + "%.");
                                return false;
                            }
                        }
                        if ($scope.itemDeclaracaoServico().aliquotaServico < 2 || $scope.itemDeclaracaoServico().aliquotaServico > 5) {
                            $scope.itemDeclaracaoServico().aliquotaServico = 0.00;
                            Notificacao.warn("Atenção", "A Alíquota do ISS deve estar entre 2% e 5%.");
                            return false;
                        }
                    }
                    return true;
                };

                $scope.verificarBloqueioAliquotaNota = function () {
                    $scope.bloqueiaAliquota = false;

                    if (!$scope.itemDeclaracaoServico().servico || !$scope.itemDeclaracaoServico().municipio) {
                        return;
                    }

                    if ($scope.isPrestadorSimplesNacional() && !$scope.notaFiscal.declaracaoPrestacaoServico.issRetido) {
                        $scope.bloqueiaAliquota = true;
                        $scope.itemDeclaracaoServico().aliquotaServico = 0;
                        return;
                    }

                    if ($scope.notaFiscal.declaracaoPrestacaoServico.naturezaOperacao === "TRIBUTACAO_FORA_MUNICIPIO") {
                        $scope.bloqueiaAliquota = true;
                        $scope.itemDeclaracaoServico().aliquotaServico = 0;
                        return;
                    }

                    if (($scope.exigibilidadeNaoIncide() && !($scope.prestador.enquadramentoFiscal.tipoIssqn === "MENSAL"))
                        || $scope.prestador.enquadramentoFiscal.tipoIssqn === "MEI"
                        || (($scope.temExigibilidade() || $scope.exigibilidadeSuspensa())
                            && ($scope.prestador.enquadramentoFiscal.tipoIssqn === "FIXO"
                                || $scope.prestador.enquadramentoFiscal.tipoIssqn === "ESTIMADO"))) {
                        $scope.bloqueiaAliquota = true;
                        $scope.itemDeclaracaoServico().aliquotaServico = 0;
                        return;
                    }

                    if (!$scope.isPrestadorSimplesNacional() &&
                        $scope.itemDeclaracaoServico().servico.aliquota &&
                        ($scope.temExigibilidade() || $scope.exigibilidadeSuspensa())) {
                        $scope.bloqueiaAliquota = true;
                        $scope.itemDeclaracaoServico().aliquotaServico = $scope.itemDeclaracaoServico().servico.aliquota;
                        return;
                    }

                    if ((!$scope.itemDeclaracaoServico().aliquotaServico || $scope.itemDeclaracaoServico().aliquotaServico <= 0) &&
                        !($scope.prestador.enquadramentoFiscal.tipoIssqn === "MENSAL")) {
                        $scope.bloqueiaAliquota = false;
                    }
                };

                $scope.validarValorDeducao = function () {
                    if ($scope.itemDeclaracaoServico().deducoes && $scope.itemDeclaracaoServico().deducoes > $scope.itemDeclaracaoServico().valorServico) {
                        $scope.itemDeclaracaoServico().deducoes = 0.00;
                        Notificacao.warn("Atenção!", "O Valor de Dedução nao pode ser maior que o Valor do Serviço.");
                        return;
                    }
                    var baseCalculo = ($scope.itemDeclaracaoServico().valorServico * $scope.itemDeclaracaoServico().quantidade) -
                        ($scope.itemDeclaracaoServico().descontosIncondicionados);
                    if ($scope.itemDeclaracaoServico().deducoes && $scope.itemDeclaracaoServico().deducoes > 0 &&
                        baseCalculo && baseCalculo > 0) {
                        var percentualDeducaoAplicado = $scope.itemDeclaracaoServico().deducoes / (baseCalculo / 100);
                        if (percentualDeducaoAplicado > $scope.itemDeclaracaoServico().servico.percentualDeducao) {
                            $scope.itemDeclaracaoServico().deducoes = 0.00;
                            Notificacao.warn("Atenção!", "O Valor de Dedução é superior ao máximo permitido.");
                            return;
                        }
                    }
                };

                $scope.calculaValoresDaNota = function (calcularRetencoes) {
                    if (!$scope.itemDeclaracaoServico()) {
                        return
                    }

                    $scope.verificarBloqueioAliquotaNota();

                    $scope.validarValorDeducao();

                    NotaFiscal.calcularValores($scope.notaFiscal, function (notaFiscal) {
                        $scope.notaFiscal = notaFiscal;
                    });
                };

                $scope.criarRetencoesFederais = function (valor) {
                    if (getTotalRetencoesFederais()) {
                        return;
                    }
                    var tributosFederais = {};
                    PrestadorServicos.getTributosFederais({prestadorId: $scope.prestador.id},
                        function (result) {
                            tributosFederais = result;
                            tributosFederais.percentualPis = tributosFederais.pis ? tributosFederais.pis : 0;
                            tributosFederais.pis = tributosFederais.percentualPis ? ((tributosFederais.percentualPis / 100) * valor) : 0;
                            tributosFederais.percentualCofins = tributosFederais.cofins ? tributosFederais.cofins : 0;
                            tributosFederais.cofins = tributosFederais.percentualCofins ? ((tributosFederais.percentualCofins / 100) * valor) : 0;
                            tributosFederais.percentualInss = tributosFederais.inss ? tributosFederais.inss : 0;
                            tributosFederais.inss = tributosFederais.percentualInss ? ((tributosFederais.percentualInss / 100) * valor) : 0;
                            tributosFederais.percentualIrrf = tributosFederais.irrf ? tributosFederais.irrf : 0;
                            tributosFederais.irrf = tributosFederais.percentualIrrf ? ((tributosFederais.percentualIrrf / 100) * valor) : 0;
                            tributosFederais.percentualCsll = tributosFederais.csll ? tributosFederais.csll : 0;
                            tributosFederais.csll = tributosFederais.percentualCsll ? ((tributosFederais.percentualCsll / 100) * valor) : 0;
                            tributosFederais.percentualCpp = tributosFederais.cpp ? tributosFederais.cpp : 0;
                            tributosFederais.cpp = tributosFederais.percentualCpp ? ((tributosFederais.percentualCpp / 100) * valor) : 0;
                            tributosFederais.percentualInss = tributosFederais.inss ? tributosFederais.inss : 0;
                            tributosFederais.inss = tributosFederais.percentualInss ? ((tributosFederais.percentualInss / 100) * valor) : 0;
                            tributosFederais.percentualOutrasRetencoes = tributosFederais.outrasRetencoes ? tributosFederais.outrasRetencoes : 0;
                            tributosFederais.outrasRetencoes = tributosFederais.percentualOutrasRetencoes ? ((tributosFederais.percentualOutrasRetencoes / 100) * valor) : 0;
                            $scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais = tributosFederais;
                        });
                };

                $scope.calcularValoresPorAliquota = function (aliquota, valor) {
                    $scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais[valor]
                        = ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais[aliquota] / 100)
                        * $scope.notaFiscal.totalServicos;
                    atualizarRentencaoFederal();
                };

                $scope.calcularAliquotaPorValor = function (valor, aliquota) {
                    $scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais[aliquota]
                        = ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais[valor] * 100)
                        / $scope.notaFiscal.totalServicos;
                    atualizarRentencaoFederal();
                };

                function atualizarRentencaoFederal() {
                    $scope.calculaValoresDaNota();
                    $scope.notaFiscal.retencoesFederais = getTotalRetencoesFederais();

                }

                $scope.calcularRetencoesFederais = function (valor) {
                    if (!$scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais) {
                        $scope.criarRetencoesFederais(valor);
                    }

                    tributosFederais = $scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais;

                    if ($scope.notaFiscal.totalServicos && $scope.notaFiscal.totalServicos > 0 && $scope.anexoLei1232006Faixa) {

                        var tributosFederais = $scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais;

                        tributosFederais.percentualPis = 0;
                        tributosFederais.pis = 0;
                        if ($scope.anexoLei1232006Faixa.pisPasep) {
                            tributosFederais.percentualPis = ($scope.anexoLei1232006Faixa.pisPasep / 100) * $scope.aliquotaEfetiva;
                            if ($scope.aliquotaServicoExcedente > 0) {
                                tributosFederais.percentualPis = tributosFederais.percentualPis + (($scope.anexoLei1232006Faixa.pisPasep / 100) * $scope.aliquotaServicoExcedente);
                            }
                            tributosFederais.percentualPis = tributosFederais.percentualPis * 100;
                            tributosFederais.percentualPis = parseFloat(tributosFederais.percentualPis).toFixed(2);
                            tributosFederais.pis = (tributosFederais.percentualPis / 100) * $scope.notaFiscal.totalServicos;
                        }

                        tributosFederais.percentualCofins = 0;
                        tributosFederais.cofins = 0;
                        if ($scope.anexoLei1232006Faixa.cofins) {
                            tributosFederais.percentualCofins = ($scope.anexoLei1232006Faixa.cofins / 100) * $scope.aliquotaEfetiva;
                            if ($scope.aliquotaServicoExcedente > 0) {
                                tributosFederais.percentualCofins = tributosFederais.percentualCofins + (($scope.anexoLei1232006Faixa.cofins / 100) * $scope.aliquotaServicoExcedente);
                            }
                            tributosFederais.percentualCofins = tributosFederais.percentualCofins * 100;
                            tributosFederais.percentualCofins = parseFloat(tributosFederais.percentualCofins).toFixed(2);
                            tributosFederais.cofins = (tributosFederais.percentualCofins / 100) * $scope.notaFiscal.totalServicos;
                        }

                        tributosFederais.percentualIrpj = 0;
                        tributosFederais.irpj = 0;
                        if ($scope.anexoLei1232006Faixa.irpj) {
                            tributosFederais.percentualIrrf = ($scope.anexoLei1232006Faixa.irpj / 100) * $scope.aliquotaEfetiva;
                            if ($scope.aliquotaServicoExcedente > 0) {
                                tributosFederais.percentualIrrf = tributosFederais.percentualIrrf +
                                    (($scope.notaFiscal.prestador.enquadramentoFiscal.anexoLei1232006.irpjExcedente / 100) * $scope.aliquotaServicoExcedente);
                            }
                            tributosFederais.percentualIrrf = tributosFederais.percentualIrrf * 100;
                            tributosFederais.percentualIrrf = parseFloat(tributosFederais.percentualIrrf).toFixed(2);
                            tributosFederais.irrf = (tributosFederais.percentualIrrf / 100) * $scope.notaFiscal.totalServicos;
                        }

                        tributosFederais.percentualCsll = 0;
                        tributosFederais.csll = 0;
                        if ($scope.anexoLei1232006Faixa.csll) {
                            tributosFederais.percentualCsll = ($scope.anexoLei1232006Faixa.csll / 100) * $scope.aliquotaEfetiva;
                            if ($scope.aliquotaServicoExcedente > 0) {
                                tributosFederais.percentualCsll = tributosFederais.percentualCsll +
                                    (($scope.notaFiscal.prestador.enquadramentoFiscal.anexoLei1232006.csllExcedente / 100) * $scope.aliquotaServicoExcedente);
                            }
                            tributosFederais.percentualCsll = tributosFederais.percentualCsll * 100;
                            tributosFederais.percentualCsll = parseFloat(tributosFederais.percentualCsll).toFixed(2);
                            tributosFederais.csll = (tributosFederais.percentualCsll / 100) * $scope.notaFiscal.totalServicos;
                        }

                        tributosFederais.percentualCpp = 0;
                        tributosFederais.cpp = 0;
                        if ($scope.anexoLei1232006Faixa.cpp) {
                            tributosFederais.percentualCpp = ($scope.anexoLei1232006Faixa.cpp / 100) * $scope.aliquotaEfetiva;
                            if ($scope.aliquotaServicoExcedente > 0) {
                                tributosFederais.percentualCpp = tributosFederais.percentualCpp +
                                    (($scope.notaFiscal.prestador.enquadramentoFiscal.anexoLei1232006.cppExcedente / 100) * $scope.aliquotaServicoExcedente);
                            }
                            tributosFederais.percentualCpp = tributosFederais.percentualCpp * 100;
                            tributosFederais.percentualCpp = parseFloat(tributosFederais.percentualCpp).toFixed(2);
                            tributosFederais.cpp = (tributosFederais.percentualCpp / 100) * $scope.notaFiscal.totalServicos;
                        }

                        tributosFederais.percentualInss = 0;
                        tributosFederais.inss = 0;
                        if ($scope.anexoLei1232006Faixa.inss) {
                            tributosFederais.percentualInss = ($scope.anexoLei1232006Faixa.inss / 100) * $scope.aliquotaEfetiva;
                            if ($scope.aliquotaServicoExcedente > 0) {
                                tributosFederais.percentualInss = tributosFederais.percentualInss +
                                    (($scope.notaFiscal.prestador.enquadramentoFiscal.anexoLei1232006.inssExcedente / 100) * $scope.aliquotaServicoExcedente);
                            }
                            tributosFederais.percentualInss = tributosFederais.percentualInss * 100;
                            tributosFederais.percentualInss = parseFloat(tributosFederais.percentualInss).toFixed(2);
                            tributosFederais.inss = (tributosFederais.percentualInss / 100) * $scope.notaFiscal.totalServicos;
                        }

                        var percentualOutrasRetencoes = 0;

                        if ($scope.anexoLei1232006Faixa.icms &&
                            $scope.anexoLei1232006Faixa.icms > 0) {
                            percentualOutrasRetencoes = percentualOutrasRetencoes + $scope.anexoLei1232006Faixa.icms;
                        }

                        if ($scope.anexoLei1232006Faixa.ipi &&
                            $scope.anexoLei1232006Faixa.ipi > 0) {
                            percentualOutrasRetencoes = percentualOutrasRetencoes + $scope.anexoLei1232006Faixa.ipi;
                        }

                        if (percentualOutrasRetencoes > 0) {
                            tributosFederais.percentualOutrasRetencoes = (percentualOutrasRetencoes / 100) * $scope.aliquotaEfetiva;
                            tributosFederais.outrasRetencoes =
                                (tributosFederais.percentualOutrasRetencoes) * $scope.notaFiscal.totalServicos;
                            tributosFederais.percentualOutrasRetencoes = tributosFederais.percentualOutrasRetencoes * 100;
                        }

                    }
                };

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
                        if ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.cpp) {
                            total = total + ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.cpp);
                        }
                        if ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.csll) {
                            total = total + ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.csll);
                        }
                        if ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.outrasRetencoes) {
                            total = total + ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.outrasRetencoes);
                        }
                    }
                    return total;
                }

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

                function getTotalAliquotaRetencoesFederais() {
                    var total = 0;
                    if ($scope.notaFiscal
                        && $scope.notaFiscal.declaracaoPrestacaoServico
                        && $scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais) {

                        if ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.percentualPis) {
                            total = total + ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.percentualPis);
                        }
                        if ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.percentualCofins) {
                            total = total + ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.percentualCofins);
                        }
                        if ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.percentualInss) {
                            total = total + ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.percentualInss);
                        }
                        if ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.percentualIrrf) {
                            total = total + ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.percentualIrrf);
                        }
                        if ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.percentualCpp) {
                            total = total + ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.percentualCpp);
                        }
                        if ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.percentualCsll) {
                            total = total + ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.percentualCsll);
                        }
                        if ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.percentualOutrasRetencoes) {
                            total = total + ($scope.notaFiscal.declaracaoPrestacaoServico.tributosFederais.percentualOutrasRetencoes);
                        }
                    }
                    return total;
                }

                $scope.removeServico = function (index) {
                    $scope.notaFiscal.declaracaoPrestacaoServico.itens.splice(index, 1);
                };

                $scope.selecionouServico = function () {
                    $scope.bloqueiaAliquota = true;
                    $scope.notaFiscal.declaracaoPrestacaoServico.construcaoCivil = null;
                    $scope.art = null;
                    if ($scope.itemDeclaracaoServico().servico) {
                        $scope.codigoServico = $scope.itemDeclaracaoServico().servico.codigo;
                        $scope.itemDeclaracaoServico().aliquotaServico = 0;
                        if (!$scope.isPrestadorSimplesNacional()) {
                            $scope.itemDeclaracaoServico().aliquotaServico = $scope.itemDeclaracaoServico().servico.aliquota;
                        }
                        $scope.itemDeclaracaoServico().descricao = $scope.itemDeclaracaoServico().servico.descricao;
                        $scope.itemDeclaracaoServico().nomeServico = $scope.itemDeclaracaoServico().servico.descricao;
                        $scope.notaFiscal.descriminacaoServico = "";

                        $scope.verificarExigibilidadeIss();
                        $scope.verificarBloqueioAliquotaNota();
                        $scope.preencherDadosSimplesNacional();
                    }
                };

                $scope.preencherDadosSimplesNacional = function () {
                    $scope.rbt12 = null;
                    $scope.anexoLei1232006 = null;
                    $scope.anexoLei1232006Faixa = null;
                    $scope.aliquotaMinima = null;
                    if ($scope.notaFiscal.declaracaoPrestacaoServico.naturezaOperacao === "RETENCAO_SIMPLES_NACIONAL") {
                        PrestadorServicos.getRBT12ParaRetencao(null, function (data) {
                            if (data && data.value && data.value > 0) {
                                $scope.rbt12 = data.value;
                                if ($scope.itemDeclaracaoServico().servico.anexoLei1232006) {
                                    $scope.anexoLei1232006 = $scope.itemDeclaracaoServico().servico.anexoLei1232006;
                                    $scope.definirFaixaEAliquotaMinimaSNA();
                                }
                            }
                        })
                    }
                }

                $scope.definirFaixaEAliquotaMinimaSNA = function () {
                    const faixas = $scope.anexoLei1232006.faixas;
                    faixas.sort(function (a, b) {
                        return a.valorMaximo - b.valorMaximo
                    });
                    const rbt12 = $scope.rbt12;
                    for (var i = 0; i < faixas.length; i++) {
                        if (rbt12 < faixas[i].valorMaximo) {
                            $scope.anexoLei1232006Faixa = faixas[i];
                            break;
                        }
                    }
                    if (!$scope.anexoLei1232006Faixa) {
                        $scope.anexoLei1232006Faixa = faixas[faixas.length - 1];
                    }
                    const faixa = $scope.anexoLei1232006Faixa;
                    console.log('faixa {}', $scope.anexoLei1232006Faixa);
                    var aliquotaMinima = faixa.aliquota / 100;
                    var aliquotaEfetiva = rbt12 * aliquotaMinima;
                    if (faixa.valorDeduzir > 0) {
                        aliquotaEfetiva = aliquotaEfetiva - faixa.valorDeduzir;
                    }
                    aliquotaEfetiva = aliquotaEfetiva / rbt12;
                    var percentualIss = faixa.iss / 100;
                    aliquotaMinima = percentualIss * aliquotaEfetiva;
                    aliquotaMinima = (aliquotaMinima * 100).toFixed(2);
                    console.log('aliquotaMinima {}', aliquotaMinima);
                    if (aliquotaMinima < 2) {
                        $scope.aliquotaMinima = 2;
                    } else if (aliquotaMinima > 5) {
                        $scope.aliquotaMinima = 5;
                    } else {
                        $scope.aliquotaMinima = aliquotaMinima;
                    }
                    console.log('aliquotaMinima final {}', $scope.aliquotaMinima);
                }

                $scope.selecionarServicoPorCodigo = function () {
                    $scope.clearServico();
                    if (!$scope.codigoServico) {
                        return;
                    }
                    if ($scope.configuracao.permiteSelcionarServicoNaoAutorizado) {
                        Servico.getPorCodigo({codigo: $scope.codigoServico.replace(/^0+/, '')}, function (data) {
                            $scope.itemDeclaracaoServico().servico = data;
                            $scope.selecionouServico();
                        })
                    } else {
                        for (var i = 0; i < $scope.servicos.length; i++) {
                            if ($scope.servicos[i].codigo.replace(/^0+/, '') === $scope.codigoServico.replace(/^0+/, '')) {
                                $scope.itemDeclaracaoServico().servico = $scope.servicos[i];
                                $scope.selecionouServico();
                                return;
                            }
                        }
                        $scope.itemDeclaracaoServico().servico = {}
                    }
                };

                $scope.searchServico = function () {
                    var modalInstance = $modal.open({
                        templateUrl: 'app/entities/prestadorServicos/servicoPrestador-search.html',
                        controller: 'ServicoPrestadorSearchController',
                        size: 'lg'
                    });
                    modalInstance.result.then(function (result) {
                        $scope.clearServico();
                        $scope.itemDeclaracaoServico().servico = result;
                        $scope.selecionouServico();
                    }, function () {
                        //$log.info('Modal dismissed at: ' + new Date());
                    });
                };

                $scope.validarCamposObrigatorios = function () {
                    $scope.mensagemCamposObrigatorios = [];
                    if ($scope.notaFiscal.declaracaoPrestacaoServico.issRetido === true && $scope.notaFiscal.declaracaoPrestacaoServico.responsavelRetencao === undefined) {
                        $scope.mensagemCamposObrigatorios.push("O campo Responsável pela Retenção é obrigatório.");
                    }
                    if ($scope.notaFiscal.modalidade == 'IDENTIFICADO') {
                        if ($scope.notaFiscal.tomador.dadosPessoais.cpfCnpj === null) {
                            $scope.mensagemCamposObrigatorios.push("O campo CPF ou CNPJ do Tomador de Serviço é obrigatório.");
                        }
                        if ($scope.notaFiscal.tomador.dadosPessoais.nomeRazaoSocial === null) {
                            $scope.mensagemCamposObrigatorios.push("O campo Nome ou Razão Social é obrigatório.");
                        }
                    }
                    if ($scope.notaFiscal.declaracaoPrestacaoServico.itens.length === 0) {
                        $scope.mensagemCamposObrigatorios.push("É necessário adicionar ao menos um serviço para a Nota Fiscal.");
                    }
                    if ($scope.notaFiscal.deducoesLegais != $scope.notaFiscal.totalServicos && $scope.notaFiscal.baseCalculo < 0) {
                        $scope.mensagemCamposObrigatorios.push("Os valores totais da base de cálculo deve ser maior ou igual a ZERO.");
                    }
                    if ($scope.notaFiscal.rps && !$scope.notaFiscal.rps.numero) {
                        $scope.mensagemCamposObrigatorios.push("O campo Número do RPS é obrigatório.");
                    }
                    if ($scope.notaFiscal.rps && !$scope.notaFiscal.rps.dataEmissao) {
                        $scope.mensagemCamposObrigatorios.push("O campo Data de Emissão do RPS é obrigatório.");
                    }
                    if ($scope.mensagemCamposObrigatorios.length > 0) {
                        Notificacao.camposObrigatorios($scope.mensagemCamposObrigatorios, "error");
                        return false;
                    } else {
                        return true;
                    }
                };


                $scope.loadEnderecoByCEP = function (cep) {
                    if (cep) {
                        CEP.getByCep({cep: cep}, function (endereco) {
                            if (endereco.municipio) {
                                $scope.notaFiscal.tomador.dadosPessoais.cep = endereco.cep;
                                $scope.notaFiscal.tomador.dadosPessoais.municipio = endereco.municipio;
                                $scope.notaFiscal.tomador.dadosPessoais.logradouro = endereco.logradouro;
                                $scope.notaFiscal.tomador.dadosPessoais.bairro = endereco.bairro;
                            }
                        });
                    }
                };

                $scope.loadPaisByCodigo = function (codigo, donoDoServico) {
                    if (codigo) {
                        Pais.getByCodigo({codigo: codigo}, function (pais) {
                            if (pais.id) {
                                donoDoServico.pais = pais;
                            } else {
                                Notificacao.warn("Atenção", "Não foi encotrado nenhum País com o código " + codigo);
                                donoDoServico.pais = {};
                            }
                        });
                    }
                };

                $scope.searchMunicipio = function (caraQueTemMunicipio) {
                    var modalInstance = $modal.open({
                        templateUrl: 'app/entities/municipio/municipio-search.html',
                        controller: 'MunicipioSearchController',
                        size: 'lg'
                    });
                    modalInstance.result.then(function (municipio) {
                        caraQueTemMunicipio.municipio = municipio;
                        caraQueTemMunicipio.pais = {};
                        $scope.verificarExigibilidadeIss();
                        $scope.verificarBloqueioAliquotaNota();
                        $scope.calculaValoresDaNota(false);
                    }, function () {
                        //$log.info('Modal dismissed at: ' + new Date());
                    });
                };

                $scope.searchPais = function (donoDoServico) {
                    var modalInstance = $modal.open({
                        templateUrl: 'app/entities/pais/pais-search.html',
                        controller: 'PaisSearchController',
                        size: 'lg'
                    });
                    modalInstance.result.then(function (pais) {
                        donoDoServico.pais = pais;
                        donoDoServico.municipio = {};
                        $scope.calculaValoresDaNota(false);
                    }, function () {
                        //$log.info('Modal dismissed at: ' + new Date());
                    });
                };

                $scope.isFisica = function () {
                    return $scope.notaFiscal
                        &&
                        (
                            ($scope.notaFiscal.modalidade == 'SEM_CPF') ||
                            ($scope.notaFiscal.declaracaoPrestacaoServico
                                && $scope.notaFiscal.tomador
                                && $scope.notaFiscal.tomador.dadosPessoais
                                && $scope.notaFiscal.tomador.dadosPessoais.cpfCnpj
                                && $scope.notaFiscal.tomador.dadosPessoais.cpfCnpj.replace(/[^\w\s]/gi, '').length == 11)
                        )
                };

                $scope.mostrarDadosServico = function () {
                    return $scope.notaFiscal &&
                        $scope.notaFiscal.modalidade &&
                        (($scope.notaFiscal.modalidade === 'IDENTIFICADO' && $scope.temCpfOuCnpj() && !$scope.cpfDigitadoIgualPrestador())
                            || $scope.notaFiscal.modalidade !== 'IDENTIFICADO')
                }

                $scope.mostrarDadosGerais = function () {
                    return $scope.notaFiscal &&
                        $scope.notaFiscal.declaracaoPrestacaoServico.itens &&
                        $scope.notaFiscal.declaracaoPrestacaoServico.itens.length > 0;
                }

                $scope.temCpfOuCnpj = function () {
                    return $scope.notaFiscal
                        && $scope.notaFiscal.tomador
                        && $scope.notaFiscal.tomador.dadosPessoais
                        && $scope.notaFiscal.tomador.dadosPessoais.cpfCnpj;
                }

                $scope.cpfDigitadoIgualPrestador = function () {
                    return $scope.temCpfOuCnpj()
                        && $scope.cpfCnpjDoPrestador
                        && $scope.notaFiscal.tomador.dadosPessoais.cpfCnpj.replace(/[^\w\s]/gi, '')
                        === $scope.cpfCnpjDoPrestador.replace(/[^\w\s]/gi, '');
                }

                $scope.temExigibilidade = function () {
                    return $scope.notaFiscal
                        && $scope.notaFiscal.declaracaoPrestacaoServico
                        && $scope.notaFiscal.declaracaoPrestacaoServico.naturezaOperacao
                        && ($scope.notaFiscal.declaracaoPrestacaoServico.naturezaOperacao === "TRIBUTACAO_MUNICIPAL"
                            || $scope.notaFiscal.declaracaoPrestacaoServico.naturezaOperacao === "RETENCAO");
                }

                $scope.exigibilidadeSuspensa = function () {
                    return $scope.notaFiscal
                        && $scope.notaFiscal.declaracaoPrestacaoServico
                        && $scope.notaFiscal.declaracaoPrestacaoServico.naturezaOperacao
                        && ($scope.notaFiscal.declaracaoPrestacaoServico.naturezaOperacao === "EXIGIBILIDADE_SUSPENSA_DECISAO_JUDICIAL"
                            || $scope.notaFiscal.declaracaoPrestacaoServico.naturezaOperacao === "EXIGIBILIDADE_SUSPENSA_PROCESSO_ADMINISTRATIVO");
                };

                $scope.exigibilidadeNaoIncide = function () {
                    return $scope.notaFiscal
                        && $scope.notaFiscal.declaracaoPrestacaoServico
                        && $scope.notaFiscal.declaracaoPrestacaoServico.naturezaOperacao
                        && ($scope.notaFiscal.declaracaoPrestacaoServico.naturezaOperacao === "NAO_INCIDENCIA"
                            || $scope.notaFiscal.declaracaoPrestacaoServico.naturezaOperacao === "ISENCAO"
                            || $scope.notaFiscal.declaracaoPrestacaoServico.naturezaOperacao === "IMUNIDADE"
                            || $scope.notaFiscal.declaracaoPrestacaoServico.naturezaOperacao === "EXPORTACAO");
                };

                $scope.alterarAliquotaSimples = function () {
                    var modalDescriminacaoServico = $modal.open({
                        templateUrl: 'app/entities/notaFiscal/aliquota-simples-anexo-lei-1232006.html',
                        controller: 'AliquotaSimplesAnesoLei1232006Controller',
                        size: 'lg',
                        resolve: {
                            rbt12: function () {
                                return $scope.rbt12;
                            },
                            anexo: function () {
                                return $scope.anexoLei1232006;
                            },
                            faixa: function () {
                                return $scope.anexoLei1232006Faixa;
                            }
                        }
                    });
                    modalDescriminacaoServico.result.then(function (data) {
                        if (data) {
                            $scope.anexoLei1232006 = data;
                            $scope.definirFaixaEAliquotaMinimaSNA();
                        }
                    });
                };

                $scope.adicionarEmail = function (email) {
                    if (!$scope.notaFiscal.emails) {
                        $scope.notaFiscal.emails = "";
                    }
                    if ($scope.notaFiscal.emails.length > 0) {
                        $scope.notaFiscal.emails += ', ';
                    }
                    $scope.notaFiscal.emails += email;
                };

                $scope.handleNumeroRps = function () {
                    NotaFiscal.getUltimoNumeroRps(null, function (data) {
                        var ultimoNumero = data.value;
                        if (!$scope.notaFiscal.rps.numero) {
                            $scope.notaFiscal.rps.numero = ultimoNumero + 1;
                        }
                        console.log($scope.notaFiscal.rps.numero);
                        console.log(ultimoNumero + 1);
                        if ($scope.notaFiscal.rps.numero > ultimoNumero + 1) {
                            Notificacao.warn('Atenção!', 'Com o número informado, você estará pulando a sequência de RPS.');
                        }
                    });
                };

                $scope.handleDataEmissaoRps = function () {
                    $scope.notaFiscal.declaracaoPrestacaoServico.competencia = $scope.notaFiscal.rps.dataEmissao;
                    console.log($scope.notaFiscal.declaracaoPrestacaoServico.competencia);
                };

                $scope.buscarObra = function () {
                    if ($scope.notaFiscal.declaracaoPrestacaoServico.construcaoCivil.art) {
                        ConstrucaoCivil.buscarConstrucaoCivilByArt({art: $scope.notaFiscal.declaracaoPrestacaoServico.construcaoCivil.art}, function (data) {
                            if (data.id) {
                                $scope.notaFiscal.declaracaoPrestacaoServico.construcaoCivil = data;
                                $scope.selecionouObra();
                            } else {
                                $scope.notaFiscal.declaracaoPrestacaoServico.construcaoCivil.id = null;
                                $scope.notaFiscal.declaracaoPrestacaoServico.construcaoCivil.incorporacao = false;
                                $scope.selecionouObra();
                                angular.element('[id="field_codigo_obra"]').focus();
                            }
                        });
                    }
                };

                $scope.tributadoEmOutroMunicipio = function () {
                    return $scope.notaFiscal &&
                        $scope.notaFiscal.declaracaoPrestacaoServico &&
                        $scope.notaFiscal.declaracaoPrestacaoServico.naturezaOperacao === "TRIBUTACAO_FORA_MUNICIPIO";

                };

                $scope.temHora = function (data) {
                    return DateUtils.temHora(data);
                };

                $scope.changeModalidade = function (modalidade) {
                    $scope.notaFiscal.modalidade = modalidade;
                    $scope.tipoPessoa = "JURIDICA";
                    $scope.notaFiscal.tomador = {dadosPessoais: {}};
                    if (modalidade == "NAO_IDENTIFICADO") {
                        $scope.notaFiscal.tomador = {};
                        $scope.proximoPasso();
                        $scope.verificarExigibilidadeIss()
                    }
                }

                $scope.changeTipoPessoa = function () {
                    $scope.notaFiscal.tomador = {dadosPessoais: {}};
                    $scope.tipoPessoa = $scope.tipoPessoa == "JURIDICA" ? "FISICA" : "JURIDICA";
                }
            }])
    ;
})();
