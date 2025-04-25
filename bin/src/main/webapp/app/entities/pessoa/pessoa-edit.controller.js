(function () {
    'use strict';

angular.module('nfseApp').controller('PessoaEditController',
    ['$scope', '$state', '$modal', '$stateParams', 'entity', 'Pessoa', 'Notificacao', 'CEP', 'Municipio',
        function ($scope, $state, $modal, $stateParams, entity, Pessoa, Notificacao, CEP, Municipio) {

            $scope.pessoa = entity;

            $scope.load = function (id) {
                Pessoa.get({id: id}, function (result) {
                    $scope.pessoa = result;
                });
            };

            $scope.loadByCpfCnpj = function () {
                Pessoa.getPorCpfCnpj(
                    {cpfCnpj: $scope.pessoa.dadosPessoais.cpfCnpj}, function (result) {
                        $scope.editForm.cpfCnpj.$setValidity("exists", true);
                        if (result.dadosPessoais) {
                            $scope.pessoa = result;
                            Notificacao.error("Atenção", "Já existe uma Pessoa com o CPF ou CNPJ informado");
                            $scope.editForm.cpfCnpj.$setValidity("exists", false);
                        }
                    });
            };

            var onSaveFinished = function (result) {
                $scope.$emit('nfseApp:pessoaUpdate', result);
                $state.go("pessoa");
                Notificacao.info("Pessoa salva com sucesso")
            };

            $scope.save = function () {
                if ($scope.pessoa.id != null) {
                    Pessoa.update($scope.pessoa, onSaveFinished);
                } else {
                    Pessoa.save($scope.pessoa, onSaveFinished);
                }
            };

            $scope.loadEnderecoByCEP = function (cep) {
                if (cep) {
                    CEP.getByCep({cep: cep}, function (endereco) {
                        if (endereco.municipio) {
                            $scope.pessoa.dadosPessoais.endereco.cep = endereco.cep;
                            $scope.pessoa.dadosPessoais.endereco.municipio = endereco.municipio;
                            $scope.pessoa.dadosPessoais.endereco.logradouro = endereco.logradouro;
                            $scope.pessoa.dadosPessoais.endereco.bairro = endereco.bairro;
                        }
                    });
                }
            };

            $scope.loadMunicipioByCodigo = function (codigo) {
                if (codigo) {
                    Municipio.getByCodigo({codigo: codigo}, function (municipio) {
                        if (municipio.id) {
                            $scope.pessoa.dadosPessoais.endereco.municipio = municipio;
                        } else {
                            Notificacao.warn("Atenção", "Não foi encotrado nenhum Municipio com o código " + codigo);
                            $scope.pessoa.dadosPessoais.endereco.municipio = {};
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
                    $scope.pessoa.dadosPessoais.endereco.municipio = municipio;
                }, function () {
                    //$log.info('Modal dismissed at: ' + new Date());
                });
            };
        }]);
})();