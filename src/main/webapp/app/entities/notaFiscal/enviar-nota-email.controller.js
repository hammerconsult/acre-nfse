(function () {
    'use strict';

angular.module('nfseApp')
    .controller('EnviarNotaEmailController', function ($scope, entity, NotaFiscal, ParseLinks, SweetAlert, $modalInstance) {
        $scope.notaFiscal = entity;

        $scope.ok = function () {
            NotaFiscal.enviarPorEmail($scope.notaFiscal, function(data){
                SweetAlert.success("Enviado", "O email foi enviado aos destinatários", "info");
                $modalInstance.close($scope.notaFiscal);
            }, function (err) {
                SweetAlert.error("Operação não realizada", "não foi possível enviar o email aos destinatários", "error");
            });
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    });
})();