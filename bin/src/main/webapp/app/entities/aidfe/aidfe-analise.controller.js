(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('AidfeAnaliseController', ['$scope', '$rootScope', '$stateParams', 'entity', 'Aidfe', '$timeout', 'Notificacao', '$state', 'SweetAlert', function ($scope, $rootScope, $stateParams, entity, Aidfe, $timeout, Notificacao, $state, SweetAlert) {
            console.log("result 1111", entity);
            $scope.aidfe = entity;
            $scope.load = function (id) {
                Aidfe.get({id: id}, function (result) {
                    $scope.aidfe = result;
                    console.log("resultadoo ", result);
                });
            };
            $rootScope.$on('nfseApp:aidfeUpdate', function (event, result) {
                $scope.aidfe = result;
            });

            function onSuccess(response) {
                $scope.$emit('nfseApp:aidfeUpdate', response);
                $state.go("aidfe.analises");
                Notificacao.info("Registro Salvo com Sucesso", "");

            }

            function onError(error) {
                console.log("error: ", error);
                var message = '';
                if (error.data.error_description) {
                    message = error.data.error_description;
                } else {
                    message = error.data.message;
                }
                Notificacao.warn("Erro: ", message);
            }

            $scope.deferir = function () {
                SweetAlert.swal({
                        title: "Confirme o deferimento",
                        text: "Tem certeza que quer deferir a quantidade total de notas solicitadas?",
                        type: "warning",
                        showCancelButton: true,
                        confirmButtonColor: "#1A7BB9", confirmButtonText: "Sim, Deferir",
                        cancelButtonText: "Não, Cancelar",
                        closeOnConfirm: false,
                        closeOnCancel: false
                    },
                    function (isConfirm) {
                        if (isConfirm) {
                            Aidfe.deferir($scope.aidfe, onSuccess, onError);
                            Notificacao.info("Registro Deferido com Sucesso.", "");
                        } else {
                            SweetAlert.swal("Cancelado", "O registro não foi deferido.", "error");
                        }
                    });
            };

            $scope.deferirParcialmente = function () {
                SweetAlert.swal({
                        title: "Confirme o deferimento parcial",
                        text: "Tem certeza que quer deferir " + $scope.aidfe.quantidadeDeferida + " notas das " + $scope.aidfe.quantidade + " solicitadas? ",
                        type: "warning",
                        showCancelButton: true,
                        confirmButtonColor: "#F7A54A", confirmButtonText: "Sim, Deferir Parcialmente",
                        cancelButtonText: "Não, Cancelar",
                        closeOnConfirm: false,
                        closeOnCancel: false
                    },
                    function (isConfirm) {
                        if (isConfirm) {
                            Aidfe.deferirParcialmente($scope.aidfe, onSuccess, onError);
                            Notificacao.info("Registro Deferido Parcialmente com Sucesso.", "");
                        } else {
                            SweetAlert.swal("Cancelado", "O registro não foi deferido parcialmente.", "error");
                        }
                    }
                )
                ;
            };

            $scope.indeferir = function () {
                SweetAlert.swal({
                        title: "Confirme o indeferimento",
                        text: "Tem certeza que quer indeferir essa AIDF-e?",
                        type: "warning",
                        showCancelButton: true,
                        confirmButtonColor: "#DD6B55", confirmButtonText: "Sim, Indeferir",
                        cancelButtonText: "Não, Cancelar",
                        closeOnConfirm: false,
                        closeOnCancel: false
                    },
                    function (isConfirm) {
                        if (isConfirm) {
                            Aidfe.indeferir($scope.aidfe, onSuccess, onError);
                            Notificacao.info("Registro Indeferido com Sucesso.", "");
                        } else {
                            SweetAlert.swal("Cancelado", "O registro não foi indeferido.", "error");
                        }
                    });
            };

            $scope.calcularNumeroNFSE = function () {
                if ($scope.aidfe.situacaoAIDFE == 'AGUARDANDO') {
                    $scope.aidfe.numeroFinal = Number($scope.aidfe.numeroInicial) + Number($scope.aidfe.quantidadeDeferida) - 1;
                }
            };

            $timeout(function () {
                angular.element('[ng-model="aidfe.quantidadeDeferida"]').focus();
                $scope.aidfe.quantidadeDeferida = entity.quantidade;
                console.log("result ", $scope.aidfe);
            });

        }
        ])
    ;
})
();