(function () {
    'use strict';

angular.module('nfseApp')
    .controller('AidfeSearchController', function ($scope, Tomador, AidfeSearch, ParseLinks, SweetAlert, $modalInstance) {
        $scope.aidfes = [];
        $scope.page = 1;
        $scope.loadAll = function () {
            Tomador.query({page: $scope.page, per_page: 5}, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.aidfes = result;
            });
        };
        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.search = function () {
            if (!$scope.searchQuery) {
                $scope.searchQuery = '';
            }
            AidfeSearch.query({nome: $scope.searchQuery},
                function (result) {
                    $scope.aidfes = result;
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
            $scope.aidfe = null;
        };

        $scope.ok = function (aidfe) {
            $modalInstance.close(aidfe);
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    });
})();