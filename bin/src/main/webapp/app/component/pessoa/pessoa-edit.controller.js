(function () {
    'use strict';

angular.module('nfseApp')
    .component('pessoaEdit', {
        restrict: 'E',
        templateUrl: 'app/component/pessoa/pessoa-edit.html',
        bindings: {
            pessoa: '=',
            formReference: '<'
        },
        controllerAs: 'vm',
        controller: function ($scope, Pessoa, CEP, $modal, $timeout) {
            var vm = this;

            $timeout(function () {
                if (!vm.pessoa) {
                    vm.pessoa = {dadosPessoais: {cpfCnpj: ""}}
                }
            }, 1000);

            $scope.preLoadResponsavel = function () {
                if (vm.pessoa.dadosPessoais.cpfCnpj) {
                    Pessoa.getPorCpfCnpj({cpfCnpj: vm.pessoa.dadosPessoais.cpfCnpj}, function (data) {
                        if (data.id) {
                            vm.pessoa = data;
                        } else {
                            vm.pessoa = {dadosPessoais: {cpfCnpj: vm.pessoa.dadosPessoais.cpfCnpj}};
                        }
                    })
                }
            };


            $scope.isFisica = function () {
                return vm.pessoa &&
                    vm.pessoa.dadosPessoais &&
                    vm.pessoa.dadosPessoais.cpfCnpj &&
                    vm.pessoa.dadosPessoais.cpfCnpj.replace(/[^\w\s]/gi, '').length == 11;
            };

            $scope.loadEnderecoByCEP = function (cep) {
                if (cep) {
                    CEP.getByCep({cep: cep}, function (endereco) {
                        if (endereco.municipio) {
                            vm.pessoa.dadosPessoais.cep = endereco.cep;
                            vm.pessoa.dadosPessoais.municipio = endereco.municipio;
                            vm.pessoa.dadosPessoais.logradouro = endereco.logradouro;
                            vm.pessoa.dadosPessoais.bairro = endereco.bairro;
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
        }
    });
})();