(function () {
    'use strict';

angular.module('nfseApp')
    .controller('SubstituicaoNotaFiscalDetailController', function ($scope, $rootScope, $stateParams, entity, SubstituicaoNotaFiscal, NotaFiscal) {
        $scope.substituicaoNotaFiscal = entity;
        $scope.load = function (id) {
            SubstituicaoNotaFiscal.get({id: id}, function(result) {
                $scope.substituicaoNotaFiscal = result;
            });
        };
        $rootScope.$on('nfseApp:substituicaoNotaFiscalUpdate', function(event, result) {
            $scope.substituicaoNotaFiscal = result;
        });
    });
})();
