(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('DiscriminacaoServicoController', function ($scope, NotaFiscal, ParseLinks, SweetAlert, $modalInstance) {
            $scope.itensDeclaracaoServico = [];
            $scope.per_page = 5;
            $scope.loadAll = function () {
                NotaFiscal.getDiscriminacoesPorPrestador({
                    page: $scope.page,
                    per_page: $scope.per_page
                }, function (result, headers) {
                    if (headers)
                        $scope.links = ParseLinks.parse(headers('link'));
                    $scope.itensDeclaracaoServico = result;
                });
            };
            $scope.loadPage = function (page) {
                $scope.page = page;
                $scope.loadAll();
            };
            $scope.loadAll();


            $scope.refresh = function () {
                $scope.loadAll();
                $scope.clear();
            };


            $scope.ok = function (item) {
                $modalInstance.close(item);
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        });
})();
