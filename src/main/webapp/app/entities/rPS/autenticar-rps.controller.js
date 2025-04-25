(function () {
    'use strict';

angular.module('nfseApp').controller('AutenticarRpsController',
    ['$rootScope', '$scope', '$modal', '$state', '$filter', '$stateParams', 'RPS', 'Notificacao', 'ImpressaoPdf',
        function ($rootScope, $scope, $modal, $state, $filter, $stateParams, RPS, Notificacao, ImpressaoPdf) {

            $scope.limpar = function () {
                $scope.autenticar = {cpfCnpj: "", numero: "", serie: ""};
                $scope.valido = null;
                $scope.rps = null;
            };
            $scope.limpar();

            $scope.autenticarRps = function () {
                console.log("Autenticar", $scope.autenticar);
                RPS.autenticar($scope.autenticar,
                    function (data) {
                        if (data.idNotaFiscal) {
                            Notificacao.info("Válido", "Os valores representam um RPS emitido e válido");
                        } else {
                            Notificacao.info("Válido", "Os valores representam um RPS emitido e válido, porém o RPS ainda não foi substituído por uma Nota Fiscal");
                        }
                        $scope.valido = true;
                        $scope.rps = data;
                    }, function (error) {
                        Notificacao.error("Inválido", "Ainda não existe nenhum RPS com os campos informados");
                        $scope.valido = false;
                    });
            };

            $scope.imprimirNota = function (id) {
                ImpressaoPdf.imprimirPdfViaUrl('/publico/notaFiscal/imprime/' + id);
            };


        }])
;
})();