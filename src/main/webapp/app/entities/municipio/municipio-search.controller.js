(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('MunicipioSearchController', function ($scope, Municipio,
                                                           ParseLinks, SweetAlert, $modalInstance) {
            $scope.municipios = [];
            $scope.page = 0;
            $scope.per_page = 10;

            $scope.loadPage = function (page) {
                $scope.page = page;
                $scope.search();
            };


            $scope.search = function () {
                if (!$scope.searchQuery)
                    $scope.searchQuery = "";
                Municipio.search({page: $scope.page, per_page: $scope.per_page,
                        filtro: $scope.searchQuery},
                    function (result, headers) {
                        if (headers)
                            $scope.links = ParseLinks.parse(headers('link'));
                        $scope.municipios = result;
                    });
            };

            $scope.search();

            $scope.ok = function (municipio) {
                $modalInstance.close(municipio);
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        });
})();