(function () {
    'use strict';

angular.module('nfseApp')
    .controller('ServicoPrestadorSearchController', function ($scope, Servico, ServicoSearch, ParseLinks, SweetAlert, $modalInstance) {
        $scope.servicos = [];
        $scope.page = 1;
        $scope.loadAll = function () {
            Servico.queryByPrestador({page: $scope.page, per_page: 5}, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.servicos = result;
            });
        };
        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };

        $scope.search = function () {
            if(!$scope.searchQuery){
                $scope.searchQuery = '';
            }
            ServicoSearch.queryByPrestador({query: $scope.searchQuery}, function (result) {
                $scope.servicos = result;
            }, function (response) {
                if (response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.search();


        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.servico = {codigo: null, descricao: null, ativo: null, id: null};
        };

        $scope.ok = function (servico) {
            $modalInstance.close(servico);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    });
})();