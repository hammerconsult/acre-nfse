(function () {
'use strict';

angular.module('nfseApp')
    .controller('RegistrarEmpresaController',
        function ($scope, $http, $translate, $timeout, Auth, Notificacao, $state, convite, CEP, Servico,
                  $modal, Municipio, Pessoa, PrestadorServicos, Upload, pagingParams, Principal, ClassificacaoAtividade,
                  NaturezaJuridica) {

            $scope.error = null;
            $scope.success = 'OK';
            $scope.prestadorServicos = {
                pessoa: {dadosPessoais: {cpfCnpj: pagingParams.cpfCnpj}},
                criarUsuario: pagingParams.criarUsuario
            };
            console.log('prestador..', $scope.prestadorServicos);
            $scope.socio = {};
            $scope.servicos = [];

            $timeout(function () {
                angular.element('[ng-model="prestadorServicos.pessoa.dadosPessoais.cpfCnpj"]').focus();
            });

            $scope.buscarEnums = function () {
                $scope.tipoPortes = getEnumValues('tipoPorte');
                $scope.tipoContribuintes = getEnumValues('tipoContribuinte');
                $scope.regimeTributarios = getEnumValues('regimeTributario');
                $scope.tiposIss = getEnumValues('tipoIssqn');
                $scope.tiposIssEstimado = getEnumValues('tipoPeriodoValorEstimado');
                $scope.classificacaoAtividades = ClassificacaoAtividade.query();
                $scope.tipoNotaFiscais = getEnumValues('tipoNotaFiscal');
                $scope.tipoEnquadramentos = getEnumValues('tipoEnquadramento');
            };

            function getEnumValues(name) {
                var enuns = [];
                $http.get('i18n/pt-br/' + name + '.json').success(function (data) {
                    angular.forEach(Object.keys(data), function (primeiroField) {
                        angular.forEach(Object.keys(data[primeiroField]), function (sugundoField) {
                            angular.forEach(Object.keys(data[primeiroField][sugundoField]), function (field) {
                                enuns.push({name: field, value: data[primeiroField][sugundoField][field]})
                            });

                        });
                    });


                });
                return enuns;
            }

            $scope.loadAll = function () {
                $scope.buscarEnums();
            };
            $scope.loadAll();

            $scope.loadEnderecoByCEP = function (cep) {
                if (cep) {
                    CEP.getByCep({cep: cep}, function (endereco) {
                        $scope.prestadorServicos.pessoa.dadosPessoais.cep = endereco.cep;
                        $scope.prestadorServicos.pessoa.dadosPessoais.municipio = endereco.municipio;
                        $scope.prestadorServicos.pessoa.dadosPessoais.logradouro = endereco.logradouro;
                        $scope.prestadorServicos.pessoa.dadosPessoais.bairro = endereco.bairro;
                    });
                }
            };

            $scope.popularReceitasTributariaBruta = function () {
                $scope.prestadorServicos.receitasTributariaBruta = [];
                var hoje = new Date();

                for (var i = 1; i <= 12; i++) {
                    var receitaTributariaBruta = {};
                    hoje.setMonth(hoje.getMonth() - 1);

                    receitaTributariaBruta.ano = hoje.getFullYear();
                    receitaTributariaBruta.mes = hoje.getMonth() + 1;
                    receitaTributariaBruta.valorBruto = 0.0;

                    $scope.prestadorServicos.receitasTributariaBruta.push(receitaTributariaBruta);
                }
            };

            $scope.popularReceitasTributariaBruta();

            $scope.loadMunicipioByCodigo = function (codigo) {
                if (codigo) {
                    Municipio.getByCodigo({codigo: codigo}, function (municipio) {
                        if (municipio.id) {
                            $scope.prestadorServicos.pessoa.dadosPessoais.municipio = municipio;
                        } else {
                            Notificacao.warn("Atenção", "Não foi encotrado nenhum Municipio com o código " + codigo);
                            $scope.prestadorServicos.pessoa.dadosPessoais.municipio = {};
                        }
                    });
                }
            };

            $scope.searchMunicipio = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'app/entities/municipio/municipio-search.html',
                    controller: 'MunicipioSearchController',
                    size: 'lg'
                });
                modalInstance.result.then(function (municipio) {
                    $scope.prestadorServicos.pessoa.dadosPessoais.endereco.municipio = municipio;
                }, function () {
                    //$log.info('Modal dismissed at: ' + new Date());
                });
            };

            $scope.searchCnae = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'app/entities/cnae/cnaes-search.html',
                    controller: 'CnaeSearchController',
                    size: 'lg'
                });
                modalInstance.result.then(function (cnae) {
                    if (!$scope.prestadorServicos.cnaes) {
                        $scope.prestadorServicos.cnaes = [];
                    }
                    $scope.prestadorServicos.cnaes.push(cnae);
                }, function () {
                    // $log.info('Modal dismissed at: ' + new Date());
                });
            };

            $scope.searchServico = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'app/entities/servico/servico-search.html',
                    controller: 'ServicoSearchController',
                    size: 'lg'
                });
                modalInstance.result.then(function (servico) {
                    if (!$scope.prestadorServicos.servicos) {
                        $scope.prestadorServicos.servicos = [];
                    }
                    $scope.prestadorServicos.servicos.push(servico);
                }, function () {
                    // $log.info('Modal dismissed at: ' + new Date());
                });
            };

            $scope.removerCnae = function (index) {
                $scope.prestadorServicos.cnaes.splice(index, 1);
                $scope.prestadorServicos.servicos = [];
                angular.forEach($scope.prestadorServicos.cnaes, function (cnae) {
                    $scope.buscarServicosPorCnae(cnae);
                })
            };

            $scope.removerServico = function (index) {
                $scope.prestadorServicos.servicos.splice(index, 1);
            };

            $scope.loadByCpfCnpj = function () {
                PrestadorServicos.getPorCpfCnpj({cpfCnpj: $scope.prestadorServicos.pessoa.dadosPessoais.cpfCnpj},
                    function (result) {
                        $scope.prestadorServicos = {};
                        Notificacao.error("Atenção", "A empresa que você está solicitando acesso já existe como cadastro municipal.");
                    }, function (error) {
                        if (error.status === 404) {
                            console.log(error);
                        }
                    });

                if ($scope.isFisica()) {
                    $scope.naturezasJuridicas = NaturezaJuridica.getFisicas();
                } else {
                    $scope.naturezasJuridicas = NaturezaJuridica.getJuridicas();
                }
            };

            $scope.isFisica = function () {
                return $scope.prestadorServicos
                    && $scope.prestadorServicos.pessoa
                    && $scope.prestadorServicos.pessoa.dadosPessoais
                    && $scope.prestadorServicos.pessoa.dadosPessoais.cpfCnpj
                    && $scope.prestadorServicos.pessoa.dadosPessoais.cpfCnpj.replace(/[^\w\s]/gi, '').length == 11

            };

            $scope.adicionarSocio = function () {
                if (!$scope.prestadorServicos.socios) {
                    $scope.prestadorServicos.socios = [];
                }
                var validou = true;
                console.log($scope.socio);
                if (!$scope.socio.socio.dadosPessoais.cpfCnpj) {
                    Notificacao.error("Atenção!", "O Campo Cpf/Cnpj do sócio é obrigatório.");
                    validou = false;
                }
                if (!$scope.socio.socio.dadosPessoais.nomeRazaoSocial) {
                    Notificacao.error("Atenção!", "O Campo Nome/Razão social do sócio é obrigatório.");
                    validou = false;
                }
                if (!$scope.socio.proporcao) {
                    Notificacao.error("Atenção!", "O Campo percentual do sócio é obrigatório.");
                    validou = false;
                }
                if (validou) {
                    $scope.prestadorServicos.socios.push($scope.socio);
                    $scope.socio = {};
                }
            };

            $scope.removerSocio = function (index) {
                $scope.prestadorServicos.socios.splice(index, 1);
            };

            function validouCriacaoUsuario() {
                if (!$scope.prestadorServicos.password) {
                    Notificacao.warn('Atenção!', 'A senha deve ser informada.');
                    return false;
                }
                if (!$scope.prestadorServicos.confirmPassword) {
                    Notificacao.warn('Atenção!', 'A confirmação da senha deve ser informada.');
                    return false;
                }
                if ($scope.prestadorServicos.password != $scope.prestadorServicos.confirmPassword) {
                    Notificacao.warn('Atenção!', 'A senha de confirmação não confere com a senha.');
                    return false;
                }
                return true;
            }


            $scope.register = function () {
                if ($scope.prestadorServicos.criarUsuario) {
                    if (validouCriacaoUsuario()) {
                        PrestadorServicos.salvar($scope.prestadorServicos, onSucessWithUsuario);
                    }
                } else {
                    PrestadorServicos.salvar($scope.prestadorServicos, onSucess);
                }
            };

            function onSucessWithUsuario(data) {
                if (data.id) {
                    Notificacao.info("Operação Realizada", "Verifique seu e-mail para validar seu acesso. Em relação ao cadastro municipal, " +
                        "sua solicitação foi encaminhada para um fiscal e assim que aprovada você receberá um email com as instruções de como prosseguir.");
                    $state.go("login");
                } else {
                    Notificacao.error("Algo inesperado aconteceu!", "Entre em contato com o suporte da prefeitura.");
                }

            }

            function onSucess(data) {
                if (data.id) {
                    Notificacao.info("Operação Realizada", "Sua solicitação foi encaminhada para um fiscal e assim que " +
                        "aprovada você receberá um email com as instruções de como prosseguir");
                    Principal.identity(true);
                    $scope.$emit('nfseApp:navbarUpdate', data);
                    $state.go("home", {}, {reload: true});
                } else {
                    Notificacao.error("Algo inesperado aconteceu!", "Entre em contato com o suporte da prefeitura.");
                }
            }

            $scope.salvarPrestador = function () {

            };

            $scope.upload = function ($file) {
                console.log('arquivo ' + $file);
                if ($file) {
                    Upload.dataUrl($file, true).then(
                        function (conteudo) {
                            var anexo = {};
                            anexo.descricao = $file.name;
                            anexo.nome = $file.name;
                            anexo.conteudo = conteudo;
                            anexo.mimeType = $file.type;
                            $scope.prestadorServicos.imagem = anexo;
                        }
                    );
                } else {
                    Alerta.warn("Atenção!", "Você deve selecionar um arquivo de até 10Mb");
                }
            }

        });
})();