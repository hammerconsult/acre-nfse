(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('LoteRpsGeralDetailController', function ($scope, LoteRPS, entity, RPS, ParseLinks,
                                                              Notificacao, Util, $timeout) {
            $scope.lote = entity;
            $scope.page = 0;
            $scope.searchQuery = "";
            $scope.per_page = 10;
            $scope.mensagemCopy;
            $scope.timeout;

            $scope.editorOptions = {
                lineWrapping: true,
                lineNumbers: true,
                readOnly: 'nocursor',
                mode: 'htmlmixed',
                lineSeparator: '</br>'
            };

            $scope.carregarDadosLoteRps = function () {
                RPS.query({
                    page: $scope.page,
                    per_page: $scope.per_page,
                    loteId: $scope.lote.id,
                    prestadorId: $scope.lote.prestador.id,
                    filtro: $scope.searchQuery
                }, function (result, headers) {
                    $scope.links = ParseLinks.parse(headers('link'));
                    $scope.listaRps = result;
                });
                LoteRPS.getXml({idLoteRps: $scope.lote.id}, function (data) {
                    if (data)
                        $scope.lote.xml = data.value;
                });
                LoteRPS.getLog({idLoteRps: $scope.lote.id}, function (data) {
                    if (data)
                        $scope.lote.log = data.value;
                });
            };

            $scope.loadPage = function (page) {
                $scope.page = page;
                $scope.carregarDadosLoteRps();
            };

            $scope.verificarSituacaoLote = function () {
                if ($scope.lote.situacao == 'AGUARDANDO') {
                    $scope.timeout = $timeout(function () {
                        $scope.load($scope.lote.id);
                    }, 5000);
                } else {
                    $scope.carregarDadosLoteRps();
                }
            };

            $scope.load = function (id) {
                LoteRPS.get({id: id}, function (result) {
                    $scope.lote = result;
                    $scope.verificarSituacaoLote();
                });
            };

            $scope.verificarSituacaoLote();

            $scope.reprocessarLote = function () {
                LoteRPS.reprocessar($scope.lote, function () {
                    Notificacao.info("Informação", "Lote enviado para reprocessamento!");
                    $scope.load($scope.lote.id);
                }, function () {
                    $scope.load($scope.lote.id);
                });
            };

            $scope.copyToClipboard = function (toCopy) {
                $scope.mensagemCopy = "";
                Util.copyToClipboard(toCopy);
                $scope.mensagemCopy = "Copiado!";
            }

            $scope.$on('$destroy', function () {
                if ($scope.timeout) {
                    $timeout.cancel($scope.timeout);
                }
            });
        });
})();
