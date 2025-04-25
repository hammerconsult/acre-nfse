(function () {
    'use strict';

angular.module('nfseApp')
    .controller('LoteServicoTomadoDetailController', function ($scope, $rootScope, $stateParams, entity,
                                                               LoteServicoTomado, $timeout, NotaFiscal, ParseLinks) {

        $scope.loteServicoTomado = entity;
        $scope.editable = false;
        $scope.per_page = 10;
        $scope.searchQuery = "";
        $scope.editorOptions = {
            lineWrapping: true,
            lineNumbers: true,
            readOnly: 'nocursor',
            mode: 'htmlmixed',
            lineSeparator: '</br>'
        };

        $scope.load = function (id) {
            LoteServicoTomado.get({id: id}, function (result) {
                $scope.loteServicoTomado = result;
                verificarSituacaoLote();
            });
        };

        $rootScope.$on('nfseApp:loteServicoTomadoUpdate', function (event, result) {
            $scope.loteServicoTomado = result;
        });

        function verificarSituacaoLote() {
            if ($scope.loteServicoTomado.situacao === 'AGUARDANDO') {
                $timeout(function () {
                    $scope.load($scope.loteServicoTomado.id);
                }, 2000)
            }
        }

        verificarSituacaoLote();



        $scope.loadAll = function () {
            NotaFiscal.buscarNotasComoTomadorAndLote({
                page: $scope.page,
                per_page: $scope.per_page,
                filtro: $scope.searchQuery,
                loteId:  $scope.loteServicoTomado.id
            }, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.notas = result;
            });
        };
        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };

        $scope.loadAll();


    });
})();