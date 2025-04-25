(function () {
    'use strict';

angular.module('nfseApp')
    .controller('DataController', function ($scope, NotaFiscal) {
        $scope.dataGet;
        $scope.dataPost;
        $scope.emissaoPopUp = false;

        $scope.get = function () {
            NotaFiscal.dataGet(null, function (data) {
                $scope.dataGet = data.emissao;
            })
        };

        $scope.post = function () {
            NotaFiscal.dataPost({emissao: $scope.dataGet}, function (data) {
                $scope.dataPost = data.emissao;
            })
        };

        $scope.openDateDialog = function ($event) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.emissaoPopUp = true;
        };
    });
})();
