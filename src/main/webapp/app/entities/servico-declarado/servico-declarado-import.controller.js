(function () {
    'use strict';

angular.module('nfseApp').controller('ServicoDeclaradoImportController',
    ['$scope', '$stateParams', 'entity', 'DeclaracaoServicoTomado', 'localStorageService', '$state',
        function ($scope, $stateParams, entity, DeclaracaoServicoTomado, localStorageService, $state) {

            $scope.lote = {file: '', prestador: localStorageService.get("prestadorPrincipal").prestador};

            $scope.uploadXmlFile = function (file) {
                var reader = new FileReader();
                reader.onload = function (evt) {
                    $scope.$apply(function ($scope) {
                        $scope.arquivoSelecionado = file;
                        $scope.lote.file = evt.target.result;
                    });
                };
                reader.readAsDataURL(file);
            };


            $scope.enviarArquivo = function () {
                DeclaracaoServicoTomado.importar($scope.lote, function (lote) {
                    $state.go("loteServicoTomado.detail", {id: lote.id})
                });
            }

        }]);
})();
