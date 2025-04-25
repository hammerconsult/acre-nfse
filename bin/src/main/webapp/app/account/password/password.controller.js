(function () {
'use strict';

angular.module('nfseApp')
    .controller('PasswordController', function ($scope, Auth, Principal, $state, Notificacao, localStorageService) {
        Principal.identity().then(function (account) {
            $scope.account = account;
        });


        $scope.success = null;
        $scope.error = null;
        $scope.doNotMatch = null;
        $scope.trocarSenhaDTO = {};
        $scope.changePassword = function () {

            if ($scope.trocarSenhaDTO.newPassword !== $scope.trocarSenhaDTO.confirmPassword) {
                $scope.doNotMatch = 'ERROR';
            } else {
                $scope.doNotMatch = null;

                Auth.changePassword($scope.trocarSenhaDTO).then(function () {
                    console.log('deveria deslogar ');
                    Auth.logout();
                    localStorageService.clearAll();
                    $state.go('login');
                    Notificacao.info('Senha alterada com sucesso!', 'É necessário entrar no sistema novamente para prosseguir.')


                }).catch(function () {
                    $scope.success = null;
                    $scope.error = 'ERROR';
                });
            }
        };
    });
})();