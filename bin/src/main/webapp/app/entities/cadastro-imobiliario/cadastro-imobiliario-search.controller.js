(function () {
    'use strict';

angular.module('nfseApp')
    .controller('CadastroImobiliarioSearchController', function ($scope, $timeout,
                                                                 CadastroImobiliario,
                                                                 ParseLinks, SweetAlert, $modalInstance) {
        $scope.cadastros = [];
        $scope.cadastro = null;
        $scope.page = 1;

        $timeout(function () {
            angular.element('[name="searchQuery"]').focus();
        });

        $scope.search = function () {
            CadastroImobiliario.query({
                page: $scope.page,
                per_page: 5,
                search: $scope.searchQuery
            }, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.cadastros = result;
            });
        };

        $scope.search();

        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.search();
        };

        $scope.refresh = function () {
            $scope.search();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.cadastro = null;
        };

        $scope.ok = function (cadastro) {
            $modalInstance.close(cadastro);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    });
})();