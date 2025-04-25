(function () {
    'use strict';

angular.module('nfseApp')
    .controller('NotaFiscalAvulsaDetailController',
        function ($scope, entity) {
            $scope.notaFiscalAvulsa = entity;
        }
    );
})();