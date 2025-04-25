(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('PlanoGeralContasComentadoDetailController', function ($state, $scope, entity, localStorageService,
                                                                           PlanoGeralContasComentado,
                                                                           PrestadorServicos, Notificacao) {
            $scope.entity = entity;
            $scope.prestadorPrincipal = localStorageService.get("prestadorPrincipal");

            $scope.init = function () {
                if ($scope.prestadorPrincipal) {
                    PrestadorServicos.get({id: $scope.prestadorPrincipal.prestador.id}, function (data) {
                        $scope.prestadorPrincipal.prestador = data;
                    });
                }
            }

            $scope.init();

            $scope.remove = function () {
                Notificacao.confirmDelete(function () {
                    PlanoGeralContasComentado.delete({id: $scope.entity.id}, function () {
                        $state.go('planoGeralContasComentado');
                    })
                })
            }

            $scope.isVersaoDesif10 = function () {
                return $scope.prestadorPrincipal &&
                    $scope.prestadorPrincipal.prestador &&
                    $scope.prestadorPrincipal.prestador.enquadramentoFiscal &&
                    $scope.prestadorPrincipal.prestador.enquadramentoFiscal.versaoDesif == 'VERSAO_1_0';
            }

            $scope.isVersaoDesifAbrasf32 = function () {
                return $scope.prestadorPrincipal &&
                    $scope.prestadorPrincipal.prestador &&
                    $scope.prestadorPrincipal.prestador.enquadramentoFiscal &&
                    $scope.prestadorPrincipal.prestador.enquadramentoFiscal.versaoDesif == 'VERSAO_ABRASF_3_2';
            }
        });
})();