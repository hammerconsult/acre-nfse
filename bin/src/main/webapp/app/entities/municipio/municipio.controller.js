(function () {
    'use strict';

angular.module('nfseApp')
    .controller('MunicipioController', function ($scope, Municipio, MunicipioSearch, ParseLinks, SweetAlert) {
        $scope.municipios = [];
        $scope.page = 1;
        $scope.per_page = 10;

        $scope.mostraBotaoLoadInicial = false;
        $scope.loadAll = function () {
            Municipio.query({page: $scope.page, per_page: $scope.per_page}, function (result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.municipios = result;
                if ($scope.page == 1 && $scope.municipios.length == 0) {
                    $scope.mostraBotaoLoadInicial = 1;
                }
            });
        };
        $scope.loadPage = function (page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Municipio.get({id: id}, function (result) {
                $scope.municipio = result;
                $('#deleteMunicipioConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Municipio.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteMunicipioConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            MunicipioSearch.query({query: $scope.searchQuery}, function (result) {
                $scope.municipios = result;
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
            $scope.municipio = {codigo: null, nome: null, id: null};
        };

        $scope.loadInitialData = function () {
            Municipio.loadInitialData();
        }
    });
})();