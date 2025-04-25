(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('AliquotaSimplesAnesoLei1232006Controller',
            function ($scope, PrestadorServicos, ParseLinks, SweetAlert, $modalInstance, rbt12, anexo, faixa) {

                $scope.rbt12 = rbt12;
                $scope.anexos = [];
                $scope.anexo = anexo;
                $scope.faixa = faixa;


                PrestadorServicos.getAnexosLei1232006({}, function (data) {
                    $scope.anexos = data;
                });


                $scope.selecionarFaixa = function () {
                    $scope.faixa = null;
                    if ($scope.anexo) {
                        const faixas = $scope.anexo.faixas;
                        for (var i = 0; i < faixas.length; i++) {
                            if (rbt12 < faixas[i].valorMaximo) {
                                $scope.faixa = faixas[i];
                                break;
                            }
                        }
                        if (!$scope.faixa) {
                            $scope.faixa = faixas[faixas.length - 1];
                        }
                    }
                };


                $scope.ok = function () {
                    $modalInstance.close($scope.anexo);
                };

                $scope.cancel = function () {
                    $modalInstance.dismiss('cancel');
                };
            }
        )
    ;
})();