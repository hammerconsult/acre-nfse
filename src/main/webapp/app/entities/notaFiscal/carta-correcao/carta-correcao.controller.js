(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('CartaCorrecaoController', function ($scope, entity, NotaFiscal, ParseLinks, SweetAlert, $modalInstance, ImpressaoPdf) {

            $scope.per_page = 5;
            $scope.cartas = [];
            $scope.carta = {
                dataEmissao: new Date(),
                numero: entity.numero,
                tomadorServicoNfse: entity.tomador,
                prestadorServicoNfseDTO: entity.prestador,
                idNotaFiscal: entity.id
            };

            $scope.formatDate = function (data) {
                return new Date(data).toLocaleDateString();
            };

            $scope.imprimirCarta = function (id) {
                ImpressaoPdf.imprimirPdfViaUrl('/api/notaFiscals/carta-correcao-impressao/' + id);
            };

            $scope.novaCarta = function () {
                $scope.operacaoNovo = true;
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

            $scope.enviarCarta = function () {
                SweetAlert.swal({
                        title: "Confirme o envio da CC-e",
                        text: "Você tem certeza que quer enviar a Carta de Correção?",
                        type: "warning",
                        showCancelButton: true,
                        confirmButtonColor: "#DD6B55", confirmButtonText: "Sim",
                        cancelButtonText: "Não",
                        closeOnConfirm: true,
                        closeOnCancel: true
                    },
                    function (isConfirm) {
                        if (isConfirm) {
                            NotaFiscal.cartaCorrecao($scope.carta, function (data) {
                                $scope.operacaoNovo = false;
                                $scope.loadPage();
                            });
                        }
                    });

            };

            $scope.loadPage = function (page) {
                $scope.page = page;
                $scope.buscarCartaCorrecao();
            };

            $scope.buscarCartaCorrecao = function () {
                NotaFiscal.buscarCartaCorrecao({
                    page: $scope.page,
                    per_page: $scope.per_page,
                    idNotaFiscal: entity.id
                }, function (result, headers) {
                    if (headers)
                        $scope.links = ParseLinks.parse(headers('link'));
                    $scope.cartas = result;
                    $scope.operacaoNovo = $scope.cartas.length === 0;
                });
            };
            $scope.buscarCartaCorrecao();
        });
})();
