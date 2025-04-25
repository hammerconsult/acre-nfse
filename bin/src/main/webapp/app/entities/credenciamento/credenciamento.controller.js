(function () {
    'use strict';

angular.module('nfseApp')
    .controller('CredenciamentoController', function ($scope, $modal, $cacheFactory, $rootScope, localStorageService,
                                                      Notificacao, Principal, $timeout,
                                                      Credenciamento, ParseLinks, $state, PrestadorServicos,
                                                      Pessoa, Municipio, CEP, User, pagingParams) {

        $scope.error = null;
        $scope.success = 'OK';
        $scope.criarUsuario = pagingParams.criarUsuario;
        $scope.confirmPassword;

        $timeout(function () {
            angular.element('[ng-model="prestadorServicos.pessoa.dadosPessoais.cpfCnpj"]').focus();
        });


        var onSucessVincularUsuario = function (result) {
            Principal.identity(true);
            $scope.$emit('nfseApp:navbarUpdate', result);
            $state.go("home", {}, {reload: true});
            Notificacao.info("Operação Realizada", "Sua solicitação foi encaminhada para um fiscal e assim que aprovada você receberá um email com as instruções de como prosseguir");
        };

        var onSucessCriarUsuario = function (result) {
            Notificacao.priority('Cadastro salvo com sucesso! Favor verificar seu e-mail para confirmar a conta.');
            $state.go('login');
        };


        function validarCriacaoUsuario() {
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

        $scope.save = function () {
            if ($scope.criarUsuario) {
                if (validarCriacaoUsuario()) {
                    PrestadorServicos.criarUsuarioParaPrestador($scope.prestadorServicos, onSucessCriarUsuario);
                }
            } else {
                PrestadorServicos.vincularUsuarioEmpresa($scope.prestadorServicos, onSucessVincularUsuario);
            }
        };

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

            });
        };

        $scope.loadByCpfCnpj = function () {
            if ($scope.cnpj) {
                if ($scope.criarUsuario) {
                    User.fromLogin({login: $scope.cnpj}, function (data) {
                        console.log(data);
                        if (data.id) {
                            Notificacao.warn("Atenção", "O Cnpj " + $scope.cnpj + " já possui usuário registrado no sistema. Caso tenha esquecido sua senha, utilize a opção 'Esqueceu a senha?'.");
                            $state.go("home");
                        } else {
                            $scope.recuperarPrestadorServicos();
                        }
                    });
                } else {
                    $scope.recuperarPrestadorServicos();
                }
            }
        };

        $scope.recuperarPrestadorServicos = function () {
            PrestadorServicos.getPorCpfCnpj({cpfCnpj: $scope.cnpj},
                function (result) {
                    // if ($scope.criarUsuario) {
                    //     $state.go("login");
                    //     Notificacao.info("Atenção", "A empresa que você está solicitando acesso já existe como cadastro municipal. Procure o município para regularizar o cadastro.");
                    // } else {
                    $scope.editForm.cpfCnpj.$setValidity("exists", true);
                    $scope.prestadorServicos = result;
                    Notificacao.info("Atenção", "A empresa que você está solicitando acesso já existe como cadastro municipal. Ao continuar sua solicitação sará encaminhada para um fiscal e assim que aprovada você receberá um email com a confirmação");
                    // }
                }, function (error) {
                    if (error.status === 404) {
                        // $state.go("registrarEmpresa", {cpfCnpj: $scope.cnpj, criarUsuario: $scope.criarUsuario});
                        Notificacao.warn("Atenção",
                            "A empresa que você está solicitando ainda não existe como cadastro municipal. Compareça a uma unidade de atendimento mais próxima para realizar o cadastro e obter acesso aos serviços on-line");
                    }

                });
        }

        $scope.irParaCredenciamento = function () {
            $state.go('credenciamento', {criarUsuario: true});
        }
    });
})();