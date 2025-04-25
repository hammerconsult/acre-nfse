(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('NoticiasController',
            function ($scope, Noticia, ParseLinks) {
                $scope.noticias = [];
                $scope.per_page = 20;

                $scope.loadAll = function () {
                    Noticia.buscarNoticias({
                        offset: $scope.page,
                        limit: $scope.per_page,
                    }, function (data, headers) {
                        if (headers)
                            $scope.links = ParseLinks.parse(headers('link'));
                        $scope.noticias = data;
                    })
                }

                $scope.loadAll();

                $scope.loadPage = function (page) {
                    $scope.page = page;
                    $scope.loadAll();
                };
            });
})();
