(function () {
    'use strict';

    angular.module('nfseApp')
        .factory('MonitoringService', function ($rootScope, $http) {
            return {
                getNamesMetrics: function () {
                    return $http.get('actuator/metrics').then(function (response) {
                        return response.data;
                    });
                },
                getMetric: function (metric) {
                    return $http.get('actuator/metrics/' + metric).then(function (response) {
                        return response.data;
                    });
                },
                checkHealth: function () {
                    return $http.get('actuator/health').then(function (response) {
                        return response.data;
                    });
                },

                threadDump: function () {
                    return $http.get('dump').then(function (response) {
                        return response.data;
                    });
                }
            };
        });
})();