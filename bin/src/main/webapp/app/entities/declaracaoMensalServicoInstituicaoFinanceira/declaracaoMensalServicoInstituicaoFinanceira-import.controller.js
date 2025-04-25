(function () {
    'use strict';

angular.module('nfseApp').controller('DeclaracaoMensalServicoInstituicaoFinanceiraImportController',
    ['$scope', '$stateParams', 'ArquivoDesif', 'localStorageService', '$state',
        function ($scope, $stateParams, ArquivoDesif, localStorageService, $state) {

            $scope.dto = {file: '', prestador: localStorageService.get("prestadorPrincipal").prestador};

            $scope.uploadXmlFile = function (file) {
                console.log(file);
                var reader = new FileReader();
                reader.onload = function (evt) {
                    console.log(evt);
                    $scope.$apply(function ($scope) {
                        $scope.arquivoSelecionado = file;
                        $scope.dto.file = evt.target.result;
                    });
                };
                reader.readAsDataURL(file);
            };


            $scope.enviarArquivo = function () {
                ArquivoDesif.importarLegado($scope.dto, function () {
                    $state.go("declaracaoMensalServicoInstituicaoFinanceira")
                });
            }

        }]);
})();