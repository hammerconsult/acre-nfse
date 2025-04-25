(function () {
    'use strict';

angular.module('nfseApp')
    .controller('PaisDetailController', function ($scope, $rootScope, $stateParams, entity, Pais) {
        $scope.pais = entity;
        $scope.load = function (id) {
            Pais.get({id: id}, function(result) {
                $scope.pais = result;
            });
        };
        $rootScope.$on('nfseApp:paisUpdate', function(event, result) {
            $scope.pais = result;
        });
    });
})();