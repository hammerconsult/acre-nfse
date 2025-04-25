(function () {
    'use strict';

angular.module('nfseApp')
    .controller('DocumentosController', function ($scope, $state, entity, localStorageService) {
        $scope.entity = entity;

        $scope.prestadorPrincipal = localStorageService.get("prestadorPrincipal");

        if ($scope.prestadorPrincipal.permitido) {
            $state.go("home");
        }

        $scope.imprimirTermoCredenciamento = function () {
            $state.go("imprimeTermoCredenciamento", {id: $scope.entity.id});
        }
    }
);
})();