(function () {
    'use strict';

angular.module('nfseApp')
    .controller('CnaeSearchController', function ($scope, Cnae, CnaeSearch, ParseLinks, SweetAlert, $modalInstance) {
        $scope.cnaes = [];
        $scope.page = 1;
        $scope.per_page = 5;

        $scope.loadAll = function () {
            Cnae.query({page: $scope.page, per_page: $scope.per_page}, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.cnaes = result;
            });
        };
        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.search = function () {
            CnaeSearch.query({query: $scope.searchQuery}, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.cnaes = result;
            }, function (response) {
                if (response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.cnae = {codigo: null, descricao: null, ativo: null, id: null};
        };

        $scope.ok = function (cnae) {
            $modalInstance.close(cnae);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    });
})();