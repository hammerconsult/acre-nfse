(function () {
    'use strict';

angular.module('nfseApp')
    .controller('CnaeDetailController', function ($scope, $rootScope, $stateParams, entity, Cnae) {
        $scope.cnae = entity;
        $scope.load = function (id) {
            Cnae.get({id: id}, function(result) {
                $scope.cnae = result;
            });
        };
        $rootScope.$on('nfseApp:cnaeUpdate', function(event, result) {
            $scope.cnae = result;
        });
    });
})();