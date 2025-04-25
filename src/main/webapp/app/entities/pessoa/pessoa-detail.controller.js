(function () {
    'use strict';

angular.module('nfseApp')
    .controller('PessoaDetailController', function ($scope, $rootScope, $stateParams, entity, Pessoa) {
        $scope.pessoa = entity;
        $scope.load = function (id) {
            Pessoa.get({id: id}, function(result) {
                $scope.pessoa = result;
            });
        };
        $rootScope.$on('nfseApp:pessoaUpdate', function(event, result) {
            $scope.pessoa = result;
        });
    });
})();