(function () {
    'use strict';

angular.module('nfseApp')
    .controller('ConstrucaoCivilEditController', function ($modal, $state, $scope, entity,
                                                           ConstrucaoCivil, localStorageService, CadastroImobiliario,
                                                           Notificacao, CEP, Configuracao, $timeout, Pessoa) {
        $scope.obra = entity;
        $scope.obra.prestador = localStorageService.get("prestadorPrincipal").prestador;
        $scope.inscricaoImovel = null;
        $scope.municipio;
        $scope.profissional;

        $timeout(function () {
            if ($scope.obra.imovel) {
                $scope.inscricaoImovel = $scope.obra.imovel.inscricaoCadastral;
            }
        }, 1000);

        Configuracao.get({}, function (data) {
            $scope.municipio = data.municipio;
        });


        $scope.save = function () {
            if ($scope.validarCamposObrigatorios()) {
                ConstrucaoCivil.save($scope.obra, function (data) {
                    $state.go('construcaoCivil');
                });
            }
        };

        $scope.validarCamposObrigatorios = function () {
            $scope.mensagemCamposObrigatorios = [];
            if (!$scope.obra.art) {
                $scope.mensagemCamposObrigatorios.push("Informe a ART");
            }
            if (!$scope.obra.dataAprovacaoProjeto) {
                $scope.mensagemCamposObrigatorios.push("Informe a Data de Apovação");
            }
            if (!$scope.obra.dataInicio) {
                $scope.mensagemCamposObrigatorios.push("Informe a Data de Início");
            }
            if (!$scope.obra.numeroAlvara) {
                $scope.mensagemCamposObrigatorios.push("Informe o Número do Alvará");
            }
            if (!$scope.obra.status) {
                $scope.mensagemCamposObrigatorios.push("Informe a Situação da Obra");
            }
            if (!$scope.obra.localizacao || !$scope.obra.localizacao.cep) {
                $scope.mensagemCamposObrigatorios.push("Informe o CEP");
            }
            if (!$scope.obra.localizacao || !$scope.obra.localizacao.municipio) {
                $scope.mensagemCamposObrigatorios.push("Informe o Município");
            }
            if (!$scope.obra.localizacao || !$scope.obra.localizacao.logradouro) {
                $scope.mensagemCamposObrigatorios.push("Informe o Logradouro");
            }
            if (!$scope.obra.localizacao || !$scope.obra.localizacao.bairro) {
                $scope.mensagemCamposObrigatorios.push("Informe o Bairro");
            }
            if (!$scope.obra.localizacao || !$scope.obra.localizacao.numero) {
                $scope.mensagemCamposObrigatorios.push("Informe o Número");
            }
            if ($scope.mensagemCamposObrigatorios.length > 0) {
                Notificacao.camposObrigatorios($scope.mensagemCamposObrigatorios, "warning");
                return false;
            } else {
                return true;
            }
        };

        $scope.buscarImovel = function () {
            if ($scope.inscricaoImovel) {
                CadastroImobiliario.query({
                    page: 0,
                    per_page: 1,
                    search: $scope.inscricaoImovel
                }, function (result) {
                    if (result && result.length > 0) {
                        if (result[0].id) {
                            $scope.obra.imovel = result[0];
                        } else {
                            $scope.inscricaoImovel = null;
                            Notificacao.warn("Atenção!", "Imóvel não encontrado.");
                        }
                    }
                });
            }
        };

        $scope.searchImovel = function () {
            var modalInstance = $modal.open({
                templateUrl: 'app/entities/cadastro-imobiliario/cadastro-imobiliario-search.html',
                controller: 'CadastroImobiliarioSearchController',
                size: 'lg'
            });
            modalInstance.result.then(function (cadastro) {
                $scope.obra.imovel = cadastro;
                $scope.inscricaoImovel = cadastro.inscricaoCadastral;
            });
        };

        $scope.loadEnderecoByCEP = function (cep) {
            if (cep) {
                CEP.getByCep({cep: cep}, function (endereco) {
                    if (endereco.municipio) {
                        $scope.obra.imovel = null;
                        $scope.obra.localizacao.cep = endereco.cep;
                        $scope.obra.localizacao.municipio = endereco.municipio;
                        $scope.obra.localizacao.logradouro = endereco.logradouro;
                        $scope.obra.localizacao.bairro = endereco.bairro;
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
                $scope.obra.imovel = null;
                $scope.obra.localizacao.municipio = municipio;
                $scope.obra.localizacao.pais = {};
            }, function () {

            });
        };

        $scope.habilitaImovel = function () {
            return $scope.obra &&
                $scope.obra.localizacao &&
                $scope.obra.localizacao.municipio &&
                $scope.obra.localizacao.municipio.id == $scope.municipio.id;
        };

        $scope.novoProfissional = function () {
            $scope.profissional = {};
        };

        $scope.adicionarProfissional = function () {
            $scope.obra.profissionais.push($scope.profissional);
            $scope.profissional = null;
        };

        $scope.cancelarProfissional = function () {
            $scope.profissional = null;
        };

        $scope.removerProfissional = function (index) {
            Notificacao.confirmDelete(function () {
                $scope.obra.profissionais.splice(index, 1);
            });

        };

        $scope.isFisica = function (pessoa) {
            return pessoa &&
                pessoa.dadosPessoais &&
                pessoa.dadosPessoais.cpfCnpj &&
                pessoa.dadosPessoais.cpfCnpj.replace(/[^\w\s]/gi, '').length == 11;
        }

        $scope.preLoadProfissional = function () {
            if ($scope.profissional.profissional.dadosPessoais.cpfCnpj) {
                Pessoa.getPorCpfCnpj({cpfCnpj: $scope.profissional.profissional.dadosPessoais.cpfCnpj}, function (data) {
                    if (data.id) {
                        $scope.profissional.profissional = data;
                    }
                })
            }
        };
    });
})();