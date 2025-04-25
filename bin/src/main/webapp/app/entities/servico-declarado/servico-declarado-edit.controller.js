(function () {
    'use strict';

    angular.module('nfseApp').controller('ServicoDeclaradoEditController',
        ['$rootScope', '$scope', '$modal', '$state', '$filter', '$stateParams', 'entity', 'ServicoDeclarado',
            'localStorageService', 'Notificacao',
            'Tomador', 'Servico', 'Municipio', 'CEP', 'Pais', 'Configuracao', 'PrestadorServicos', 'DateUtils',
            function ($rootScope, $scope, $modal, $state, $filter, $stateParams, entity, ServicoDeclarado,
                      localStorageService, Notificacao,
                      Tomador, Servico, Municipio, CEP, Pais, Configuracao, PrestadorServicos) {

                $scope.mensagemCamposObrigatorios = [];
                $scope.itensDeclaracaoServico = [];
                $scope.aliquotas = [0.00, 2.00, 2.79, 3.00, 3.50, 3.84, 3.87, 4.23, 4.26, 4.31, 4.61, 4.65, 5.00];
                $scope.permiteExigibilidade = true;
                $scope.bloqueiaAliquota = false;
                $scope.mostraFormServicos = true;
                $scope.tiposServicosDeclarados = ServicoDeclarado.getAllTiposDocumentosServicoDeclarado();
                $scope.codigoServico = "";
                $scope.emissaoPopUp = false;

                $scope.openDateDialog = function ($event) {
                    $event.preventDefault();
                    $event.stopPropagation();
                    $scope.emissaoPopUp = true;
                };
                $scope.clearServico = function () {
                    $scope.servico = {
                        baseCalculo: 0.0,
                        valorServico: 0.0,
                        quantidade: 1,
                        iss: 0.0,
                        descontosCondicionados: 0.0,
                        descontosIncondicionados: 0.0,
                        deducoes: 0.0,
                        prestadoNoPais: true,
                        municipio: $scope.municipio
                    };

                };

                $scope.calculaValoresDaNota = function () {
                    $scope.servicoDeclarado.declaracaoPrestacaoServico.totalServicos = 0.0;
                    $scope.servicoDeclarado.declaracaoPrestacaoServico.baseCalculo = 0.0;
                    $scope.servicoDeclarado.declaracaoPrestacaoServico.issCalculado = 0.0;
                    $scope.servicoDeclarado.declaracaoPrestacaoServico.deducoesLegais = 0.0;
                    $scope.servicoDeclarado.declaracaoPrestacaoServico.valorLiquido = 0.0;
                    if ($scope.servicoDeclarado.declaracaoPrestacaoServico.itens) {
                        angular.forEach($scope.servicoDeclarado.declaracaoPrestacaoServico.itens, function (servico) {
                            $scope.calculaValoresServico(servico);
                            $scope.servicoDeclarado.declaracaoPrestacaoServico.totalServicos =
                                Number($scope.servicoDeclarado.declaracaoPrestacaoServico.totalServicos) + Number(servico.total);
                            $scope.servicoDeclarado.declaracaoPrestacaoServico.baseCalculo =
                                Number($scope.servicoDeclarado.declaracaoPrestacaoServico.baseCalculo) + Number(servico.baseCalculo);
                            $scope.servicoDeclarado.declaracaoPrestacaoServico.issCalculado =
                                Number($scope.servicoDeclarado.declaracaoPrestacaoServico.issCalculado) + Number(servico.iss);
                            $scope.servicoDeclarado.declaracaoPrestacaoServico.deducoes =
                                Number($scope.servicoDeclarado.declaracaoPrestacaoServico.deducoesLegais) + Number(servico.deducoes);
                        });
                        if ($scope.servicoDeclarado.declaracaoPrestacaoServico.issRetido) {
                            $scope.servicoDeclarado.declaracaoPrestacaoServico.valorLiquido =
                                $scope.servicoDeclarado.declaracaoPrestacaoServico.totalServicos
                                - $scope.servicoDeclarado.declaracaoPrestacaoServico.issCalculado
                                - $scope.servicoDeclarado.declaracaoPrestacaoServico.deducoesLegais;
                            $scope.servicoDeclarado.declaracaoPrestacaoServico.issPagar =
                                $scope.servicoDeclarado.declaracaoPrestacaoServico.issCalculado;
                        } else {
                            $scope.servicoDeclarado.declaracaoPrestacaoServico.valorLiquido =
                                $scope.servicoDeclarado.declaracaoPrestacaoServico.totalServicos
                                - $scope.servicoDeclarado.declaracaoPrestacaoServico.deducoesLegais;
                        }
                    }
                };

                $scope.seleciouTipoServicoDeclarado = function (tipoServicoDeclarado) {
                    if (!$scope.servicoDeclarado.tipoServicoDeclarado) {
                        ServicoDeclarado.new({tipoServicoDeclarado: tipoServicoDeclarado}, function (data) {
                            $scope.servicoDeclarado = data;
                            definirServicoPadrao();
                        });
                    } else {
                        Notificacao.confirm('Atenção!', 'Tem certeza que deseja alterar o tipo de serviço, os dados digitados serão apagados!', 'warning',
                            function () {
                                ServicoDeclarado.new({tipoServicoDeclarado: tipoServicoDeclarado}, function (data) {
                                    $scope.servicoDeclarado = data;
                                    definirServicoPadrao();

                                });
                            })
                    }
                };

                $scope.verificarTiposServicoPossiveis = function () {
                    if (!$scope.configuracao.permiteServicoDeclaradoPrestado) {
                        $scope.seleciouTipoServicoDeclarado('SERVICO_TOMADO');
                    }
                };

                Configuracao.get({}, function (configuracao) {
                    $scope.municipio = configuracao.municipio;
                    $scope.configuracao = configuracao;
                    PrestadorServicos.get({id: localStorageService.get("prestadorPrincipal").prestador.id}, function (data) {
                        $scope.tomador = data;
                        $scope.cpfCnpjDoTomador = $scope.tomador.pessoa.dadosPessoais.cpfCnpj;

                        if (entity.$promise) {
                            entity.$promise.then(function (nota) {
                                $scope.servicoDeclarado = nota;
                                $scope.calculaValoresDaNota();
                            });
                        } else {
                            $scope.servicoDeclarado = entity;
                            $scope.calculaValoresDaNota();
                        }
                        $scope.clearServico();
                        $scope.verificarTiposServicoPossiveis();
                    });
                });


                $scope.$watch('editForm.cpfCnpj.$valid', function (newVal) {
                    $scope.preLoadPrestador();
                });

                $scope.preLoadPrestador = function () {
                    if ($scope.temCpfOuCnpj()) {
                        var cpfCnpj = $scope.servicoDeclarado.dadosPessoais.cpfCnpj;
                        $scope.servicoDeclarado.dadosPessoais = {};
                        $scope.servicoDeclarado.dadosPessoais.cpfCnpj = cpfCnpj;
                        if ($scope.cpfDigitadoIgualTomador()) {
                            Notificacao.error("Atenção", "Não é permitido que o Prestador de Serviços seja o próprio Tomador de Serviços.");
                        } else {
                            Tomador.getInPessoRepoByCpfCnpj({cpfCnpj: cpfCnpj}, function (result) {
                                if (result.dadosPessoais && result.dadosPessoais.cpfCnpj) {
                                    result.prestador = localStorageService.get("prestadorPrincipal").prestador;
                                    $scope.servicoDeclarado.dadosPessoais = result.dadosPessoais;
                                    verificaSeEhEmitenteNota();
                                }
                            });
                        }
                    }
                };

                function verificaSeEhEmitenteNota() {
                    if ($scope.servicoDeclarado.dadosPessoais.cpfCnpj) {
                        PrestadorServicos.getPorCpfCnpj({cpfCnpj: $scope.servicoDeclarado.dadosPessoais.cpfCnpj}, function (data) {
                            if (data.pessoa
                                && data.situacao == "ATIVO"
                                && data.pessoa.dadosPessoais.municipio.nome.trim().toLowerCase() == $scope.configuracao.municipio.nome.trim().toLowerCase()
                                && data.pessoa.dadosPessoais.municipio.estado.trim().toLowerCase() == $scope.configuracao.municipio.estado.trim().toLowerCase()) {
                                Notificacao.error("Declaração não permitida", "A Empresa " + data.pessoa.dadosPessoais.nomeRazaoSocial + " é do Município de Rio Branco, portanto deve emitir Nota Fiscal Eletrônica. ");
                                $scope.servicoDeclarado.dadosPessoais = {};
                            }
                        });
                    }

                }


                $scope.searchPrestadorServico = function () {
                    var modalInstance = $modal.open({
                        templateUrl: 'app/entities/tomador/tomador-search.html',
                        controller: 'TomadorSearchController',
                        size: 'lg'
                    });
                    modalInstance.result.then(function (tomador) {

                        Tomador.get({id: tomador.id}, function (data) {
                            if (data && data.dadosPessoais) {
                                $scope.servicoDeclarado.dadosPessoais = {};
                                $scope.servicoDeclarado.dadosPessoais = data.dadosPessoais;
                                $scope.servicoDeclarado.dadosPessoais.id = null;
                                if ($scope.servicoDeclarado.dadosPessoais.endereco) {
                                    $scope.servicoDeclarado.dadosPessoais.endereco.id = null;
                                }
                            }
                        });


                    }, function () {
                        //$log.info('Modal dismissed at: ' + new Date());
                    });
                };

                $scope.saveAndFinalize = function () {
                    if ($scope.servicoDeclarado.tipoServicoDeclarado == 'SERVICO_PRESTADO') {
                        $scope.servicoDeclarado.declaracaoPrestacaoServico.dadosPessoaisTomador = $scope.servicoDeclarado.dadosPessoais;
                    } else {
                        $scope.servicoDeclarado.declaracaoPrestacaoServico.dadosPessoaisPrestador = $scope.servicoDeclarado.dadosPessoais;
                    }
                    $scope.save();
                };

                $scope.save = function () {
                    ServicoDeclarado.save($scope.servicoDeclarado, function () {
                        Notificacao.info("Registro salvo com sucesso!");
                        $state.go("servicoDeclarado");
                    });
                };

                $scope.openCompetencia = function ($event) {
                    $scope.status.openedCompetencia = true;
                };

                $scope.status = {openedCompetencia: false};

                $scope.clear = function () {
                };

                $scope.validarValorDeducao = function (servico) {
                    if (servico.deducoes && servico.deducoes > servico.valorServico) {
                        servico.deducoes = 0.00;
                        Notificacao.warn("Atenção!", "O Valor de Dedução nao pode ser maior que o Valor do Serviço.");
                        return;
                    }
                    if (servico.deducoes && !servico.servico.percentualDeducao) {
                        servico.deducoes = 0.00;
                        Notificacao.warn("Atenção!", "Esse serviços não permite Dedução");
                        return;
                    }
                    if (servico.deducoes && servico.deducoes > 0 && servico.valorServico && servico.valorServico > 0) {
                        var percentualDeducaoAplicado = servico.deducoes / (servico.valorServico / 100);
                        percentualDeducaoAplicado = parseFloat(percentualDeducaoAplicado).toFixed(2);
                        console.log(percentualDeducaoAplicado);
                        if (percentualDeducaoAplicado > servico.servico.percentualDeducao) {
                            $scope.servico.deducoes = 0.00;
                            Notificacao.warn("Atenção!", "O Valor de Dedução é superior ao máximo permitido (" + servico.servico.percentualDeducao + "%)");
                            return;
                        }
                    }
                };

                $scope.validarAliquota = function (servico) {
                    if ($scope.servicoDeclarado.tipoServicoDeclarado === 'SERVICO_TOMADO'
                        && (servico.aliquotaServico > 5 || servico.aliquotaServico < 2)) {
                        servico.aliquotaServico = 2;
                        Notificacao.warn("Atenção", "A Alíquota do ISS deve estar entre 2% e 5%.");
                    } else if (!servico.aliquotaServico || servico.aliquotaServico <= 0) {
                        Notificacao.warn("Atenção", "Informe a Alíquota do ISS.");
                    }
                };

                $scope.calculaValoresServico = function (servico) {

                    $scope.validarAliquota(servico);
                    $scope.validarValorDeducao(servico);

                    servico.total = servico.valorServico * servico.quantidade;
                    servico.baseCalculo = servico.total;

                    if (servico.deducoes && servico.deducoes > 0) {
                        servico.baseCalculo = servico.baseCalculo - servico.deducoes;
                    }
                    servico.iss = servico.baseCalculo * (servico.aliquotaServico / 100);
                };

                $scope.validarAddServico = function () {
                    $scope.mensagemCamposObrigatorios = [];

                    if (!$scope.servicoDeclarado.declaracaoPrestacaoServico.naturezaOperacao) {
                        $scope.mensagemCamposObrigatorios.push("A Natureza da Operação é obrigatória, informe para continuar.");
                    }

                    if ($scope.servicoDeclarado.tipoServicoDeclarado === 'SERVICO_TOMADO' && !$scope.servico.nomeServico) {
                        $scope.mensagemCamposObrigatorios.push("O Serviço é obrigatório, informe o serviço prestado para continuar.");
                    }
                    if (!$scope.servico.descricao) {
                        $scope.mensagemCamposObrigatorios.push("O campo descrição do serviço é obrigatório.");
                    }
                    if ($scope.servico.deducoes > $scope.servico.total) {
                        $scope.mensagemCamposObrigatorios.push("Os valores de desconto não podem ser maiores que o valor total dos serviços.");
                    }
                    if (!$scope.servico.valorServico || $scope.servico.valorServico === 0) {
                        $scope.mensagemCamposObrigatorios.push("O campo Valor do Serviço é obrigatório e deve ser maior que zero.");
                    }
                    if ($scope.mensagemCamposObrigatorios.length > 0) {
                        Notificacao.camposObrigatorios($scope.mensagemCamposObrigatorios, "warning");
                        return false;
                    }
                    return true;
                };


                $scope.continuarServico = function () {
                    if ($scope.mostraFormServicos) {
                        $scope.addServico(true);
                    } else {
                        $scope.calculaValoresDaNota();
                    }
                };

                $scope.novoServico = function () {
                    if ($scope.mostraFormServicos) {
                        $scope.addServico(false);
                    } else {
                        $scope.mostraFormServicos = true
                    }
                };

                $scope.addServico = function (continua) {
                    if ($scope.validarAddServico()) {
                        if (!$scope.servicoDeclarado.declaracaoPrestacaoServico.itens) {
                            $scope.servicoDeclarado.declaracaoPrestacaoServico.itens = [];
                        }
                        $scope.servicoDeclarado.declaracaoPrestacaoServico.itens.push($scope.servico);
                        $scope.clearServico();
                        $scope.calculaValoresDaNota();
                        if (continua) {
                            $scope.mostrarValoresFinais = !$scope.mostrarValoresFinais;
                        }
                    }

                };


                $scope.removeServico = function (index) {
                    $scope.servicoDeclarado.declaracaoPrestacaoServico.itens.splice(index, 1);
                };

                $scope.editaServico = function (index) {
                    $scope.servico = $scope.servicoDeclarado.declaracaoPrestacaoServico.itens[index];
                    $scope.removeServico(index);
                };


                $scope.selecionouServico = function () {
                    $scope.bloqueiaAliquota = false;
                    if ($scope.servico && $scope.servico.servico) {
                        $scope.servico.nomeServico = $scope.servico.servico.descricao;
                        if (($scope.temExigibilidade() || $scope.exigibilidadeSuspensa()) && $scope.tomador.enquadramentoFiscal.tipoIss === "MENSAL") {
                            $scope.bloqueiaAliquota = true;
                            $scope.servico.aliquotaServico = $scope.servico.servico.aliquota;
                            $scope.calculaValoresServico($scope.servico);
                            return;
                        }
                        if ($scope.exigibilidadeNaoIncide()
                            || $scope.tomador.enquadramentoFiscal.tipoIss === "MEI"
                            || (($scope.temExigibilidade() || $scope.exigibilidadeSuspensa())
                                && ($scope.tomador.enquadramentoFiscal.tipoIss === "FIXO" || $scope.tomador.enquadramentoFiscal.tipoIss === "ESTIMADO"))) {
                            $scope.bloqueiaAliquota = true;
                            $scope.servico.aliquotaServico = $scope.aliquotas[0];
                            $scope.calculaValoresServico($scope.servico);
                            return;
                        }
                        if ($scope.servicoDeclarado.declaracaoPrestacaoServico.naturezaOperacao === 'RETENCAO') {
                            $scope.bloqueiaAliquota = true;
                            $scope.servico.aliquotaServico = $scope.servico.servico.aliquota;
                        }
                        if ($scope.aliquotas.indexOf($scope.servico.servico.aliquota) >= 0) {
                            $scope.servico.aliquotaServico = $scope.servico.servico.aliquota;
                            $scope.calculaValoresServico($scope.servico);
                        }
                    }
                };

                $scope.searchServico = function () {
                    var modalInstance = $modal.open({
                        templateUrl: 'app/entities/servico/servico-search.html',
                        controller: 'ServicoSearchController',
                        size: 'lg'
                    });
                    modalInstance.result.then(function (result) {
                        $scope.servico.servico = result;
                        $scope.codigoServico = $scope.servico.servico.codigo;
                        $scope.selecionouServico();
                    }, function () {
                        //$log.info('Modal dismissed at: ' + new Date());
                    });
                };

                $scope.loadEnderecoByCEP = function (cep) {
                    if (cep) {
                        CEP.getByCep({cep: cep}, function (endereco) {
                            if (endereco.municipio) {
                                $scope.servicoDeclarado.dadosPessoais.cep = endereco.cep;
                                $scope.servicoDeclarado.dadosPessoais.municipio = endereco.municipio;
                                $scope.servicoDeclarado.dadosPessoais.logradouro = endereco.logradouro;
                                $scope.servicoDeclarado.dadosPessoais.bairro = endereco.bairro;
                            }
                        });
                    }
                };

                $scope.loadPaisByCodigo = function (codigo, donoDoServico) {
                    if (codigo) {
                        Pais.getByCodigo({codigo: codigo}, function (pais) {
                            if (pais.id) {
                                donoDoServico.pais = pais;
                                donoDoServico.municipio = {};
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
                    }, function () {
                        //$log.info('Modal dismissed at: ' + new Date());
                    });
                };

                $scope.temCpfOuCnpj = function () {
                    return $scope.servicoDeclarado
                        && $scope.servicoDeclarado.dadosPessoais
                        && $scope.servicoDeclarado.dadosPessoais.cpfCnpj;
                };

                $scope.cpfDigitadoIgualTomador = function () {
                    return $scope.temCpfOuCnpj()
                        && $scope.cpfCnpjDoTomador
                        && $scope.servicoDeclarado.dadosPessoais.cpfCnpj.replace(/[^\w\s]/gi, '')
                        === $scope.cpfCnpjDoTomador.replace(/[^\w\s]/gi, '');
                }

                $scope.temExigibilidade = function () {
                    return $scope.notaFiscal
                        && $scope.servicoDeclarado
                        && $scope.servicoDeclarado.declaracaoPrestacaoServico
                        && $scope.servicoDeclarado.declaracaoPrestacaoServico.naturezaOperacao
                        && $scope.servicoDeclarado.declaracaoPrestacaoServico.naturezaOperacao === "TRIBUTACAO_MUNICIPAL";
                }

                $scope.exigibilidadeSuspensa = function () {
                    return $scope.notaFiscal
                        && $scope.servicoDeclarado
                        && $scope.servicoDeclarado.declaracaoPrestacaoServico
                        && $scope.servicoDeclarado.declaracaoPrestacaoServico.naturezaOperacao
                        && ($scope.servicoDeclarado.declaracaoPrestacaoServico.naturezaOperacao === "EXIGIBILIDADE_SUSPENSA_DECISAO_JUDICIAL"
                            || $scope.servicoDeclarado.declaracaoPrestacaoServico.naturezaOperacao === "EXIGIBILIDADE_SUSPENSA_PROCESSO_ADMINISTRATIVO");
                };

                $scope.exigibilidadeNaoIncide = function () {
                    return $scope.notaFiscal
                        && $scope.servicoDeclarado
                        && $scope.servicoDeclarado.declaracaoPrestacaoServico
                        && $scope.servicoDeclarado.declaracaoPrestacaoServico.naturezaOperacao
                        && ($scope.servicoDeclarado.declaracaoPrestacaoServico.naturezaOperacao === "NAO_INCIDENCIA"
                            || $scope.servicoDeclarado.declaracaoPrestacaoServico.naturezaOperacao === "ISENCAO"
                            || $scope.servicoDeclarado.declaracaoPrestacaoServico.naturezaOperacao === "IMUNIDADE"
                            || $scope.servicoDeclarado.declaracaoPrestacaoServico.naturezaOperacao === "EXPORTACAO");
                };

                $scope.isFisica = function () {
                    return $scope.servicoDeclarado
                        && $scope.servicoDeclarado.dadosPessoais
                        && $scope.servicoDeclarado.dadosPessoais.cpfCnpj
                        && $scope.servicoDeclarado.dadosPessoais.cpfCnpj.replace(/[^\w\s]/gi, '').length == 11

                };


                function definirServicoPadrao() {
                    if ($scope.servicoDeclarado.tipoServicoDeclarado === 'SERVICO_PRESTADO' && $scope.configuracao.padraoServicoPrestado) {
                        $scope.servico.servico = $scope.configuracao.padraoServicoPrestado;
                        $scope.codigoServico = $scope.servico.servico.codigo;
                        $scope.selecionouServico();
                    }
                }

                $scope.selecionarServicoPorCodigo = function (codigoServico) {
                    Servico.getPorCodigo({codigo: codigoServico.replace(/^0+/, '')}, function (data) {
                        $scope.servico.servico = data;
                        $scope.selecionouServico();
                    });
                };
            }]);
})();
