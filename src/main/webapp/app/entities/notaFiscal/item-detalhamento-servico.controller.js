(function () {
    'use strict';

angular.module('nfseApp')
    .controller('ItemDetalhamentoServicoController',
        function ($scope, NotaFiscal, ParseLinks, SweetAlert, $modalInstance, servico, notaFiscal) {

            $scope.itens = [];
            $scope.servico = servico;
            $scope.notaFiscal = notaFiscal;
            $scope.detalhe = {};

            $scope.ok = function () {
                if ($scope.getDiferenca() > 0) {
                    SweetAlert.warning("Atenção", "O valor total dos itens deve ser igual ao valor total do serviço");
                } else {
                    $modalInstance.close($scope.servico);
                }
            };

            $scope.cancel = function () {
                $scope.servico.detalhes = [];
                $modalInstance.dismiss('cancel');
            };

            $scope.adicionarItem = function () {
                if (!$scope.servico.detalhes) {
                    $scope.servico.detalhes = [];
                }
                $scope.servico.detalhes.push($scope.detalhe);
                $scope.detalhe = {};
                angular.element('[ng-model="detalhe.descricao"]').focus();
            };

            $scope.removerItem = function (index) {
                $scope.servico.detalhes.splice(index, 1);
            };
            $scope.editarItem = function (index) {
                $scope.detalhe = $scope.servico.detalhes[index];
                $scope.servico.detalhes.splice(index, 1);
            };

            $scope.getTotal = function () {
                var total = 0.0;
                if ($scope.servico.detalhes) {
                    for (var i = 0; i < $scope.servico.detalhes.length; i++) {
                        var detalhe = $scope.servico.detalhes[i];
                        total += detalhe.valorServico;
                    }
                }
                return total;
            };

            $scope.getDiferenca = function () {
                if ($scope.servico && $scope.notaFiscal.totalNota) {
                    var diferenca = $scope.getTotal() - $scope.notaFiscal.totalNota;
                    return parseFloat(diferenca > 0 ? diferenca : diferenca * -1).toFixed(2);
                }
                return 0.0;
            };

            $scope.calculaValoresServico = function () {
                $scope.detalhe.valorServico = $scope.detalhe.quantidade * $scope.detalhe.valorUnitario;
            };

            $scope.disabledItem = function () {
                return !$scope.detalhe.valorServico || $scope.detalhe.valorServico < 0 ||
                    !$scope.detalhe.quantidade || $scope.detalhe.quantidade <= 0;
            }
        });
})();