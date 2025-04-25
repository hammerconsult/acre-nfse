(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('ArquivoDesifValidateController',
            function ($scope, $state, localStorageService, PrestadorServicos, ArquivoDesif) {
                $scope.prestador = null;
                $scope.file = null;
                $scope.fileName = null;
                $scope.importacao = null;

                PrestadorServicos.get({id: localStorageService.get("prestadorPrincipal").prestador.id}, function (data) {
                    $scope.prestador = data;
                });


                $scope.uploadFile = function (file) {
                    $scope.importacao = null;
                    var reader = new FileReader();
                    reader.onload = function (evt) {
                        $scope.$apply(function ($scope) {
                            $scope.file = evt.target.result;
                            $scope.fileName = file.name;
                        });
                    };
                    reader.readAsDataURL(file);
                };

                $scope.disableValidarArquivo = function () {
                    return !$scope.file;
                }

                $scope.validarImportacaoDesif = function () {
                    ArquivoDesif.validar({
                        prestador: $scope.prestador,
                        file: $scope.file
                    }, function (data) {
                        $scope.importacao = data;
                        if ($scope.importacao.idArquivo > 0) {
                            $state.go("arquivoDesif.detalhe", {id: $scope.importacao.idArquivo});
                        }
                    });
                }
            });
})();
