(function () {
    'use strict';

    angular.module('nfseApp').controller('MensagemDfeAdnController',
        function ($scope, $state, $modalInstance, ParseLinks, mensagens) {
            $scope.mensagens = mensagens;

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        });
})();
