(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('CancelamentoNotaFiscalController', function ($scope, NotaFiscal,
                                                                  entity, SweetAlert,
                                                                  $modalInstance, Account) {
            $scope.notaFiscal = entity;
            $scope.podeSolicitar = false;
            $scope.cancelando = false;
            $scope.substituida = false;
            $scope.numeroSubstituta;

            NotaFiscal.getCancelamentos({id: entity.id}, function (res) {
                $scope.cancelamentos = res;
                if ($scope.notaFiscal.situacao == 'EMITIDA') {
                    $scope.podeSolicitar = true;
                    for (var i = 0; i < res.length; i++) {
                        var cancelamento = res[i];
                        if (cancelamento.situacaoFiscal == 'EM_ANALISE') {
                            $scope.podeSolicitar = false;
                            break
                        }
                    }
                    $scope.cancelamento = {notaFiscal: entity};
                    Account.get().$promise
                        .then(function (account) {
                            $scope.cancelamento.usuarioTomador = account.data;
                        });
                }
            });

            $scope.cancelarNota = function () {
                SweetAlert.swal({
                        title: "Confirme o cancelamento",
                        text: "Você tem certeza que quer cancelar a Nota Fiscal?",
                        type: "warning",
                        showCancelButton: true,
                        confirmButtonColor: "#DD6B55", confirmButtonText: "Sim",
                        cancelButtonText: "Não",
                        closeOnConfirm: false,
                        closeOnCancel: true
                    },
                    function (isConfirm) {
                        if (isConfirm && !$scope.cancelando) {
                            $scope.cancelando = true;
                            NotaFiscal.cancelar($scope.cancelamento, function (data) {
                                $modalInstance.close(data);
                                $scope.cancelando = false;
                            }, function (error) {
                                console.log(error);
                                $scope.cancelando = false;
                            });
                        }
                    });
            };

            $scope.findNotaSubstituta = function () {
                $scope.cancelamento.notaFiscalSubstituta = null;
                NotaFiscal.porNumero({numero: $scope.numeroSubstituta}, function (nota) {
                    if (nota && nota.id == $scope.cancelamento.notaFiscal.id) {
                        Notificacao.warn("Atenção", "Nota Fisca de Substituição deve ser diferente da " +
                            "Nota Fiscal Cancelada.");
                    }
                    $scope.cancelamento.notaFiscalSubstituta = nota;
                });
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        });
})();
