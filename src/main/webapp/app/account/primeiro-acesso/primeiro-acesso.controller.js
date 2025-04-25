(function () {
'use strict';

angular.module('nfseApp')
    .controller('PrimeiroAcessoController', function ($scope, Auth, Principal, $state, Notificacao) {
        $scope.trocarSenhaDTO = {};

        $scope.changePassword = function () {
            if ($scope.trocarSenhaDTO.newPassword !== $scope.trocarSenhaDTO.confirmPassword) {
                Notificacao.error("Erro", "Confirmação de Senha incorreta. Por favor digite novamente a nova senha e a confirmação da senha.");
            } else {
                Auth.changePassword($scope.trocarSenhaDTO).then(function () {
                    $state.go('home');
                    Notificacao.info('Informação', 'Senha alterada com sucesso.');
                }).catch(function (error) {
                    console.log('ERRO {}', error);
                    Notificacao.error("Erro", "Senha Atual incorreta.");
                });
            }
        };
    });
})();