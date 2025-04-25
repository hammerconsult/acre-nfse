(function () {
    'use strict';

angular.module('nfseApp')
    .controller('NotaFiscalAvulsaEditController',
        function ($rootScope, $state, $scope, entity, NotaFiscalAvulsa, Account, Notificacao, Pessoa, CEP, $modal) {

            $scope.notaFiscalAvulsa = entity;
            $scope.item = {};

            $scope.atribuirUsuarioNfse = function () {
                Account.get().$promise
                    .then(function (account) {
                        $scope.notaFiscalAvulsa.user = account.data;
                        Pessoa.getPessoaDoLogin({login: account.data.login}).$promise.then(function (prestador) {
                            console.log(prestador);
                            $scope.notaFiscalAvulsa.prestador = prestador;
                        })
                    });
            };

            $scope.atribuirUsuarioNfse();


            $scope.validarCamposObrigatorios = function () {
                if (!$scope.notaFiscalAvulsa.dataNota) {
                    Notificacao.warn('Informe a Data da Nota', 'Atenção');
                    return false;
                }
                if (!$scope.notaFiscalAvulsa.tomador) {
                    Notificacao.warn('Informe o Tomador', 'Atenção');
                    return false;
                }
                if (!$scope.notaFiscalAvulsa.itens ||
                    $scope.notaFiscalAvulsa.itens.length == 0) {
                    Notificacao.warn('Informe o Serviço Prestado', 'Atenção');
                    return false;
                }
                return true;
            };

            $scope.salvar = function () {
                if ($scope.validarCamposObrigatorios()) {
                    NotaFiscalAvulsa.save($scope.notaFiscalAvulsa,
                        function (data) {
                            $state.go('notaFiscalAvulsa');
                        },
                        function (error) {
                            console.log(error);
                            $state.go('notaFiscalAvulsa');
                        });
                }
            }

            $scope.$watch('editForm.cpfCnpj.$valid', function (newVal) {
                $scope.preLoadTomador();
            });

            $scope.preLoadTomador = function () {
                if ($scope.temCpfOuCnpj()) {

                    var cpfCnpj = $scope.notaFiscalAvulsa.tomador.dadosPessoais.cpfCnpj;
                    Pessoa.getPorCpfCnpj({cpfCnpj: cpfCnpj}, function (result) {
                        if (result.id) {
                            $scope.notaFiscalAvulsa.tomador = result;
                        } else {
                            $scope.notaFiscalAvulsa.tomador.id = null;
                            $scope.notaFiscalAvulsa.tomador.dadosPessoais = {};
                            $scope.notaFiscalAvulsa.tomador.dadosPessoais.cpfCnpj = cpfCnpj;
                        }
                    });
                }
            };

            $scope.temCpfOuCnpj = function () {
                return $scope.notaFiscalAvulsa &&
                    $scope.notaFiscalAvulsa.tomador &&
                    $scope.notaFiscalAvulsa.tomador.dadosPessoais &&
                    $scope.notaFiscalAvulsa.tomador.dadosPessoais.cpfCnpj;
            };

            $scope.isFisica = function () {
                return $scope.notaFiscalAvulsa &&
                    $scope.notaFiscalAvulsa.tomador &&
                    $scope.notaFiscalAvulsa.tomador.dadosPessoais &&
                    $scope.notaFiscalAvulsa.tomador.dadosPessoais.cpfCnpj &&
                    $scope.notaFiscalAvulsa.tomador.dadosPessoais.cpfCnpj.replace(/[^\w\s]/gi, '').length == 11

            };

            $scope.isJuridica = function () {
                return $scope.notaFiscalAvulsa &&
                    $scope.notaFiscalAvulsa.tomador &&
                    $scope.notaFiscalAvulsa.tomador.dadosPessoais &&
                    $scope.notaFiscalAvulsa.tomador.dadosPessoais.cpfCnpj &&
                    $scope.notaFiscalAvulsa.tomador.dadosPessoais.cpfCnpj.replace(/[^\w\s]/gi, '').length == 14

            };

            $scope.loadEnderecoByCEP = function (cep) {
                if (cep) {
                    CEP.getByCep({cep: cep}, function (endereco) {
                        if (endereco.municipio) {
                            $scope.notaFiscalAvulsa.tomador.dadosPessoais.cep = endereco.cep;
                            $scope.notaFiscalAvulsa.tomador.dadosPessoais.municipio = endereco.municipio;
                            $scope.notaFiscalAvulsa.tomador.dadosPessoais.logradouro = endereco.logradouro;
                            $scope.notaFiscalAvulsa.tomador.dadosPessoais.bairro = endereco.bairro;
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

            $scope.searchServico = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'app/entities/servico/servico-search.html',
                    controller: 'ServicoSearchController',
                    size: 'lg'
                });
                modalInstance.result.then(function (result) {
                    $scope.item.servico = result;
                    $scope.item.aliquota = $scope.item.servico.aliquota;
                }, function () {

                });
            };

            $scope.validarCamposObrigatoriosServico = function () {
                if (!$scope.item) {
                    return false;
                }
                if (!$scope.item.servico) {
                    Notificacao.warn('Informe um serviço.');
                    return false;
                }
                if (!$scope.item.descricao) {
                    Notificacao.warn('Informe a discriminação do serviço.');
                    return false;
                }
                if (!$scope.item.quantidade || $scope.item.quantidade <= 0) {
                    Notificacao.warn('Informe uma quantidade maior que 0.');
                    return false;
                }
                if (!$scope.item.valorUnitario || $scope.item.valorUnitario <= 0) {
                    Notificacao.warn('Informe um valor unitário maior que 0.');
                    return false;
                }
                return true;
            };

            $scope.addServico = function () {
                if ($scope.validarCamposObrigatoriosServico()) {
                    $scope.notaFiscalAvulsa.itens.push($scope.item);
                    $scope.notaFiscalAvulsa.valorIss = $scope.item.aliquota;
                    $scope.notaFiscalAvulsa.valorServicos = $scope.item.valorTotal;
                    $scope.notaFiscalAvulsa.valorTotalIss = $scope.item.valorIss;
                    $scope.item = {};
                }
            };

            $scope.delServico = function (index) {
                $scope.notaFiscalAvulsa.itens.splice(index, 1);
            };

            $scope.editServico = function (index) {
                $scope.item = $scope.notaFiscalAvulsa.itens[index];
                $scope.delServico(index);
            };

            $scope.calcularValoresServico = function () {
                if ($scope.item.quantidade && $scope.item.valorUnitario) {
                    $scope.item.valorTotal = $scope.item.quantidade * $scope.item.valorUnitario;
                    $scope.item.valorIss = $scope.item.valorTotal * ($scope.item.aliquota / 100);
                }
            }
        }
    );
})();