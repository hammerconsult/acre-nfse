(function () {
    'use strict';

angular.module('nfseApp').controller('AidfeEditController',
    ['$scope', '$state', '$modal', '$stateParams', 'entity', 'Notificacao', 'localStorageService', '$timeout', 'Aidfe', 'Principal',
        function ($scope, $state, $modal, $stateParams, entity, Notificacao, localStorageService, $timeout, Aidfe, Principal) {

            $scope.aidfe = entity;
            $scope.mensagemCamposObrigatorios = [];
            $timeout(function () {
                angular.element('[ng-model="aidfe.quantidade"]').focus();
            });
            $scope.aidfe.prestadorServicos = localStorageService.get("prestadorPrincipal").prestador;
            Principal.identity().then(function (account) {
                $scope.account = account;
                $scope.aidfe.usuarioEmpresa = account;
            });

            var onSaveFinished = function (result) {
                $scope.$emit('nfseApp:aidfeUpdate', result);
                $state.go("aidfe");
                Notificacao.priority("Solicitação de AIDF-e atualizada com sucesso!")
            };

            function onSucessBuscarAidfeAguardando(response) {
                if (response.situacaoAIDFE === 'AGUARDANDO') {
                    Notificacao.warn("Atenção!", "Já existe uma solicitação de AIDF-e com o status aguardando.");
                    $state.go('aidfe');
                }
            }

            function onErrorBuscarAidfeAguardando(error) {
                if (error.status === 404) {
                    $scope.aidfe.situacaoAIDFE = 'AGUARDANDO';
                    $scope.aidfe.solicitadoEm = new Date();
                    Aidfe.save($scope.aidfe, onSaveFinished, function () {
                        $state.go("aidfe");
                    });
                }

            }

            $scope.save = function () {
                if ($scope.validar()) {
                    if ($scope.aidfe.id) {
                        var aidfeDTO = {
                            'id': $scope.aidfe.id,
                            'prestadorServicos': $scope.aidfe.prestadorServicos,
                            'usuarioEmpresa': $scope.aidfe.usuarioEmpresa,
                            'numero': $scope.aidfe.numero,
                            'solicitadoEm': $scope.aidfe.solicitadoEm,
                            'quantidade': $scope.aidfe.quantidade,
                            'numeroInicial': $scope.aidfe.numeroInicial,
                            'numeroFinal': $scope.aidfe.numeroFinal,
                            'observacao': $scope.aidfe.observacao,
                            'situacaoAIDFE': $scope.aidfe.situacaoAIDFE,
                            'deferidoEm': $scope.aidfe.deferidoEm,
                            'quantidadeDeferida': $scope.aidfe.quantidadeDeferida,
                            'observacaoDeferimento': $scope.aidfe.observacaoDeferimento,
                            'usuarioPrefeitura': $scope.aidfe.usuarioPrefeitura,
                            'versao': $scope.aidfe.versao
                        };

                        Aidfe.update(aidfeDTO, onSaveFinished, function () {
                            $state.go("aidfe");
                        });
                    } else {
                        Aidfe.buscarAidfeWithStatusAguardando({prestador_id: $scope.aidfe.prestadorServicos.id}, onSucessBuscarAidfeAguardando, onErrorBuscarAidfeAguardando);

                    }
                }
            };

            $scope.validar = function () {
                $scope.mensagemCamposObrigatorios = [];
                if ($scope.aidfe.quantidade <= 0 || $scope.aidfe.quantidade === undefined) {
                    $scope.mensagemCamposObrigatorios.push("O campo Quantidade Solicitada de NFS-e é obrigatória e deve ser maior que zero.")
                    Notificacao.camposObrigatorios($scope.mensagemCamposObrigatorios, "warning");
                    return false;
                }
                if ($scope.aidfe.quantidade >= 999999) {
                    $scope.mensagemCamposObrigatorios.push("Quantidade Solicitada deve ser inferior a 999999.")
                    Notificacao.camposObrigatorios($scope.mensagemCamposObrigatorios, "warning");
                    return false;
                }
                else {
                    return true;
                }
                return true;
            };

            function onSuccessBuscarNumero(response) {
                $scope.aidfe.numeroInicial = response.numeroInicial;
                $scope.aidfe.numeroFinal = $scope.aidfe.quantidade + $scope.aidfe.numeroInicial;
            }

            function onErrorBuscarNumero(error) {
                if (error.status === 404) {
                    Notificacao.error("Error: ", "Não foi encontrada uma sequência para o numero NFS-e Inicial.");
                }
            }

            $scope.calcularNumeroNFSE = function () {
                Aidfe.buscarAidfeNumeroInicial({},
                    onSuccessBuscarNumero, onErrorBuscarNumero);
            }
        }
    ])
;
})();