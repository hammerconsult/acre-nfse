(function () {
    'use strict';
    angular.module('nfseApp')
        .controller('ServicoDeclaradoDetailController',
            function ($scope, $rootScope, $stateParams, entity, ServicoDeclarado, Debitos) {
                $scope.servicoDeclarado = entity;
                $scope.parcelas;

                $scope.definirDadosPessoais = function () {
                    if (entity.$promise) {
                        entity.$promise.then(function (data) {
                            if (data.tipoServicoDeclarado == 'SERVICO_PRESTADO') {
                                $scope.servicoDeclarado.dadosPessoais = data.declaracaoPrestacaoServico.dadosPessoaisTomador;
                            } else {
                                $scope.servicoDeclarado.dadosPessoais = data.declaracaoPrestacaoServico.dadosPessoaisPrestador;
                            }
                        });
                    }
                };

                $scope.definirDadosPessoais();

                $scope.isFisica = function () {
                    return $scope.servicoDeclarado
                        && $scope.servicoDeclarado.dadosPessoais
                        && $scope.servicoDeclarado.dadosPessoais.cpfCnpj
                        && $scope.servicoDeclarado.dadosPessoais.cpfCnpj.replace(/[^\w\s]/gi, '').length == 11

                };

                $scope.buscarParcelas = function () {
                    ServicoDeclarado.buscarParcelas({id: $stateParams.id}, function (data) {
                        $scope.parcelas = data;
                        for (var i = 0; i < $scope.parcelas.length; i++) {
                            var parcela = $scope.parcelas[i];
                            Debitos.buscarUltimoDamParcela({idParcela: parcela.id}, function (dam) {
                                parcela.dam = dam;
                            });
                        }
                    })
                }

                $scope.buscarParcelas();
            });
})();
