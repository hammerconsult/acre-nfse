(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('EscritorioContabilSearchController', function ($scope, EscritorioContabil, ParseLinks, SweetAlert, $modalInstance) {
            $scope.escritorios = [];
            $scope.per_page = 10;
            $scope.searchQuery = "";
            $scope.links;

            $scope.montarConsulta = function () {
                var campos = [];

                if ($scope.searchQuery) {
                    campos.push({
                        campo: "obj.nomeescritorio",
                        operador: "LIKE",
                        valorString: '%' + $scope.searchQuery + '%'
                    });
                    campos.push({
                        campo: "obj.crcescritorio",
                        operador: "LIKE",
                        valorString: '%' + $scope.searchQuery + '%'
                    });
                }

                return {
                    offset: $scope.page,
                    limit: $scope.per_page,
                    parametrosQuery: [{
                        juncao: " or ",
                        parametroQueryCampos: campos
                    }]
                };
            };

            $scope.loadAll = function () {
                EscritorioContabil.consultar($scope.montarConsulta(),
                    function (result, headers) {
                        if (headers)
                            $scope.links = ParseLinks.parse(headers('link'));
                        $scope.escritorios = result;
                    });
            };

            $scope.loadPage = function (page) {
                $scope.page = page;
                $scope.loadAll();
            };

            $scope.loadAll();


            $scope.ok = function (escritorio) {
                $modalInstance.close(escritorio);
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        });
})();