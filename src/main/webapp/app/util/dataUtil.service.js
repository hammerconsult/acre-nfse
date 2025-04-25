(function () {
    'use strict';

angular.module('nfseApp')
    .factory('DataUtil', function ($resource) {
        var service = this;
        service.dateWithOutHoursMinutesSeconds = function dateWithOutHoursMinutesSeconds(date) {
            date = new Date(date);
            date.setHours(0);
            date.setMinutes(0);
            date.setSeconds(0);
            return date;
        };
        return service;
    });})();
