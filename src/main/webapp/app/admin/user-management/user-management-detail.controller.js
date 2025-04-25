(function () {
    'use strict';

angular.module('nfseApp')
    .controller('UserManagementDetailController', function ($scope, $window, $stateParams, $state, User, Notificacao, Pessoa) {
        $scope.user = {};
        $scope.pessoa = {};
        $scope.authorities = ["ROLE_USER", "ROLE_ADMIN", "ROLE_CONTRIBUINTE"];
        $scope.load = function (login) {
            User.get({login: login}, function (result) {
                $scope.user = result;
            });
        };
        $scope.load($stateParams.login);

        var onSaveFinished = function (result) {
            $state.go("user-management");
            Notificacao.info("Usuário salvo com sucesso")
        };

        $scope.save = function () {
            User.update($scope.user, onSaveFinished);
        };

        $scope.clear = function () {
            $state.go("user-management");
        };

        $scope.visualizarPessoaUsuario = function () {
            console.log('visualizarPessoaUsuario');

            Pessoa.getPessoaDoLogin({login: $scope.user.login},
                function (result) {
                    $state.go("pessoa.detail", {id: result.id});
                });
        }
    });
})();