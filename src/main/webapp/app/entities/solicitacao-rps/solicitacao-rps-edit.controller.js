(function () {
    'use strict';

angular.module('nfseApp')
    .controller('SolicitacaoRPSEditarController', function ($scope, $state, Solicitacaorps, Notificacao, Principal, $filter, ParseLinks, SweetAlert, localStorageService) {

        $scope.per_page = 10;
        $scope.searchQuery = "";

        $scope.loadAll = function () {
            $scope.prestador = localStorageService.get("prestadorPrincipal");

            Principal.identity().then(function (account) {
                if (account) {
                    $scope.account = account;
                }
            });
        };

        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };

        $scope.loadAll();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clean();
        };

        $scope.clear = function () {
            $scope.solicitacaorps = {id: null};
        };

        $scope.save = function () {
            if ($scope.validar()) {

                var solicitacaoRPSDTO = {
                    prestador: $scope.prestador.prestador,
                    userEmpresa: $scope.account,
                    solicitadoEm: $scope.solicitacaorps.solicitadoEm,
                    quantidadeSolicitada: $scope.solicitacaorps.quantidadeSolicitada,
                    observacaoSolicitacao: $scope.solicitacaorps.observacaoSolicitacao,
                    observacaoAnalise: $scope.solicitacaorps.observacaoAnalise,
                    situacaosolicitacaorps: $scope.solicitacaorps.situacaosolicitacaorps,
                    situacao: $scope.solicitacaorps.situacao
                };

                console.log(JSON.stringify(solicitacaoRPSDTO))

                Solicitacaorps.save(solicitacaoRPSDTO, onSaveFinished, function () {
                    $state.go("solicitacaorps");
                });

            }
        };

        var onSaveFinished = function (result) {
            $state.go("solicitacaorps");
            Notificacao.priority("Solicitação de RPS salva com sucesso!")
        };

        $scope.validar = function () {
            $scope.mensagemCamposObrigatorios = [];
            if ($scope.solicitacaorps.quantidadeSolicitada <= 0 || $scope.solicitacaorps.quantidadeSolicitada === undefined) {
                $scope.mensagemCamposObrigatorios.push("O campo Quantidade Solicitada de RPS é obrigatória e deve ser maior que zero.")
                Notificacao.camposObrigatorios($scope.mensagemCamposObrigatorios, "warning");
                return false;
            }
            if ($scope.solicitacaorps.quantidadeSolicitada >= 999999) {
                $scope.mensagemCamposObrigatorios.push("Quantidade Solicitada deve ser inferior a 999999.")
                Notificacao.camposObrigatorios($scope.mensagemCamposObrigatorios, "warning");
                return false;
            } else {
                return true;
            }
            return true;
        };
    });
})();
