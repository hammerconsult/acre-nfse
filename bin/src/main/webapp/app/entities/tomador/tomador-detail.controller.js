(function () {
    'use strict';

angular.module('nfseApp')
    .controller('TomadorDetailController', function ($scope, $rootScope, $stateParams, entity, Tomador) {
        $scope.tomador = entity;
        $scope.load = function (id) {
            Tomador.get({id: id}, function(result) {
                $scope.tomador = result;
            });
        };
        $rootScope.$on('nfseApp:tomadorUpdate', function(event, result) {
            $scope.tomador = result;
        });
    });
})();
