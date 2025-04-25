(function () {
    'use strict';
angular.module('nfseApp')
    .controller('CancelamentoServicoDeclaradoController',
        function ($scope, ServicoDeclarado, entity, SweetAlert, $modalInstance, Account) {

            $scope.servicoDeclarado = entity;
            console.log("Serviço Declarado {}", $scope.servicoDeclarado);
            $scope.podeSolicitar = false;
            $scope.cancelando = false;

            if (!$scope.servicoDeclarado.declaracaoPrestacaoServico.cancelamento ||
                !$scope.servicoDeclarado.declaracaoPrestacaoServico.cancelamento.situacaoFiscal ||
                $scope.servicoDeclarado.declaracaoPrestacaoServico.cancelamento.situacaoFiscal == 'NAO_DEFERIDO') {
                $scope.podeSolicitar = true;
                $scope.cancelamento = {servicoDeclarado: entity};
                Account.get().$promise
                    .then(function (account) {
                        $scope.cancelamento.usuarioTomador = account.data;
                    });
            }

            $scope.cancelarServicoDeclarado = function () {
                $scope.erroAoCancelar = null;
                SweetAlert.swal({
                        title: "Confirme o cancelamento",
                        text: "Você tem certeza que quer cancelar o Serviço Declarado?",
                        type: "warning",
                        showCancelButton: true,
                        confirmButtonColor: "#DD6B55", confirmButtonText: "Sim",
                        cancelButtonText: "Não",
                        closeOnConfirm: true,
                        closeOnCancel: true
                    },
                    function (isConfirm) {

                        if (isConfirm && !$scope.cancelando) {
                            $scope.cancelando = true;
                            ServicoDeclarado.cancelar($scope.cancelamento, function (data) {
                                $modalInstance.close(data);
                                $scope.cancelando = false;
                            }, function (error) {
                                console.error(error);
                                $scope.cancelando = false;
                                if(error && error.data && error.data.message){
                                    $scope.erroAoCancelar = error.data.message;
                                }
                            });
                        }
                    });
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

        });

})();
