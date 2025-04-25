(function () {
    'use strict';

angular.module('nfseApp')
    .controller('LoteRPSDetailController', function ($scope, $rootScope, $stateParams,
                                                     entity, LoteRPS, $timeout, RPS, ParseLinks, Notificacao) {

        $scope.loteRPS = entity;
        $scope.editable = false;
        $scope.page = 0;
        $scope.searchQuery = "";
        $scope.per_page = 50;

        $scope.editorOptions = {
            lineWrapping: true,
            lineNumbers: true,
            readOnly: 'nocursor',
            mode: 'htmlmixed',
            lineSeparator: '</br>'
        };

        $scope.loadAll = function () {
            RPS.query({
                page: $scope.page,
                per_page: $scope.per_page,
                loteId: $scope.loteRPS.id,
                prestadorId: $scope.loteRPS.prestador.id,
                filtro: $scope.searchQuery
            }, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.listaRps = result;
            });
            LoteRPS.getLog({idLoteRps: $scope.loteRPS.id}, function (data) {
                if (data)
                    $scope.loteRPS.log = data.value;
            });
        };

        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };

        $scope.load = function (id) {
            LoteRPS.get({id: id}, function (result) {
                $scope.loteRPS = result;
                verificarSituacaoLote();
            });
        };

        $rootScope.$on('nfseApp:loteRPSUpdate', function (event, result) {
            $scope.loteRPS = result;
        });

        function verificarSituacaoLote() {
            if ($scope.loteRPS.situacao === 'AGUARDANDO') {
                $scope.timeout = $timeout(function () {
                    $scope.load($scope.loteRPS.id);
                }, 5000)
            }else{
                $scope.loadAll();
            }
        }

        $scope.$on("$destroy", function (event) {
            $timeout.cancel($scope.timeout);
        });

        verificarSituacaoLote();

        $scope.reprocessarLote = function () {
            LoteRPS.reprocessar($scope.loteRPS, function () {
                Notificacao.info("Informação", "Lote enviado para reprocessamento!");
                $scope.load($scope.loteRPS.id);
            }, function () {
                $scope.load($scope.loteRPS.id);
            });
        };

        $scope.loadAll();

    });
})();