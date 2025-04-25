(function () {
    'use strict';

    angular.module('nfseApp').controller('TomadorEditController',
        ['$scope', '$state', '$modal', '$stateParams', 'entity', 'Tomador', 'PrestadorServicos', 'Notificacao', 'localStorageService',
            'Municipio', 'CEP',
            function ($scope, $state, $modal, $stateParams, entity, Tomador, PrestadorServicos, Notificacao, localStorageService,
                      Municipio, CEP) {

                $scope.tomador = entity;

                $scope.loadEnderecoByCEP = function (cep) {
                    if (cep) {
                        CEP.getByCep({cep: cep}, function (endereco) {
                            console.log(endereco);
                            if (endereco.municipio) {
                                $scope.tomador.dadosPessoais.cep = endereco.cep;
                                $scope.tomador.dadosPessoais.municipio = endereco.municipio;
                                $scope.tomador.dadosPessoais.logradouro = endereco.logradouro;
                                $scope.tomador.dadosPessoais.bairro = endereco.bairro;
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
                        $scope.tomador.dadosPessoais.municipio = municipio;
                    }, function () {
                        //$log.info('Modal dismissed at: ' + new Date());
                    });
                };

                $scope.loadInPessoaRepoByCpfCnpj = function () {
                    if ($scope.tomador && $scope.tomador.dadosPessoais) {
                        Tomador.getTomadorPorCpfCnpj({cpfCnpj: $scope.tomador.dadosPessoais.cpfCnpj}, function (result) {
                            if (!result.dadosPessoais) {
                                Tomador.getInPessoRepoByCpfCnpj({cpfCnpj: $scope.tomador.dadosPessoais.cpfCnpj}, function (result) {
                                    if (result.dadosPessoais && result.dadosPessoais.cpfCnpj) {
                                        $scope.tomador = result;
                                    }
                                });
                            } else {
                                $scope.tomador = result;
                                Notificacao.error("Atenção", "Já existe um Tomador com o CPF ou CNPJ informado");
                                $scope.editForm.cpfCnpj.$setValidity("exists", false);
                            }
                        });
                    }
                };

                var onSaveFinished = function (result) {
                    $scope.$emit('nfseApp:tomadorUpdate', result);
                    $state.go("tomador");
                    Notificacao.info("Tomador salvo com sucesso")
                };

                $scope.save = function () {
                    $scope.tomador.prestador = localStorageService.get("prestadorPrincipal").prestador;
                    Tomador.save($scope.tomador, onSaveFinished);
                };
            }]);
})();
