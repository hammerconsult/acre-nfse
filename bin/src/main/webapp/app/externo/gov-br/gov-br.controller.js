(function () {
    'use strict';
    angular.module('nfseApp')
        .controller('GovBrController', function ($scope, $state, $stateParams, GovBr, localStorageService, Principal, Notificacao) {
            $scope.code = $stateParams.code;

            console.debug('code [{}]', $stateParams.code);

            GovBr.autenticar({code: $stateParams.code}, function (data) {
                console.debug('TOKEN [{}]', data);
                if (data.access_token) {
                    var expiredAt = new Date();
                    expiredAt.setSeconds(expiredAt.getSeconds() + data.expires_in);
                    data.expires_at = expiredAt.getTime();
                    localStorageService.set('token', data);
                    Principal.identity(true).then(function (account) {
                        $state.go('home', {}, {reload: true});
                    });
                } else {
                    irParaLogin();
                }
            }, function (error) {
                console.debug('Erro [{}]', error);
                irParaLogin();
            });

            function irParaLogin() {
                Notificacao.warn('Atenção', 'Usuário não registrado na nfs-e.');
                $state.go('login');
            }
        });
})();
