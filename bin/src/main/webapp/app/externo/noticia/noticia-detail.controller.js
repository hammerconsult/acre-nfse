(function () {
    'use strict';

    angular.module('nfseApp')
        .controller('NoticiaDetailController',
            function ($scope, $stateParams, entity) {
                $scope.noticia = entity;


            });
})();
