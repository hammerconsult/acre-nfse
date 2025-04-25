(function () {
    'use strict';

angular.module('nfseApp')
    .controller('MunicipioDetailController', function ($scope, $rootScope, $stateParams, entity, Municipio) {
        $scope.municipio = entity;
        $scope.load = function (id) {
            Municipio.get({id: id}, function(result) {
                $scope.municipio = result;
            });
        };
        $rootScope.$on('nfseApp:municipioUpdate', function(event, result) {
            $scope.municipio = result;
        });
    });
})();