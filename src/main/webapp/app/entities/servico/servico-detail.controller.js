(function () {
    'use strict';

angular.module('nfseApp')
    .controller('ServicoDetailController', function ($scope, $rootScope, $stateParams, entity, Servico) {
        $scope.servico = entity;
        $scope.load = function (id) {
            Servico.get({id: id}, function(result) {
                $scope.servico = result;
            });
        };
        $rootScope.$on('nfseApp:servicoUpdate', function(event, result) {
            $scope.servico = result;
        });
    });
})();