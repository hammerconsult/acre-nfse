(function () {
    'use strict';

angular.module('nfseApp')
    .controller('SyncController', function ($scope, SyncElasticSearch) {
        $scope.sync = function () {
            SyncElasticSearch.update();
        }
    });
})();
